package com.TimeToWork.TimeToWork.NavigationFragment;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

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
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
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

    private EditText etStartDate, etEndDate;
    private EditText etStartTime, etEndTime;
    private EditText etLocation;
    private Spinner spinnerPaymentTerm, spinnerCategory;
    private SeekBar seekBarWages;

    private LocationManager mLocationManager;
    private Jobseeker jobseeker;

    private JobListAdapter adapter;
    private List<JobPost> jobPostList;

    private String[] option = new String[8];
    private TextView currentSort;

    private Calendar calendar = Calendar.getInstance();
    private DatePickerDialog startDatePickerDialog;
    private DatePickerDialog endDatePickerDialog;
    private TimePickerDialog startTimePickerDialog;
    private TimePickerDialog endTimePickerDialog;

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
        adapter = new JobListAdapter(getContext(), jobPostList);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_job_list);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);

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

        // Set default option
        option[0] = "01012018";
        option[1] = "31122099";
        option[2] = "00:00";
        option[3] = "23:59";
        option[4] = "";
        option[5] = "";
        option[6] = "0";
        option[7] = "60";

        new FragmentAsyncTask() {

            @Override
            protected void onPreExecute() {

                // Show progress dialog at the beginning
                mProgressDialog.setMessage("Loading job post ...");
                mProgressDialog.show();
                super.onPreExecute();
            }

            @Override
            protected Boolean doInBackground(String... params) {

                // Get current location
                getCurrentLocation();
                //Get jobseeker data from local database
                JobseekerDA jobseekerDA = new JobseekerDA(getActivity());
                jobseeker = jobseekerDA.getJobseekerData();
                jobseekerDA.close();
                // Show job posts in list
                setupJobPostList();

                return null;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {

                // Setup filter view
                setupFilterView();
                super.onPostExecute(aBoolean);
            }
        }.execute();

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

        etStartDate = (EditText) navigationView.findViewById(R.id.et_working_date_start);
        etEndDate = (EditText) navigationView.findViewById(R.id.et_working_date_end);

        // Get Current Date
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        startDatePickerDialog = new DatePickerDialog(
                getActivity(), new DatePickerListener(etStartDate), mYear, mMonth, mDay);
        startDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        etStartDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startDatePickerDialog.show();
            }
        });

        endDatePickerDialog = new DatePickerDialog(
                getActivity(), new DatePickerListener(etEndDate), mYear, mMonth, mDay);
        endDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        etEndDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                endDatePickerDialog.show();
            }
        });

        etStartTime = (EditText) navigationView.findViewById(R.id.et_working_time_start);
        etEndTime = (EditText) navigationView.findViewById(R.id.et_working_time_end);

        // Get Current Time
        int mHour = calendar.get(Calendar.HOUR_OF_DAY);
        int mMinute = calendar.get(Calendar.MINUTE);

        startTimePickerDialog = new TimePickerDialog(
                getActivity(), new TimePickerListener(etStartTime), mHour, mMinute, true);
        etStartTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startTimePickerDialog.show();
            }
        });

        endTimePickerDialog = new TimePickerDialog(
                getActivity(), new TimePickerListener(etEndTime), mHour, mMinute, true);
        etEndTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                endTimePickerDialog.show();
            }
        });

        etLocation = (EditText) navigationView.findViewById(R.id.et_location_name);
        spinnerPaymentTerm = (Spinner) navigationView.findViewById(R.id.spinner_payment_term);
        spinnerCategory = (Spinner) navigationView.findViewById(R.id.spinner_category);

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

        Button btnSubmitFilter = (Button) navigationView.findViewById(R.id.btn_submit_filter);
        btnSubmitFilter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!etStartDate.getText().toString().equals("") &&
                        !etEndDate.getText().toString().equals("")) {

                    option[0] = etStartDate.getText().toString();
                    option[1] = etEndDate.getText().toString();
                }

                if (!etStartTime.getText().toString().equals("") &&
                        !etEndTime.getText().toString().equals("")) {

                    option[2] = etStartTime.getText().toString();
                    option[3] = etEndTime.getText().toString();
                }

                option[4] = etLocation.getText().toString();
                option[5] = spinnerCategory.getSelectedItem().toString();

                if (seekBarWages.getProgress() >= 0) {
                    option[6] = String.format(Locale.ENGLISH, "%d", seekBarWages.getProgress());
                }

                String paymentTerm = "0";
                if (spinnerPaymentTerm.getSelectedItemPosition() != 0) {
                    paymentTerm = spinnerPaymentTerm.getSelectedItem().toString().substring(0, 2);
                }
                option[7] = paymentTerm;


                mProgressDialog.setMessage("Filtering job post ...");
                mProgressDialog.show();

                setupJobPostList();

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

                                Company company = new Company();
                                company.setName(jsonobject.getString("company_name"));
                                company.setRating(Double.parseDouble(jsonobject.getString("company_rating")));
                                company.setImg(jsonobject.getString("company_img"));

                                JobLocation jobLocation = new JobLocation();
                                jobLocation.setId(jsonobject.getString("job_location_id"));
                                jobLocation.setName(jsonobject.getString("job_location_name"));
                                jobLocation.setAddress(jsonobject.getString("job_location_address"));
                                jobLocation.setLatitude(Double.parseDouble(jsonobject.getString("job_location_lat")));
                                jobLocation.setLongitude(Double.parseDouble(jsonobject.getString("job_location_long")));

                                JobPost jobPost = new JobPost();
                                jobPost.setId(jsonobject.getString("job_post_id"));
                                jobPost.setCompany(company);
                                jobPost.setJobLocation(jobLocation);
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

                                jobPostList.add(jobPost);
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

                    //To close progress dialog
                    mProgressDialog.dismiss();
                    e.printStackTrace();
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

        Log.e("op", Arrays.toString(option));

        CustomVolleyErrorListener errorListener
                = new CustomVolleyErrorListener(getActivity(), mProgressDialog, mRequestQueue);
        JobseekerHomeFragment.FetchJobPostRequest fetchJobPostRequest = new JobseekerHomeFragment.FetchJobPostRequest(
                Character.toString(jobseeker.getGender()),
                option,
                root + getString(R.string.url_get_job_post_for_jobseeker),
                responseListener,
                errorListener
        );
        mRequestQueue.add(fetchJobPostRequest);
    }

    private class FetchJobPostRequest extends StringRequest {

        private Map<String, String> params;

        FetchJobPostRequest(
                String gender,
                String[] option,
                String url,
                Response.Listener<String> listener,
                Response.ErrorListener errorListener) {
            super(Method.POST, url, listener, errorListener);

            params = new HashMap<>();
            params.put("jobseeker_id", userId);
            params.put("jobseeker_gender", gender);
            params.put("date1", option[0]);
            params.put("date2", option[1]);
            params.put("time1", option[2]);
            params.put("time2", option[3]);
            params.put("location", option[4]);
            params.put("category", option[5]);
            params.put("wages", option[6]);
            params.put("paymentTerm", option[7]);
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

            if (currentSort == v) {
                currentSort.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextPrimary));
                currentSort = null;
                reset();
            } else {
                ((TextView) v).setTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent));
                if (currentSort != null) {
                    currentSort.setTextColor(ContextCompat.getColor(getContext(), R.color.colorTextPrimary));
                }
                currentSort = ((TextView) v);
                sort();
            }
        }

        private void sort() {

            switch (sortType) {

                case "distance":

                    break;
                case "wages":
                    Collections.sort(jobPostList, new Comparator<JobPost>() {

                        @Override
                        public int compare(JobPost o1, JobPost o2) {
                            return o1.getWages() > o2.getWages() ? -1
                                    : (o1.getWages() < o2.getWages()) ? 1 : 0;
                        }
                    });
                    break;
                case "rating":
                    Collections.sort(jobPostList, new Comparator<JobPost>() {

                        @Override
                        public int compare(JobPost o1, JobPost o2) {
                            return o1.getCompany().getRating() > o2.getCompany().getRating() ? -1
                                    : (o1.getCompany().getRating() < o2.getCompany().getRating()) ? 1 : 0;
                        }
                    });
                    break;
            }

            adapter.notifyDataSetChanged();
        }

        private void reset() {

            mProgressDialog.setMessage("Resetting job posts ...");
            mProgressDialog.show();
            setupJobPostList();
        }
    }

    private class DatePickerListener implements DatePickerDialog.OnDateSetListener {

        private EditText editText;

        private DatePickerListener(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void onDateSet(DatePicker view, int year,
                              int monthOfYear, int dayOfMonth) {

            String dateStr = dayOfMonth + "" + (monthOfYear + 1) + "" + year;
            editText.setText(dateStr);
        }
    }

    private class TimePickerListener implements TimePickerDialog.OnTimeSetListener {

        private EditText editText;

        private TimePickerListener(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void onTimeSet(TimePicker view, int hourOfDay,
                              int minute) {

            String hour;
            String min;

            if (hourOfDay == 0) {
                hour = "00";
            } else if (hourOfDay < 10) {
                hour = "0" + hourOfDay;
            } else {
                hour = Integer.toString(hourOfDay);
            }
            if (minute == 0) {
                min = "00";
            } else if (minute < 10) {
                min = "0" + minute;
            } else {
                min = Integer.toString(minute);
            }
            editText.setText(hour + ":" + min);
            editText.clearFocus();
        }
    }

    private abstract class FragmentAsyncTask extends AsyncTask<String, Boolean, Boolean> {

        private FragmentAsyncTask() {
        }
    }
}
