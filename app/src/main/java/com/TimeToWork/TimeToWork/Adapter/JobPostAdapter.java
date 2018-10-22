package com.TimeToWork.TimeToWork.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.TimeToWork.TimeToWork.Database.Entity.JobLocation;
import com.TimeToWork.TimeToWork.Database.Entity.JobPost;
import com.TimeToWork.TimeToWork.Jobseeker.ViewJobDetailActivity;
import com.TimeToWork.TimeToWork.R;
import com.TimeToWork.TimeToWork.Company.UpdateJobPostActivity;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

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

public class JobPostAdapter extends RecyclerView.Adapter<JobPostAdapter.JobPostViewHolder> {

    private Context mContext;
    private List<JobPost> jobPostList;
    private List<JobLocation> jobLocationList;

    public JobPostAdapter(Context mContext,
                          List<JobPost> jobPostList,
                          List<JobLocation> jobLocationList) {

        this.mContext = mContext;
        this.jobPostList = jobPostList;
        this.jobLocationList = jobLocationList;
    }

    @Override
    public JobPostViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_job_post, viewGroup, false);
        return new JobPostViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final JobPostViewHolder jobPostViewHolder, int i) {

        final JobPost jobPost = jobPostList.get(i);
        final JobLocation jobLocation = jobLocationList.get(i);

        SimpleDateFormat newDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);

        try {
            JSONObject workingDateObject = new JSONObject(jobPost.getWorkingDate());

            Date startWorkingDate = new SimpleDateFormat("ddMMyyyy", Locale.ENGLISH)
                    .parse(workingDateObject.getString("wd1"));

            String workingDate = newDateFormat.format(startWorkingDate);

            int j = workingDateObject.names().length();
            if (j != 0) {

                Date endWorkingDate = new SimpleDateFormat("ddMMyyyy", Locale.ENGLISH)
                        .parse(workingDateObject.getString("wd" + j));

                workingDate = workingDate + " - " + newDateFormat.format(endWorkingDate);
            }
            jobPostViewHolder.workingDate.setText(workingDate);

        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }

        jobPostViewHolder.title.setText(jobPost.getTitle());
        jobPostViewHolder.postedDate.setText(String.format("%s%s", mContext.getString(R.string.example_posted_on),
                newDateFormat.format(jobPost.getPostedDate())));
        jobPostViewHolder.workplaceName.setText(jobLocation.getName());
        jobPostViewHolder.workplaceAddress.setText(jobLocation.getAddress());
        jobPostViewHolder.wages.setText(String.format(Locale.getDefault(), "RM %.2f /day", jobPost.getWages()));

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
                intent.putExtra("JOBPOST", jobPost);
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

        new JobPostAdapter.AdapterAsyncTask() {

            @Override
            protected String doInBackground(String... params) {

                calculatePosition(jobPost, jobPostViewHolder.jobPosition);
                return null;
            }
        }.execute();
    }

    @Override
    public int getItemCount() {
        return jobPostList.size();
    }

    private void calculatePosition(final JobPost jobPost, final TextView jobPosition) {

        final Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {

                    JSONObject jsonResponse = new JSONObject(response);
                    int position = jsonResponse.getInt("POSITION");
                    jobPosition.setText(String.format(
                            Locale.ENGLISH, "%d/%d", position, jobPost.getPositionNumber()));

                } catch (JSONException e) {

                    e.printStackTrace();
                    jobPosition.setText(null);
                }
            }
        };

        JobPostAdapter.CalculatePositionRequest calculatePositionRequest
                = new JobPostAdapter.CalculatePositionRequest(
                jobPost.getId(),
                root,
                responseListener,
                null
        );
        mRequestQueue.add(calculatePositionRequest);
    }

    public void clear() {

        int size = jobPostList.size();
        jobPostList.clear();
        notifyItemRangeRemoved(0, size);
    }

    class JobPostViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layoutJob;
        TextView title, jobPosition, postedDate, workplaceName,
                workplaceAddress, workingDate, wages;
        Button btnUpdate, btnRemove;

        JobPostViewHolder(View view) {

            super(view);
            layoutJob = (LinearLayout) view.findViewById(R.id.layout_job);
            title = (TextView) view.findViewById(R.id.tv_job_title);
            jobPosition = (TextView) view.findViewById(R.id.tv_job_position);
            postedDate = (TextView) view.findViewById(R.id.tv_post_date);
            workplaceName = (TextView) view.findViewById(R.id.tv_workplace_name);
            workplaceAddress = (TextView) view.findViewById(R.id.tv_workplace_address);
            workingDate = (TextView) view.findViewById(R.id.tv_working_date);
            wages = (TextView) view.findViewById(R.id.tv_wages);
            btnUpdate = (Button) view.findViewById(R.id.btn_update_job_post);
            btnRemove = (Button) view.findViewById(R.id.btn_remove_job_post);
        }
    }

    private class CalculatePositionRequest extends StringRequest {

        private Map<String, String> params;

        CalculatePositionRequest(
                String jobPostId,
                String url,
                Response.Listener<String> listener,
                Response.ErrorListener errorListener) {
            super(Method.POST, url, listener, errorListener);

            params = new HashMap<>();
            params.put("job_post_id", jobPostId);
        }

        public Map<String, String> getParams() {
            return params;
        }
    }

    private abstract class AdapterAsyncTask extends AsyncTask<String, String, String> {

        private AdapterAsyncTask() {
        }
    }
}
