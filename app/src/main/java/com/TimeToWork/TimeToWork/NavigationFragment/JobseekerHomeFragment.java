package com.TimeToWork.TimeToWork.NavigationFragment;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.TimeToWork.TimeToWork.Adapter.JobListAdapter;
import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.CustomClass.CustomVolleyErrorListener;
import com.TimeToWork.TimeToWork.Database.Entity.Company;
import com.TimeToWork.TimeToWork.Database.Entity.JobLocation;
import com.TimeToWork.TimeToWork.Database.Entity.JobPost;
import com.TimeToWork.TimeToWork.Database.Entity.Jobseeker;
import com.TimeToWork.TimeToWork.Database.Entity.Schedule;
import com.TimeToWork.TimeToWork.Database.JobseekerDA;
import com.TimeToWork.TimeToWork.Jobseeker.JobFilterNavigationView;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.TimeToWork.TimeToWork.MainApplication.latitude;
import static com.TimeToWork.TimeToWork.MainApplication.longitude;
import static com.TimeToWork.TimeToWork.MainApplication.mRequestQueue;
import static com.TimeToWork.TimeToWork.MainApplication.root;
import static com.TimeToWork.TimeToWork.MainApplication.userId;

public class JobseekerHomeFragment extends Fragment
        implements JobFilterNavigationView.OnFilterOptionChangeListener {

    private CustomProgressDialog mProgressDialog;
    private CustomVolleyErrorListener errorListener;

    private DrawerLayout mDrawerLayout;
    private LinearLayout layoutSort;
    private SwipeRefreshLayout swipeContainer;
    private TextView tvEmpty;

    private Jobseeker jobseeker;

    private JobListAdapter adapter;
    private List<JobPost> jobPostList;
    private List<JobPost> originalJobPostList;
    private List<Schedule> scheduleList;

    private String[] option = new String[8];
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

        mProgressDialog = new CustomProgressDialog(getActivity());
        errorListener = new CustomVolleyErrorListener(getActivity(), mProgressDialog, mRequestQueue);

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
        originalJobPostList = new ArrayList<>();
        scheduleList = new ArrayList<>();
        adapter = new JobListAdapter(getContext(), jobPostList, originalJobPostList);

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
/*
        //Notification
        MaintainNotification maintainNotification = new MaintainNotification();
        JobApplication jobApplication = null;
        try {
            jobApplication = maintainNotification.getJobApplicationDetails();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(getActivity())
                .setDefaults(NotificationCompat.DEFAULT_ALL)
                .setSmallIcon(R.drawable.baseline_notifications_black_18)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(),
                        R.drawable.baseline_notifications_black_18))
                .setContentTitle(jobApplication.getId());

                notificationBuilder.setContentText("Your " + jobApplication.getStatus() + " has been approved.");

        NotificationManager notificationManager = (NotificationManager) getActivity()
                .getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notificationBuilder.build());*/

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

                // Set default option
                setupDefaultOption();
                //Sync jobseeker data from server
                syncJobseekerData();

                return null;
            }
        }.execute();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_home, menu);

        SearchView searchView = ((SearchView) menu.findItem(R.id.search).getActionView());
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Job ID, Title, Company Name");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        MenuItemCompat.setOnActionExpandListener(
                menu.findItem(R.id.search), new MenuItemCompat.OnActionExpandListener() {

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
                        return true;
                    }

                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {

                        jobPostList.clear();
                        jobPostList.addAll(originalJobPostList);
                        adapter.notifyDataSetChanged();
                        return true;
                    }
                });

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void OnFilterOptionChange(String[] option) {

        // Pass option to activity
        this.option = option;
        // Show progress dialog
        mProgressDialog.setMessage("Filtering job post ...");
        mProgressDialog.show();
        // Filter job list
        setupJobPostList();
        // Close filter navigation view
        mDrawerLayout.closeDrawer(GravityCompat.END);
    }

    @Override
    public void OnSwitchPersonalFilterChange(boolean isChecked) {

        // Close filter navigation view
        mDrawerLayout.closeDrawer(GravityCompat.END);

        if (isChecked) {
            getJobseekerSchedule();
        } else {
            mProgressDialog.setMessage("Resetting job posts ...");
            mProgressDialog.show();
            // Set default option
            setupDefaultOption();
            // Retrieve part time job
            setupJobPostList();
        }
    }

    private void startPersonalFilter() {

        List<JobPost> matchedCategoryJobList = new ArrayList<>();
        List<JobPost> matchedLocationJobList = new ArrayList<>();
        List<JobPost> matchedScheduleJobList = new ArrayList<>();

        List<String> categoryList = Arrays.asList(jobseeker.getPreferred_job().split(","));
        List<String> locationList = Arrays.asList(jobseeker.getPreferred_location().split(","));

        boolean isCategoryList = !categoryList.isEmpty() && !categoryList.get(0).equals("null")
                && !categoryList.get(0).equals("");
        boolean isLocationList = !locationList.isEmpty() && !locationList.get(0).equals("null")
                && !locationList.get(0).equals("");

        if (isCategoryList) {
            for (String category : categoryList) {
                for (JobPost jp : jobPostList) {
                    Log.e(category.trim(), jp.getCategory().trim());
                    if (jp.getCategory().trim().equals(category.trim())) {
                        matchedCategoryJobList.add(jp);
                    }
                }
            }
        } else {
            matchedCategoryJobList.addAll(jobPostList);
        }

        if (isLocationList) {
            for (String location : locationList) {
                for (JobPost jp : jobPostList) {
                    JobLocation jl = jp.getJobLocation();
                    if (jl.getName().contains(location) || jl.getAddress().contains(location)) {
                        matchedLocationJobList.add(jp);
                    }
                }
            }
        } else {
            matchedLocationJobList.addAll(jobPostList);
        }

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.ENGLISH);
        SimpleDateFormat dateFormat1 = new SimpleDateFormat("ddMMyyyy", Locale.ENGLISH);
        SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

        try {
            if (!scheduleList.isEmpty()) {
                for (Schedule schedule : scheduleList) {
                    for (JobPost jp : jobPostList) {

                        JSONObject workingDateObject = new JSONObject(jp.getWorkingDate());
                        Date workingDate = dateFormat1.parse(workingDateObject.getString("wd1"));

                        if (dateFormat2.format(workingDate)
                                .equals(schedule.getSchedule_list_date())) {

                            JSONObject workingTimeObject = new JSONObject(jp.getWorkingTime());

                            Date scheduleTimeFrom = sdf.parse(schedule.getTimeFrom());
                            Date scheduleTimeTo = sdf.parse(schedule.getTimeTo());

                            Date workingTimeFrom = sdf.parse(
                                    workingTimeObject.getString("startWorkTime"));
                            Date workingTimeTo = sdf.parse(
                                    workingTimeObject.getString("endWorkTime"));

                            if (scheduleTimeFrom.before(workingTimeFrom)
                                    && scheduleTimeTo.after(workingTimeFrom)
                                    && scheduleTimeFrom.before(workingTimeTo)
                                    && scheduleTimeTo.after(workingTimeTo)) {

                                Log.e("GOT", "----");
                                Log.e("INFO", "schedule between " + schedule.getTimeFrom()
                                        + " " + schedule.getTimeTo());
                                Log.e("INFO", "working between " + workingTimeObject.getString("startWorkTime")
                                        + " " + workingTimeObject.getString("endWorkTime"));
                                matchedScheduleJobList.add(jp);
                            }
                        }
                    }
                }
            } else {
                matchedScheduleJobList.addAll(jobPostList);
            }
        } catch (ParseException | JSONException e) {
            e.printStackTrace();
        }

        jobPostList.retainAll(matchedCategoryJobList);
        jobPostList.retainAll(matchedLocationJobList);
        jobPostList.retainAll(matchedScheduleJobList);

        adapter.notifyDataSetChanged();

        if (jobPostList.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
        }
        mProgressDialog.dismiss();
    }

    private void setupDefaultOption() {

        // Set default option
        option[0] = "01012018";
        option[1] = "31122099";
        option[2] = "00:00";
        option[3] = "23:59";
        option[4] = "";
        option[5] = "";
        option[6] = "0";
        option[7] = "60";
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

    private void syncJobseekerData() {

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {

                        //Change jobseeker JSON to Array
                        JSONObject jobseekerObject = jsonResponse.getJSONObject("JOBSEEKER");
                        //Create jobseeker object
                        Jobseeker j = new Jobseeker();
                        j.setId(jobseekerObject.getString("jobseeker_id"));
                        j.setName(jobseekerObject.getString("jobseeker_name"));
                        j.setGender(jobseekerObject.getString("jobseeker_gender").charAt(0));
                        j.setDob(new SimpleDateFormat("yyyy-MM-dd",
                                Locale.ENGLISH).parse(jobseekerObject.getString("jobseeker_dob")));
                        j.setIc(jobseekerObject.getString("jobseeker_ic"));
                        j.setAddress(jobseekerObject.getString("jobseeker_address"));
                        j.setPhone_number(jobseekerObject.getString("jobseeker_phone_number"));
                        j.setEmail(jobseekerObject.getString("jobseeker_email"));
                        j.setPreferred_job(jobseekerObject.getString("jobseeker_preferred_job"));
                        j.setPreferred_location(jobseekerObject.getString("jobseeker_preferred_location"));
                        j.setExperience(jobseekerObject.getString("jobseeker_experience"));
                        j.setRating(jobseekerObject.getDouble("jobseeker_rating"));
                        j.setImg(jobseekerObject.getString("jobseeker_img"));

                        //Update jobseeker data
                        JobseekerDA jobseekerDA = new JobseekerDA(getActivity());
                        jobseekerDA.updateJobseekerData(j);
                        jobseekerDA.close();

                        // Show job posts in list
                        setupJobPostList();
                    }
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
            }
        };

        GetJobseekerRequest getJobseekerRequest = new GetJobseekerRequest(
                root + getString(R.string.url_get_jobseeker),
                responseListener,
                errorListener
        );
        mRequestQueue.add(getJobseekerRequest);
    }

    private void setupJobPostList() {

        //Get jobseeker data from local database
        JobseekerDA jobseekerDA = new JobseekerDA(getActivity());
        jobseeker = jobseekerDA.getJobseekerData();
        jobseekerDA.close();

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    int total = jsonResponse.getInt("total");

                    if (success) {

                        // Clear all item inside recycler view
                        adapter.clear();
                        originalJobPostList.clear();

                        if (total != 0) {

                            // Convert job post list to json array
                            JSONArray jobPostArray = jsonResponse.getJSONArray("JOBPOST");

                            for (int i = 0; i < jobPostArray.length(); i++) {

                                JSONObject jsonobject = jobPostArray.getJSONObject(i);

                                Company company = new Company();
                                company.setId(jsonobject.getString("company_id"));
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
                                originalJobPostList.add(jobPost);
                            }
                            tvEmpty.setVisibility(View.GONE);
                        } else {
                            tvEmpty.setVisibility(View.VISIBLE);
                        }
                        // Show distance
                        calculateDistance();

                    } else {
                        //If failed, then show alert dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(jsonResponse.getString("msg"))
                                .setPositiveButton("OK", null)
                                .create()
                                .show();
                    }
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

        Log.e("option", Arrays.toString(option));
        Log.e("jobseeker name", jobseeker.getName());

        FetchJobPostRequest fetchJobPostRequest = new FetchJobPostRequest(
                Character.toString(jobseeker.getGender()),
                option,
                root + getString(R.string.url_get_job_post_for_jobseeker),
                responseListener,
                errorListener
        );
        mRequestQueue.add(fetchJobPostRequest);
    }

    private void calculateDistance() {

        String destination = null;
        for (JobPost jobPost : jobPostList) {

            if (destination == null) {
                destination = jobPost.getJobLocation().getLatitude() + ","
                        + jobPost.getJobLocation().getLongitude();
            } else {
                destination += "|" + jobPost.getJobLocation().getLatitude() + ","
                        + jobPost.getJobLocation().getLongitude();
            }
        }

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    JSONArray rowArray = jsonResponse.getJSONArray("rows");
                    JSONArray elementsArray = rowArray.getJSONObject(0).getJSONArray("elements");

                    for (int i = 0; i < elementsArray.length(); i++) {

                        String status = elementsArray.getJSONObject(i).getString("status");
                        if (status.equals("OK")) {
                            JSONObject distanceObject = elementsArray.getJSONObject(i).getJSONObject("distance");
                            String distanceString = distanceObject.getString("text");
                            double distance = Double.parseDouble(distanceString.substring(0, (distanceString.length() - 2)));
                            jobPostList.get(i).getJobLocation().setDistance(distance);
                        } else {
                            jobPostList.get(i).getJobLocation().setDistance(99999999);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //To close progress dialog
                mProgressDialog.dismiss();
                adapter.notifyDataSetChanged();
                swipeContainer.setRefreshing(false);
            }
        };

        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?units=metric&origins="
                + latitude + "," + longitude + "&destinations=" + destination
                + "&key=AIzaSyBskYfSet3LPn3SB6KldlvTJdVUIDnsprQ";

        CalculateDistanceRequest calculateDistanceRequest = new CalculateDistanceRequest(
                url,
                responseListener,
                null
        );

        if (latitude != Double.NaN && !jobPostList.isEmpty()) {
            mRequestQueue.add(calculateDistanceRequest);
        } else {
            //To close progress dialog
            mProgressDialog.dismiss();
            adapter.notifyDataSetChanged();
            swipeContainer.setRefreshing(false);
        }
    }

    private void getJobseekerSchedule() {

        mProgressDialog.setMessage("Matching job posts ...");
        mProgressDialog.show();

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    int total = jsonResponse.getInt("total");

                    scheduleList.clear();
                    if (success && total > 0) {

                        JSONArray scheduleArray = jsonResponse.getJSONArray("SCHEDULE");

                        for (int i = 0; i < scheduleArray.length(); i++) {

                            JSONObject jsonobject = scheduleArray.getJSONObject(i);

                            Schedule schedule = new Schedule();
                            schedule.setSchedule_list_date(jsonobject.getString("schedule_date"));
                            schedule.setTimeFrom(jsonobject.getString("schedule_time_from"));
                            schedule.setTimeTo(jsonobject.getString("schedule_time_to"));

                            scheduleList.add(schedule);
                        }
                    }
                    startPersonalFilter();

                } catch (JSONException e) {
                    e.printStackTrace();
                    mProgressDialog.dismiss();
                }
            }
        };

        GetScheduleRequest getScheduleRequest = new GetScheduleRequest(
                root + getString(R.string.url_get_schedule),
                responseListener,
                errorListener
        );
        mRequestQueue.add(getScheduleRequest);
    }

    private class GetJobseekerRequest extends StringRequest {

        private Map<String, String> params;

        GetJobseekerRequest(String url,
                            Response.Listener<String> listener,
                            Response.ErrorListener errorListener) {
            super(Method.POST, url, listener, errorListener);

            params = new HashMap<>();
            params.put("id", userId);
        }

        public Map<String, String> getParams() {
            return params;
        }
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

    private class CalculateDistanceRequest extends StringRequest {

        CalculateDistanceRequest(String url,
                                 Response.Listener<String> listener,
                                 Response.ErrorListener errorListener) {
            super(Method.GET, url, listener, errorListener);
        }
    }

    private class GetScheduleRequest extends StringRequest {

        private Map<String, String> params;

        GetScheduleRequest(String url,
                           Response.Listener<String> listener,
                           Response.ErrorListener errorListener) {
            super(Method.POST, url, listener, errorListener);

            params = new HashMap<>();
            params.put("jobseeker_id", userId);
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
                    Collections.sort(jobPostList, new Comparator<JobPost>() {

                        @Override
                        public int compare(JobPost o1, JobPost o2) {
                            return o1.getJobLocation().getDistance() < o2.getJobLocation().getDistance() ? -1
                                    : (o1.getJobLocation().getDistance() > o2.getJobLocation().getDistance()) ? 1 : 0;
                        }
                    });
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
            collapseSortLayout();
            adapter.notifyDataSetChanged();
        }

        private void reset() {

            collapseSortLayout();
            mProgressDialog.setMessage("Resetting job posts ...");
            mProgressDialog.show();
            // Set default option
            setupDefaultOption();
            // Retrieve part time job
            setupJobPostList();
        }
    }

    private abstract class FragmentAsyncTask extends AsyncTask<String, Boolean, Boolean> {

        private FragmentAsyncTask() {
        }
    }
}