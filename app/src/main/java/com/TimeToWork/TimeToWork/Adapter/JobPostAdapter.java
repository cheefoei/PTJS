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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.TimeToWork.TimeToWork.Database.Entity.JobPost;
import com.TimeToWork.TimeToWork.Jobseeker.ViewJobDetailActivity;
import com.TimeToWork.TimeToWork.R;
import com.TimeToWork.TimeToWork.Company.UpdateJobPostActivity;

import java.util.List;
import java.util.Locale;

public class JobPostAdapter extends RecyclerView.Adapter<JobPostAdapter.JobPostViewHolder> {

    private Context mContext;
    private List<JobPost> jobPostList;

    public JobPostAdapter(Context mContext, List<JobPost> jobPostList) {
        this.mContext = mContext;
        this.jobPostList = jobPostList;
    }

    @Override
    public JobPostViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_job_post, viewGroup, false);
        return new JobPostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(JobPostViewHolder jobPostViewHolder, int i) {

        final JobPost jobPost = jobPostList.get(i);

        jobPostViewHolder.title.setText(jobPost.getTitle());
//        jobPostViewHolder.workplace.setText(jobPost.getWorkplace());
        jobPostViewHolder.workingDate.setText("30 July 2018");
        jobPostViewHolder.wages.setText(String.format(Locale.getDefault(), "RM %.2f", jobPost.getWages()));

        jobPostViewHolder.layoutJob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, ViewJobDetailActivity.class);
                intent.putExtra("JOB", jobPost);
                mContext.startActivity(intent);
            }
        });

        jobPostViewHolder.btnUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, UpdateJobPostActivity.class);
                intent.putExtra("JOB", jobPost);
                mContext.startActivity(intent);
            }
        });

        jobPostViewHolder.btnRemove.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder
                        = new AlertDialog.Builder(mContext, R.style.DialogStyle)
                        .setTitle("Confirmation")
                        .setMessage("Delete this jobPost post? You cannot undo this action. ")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton("Cancel", null);
                builder.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobPostList.size();
    }

    class JobPostViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layoutJob;
        TextView title, workplace, workingDate, wages;
        Button btnUpdate, btnRemove;

        JobPostViewHolder(View view) {

            super(view);
            layoutJob = (LinearLayout) view.findViewById(R.id.layout_job);
            title = (TextView) view.findViewById(R.id.tv_job_title);
            workplace = (TextView) view.findViewById(R.id.tv_workplace);
            workingDate = (TextView) view.findViewById(R.id.tv_working_date);
            wages = (TextView) view.findViewById(R.id.tv_wages);
            btnUpdate = (Button) view.findViewById(R.id.btn_update_job_post);
            btnRemove = (Button) view.findViewById(R.id.btn_remove_job_post);
        }
    }
}
