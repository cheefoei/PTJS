package com.TimeToWork.TimeToWork.Company;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.TimeToWork.TimeToWork.Adapter.ApplicantListAdapter;
import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.CustomClass.CustomVolleyErrorListener;
import com.TimeToWork.TimeToWork.Database.Entity.JobApplication;
import com.TimeToWork.TimeToWork.Database.Entity.JobPost;
import com.TimeToWork.TimeToWork.Database.Entity.Jobseeker;
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

public class ApplicantFragment extends Fragment
        implements ApplicantListAdapter.OnJobApplicationStatusChangeListener {

    private CustomProgressDialog mProgressDialog;
    private SwipeRefreshLayout swipeContainer;
    private TextView tvEmpty;

    private ApplicantListAdapter adapter;
    private List<JobApplication> jobApplicationList;
    private List<Jobseeker> jobseekerList;

    private JobPost jobPost;
    private String status;

    public ApplicantFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_applicant, container, false);

        jobPost = (JobPost) getArguments().getSerializable("JOBPOST");
        status = getArguments().getString("STATUS");

        mProgressDialog = new CustomProgressDialog(getActivity());
        tvEmpty = (TextView) view.findViewById(R.id.tv_empty_applicant);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                getApplicantList();
            }
        });
        swipeContainer.setColorSchemeResources(R.color.colorAccent);

        jobApplicationList = new ArrayList<>();
        jobseekerList = new ArrayList<>();
        adapter = new ApplicantListAdapter(
                getActivity(), jobApplicationList, jobseekerList, this);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_applicant_list);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);

        getApplicantList();

        return view;
    }

    @Override
    public void OnJobApplicationStatusChange() {
        getActivity().recreate();
    }

    private void getApplicantList() {

        // Show progress dialog
        mProgressDialog.setMessage("Getting all applicants …");
        mProgressDialog.toggleProgressDialog();

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
                            JSONArray jobPostArray = jsonResponse.getJSONArray("JOBAPPLICATION");

                            for (int i = 0; i < jobPostArray.length(); i++) {

                                JSONObject jsonobject = jobPostArray.getJSONObject(i);

                                JobApplication jobApplication = new JobApplication();
                                jobApplication.setId(jsonobject.getString("job_application_id"));
                                jobApplication.setJobPostId(jsonobject.getString("job_post_id"));
                                jobApplication.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
                                        .parse(jsonobject.getString("job_application_date")));
                                jobApplication.setStatus(jsonobject.getString("job_application_status"));
                                jobApplication.setRejectReason(jsonobject.getString("job_application_reject_reason"));

                                Jobseeker jobseeker = new Jobseeker();
                                jobseeker.setId(jsonobject.getString("jobseeker_id"));
                                jobseeker.setName(jsonobject.getString("jobseeker_name"));
                                jobseeker.setImg(jsonobject.getString("jobseeker_img"));
                                jobseeker.setRating(jsonobject.getDouble("jobseeker_rating"));

                                jobApplicationList.add(jobApplication);
                                jobseekerList.add(jobseeker);

                                tvEmpty.setVisibility(View.GONE);
                            }
                        } else {
                            tvEmpty.setVisibility(View.VISIBLE);
                        }
                        adapter.notifyDataSetChanged();
                        swipeContainer.setRefreshing(false);
                        //To close progress dialog
                        mProgressDialog.dismiss();

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
        ApplicantFragment.FetchJobApplicationRequest fetchJobApplicationRequest
                = new ApplicantFragment.FetchJobApplicationRequest(
                jobPost,
                status,
                root + getString(R.string.url_get_job_application_for_company),
                responseListener,
                errorListener
        );
        mRequestQueue.add(fetchJobApplicationRequest);

        adapter.notifyDataSetChanged();
    }

    private class FetchJobApplicationRequest extends StringRequest {

        private Map<String, String> params;

        FetchJobApplicationRequest(
                JobPost jobPost,
                String status,
                String url,
                Response.Listener<String> listener,
                Response.ErrorListener errorListener) {
            super(Method.POST, url, listener, errorListener);

            params = new HashMap<>();
            params.put("job_post_id", jobPost.getId());
            params.put("status", status);
        }

        public Map<String, String> getParams() {
            return params;
        }
    }
}
