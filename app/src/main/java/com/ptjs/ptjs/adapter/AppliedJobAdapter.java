package com.ptjs.ptjs.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ptjs.ptjs.JobDetailActivity;
import com.ptjs.ptjs.R;
import com.ptjs.ptjs.db.entity.Job;

import java.util.List;
import java.util.Locale;

public class AppliedJobAdapter extends RecyclerView.Adapter<AppliedJobAdapter.AppliedJobViewHolder> {

    private Context mContext;
    private List<Job> jobList;

    public AppliedJobAdapter(Context mContext, List<Job> jobList) {
        this.mContext = mContext;
        this.jobList = jobList;
    }

    @Override
    public AppliedJobViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_applied_job, viewGroup, false);
        return new AppliedJobViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(AppliedJobViewHolder appliedJobViewHolder, int i) {

        final Job job = jobList.get(i);

        appliedJobViewHolder.title.setText(job.getTitle());
        appliedJobViewHolder.companyName.setText("Connect Dots");
        appliedJobViewHolder.workplace.setText(job.getWorkplace());
        appliedJobViewHolder.workingDate.setText("30 July 2018");
        appliedJobViewHolder.wages.setText(String.format(Locale.getDefault(), "RM %.2f", job.getWages()));

        appliedJobViewHolder.layoutJob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, JobDetailActivity.class);
                intent.putExtra("JOB", job);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    class AppliedJobViewHolder extends RecyclerView.ViewHolder {

        CardView layoutJob;
        TextView title, companyName, workplace, workingDate, wages;

        AppliedJobViewHolder(View view) {

            super(view);
            layoutJob = (CardView) view.findViewById(R.id.cv_applied_job);
            title = (TextView) view.findViewById(R.id.tv_job_title);
            companyName = (TextView) view.findViewById(R.id.tv_company_name);
            workplace = (TextView) view.findViewById(R.id.tv_workplace);
            workingDate = (TextView) view.findViewById(R.id.tv_working_date);
            wages = (TextView) view.findViewById(R.id.tv_wages);
        }
    }
}
