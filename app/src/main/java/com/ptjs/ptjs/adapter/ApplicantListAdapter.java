package com.ptjs.ptjs.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ptjs.ptjs.JobDetailActivity;
import com.ptjs.ptjs.R;
import com.ptjs.ptjs.db.entity.Job;

import java.util.List;

public class ApplicantListAdapter extends RecyclerView.Adapter<ApplicantListAdapter.ApplicantListViewHolder> {

    private Context mContext;
    private List<String> jobseekerList;

    public ApplicantListAdapter(Context mContext, List<String> jobseekerList) {
        this.mContext = mContext;
        this.jobseekerList = jobseekerList;
    }

    @Override
    public ApplicantListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_applicant_list, viewGroup, false);
        return new ApplicantListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ApplicantListViewHolder applicantListViewHolder, int i) {

        applicantListViewHolder.layoutJobseeker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(mContext, JobDetailActivity.class);
//                mContext.startActivity(intent);
            }
        });

        applicantListViewHolder.btnRate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                rateJob();
            }
        });

        applicantListViewHolder.btnOption.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // inflate menu
                PopupMenu popup = new PopupMenu(view.getContext(), view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.menu_popup_applicant, popup.getMenu());
//                popup.setOnMenuItemClickListener(new MyMenuItemClickListener(i));
                popup.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobseekerList.size();
    }

    private void rateJob() {

        View dialogView = View.inflate(mContext, R.layout.dialog_rate_jobseeker, null);

        final AlertDialog dialogRateJob = new AlertDialog.Builder(mContext, R.style.DialogTheme)
                .setTitle("Rate Jobseeker")
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

    class ApplicantListViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layoutJobseeker;
        TextView jobseekerName;
        Button btnRate;
        ImageButton btnOption;

        ApplicantListViewHolder(View view) {

            super(view);
            layoutJobseeker = (LinearLayout) view.findViewById(R.id.layout_jobseeker);
            jobseekerName = (TextView) view.findViewById(R.id.tv_jobseeker_name);
            btnRate = (Button) view.findViewById(R.id.btn_rate_jobseeker);
            btnOption = (ImageButton) view.findViewById(R.id.btn_option);
        }
    }
}
