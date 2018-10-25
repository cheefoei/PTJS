package com.TimeToWork.TimeToWork.Company;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.Database.Entity.JobLocation;
import com.TimeToWork.TimeToWork.Database.Entity.JobPost;
import com.TimeToWork.TimeToWork.R;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.TimeToWork.TimeToWork.MainApplication.mRequestQueue;
import static com.TimeToWork.TimeToWork.MainApplication.root;

public class CompanyJobDetailActivity extends AppCompatActivity {

    private CustomProgressDialog mProgressDialog;

    private JobPost jobPost;
    private JobLocation jobLocation;

    private boolean isRemovable = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_job_detail);

        mProgressDialog = new CustomProgressDialog(CompanyJobDetailActivity.this);
        mProgressDialog.setMessage("Loading …");
//        mProgressDialog.toggleProgressDialog();

        jobPost = (JobPost) getIntent().getSerializableExtra("JOBPOST");
        jobLocation = (JobLocation) getIntent().getSerializableExtra("JOBLOCATION");
        isRemovable = getIntent().getBooleanExtra("REMOVE", true);

        SimpleDateFormat newDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);
        String workingDate = "";

        try {
            JSONObject workingDateObject = new JSONObject(jobPost.getWorkingDate());
            int len = workingDateObject.names().length();

            for (int i = 1; i <= len; i++) {

                Date date = new SimpleDateFormat("ddMMyyyy", Locale.ENGLISH)
                        .parse(workingDateObject.getString("wd" + i));

                if (!workingDate.equals("")) {
                    workingDate = workingDate + "\n";
                }
                workingDate = workingDate + newDateFormat.format(date);
            }
        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }

        TextView tvJobTitle = (TextView) findViewById(R.id.tv_job_title);
        tvJobTitle.setText(jobPost.getTitle());

        TextView tvPostedDate = (TextView) findViewById(R.id.tv_post_date);
        tvPostedDate.setText(String.format("%s%s", getString(R.string.example_posted_on), newDateFormat.format(jobPost.getPostedDate())));

        TextView tvWorkplaceName = (TextView) findViewById(R.id.tv_workplace_name);
        tvWorkplaceName.setText(jobLocation.getName());

        TextView tvWorkplaceAddress = (TextView) findViewById(R.id.tv_workplace_address);
        tvWorkplaceAddress.setText(jobLocation.getAddress());

        TextView tvWorkingDate = (TextView) findViewById(R.id.tv_working_date);
        tvWorkingDate.setText(workingDate);

        String wage = String.format(Locale.getDefault(), "RM %.2f /day ", jobPost.getWages());
        String paymentTerm;

        if (jobPost.getPaymentTerm() == 0) {
            paymentTerm = "(On-the-spot)";
        } else {
            paymentTerm = String.format(Locale.getDefault(), "(Within %d days)", jobPost.getPaymentTerm());
        }

        TextView tvWages = (TextView) findViewById(R.id.tv_wages);
        tvWages.setText(String.format("%s%s", wage, paymentTerm));

        TextView tvRequirement = (TextView) findViewById(R.id.tv_job_requirement);
        tvRequirement.setText(jobPost.getRequirement());

        TextView tvDescription = (TextView) findViewById(R.id.tv_job_description);
        tvDescription.setText(jobPost.getDescription());

        String note = jobPost.getNote();
        if (note.equals("")) {
            note = "(Empty)";
        }
        TextView tvNote = (TextView) findViewById(R.id.tv_job_note);
        tvNote.setText(note);

        if (!jobPost.isAds()) {
            TextView tvAds = (TextView) findViewById(R.id.tv_is_ads);
            tvAds.setVisibility(View.GONE);
        }

        calculatePosition();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        if (isRemovable) {
            getMenuInflater().inflate(R.menu.menu_company_job_detail, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_company_job_detail_no_delete, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.applicant) {

            Intent intent = new Intent(CompanyJobDetailActivity.this, ApplicantListActivity.class);
            intent.putExtra("JOBPOST", jobPost);
            startActivity(intent);

        } else if (id == R.id.job_post_update) {

            Intent intent = new Intent(CompanyJobDetailActivity.this, UpdateJobPostActivity.class);
            intent.putExtra("JOBPOST", jobPost);
            intent.putExtra("JOBLOCATION", jobLocation);
            startActivity(intent);
            finish();

        } else if (id == R.id.job_post_delete) {

            AlertDialog.Builder builder
                    = new AlertDialog.Builder(CompanyJobDetailActivity.this, R.style.DialogStyle)
                    .setTitle("Confirmation")
                    .setMessage("Delete this job post? All the job application for this job will be removed automatically.\n" +
                            "You cannot undo this action.")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            deleteJobPost();
                        }
                    })
                    .setNegativeButton("Cancel", null);
            builder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void calculatePosition() {

        final TextView tvPosition = (TextView) findViewById(R.id.tv_job_position);

        final Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    if (success) {
                        int position = jsonResponse.getInt("total");
                        tvPosition.setText(String.format(
                                Locale.ENGLISH, "%d/%d", position, jobPost.getPositionNumber()));
                    }
//                    mProgressDialog.toggleProgressDialog();
                } catch (JSONException e) {
                    e.printStackTrace();
                    tvPosition.setText(null);
//                    mProgressDialog.toggleProgressDialog();
                }
            }
        };

        CompanyJobDetailActivity.CalculatePositionRequest calculatePositionRequest
                = new CompanyJobDetailActivity.CalculatePositionRequest(
                jobPost.getId(),
                root + getString(R.string.url_get_job_application_for_company),
                responseListener,
                null
        );
        mRequestQueue.add(calculatePositionRequest);
    }

    private void deleteJobPost() {

        // Show progress dialog
        mProgressDialog.setMessage("Deleting your job post...");
        mProgressDialog.toggleProgressDialog();

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
                    mProgressDialog.toggleProgressDialog();
                    //show message from server
                    AlertDialog.Builder builder = new AlertDialog.Builder(CompanyJobDetailActivity.this);
                    builder.setTitle(title)
                            .setMessage(jsonResponse.getString("msg"))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .create()
                            .show();

                } catch (JSONException e) {

                    e.printStackTrace();
                    //To close progress dialog
                    mProgressDialog.toggleProgressDialog();
                    //If exception, then show alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(CompanyJobDetailActivity.this);
                    builder.setMessage(e.getMessage())
                            .setPositiveButton("OK", null)
                            .create()
                            .show();
                }
            }
        };

        CompanyJobDetailActivity.DeleteJobPostRequest deleteJobPostRequest
                = new CompanyJobDetailActivity.DeleteJobPostRequest(
                jobPost.getId(),
                root + getString(R.string.url_delete_job_post),
                responseListener,
                null
        );
        deleteJobPostRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(deleteJobPostRequest);
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
            params.put("status", "Approved");
        }

        public Map<String, String> getParams() {
            return params;
        }
    }

    private class DeleteJobPostRequest extends StringRequest {

        private Map<String, String> params;

        DeleteJobPostRequest(
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

}
