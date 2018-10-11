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

import com.TimeToWork.TimeToWork.JobDetailActivity;
import com.TimeToWork.TimeToWork.R;
import com.TimeToWork.TimeToWork.Database.Entity.JobPost;

import java.util.List;
import java.util.Locale;

public class AppliedJobAdapter extends RecyclerView.Adapter<AppliedJobAdapter.AppliedJobViewHolder> {

    private Context mContext;
    private List<JobPost> jobPostList;

    public AppliedJobAdapter(Context mContext, List<JobPost> jobPostList) {
        this.mContext = mContext;
        this.jobPostList = jobPostList;
    }

    @Override
    public AppliedJobViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_applied_job, viewGroup, false);
        return new AppliedJobViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AppliedJobViewHolder appliedJobViewHolder, int i) {

        final JobPost jobPost = jobPostList.get(i);

        appliedJobViewHolder.title.setText(jobPost.getTitle());
        appliedJobViewHolder.companyName.setText("Connect Dots");
//        appliedJobViewHolder.workplace.setText(jobPost.getWorkplace());
        appliedJobViewHolder.workingDate.setText("30 July 2018");
        appliedJobViewHolder.wages.setText(String.format(Locale.getDefault(), "RM %.2f", jobPost.getWages()));

        appliedJobViewHolder.layoutJob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, JobDetailActivity.class);
                intent.putExtra("JOB", jobPost);
                mContext.startActivity(intent);
            }
        });

        appliedJobViewHolder.btnRate.setOnClickListener(new View.OnClickListener() {

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

    class AppliedJobViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layoutJob;
        TextView title, companyName, workplace, workingDate, wages;
        Button btnRate;

        AppliedJobViewHolder(View view) {

            super(view);
            layoutJob = (LinearLayout) view.findViewById(R.id.layout_job);
            title = (TextView) view.findViewById(R.id.tv_job_title);
            companyName = (TextView) view.findViewById(R.id.tv_company_name);
            workplace = (TextView) view.findViewById(R.id.tv_workplace);
            workingDate = (TextView) view.findViewById(R.id.tv_working_date);
            wages = (TextView) view.findViewById(R.id.tv_wages);
            btnRate = (Button) view.findViewById(R.id.btn_rate_employer);
        }
    }
}
