package com.TimeToWork.TimeToWork.NavigationFragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.TimeToWork.TimeToWork.Adapter.JobPostAdapter;
import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.CustomClass.CustomVolleyErrorListener;
import com.TimeToWork.TimeToWork.Database.Entity.JobLocation;
import com.TimeToWork.TimeToWork.Database.Entity.JobPost;
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

public class CompanyHomeFragment extends Fragment {

    private CustomProgressDialog mProgressDialog;
    private SwipeRefreshLayout swipeContainer;
    private TextView tvEmpty;

    private LinearLayout layoutTop;
    private TextView tvJobPostTotal, tvAdsTotal;

    private JobPostAdapter adapter;
    private List<JobPost> jobPostList;
    private List<JobLocation> jobLocationList;

    private JSONObject optionJSON = new JSONObject();

    public CompanyHomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(getString(R.string.fragment_home));

        // Enable menu in toolbar
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home_company, container, false);

        layoutTop = (LinearLayout) view.findViewById(R.id.layout_top);
        tvJobPostTotal = (TextView) view.findViewById(R.id.tv_job_post_total);
        tvAdsTotal = (TextView) view.findViewById(R.id.tv_ads_total);

        mProgressDialog = new CustomProgressDialog(getActivity());
        tvEmpty = (TextView) view.findViewById(R.id.tv_empty_job_post);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                showJobPostSummary();
                setupCurrentJobPostList();
            }
        });
        swipeContainer.setColorSchemeResources(R.color.colorAccent);

        jobPostList = new ArrayList<>();
        jobLocationList = new ArrayList<>();
        adapter = new JobPostAdapter(getContext(), jobPostList, jobLocationList);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_job_post_list);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.addOnScrollListener(new RecyclerViewScrollListener() {

            @Override
            void onHide() {
                layoutTop.animate().translationY(-layoutTop.getHeight())
                        .setInterpolator(new AccelerateInterpolator(2));
//                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mFabButton.getLayoutParams();
//                int fabBottomMargin = lp.bottomMargin;
//                mFabButton.animate().translationY(mFabButton.getHeight()+fabBottomMargin).setInterpolator(new AccelerateInterpolator(2)).start();
            }

            @Override
            void onShow() {
                layoutTop.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2));
//                mFabButton.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }
        });

        // Show progress dialog at the beginning
        mProgressDialog.setMessage("Loading your job post ...");
        mProgressDialog.show();
        // Show job post summary
        showJobPostSummary();
        // Show job posts in list
        setupCurrentJobPostList();

        return view;
    }

//    @Override
//    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//
//        inflater.inflate(R.menu.menu_home, menu);
//
//        SearchView searchView = ((SearchView) menu.findItem(R.id.search).getActionView());
//        searchView.setMaxWidth(Integer.MAX_VALUE);
//        searchView.setQueryHint("Search job");
//
//        super.onCreateOptionsMenu(menu, inflater);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

//        int id = item.getItemId();
//
//        if (id == R.id.notification) {
//            startActivity(i);
//        }

        return super.onOptionsItemSelected(item);
    }

    private void showJobPostSummary() {

        final Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {

                        int jobPostTotal = jsonResponse.getInt("JOBPOST_TOTAL");
                        int adsTotal = jsonResponse.getInt("ADS_TOTAL");

                        tvJobPostTotal.setText(String.format(Locale.ENGLISH, "%d", jobPostTotal));
                        tvAdsTotal.setText(String.format(Locale.ENGLISH, "%d", adsTotal));
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

                }
            }
        };

        CustomVolleyErrorListener errorListener
                = new CustomVolleyErrorListener(getActivity(), mProgressDialog, mRequestQueue);
        CompanyHomeFragment.FetchJobPostSummaryRequest fetchJobPostSummaryRequest
                = new CompanyHomeFragment.FetchJobPostSummaryRequest(
                root + getString(R.string.url_get_job_post_summary),
                responseListener,
                errorListener
        );
        mRequestQueue.add(fetchJobPostSummaryRequest);
    }

    private void setupCurrentJobPostList() {

        final Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                Log.e("res", response);

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
                                    jobPost.setPreferGender(jsonobject.getString("job_post_prefer_gender").charAt(0));
                                }

                                JobLocation jobLocation = new JobLocation();
                                jobLocation.setId(jsonobject.getString("job_location_id"));
                                jobLocation.setName(jsonobject.getString("job_location_name"));
                                jobLocation.setAddress(jsonobject.getString("job_location_address"));
                                jobLocation.setLatitude(Double.parseDouble(jsonobject.getString("job_location_lat")));
                                jobLocation.setLongitude(Double.parseDouble(jsonobject.getString("job_location_long")));

                                jobPostList.add(jobPost);
                                jobLocationList.add(jobLocation);

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

        try {
            optionJSON.put("onlyAvailable", true);
            optionJSON.put("companyID", userId);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        CustomVolleyErrorListener errorListener
                = new CustomVolleyErrorListener(getActivity(), mProgressDialog, mRequestQueue);
        CompanyHomeFragment.FetchJobPostRequest fetchJobPostRequest = new CompanyHomeFragment.FetchJobPostRequest(
                optionJSON.toString(),
                root + getString(R.string.url_get_job_application_for_company),
                responseListener,
                errorListener
        );
        mRequestQueue.add(fetchJobPostRequest);
    }

    private class FetchJobPostSummaryRequest extends StringRequest {

        private Map<String, String> params;

        FetchJobPostSummaryRequest(
                String url,
                Response.Listener<String> listener,
                Response.ErrorListener errorListener) {
            super(Method.POST, url, listener, errorListener);

            params = new HashMap<>();
            params.put("company_id", userId);
        }

        public Map<String, String> getParams() {
            return params;
        }
    }

    private class FetchJobPostRequest extends StringRequest {

        private Map<String, String> params;

        FetchJobPostRequest(
                String option,
                String url,
                Response.Listener<String> listener,
                Response.ErrorListener errorListener) {
            super(Method.POST, url, listener, errorListener);

            params = new HashMap<>();
            params.put("option", option);
        }

        public Map<String, String> getParams() {
            return params;
        }
    }

    private abstract class RecyclerViewScrollListener extends RecyclerView.OnScrollListener {

        private static final int HIDE_THRESHOLD = 20;
        private int scrolledDistance = 0;
        private boolean controlsVisible = true;

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {

            super.onScrolled(recyclerView, dx, dy);
            if (scrolledDistance > HIDE_THRESHOLD && controlsVisible) {
                onHide();
                controlsVisible = false;
                scrolledDistance = 0;
            } else if (scrolledDistance < -HIDE_THRESHOLD && !controlsVisible) {
                onShow();
                controlsVisible = true;
                scrolledDistance = 0;
            }

            if ((controlsVisible && dy > 0) || (!controlsVisible && dy < 0)) {
                scrolledDistance += dy;
            }
        }

        abstract void onHide();

        abstract void onShow();
    }
}
