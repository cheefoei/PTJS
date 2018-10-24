package com.TimeToWork.TimeToWork.NavigationFragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.TimeToWork.TimeToWork.Adapter.JobListAdapter;
import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.CustomClass.CustomVolleyErrorListener;
import com.TimeToWork.TimeToWork.Database.Entity.Company;
import com.TimeToWork.TimeToWork.Database.Entity.JobLocation;
import com.TimeToWork.TimeToWork.Database.Entity.JobPost;
import com.TimeToWork.TimeToWork.Database.Entity.Jobseeker;
import com.TimeToWork.TimeToWork.Database.JobseekerDA;
import com.TimeToWork.TimeToWork.R;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.TimeToWork.TimeToWork.MainApplication.mRequestQueue;
import static com.TimeToWork.TimeToWork.MainApplication.root;
import static com.TimeToWork.TimeToWork.MainApplication.userId;

public class JobseekerHomeFragment extends Fragment {

    private CustomProgressDialog mProgressDialog;
    private DrawerLayout mDrawerLayout;
    private NavigationView navigationView;
    private LinearLayout layoutSort;
    private SwipeRefreshLayout swipeContainer;
    private TextView tvEmpty;

    private Spinner spinnerPaymentTerm;
    private SeekBar seekBarWages;

    private LocationManager mLocationManager;
    private Jobseeker jobseeker;

    private JobListAdapter adapter;
    private List<JobPost> jobPostList;
    private List<JobLocation> jobLocationList;
    private List<Company> companyList;

    private JSONObject optionJSON = new JSONObject();
    private TextView currentSort;

    public JobseekerHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(getString(R.string.fragment_home));

        // Enable menu in toolbar
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_jobseeker, container, false);

        mDrawerLayout = (DrawerLayout) view.findViewById(R.id.layout_filter);
//        mDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        navigationView = (NavigationView) view.findViewById(R.id.nav_view_filter);

        mLocationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        mProgressDialog = new CustomProgressDialog(getActivity());

        layoutSort = (LinearLayout) view.findViewById(R.id.layout_sort);
        tvEmpty = (TextView) view.findViewById(R.id.tv_empty_job_post);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                setupJobPostList();
            }
        });
        swipeContainer.setColorSchemeResources(R.color.colorAccent);

        jobPostList = new ArrayList<>();
        jobLocationList = new ArrayList<>();
        companyList = new ArrayList<>();
        adapter = new JobListAdapter(getContext(), jobPostList, jobLocationList, companyList);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_job_list);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);

        // Show progress dialog at the beginning
        mProgressDialog.setMessage("Loading job post ...");
        mProgressDialog.show();
        // Get current location
        getCurrentLocation();
        //Get jobseeker data from local database
        JobseekerDA jobseekerDA = new JobseekerDA(getActivity());
        jobseeker = jobseekerDA.getJobseekerData();
        jobseekerDA.close();
        // Show job posts in list
        setupJobPostList();

        Button btnSortBy = (Button) view.findViewById(R.id.btn_sort);
        btnSortBy.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (layoutSort.getVisibility() == View.GONE) {
                    expandSortLayout();
                } else {
                    collapseSortLayout();
                }
            }
        });

        TextView tvNearest = (TextView) view.findViewById(R.id.tv_nearest_distance);
        TextView tvHighestWages = (TextView) view.findViewById(R.id.tv_highest_wages);
        TextView tvHighestRating = (TextView) view.findViewById(R.id.tv_highest_rating);

        tvNearest.setOnClickListener(new SortOnClickListener("distance"));
        tvHighestWages.setOnClickListener(new SortOnClickListener("wages"));
        tvHighestRating.setOnClickListener(new SortOnClickListener("rating"));

        Button btnFilter = (Button) view.findViewById(R.id.btn_filter);
        btnFilter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (layoutSort.getVisibility() != View.GONE) {
                    collapseSortLayout();
                }
                mDrawerLayout.openDrawer(GravityCompat.END);
            }
        });
        setupFilterView();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_home, menu);

        SearchView searchView = ((SearchView) menu.findItem(R.id.search).getActionView());
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Search job");

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

