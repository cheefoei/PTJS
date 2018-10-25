package com.TimeToWork.TimeToWork.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.CustomClass.CustomVolleyErrorListener;
import com.TimeToWork.TimeToWork.Database.Entity.JobApplication;
import com.TimeToWork.TimeToWork.Database.Entity.Jobseeker;
import com.TimeToWork.TimeToWork.R;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.TimeToWork.TimeToWork.MainApplication.mRequestQueue;
import static com.TimeToWork.TimeToWork.MainApplication.root;

public class ApplicantListAdapter extends RecyclerView.Adapter<ApplicantListAdapter.ApplicantListViewHolder> {

    private Context mContext;
    private List<JobApplication> jobApplicationList;
    private List<Jobseeker> jobseekerList;

    private OnJobApplicationStatusChangeListener listener;

    public ApplicantListAdapter(Context mContext,
                                List<JobApplication> jobApplicationList,
                                List<Jobseeker> jobseekerList,
                                OnJobApplicationStatusChangeListener listener) {

        this.mContext = mContext;
        this.jobApplicationList = jobApplicationList;
        this.jobseekerList = jobseekerList;
        this.listener = listener;
    }

    @Override
    public ApplicantListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_applicant_list, viewGroup, false);
        return new ApplicantListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ApplicantListViewHolder applicantListViewHolder, int i) {

        final int index = i;
        final JobApplication jobApplication = jobApplicationList.get(i);
        Jobseeker jobseeker = jobseekerList.get(i);

        if (jobseeker.getImg() != null) {
            byte[] decodedString = Base64.decode(jobseeker.getImg(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory
                    .decodeByteArray(decodedString, 0, decodedString.length);
            applicantListViewHolder.imgJobseeker.setImageBitmap(decodedByte);
        }

        applicantListViewHolder.jobseekerName.setText(jobseeker.getName());
        applicantListViewHolder.jobseekerRating.setText(String.format("%s", jobseeker.getRating()));
        applicantListViewHolder.ratingBar.setRating((float) jobseeker.getRating());

        applicantListViewHolder.layoutJobseeker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

//                Intent intent = new Intent(mContext, ViewJobDetailActivity.class);
//                mContext.startActivity(intent);
            }
        });

        applicantListViewHolder.btnOption.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // inflate menu
                PopupMenu popup = new PopupMenu(view.getContext(), view);
                MenuInflater inflater = popup.getMenuInflater();

                if (jobApplication.getStatus().equals("Sent")) {
                    inflater.inflate(R.menu.menu_popup_applicant_sent, popup.getMenu());
                } else if (jobApplication.getStatus().equals("Approved")) {
                    inflater.inflate(R.menu.menu_popup_applicant_approved, popup.getMenu());
                }
                if (jobApplication.getStatus().equals("Rejected")) {
                    inflater.inflate(R.menu.menu_popup_applicant_rejected, popup.getMenu());
                }
                popup.setOnMenuItemClickListener(new MenuItemClickListener(index));
                popup.show();
            }
        });

        if (jobApplication.getStatus().equals("Rejected")) {
            applicantListViewHolder.rejectReason.setText(jobApplication.getRejectReason());
        } else {
            applicantListViewHolder.layoutReject.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return jobseekerList.size();
    }

    public void clear() {

        int size = jobseekerList.size();
        jobseekerList.clear();
        notifyItemRangeRemoved(0, size);
    }

    class ApplicantListViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layoutJobseeker, layoutReject;
        CircleImageView imgJobseeker;
        TextView jobseekerName, jobseekerRating, rejectReason;
        RatingBar ratingBar;
        ImageButton btnOption;

        ApplicantListViewHolder(View view) {

            super(view);
            layoutJobseeker = (LinearLayout) view.findViewById(R.id.layout_jobseeker);
            layoutReject = (LinearLayout) view.findViewById(R.id.layout_reject);
            imgJobseeker = (CircleImageView) view.findViewById(R.id.img_jobseeker);
            jobseekerName = (TextView) view.findViewById(R.id.tv_jobseeker_name);
            jobseekerRating = (TextView) view.findViewById(R.id.tv_jobseeker_rating);
            rejectReason = (TextView) view.findViewById(R.id.tv_reject_reason);
            ratingBar = (RatingBar) view.findViewById(R.id.rate_bar_jobseeker);
            btnOption = (ImageButton) view.findViewById(R.id.btn_option);
        }
    }

    private class MenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        private JobApplication jobApplication;
        private StringRequest jobApplicationRequest;

        private MenuItemClickListener(int index) {
            this.jobApplication = jobApplicationList.get(index);
            this.jobApplicationRequest = null;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {

            int id = item.getItemId();

            // Show progress dialog
            final CustomProgressDialog mProgressDialog = new CustomProgressDialog(mContext);
            final Response.Listener<String> responseListener = new Response.Listener<String>() {

                @Override
                public void onResponse(String response) {

                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        String title;

                        if (success) {
                            title = "Success";
                        } else {
                            title = "Failed";
                        }
                        //To close progress dialog
                        mProgressDialog.dismiss();
                        //show message from server
                        AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                        builder.setTitle(title)
                                .setMessage(jsonResponse.getString("msg"))
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        listener.OnJobApplicationStatusChange();
                                    }
                                })
                                .create()
                                .show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                        mProgressDialog.dismiss();
                    }
                }
            };
            final CustomVolleyErrorListener errorListener = new CustomVolleyErrorListener(
                    (AppCompatActivity) mContext, mProgressDialog, mRequestQueue);

            if (id == R.id.approve) {

                mProgressDialog.setMessage("Approving this applicant...");
                mProgressDialog.show();

                jobApplicationRequest = new ApplicantListAdapter.ApproveJobApplicationRequest(
                        jobApplication,
                        root + mContext.getString(R.string.url_approve_job_application),
                        responseListener,
                        errorListener
                );
                mRequestQueue.add(jobApplicationRequest);

            } else if (id == R.id.reject) {

                final View dialogView = View.inflate(
                        mContext, R.layout.dialog_reject_applicant, null);

                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setTitle("Rejecting Job Application")
                        .setView(dialogView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                mProgressDialog.setMessage("Rejecting this applicant...");
                                mProgressDialog.show();

                                EditText etReason = (EditText) dialogView.findViewById(R.id.et_reject_reason);

                                jobApplicationRequest = new ApplicantListAdapter.RejectJobApplicationRequest(
                                        jobApplication.getId(),
                                        etReason.getText().toString(),
                                        root + mContext.getString(R.string.url_reject_job_application),
                                        responseListener,
                                        errorListener
                                );
                                mRequestQueue.add(jobApplicationRequest);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .create()
                        .show();
            }

            return false;
        }
    }

    private class ApproveJobApplicationRequest extends StringRequest {

        private Map<String, String> params;

        ApproveJobApplicationRequest(
                JobApplication jobApplication,
                String url,
                Response.Listener<String> listener,
                Response.ErrorListener errorListener) {
            super(Method.POST, url, listener, errorListener);

            params = new HashMap<>();
            params.put("job_application_id", jobApplication.getId());
            params.put("job_post_id", jobApplication.getJobPostId());
        }

        public Map<String, String> getParams() {
            return params;
        }
    }

    private class RejectJobApplicationRequest extends StringRequest {

        private Map<String, String> params;

        RejectJobApplicationRequest(
                String jobApplicationId,
                String reason,
                String url,
                Response.Listener<String> listener,
                Response.ErrorListener errorListener) {
            super(Method.POST, url, listener, errorListener);

            params = new HashMap<>();
            params.put("job_application_id", jobApplicationId);
            params.put("reason", reason);
        }

        public Map<String, String> getParams() {
            return params;
        }
    }

    public interface OnJobApplicationStatusChangeListener {

        void OnJobApplicationStatusChange();
    }
}
