package com.TimeToWork.TimeToWork.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.Database.Entity.Company;
import com.TimeToWork.TimeToWork.Database.Entity.JobApplication;
import com.TimeToWork.TimeToWork.Database.Entity.JobLocation;
import com.TimeToWork.TimeToWork.Database.Entity.Review;
import com.TimeToWork.TimeToWork.Jobseeker.JobseekerProvideReviewActivity;
import com.TimeToWork.TimeToWork.Jobseeker.JobseekerViewReviewActivity;
import com.TimeToWork.TimeToWork.Jobseeker.ViewJobDetailActivity;
import com.TimeToWork.TimeToWork.R;
import com.TimeToWork.TimeToWork.Database.Entity.JobPost;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.TimeToWork.TimeToWork.MainApplication.mRequestQueue;
import static com.TimeToWork.TimeToWork.MainApplication.root;

public class JobApplicationAdapter extends RecyclerView.Adapter<JobApplicationAdapter.JobApplicationViewHolder> {

    private Context mContext;
    private List<JobPost> jobPostList;
    private List<JobLocation> jobLocationList;
    private List<Company> companyList;
    private List<JobApplication> jobApplicationList;

    public static JobApplicationAdapter getAdapter(Context mContext,
                                                   List<JobPost> jobPostList,
                                                   List<JobLocation> jobLocationList,
                                                   List<Company> companyList,
                                                   List<JobApplication> jobApplicationList) {

        return new JobApplicationAdapter(mContext, jobPostList,
                jobLocationList, companyList, jobApplicationList);
    }

    private JobApplicationAdapter(Context mContext,
                                  List<JobPost> jobPostList,
                                  List<JobLocation> jobLocationList,
                                  List<Company> companyList,
                                  List<JobApplication> jobApplicationList) {

        this.mContext = mContext;
        this.jobPostList = jobPostList;
        this.jobLocationList = jobLocationList;
        this.companyList = companyList;
        this.jobApplicationList = jobApplicationList;
    }

