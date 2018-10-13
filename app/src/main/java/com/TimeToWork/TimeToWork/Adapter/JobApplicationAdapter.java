package com.TimeToWork.TimeToWork.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.TimeToWork.TimeToWork.Database.Entity.Company;
import com.TimeToWork.TimeToWork.Database.Entity.JobApplication;
import com.TimeToWork.TimeToWork.Database.Entity.JobLocation;
import com.TimeToWork.TimeToWork.Jobseeker.ViewJobDetailActivity;
import com.TimeToWork.TimeToWork.R;
import com.TimeToWork.TimeToWork.Database.Entity.JobPost;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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
        JobApplication jobApplication = jobApplicationList.get(i);

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
                mContext.startActivity(intent);
            }
        });

        jobApplicationViewHolder.btnRate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                rateJob();
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

    private void rateJob() {

        View dialogView = View.inflate(mContext, R.layout.dialog_rate_employer, null);

        final AlertDialog dialogRateJob = new AlertDialog.Builder(mContext, R.style.DialogStyle)
                .setTitle("Rate Employer")
                .setView(dialogView)
                .setPositiveButton("Submit", null)
                .setNegativeButton("Cancel", null)
                .setCancelable(false)
                .create();

        dialogRateJob.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialogInterface) {

                Button button = dialogRateJob.getButton(AlertDialog.BUTTON_POSITIVE);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                    }
                });
            }
        });

        dialogRateJob.show();

        EditText etReview = (EditText) dialogView.findViewById(R.id.et_job_review);
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
}