//        int id = item.getItemId();
//
//        if (id == R.id.notification) {
//            startActivity(i);
//        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                getCurrentLocation();
                break;
        }
    }

    private void setupFilterView() {

        final TextView tvWagesValue = (TextView) navigationView.findViewById(R.id.tv_wages_value);

        seekBarWages = (SeekBar) navigationView.findViewById(R.id.seekbar_wages);
        seekBarWages.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvWagesValue.setText(String.format(Locale.ENGLISH, "minimum RM %d", progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        spinnerPaymentTerm = (Spinner) navigationView.findViewById(R.id.spinner_payment_term);

        Button btnSubmitFilter = (Button) navigationView.findViewById(R.id.btn_submit_filter);
        btnSubmitFilter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                try {
                    if (seekBarWages.getProgress() > 0) {
                        optionJSON.put("wages", String.format(Locale.ENGLISH, "%d", seekBarWages.getProgress()));
                    }

                    String paymentTerm = "0";
                    if (spinnerPaymentTerm.getSelectedItemPosition() != 0) {
                        paymentTerm = spinnerPaymentTerm.getSelectedItem().toString().substring(0, 2);
                    }
                    optionJSON.put("paymentTerm", paymentTerm);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String option = optionJSON.toString();
                if (!option.equals("{}")) {
                    mProgressDialog.setMessage("Filtering job post ...");
                    mProgressDialog.show();
                    setupJobPostList();
                }
                mDrawerLayout.closeDrawer(GravityCompat.END);
            }
        });
    }

    private void expandSortLayout() {

        layoutSort.measure(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);

        final int targetHeight = layoutSort.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        layoutSort.getLayoutParams().height = 1;
        layoutSort.setVisibility(View.VISIBLE);

        Animation a = new Animation() {

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {

                layoutSort.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                layoutSort.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (targetHeight / layoutSort.getContext().getResources().getDisplayMetrics().density));
        layoutSort.startAnimation(a);
    }

    private void collapseSortLayout() {

        final int initialHeight = layoutSort.getMeasuredHeight();

        Animation a = new Animation() {

            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {

                if (interpolatedTime == 1) {
                    layoutSort.setVisibility(View.GONE);
                } else {
                    layoutSort.getLayoutParams().height =
                            initialHeight - (int) (initialHeight * interpolatedTime);
                    layoutSort.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration((int) (initialHeight / layoutSort.getContext().getResources().getDisplayMetrics().density));
        layoutSort.startAnimation(a);
    }

    private void getCurrentLocation() {

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {

            Location location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null) {
                adapter.setLatitude(location.getLatitude());
                adapter.setLongitude(location.getLongitude());
            } else {
                adapter.setLatitude(Double.NaN);
                adapter.setLongitude(Double.NaN);
            }
        }

    }

    private void setupJobPostList() {

        final Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    int total = jsonResponse.getInt("total");

                    if (success) {

                        // Clear all item inside recycler view
                        adapter.clear();

                        if (total != 0) {

                            // Convert job post list to json array
                            JSONArray jobPostArray = jsonResponse.getJSONArray("JOBPOST");

                            for (int i = 0; i < jobPostArray.length(); i++) {

                                JSONObject jsonobject = jobPostArray.getJSONObject(i);

                                JobPost jobPost = new JobPost();
                                jobPost.setId(jsonobject.getString("job_post_id"));
                                jobPost.setCompanyId(jsonobject.getString("company_id"));
                                jobPost.setLocation_id(jsonobject.getString("job_location_id"));
                                jobPost.setTitle(jsonobject.getString("job_post_title"));
                                jobPost.setPostedDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
                                        .parse(jsonobject.getString("job_post_posted_date")));
                                jobPost.setWorkingDate(jsonobject.getString("job_post_working_date"));
                                jobPost.setWorkingTime(jsonobject.getString("job_post_working_timetable"));
                                jobPost.setCategory(jsonobject.getString("job_post_category"));
                                jobPost.setRequirement(jsonobject.getString("job_post_requirement"));
                                jobPost.setDescription(jsonobject.getString("job_post_description"));
                                jobPost.setNote(jsonobject.getString("job_post_note"));
                                jobPost.setWages(jsonobject.getDouble("job_post_wages"));
                                jobPost.setPaymentTerm(jsonobject.getInt("job_post_payment_term"));
                                jobPost.setPositionNumber(jsonobject.getInt("job_post_position_num"));
                                jobPost.setAds(jsonobject.getInt("job_post_isAds") == 1);

                                if (jsonobject.getString("job_post_prefer_gender").length() > 0) {
                                    jobPost.setPreferGender(jsonobject.getString("job_post_prefer_gender"));
                                }

                                JobLocation jobLocation = new JobLocation();
                                jobLocation.setId(jsonobject.getString("job_location_id"));
                                jobLocation.setName(jsonobject.getString("job_location_name"));
                                jobLocation.setAddress(jsonobject.getString("job_location_address"));
                                jobLocation.setLatitude(Double.parseDouble(jsonobject.getString("job_location_lat")));
                                jobLocation.setLongitude(Double.parseDouble(jsonobject.getString("job_location_long")));

                                Company company = new Company();
                                company.setName(jsonobject.getString("company_name"));
                                company.setRating(Double.parseDouble(jsonobject.getString("company_rating")));
                                company.setImg(jsonobject.getString("company_img"));

                                jobPostList.add(jobPost);
                                jobLocationList.add(jobLocation);
                                companyList.add(company);

                                tvEmpty.setVisibility(View.GONE);
                            }
                        } else {
                            tvEmpty.setVisibility(View.VISIBLE);
                        }
                        adapter.notifyDataSetChanged();
                        swipeContainer.setRefreshing(false);

                    } else {
                        //If failed, then show alert dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(jsonResponse.getString("msg"))
                                .setPositiveButton("OK", null)
                                .create()
                                .show();
                    }
                    //To close progress dialog
                    mProgressDialog.dismiss();

                } catch (JSONException e) {

                    e.printStackTrace();
                    //To close progress dialog
                    mProgressDialog.toggleProgressDialog();
                    //If exception, then show alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(e.getMessage())
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getActivity().finish();
                                }
                            })
                            .create()
                            .show();

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };

        CustomVolleyErrorListener errorListener
                = new CustomVolleyErrorListener(getActivity(), mProgressDialog, mRequestQueue);
        JobseekerHomeFragment.FetchJobPostRequest fetchJobPostRequest = new JobseekerHomeFragment.FetchJobPostRequest(
                optionJSON.toString(),
                Character.toString(jobseeker.getGender()),
                root + getString(R.string.url_get_job_post_for_jobseeker),
                responseListener,
                errorListener
        );
        mRequestQueue.add(fetchJobPostRequest);
    }

    private class FetchJobPostRequest extends StringRequest {

        private Map<String, String> params;

        FetchJobPostRequest(
                String option,
                String gender,
                String url,
                Response.Listener<String> listener,
                Response.ErrorListener errorListener) {
            super(Method.POST, url, listener, errorListener);

            params = new HashMap<>();
            params.put("option", option);
            params.put("jobseeker_id", userId);
            params.put("jobseeker_gender", gender);
        }

        public Map<String, String> getParams() {
            return params;
        }
    }

    private class SortOnClickListener implements View.OnClickListener {

        private String sortType;

        SortOnClickListener(String sortType) {
            this.sortType = sortType;
        }

        @Override
        public void onClick(View v) {

            ((TextView) v).setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
            if (currentSort != null) {
                currentSort.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextPrimary));
            }
            currentSort = ((TextView) v);
            sort();
        }

        private void sort() {

        }
    }
}