    @Override
    public JobApplicationViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_applied_job, viewGroup, false);
        return new JobApplicationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(JobApplicationViewHolder jobApplicationViewHolder, int i) {

        final JobPost jobPost = jobPostList.get(i);
        final JobLocation jobLocation = jobLocationList.get(i);
        final Company company = companyList.get(i);
        final JobApplication jobApplication = jobApplicationList.get(i);

        try {
            JSONObject workingDateObject = new JSONObject(jobPost.getWorkingDate());

            Date startWorkingDate = new SimpleDateFormat("ddMMyyyy", Locale.ENGLISH)
                    .parse(workingDateObject.getString("wd1"));
            SimpleDateFormat newDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);

            String workingDate = newDateFormat.format(startWorkingDate);

            int j = workingDateObject.names().length();
            if (j != 0) {

                Date endWorkingDate = new SimpleDateFormat("ddMMyyyy", Locale.ENGLISH)
                        .parse(workingDateObject.getString("wd" + j));

                workingDate = workingDate + " - " + newDateFormat.format(endWorkingDate);
            }
            jobApplicationViewHolder.workingDate.setText(workingDate);

        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }

        jobApplicationViewHolder.title.setText(jobPost.getTitle());
        jobApplicationViewHolder.workplaceName.setText(jobLocation.getName());
        jobApplicationViewHolder.workplaceAddress.setText(jobLocation.getAddress());
        jobApplicationViewHolder.wages.setText(String.format(Locale.getDefault(), "RM %.2f", jobPost.getWages()));

        if (jobApplication.getStatus().equals("Completed")) {
            jobApplicationViewHolder.btnRate.setVisibility(View.VISIBLE);
        }

        jobApplicationViewHolder.layoutJob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, ViewJobDetailActivity.class);
                intent.putExtra("JOB", jobPost);
                intent.putExtra("JOBLOCATION", jobLocation);
                intent.putExtra("COMPANY", company);
                intent.putExtra("APPLY", false);

                if (jobApplication.getStatus().equals("Sent")) {
                    intent.putExtra("JOBAPPLICATION", jobApplication);
                    intent.putExtra("CANCEL", true);
                }
                mContext.startActivity(intent);
            }
        });

        jobApplicationViewHolder.btnRate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                checkReviewExist(jobApplication, company);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobPostList.size();
    }

    public void clear() {

        int size = jobPostList.size();
        jobPostList.clear();
        notifyItemRangeRemoved(0, size);
    }

    private void checkReviewExist(final JobApplication jobApplication, Company company) {

        // Show progress dialog
        final CustomProgressDialog mProgressDialog = new CustomProgressDialog(mContext);
        mProgressDialog.setMessage("Checking your review ...");
        mProgressDialog.toggleProgressDialog();

        final Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {

                        // Convert review list to json array
                        JSONArray reviewArray = jsonResponse.getJSONArray("REVIEW");

                        if (reviewArray.length() != 0) {

                            JSONObject reviewObject = reviewArray.getJSONObject(0);

                            Review review = new Review();
                            review.setId(reviewObject.getString("review_id"));
                            review.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
                                    .parse(reviewObject.getString("review_date")));
                            review.setComment(reviewObject.getString("review_comment"));
                            review.setTag(reviewObject.getString("review_tag"));
                            review.setStarValue(reviewObject.getDouble("review_star_value"));

                            Intent intent = new Intent(mContext, JobseekerViewReviewActivity.class);
                            intent.putExtra("REVIEW", review);
                            mContext.startActivity(intent);
                        } else {

                            Intent intent = new Intent(mContext, JobseekerProvideReviewActivity.class);
                            intent.putExtra("JOBAPPLICATION", jobApplication);
                            mContext.startActivity(intent);
                        }
                    } else {

                        //If failed, then show alert dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setMessage(jsonResponse.getString("msg"))
                                .setPositiveButton("OK", null)
                                .create()
                                .show();
                    }
                    //To close progress dialog
                    mProgressDialog.toggleProgressDialog();

                } catch (JSONException e) {

                    e.printStackTrace();
                    //To close progress dialog
                    mProgressDialog.toggleProgressDialog();
                    //If exception, then show alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                    builder.setMessage(e.getMessage())
                            .setPositiveButton("OK", null)
                            .create()
                            .show();

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };

        JobApplicationAdapter.CheckReviewRequest checkReviewRequest = new JobApplicationAdapter.CheckReviewRequest(
                jobApplication.getId(),
                company.getId(),
                root + mContext.getString(R.string.url_get_review_of_company),
                responseListener,
                null
        );

        mRequestQueue.add(checkReviewRequest);
    }

    class JobApplicationViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layoutJob;
        TextView title, workplaceName, workplaceAddress, workingDate, wages;
        Button btnRate;

        JobApplicationViewHolder(View view) {

            super(view);
            layoutJob = (LinearLayout) view.findViewById(R.id.layout_job);
            title = (TextView) view.findViewById(R.id.tv_job_title);
            workplaceName = (TextView) view.findViewById(R.id.tv_workplace_name);
            workplaceAddress = (TextView) view.findViewById(R.id.tv_workplace_address);
            workingDate = (TextView) view.findViewById(R.id.tv_working_date);
            wages = (TextView) view.findViewById(R.id.tv_wages);
            btnRate = (Button) view.findViewById(R.id.btn_review);
        }
    }

    private class CheckReviewRequest extends StringRequest {

        private Map<String, String> params;

        CheckReviewRequest(
                String jobApplicationId,
                String companyId,
                String url,
                Response.Listener<String> listener,
                Response.ErrorListener errorListener) {
            super(Method.POST, url, listener, errorListener);

            params = new HashMap<>();
            params.put("job_application_id", jobApplicationId);
            params.put("company_id", companyId);
        }

        public Map<String, String> getParams() {
            return params;
        }
    }
}
