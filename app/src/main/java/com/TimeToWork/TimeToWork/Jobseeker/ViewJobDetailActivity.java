package com.TimeToWork.TimeToWork.Jobseeker;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.CustomClass.CustomVolleyErrorListener;
import com.TimeToWork.TimeToWork.Database.Entity.Company;
import com.TimeToWork.TimeToWork.Database.Entity.JobApplication;
import com.TimeToWork.TimeToWork.Database.Entity.JobLocation;
import com.TimeToWork.TimeToWork.Database.Entity.JobPost;
import com.TimeToWork.TimeToWork.R;
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
import static com.TimeToWork.TimeToWork.MainApplication.userId;

public class ViewJobDetailActivity extends AppCompatActivity {

    private JobPost jobPost;
    private JobApplication jobApplication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);

        jobPost = (JobPost) getIntent().getSerializableExtra("JOB");
        JobLocation jobLocation = (JobLocation) getIntent().getSerializableExtra("JOBLOCATION");
        Company company = (Company) getIntent().getSerializableExtra("COMPANY");

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

        TextView tvCompanyName = (TextView) findViewById(R.id.tv_company_name);
        tvCompanyName.setText(company.getName());

        RatingBar ratingBarCompany = (RatingBar) findViewById(R.id.rate_bar_company);
        ratingBarCompany.setRating((float) company.getRating());

        TextView tvRating = (TextView) findViewById(R.id.tv_company_rating);
        tvRating.setText(String.format(Locale.ENGLISH, "%.1f", company.getRating()));

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

        Button btnApply = (Button) findViewById(R.id.btn_apply_job);
        btnApply.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                applyJob();
            }
        });

        boolean apply = getIntent().getBooleanExtra("APPLY", true);
        if (!apply) {
            btnApply.setVisibility(View.GONE);
        }

        boolean cancel = getIntent().getBooleanExtra("CANCEL", false);
        if (cancel) {

            jobApplication = (JobApplication) getIntent().getSerializableExtra("JOBAPPLICATION");

            Button btnCancel = (Button) findViewById(R.id.btn_cancel_job);
            btnCancel.setVisibility(View.VISIBLE);
            btnCancel.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    AlertDialog.Builder builder = new AlertDialog.Builder(ViewJobDetailActivity.this);
                    builder.setTitle("Confirmation")
                            .setMessage("Confirm to cancel this job? You cannot apply this job again after performed this action.")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    cancelJob();
                                }
                            })
                            .setNegativeButton("No", null)
                            .create()
                            .show();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_view_job_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

//        int id = item.getItemId();

//        if (id == R.id.company_profile) {
//
//        } else if (id == R.id.share) {
//
//        }
        return super.onOptionsItemSelected(item);
    }

    private void applyJob() {

        //Show progress dialog
        final CustomProgressDialog mProgressDialog = new CustomProgressDialog(ViewJobDetailActivity.this);
        mProgressDialog.setMessage(getString(R.string.progress_applying_job));
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
                    //To show message from server
                    AlertDialog.Builder builder = new AlertDialog.Builder(ViewJobDetailActivity.this);
                    builder.setMessage(jsonResponse.getString("msg"))
                            .setTitle(title)
                            .setPositiveButton("OK", null)
                            .create()
                            .show();

                } catch (JSONException e) {

                    e.printStackTrace();
                    //To close progress dialog
                    mProgressDialog.toggleProgressDialog();
                    //If exception, then show alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(ViewJobDetailActivity.this);
                    builder.setMessage(e.getMessage())
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ViewJobDetailActivity.this.finish();
                                }
                            })
                            .create()
                            .show();
                }
            }
        };

        CustomVolleyErrorListener errorListener
                = new CustomVolleyErrorListener(ViewJobDetailActivity.this, mProgressDialog, mRequestQueue);
        ViewJobDetailActivity.ApplyJobRequest loginRequest = new ViewJobDetailActivity.ApplyJobRequest(
                jobPost.getId(),
                userId,
                root + getString(R.string.url_apply_job),
                responseListener,
                errorListener
        );
        mRequestQueue.add(loginRequest);
    }

    private void cancelJob() {

        //Show progress dialog
        final CustomProgressDialog mProgressDialog = new CustomProgressDialog(ViewJobDetailActivity.this);
        mProgressDialog.setMessage(getString(R.string.progress_cancel_job));
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
                    //To show message from server
                    AlertDialog.Builder builder = new AlertDialog.Builder(ViewJobDetailActivity.this);
                    builder.setMessage(jsonResponse.getString("msg"))
                            .setTitle(title)
                            .setPositiveButton("OK", null)
                            .create()
                            .show();

                } catch (JSONException e) {

                    e.printStackTrace();
                    //To close progress dialog
                    mProgressDialog.toggleProgressDialog();
                    //If exception, then show alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(ViewJobDetailActivity.this);
                    builder.setMessage(e.getMessage())
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ViewJobDetailActivity.this.finish();
                                }
                            })
                            .create()
                            .show();
                }
            }
        };

        CustomVolleyErrorListener errorListener
                = new CustomVolleyErrorListener(ViewJobDetailActivity.this, mProgressDialog, mRequestQueue);
        ViewJobDetailActivity.CancelJobRequest cancelJobRequest
                = new ViewJobDetailActivity.CancelJobRequest(
                jobApplication.getId(),
                jobPost.getId(),
                userId,
                root + getString(R.string.url_cancel_job),
                responseListener,
                errorListener
        );
        mRequestQueue.add(cancelJobRequest);
    }

    private class ApplyJobRequest extends StringRequest {

        private Map<String, String> params;

        ApplyJobRequest(String jobPostId,
                        String jobseekerId,
                        String url,
                        Response.Listener<String> listener,
                        Response.ErrorListener errorListener) {

            super(Method.POST, url, listener, errorListener);

            params = new HashMap<>();
            params.put("job_post_id", jobPostId);
            params.put("jobseeker_id", jobseekerId);
        }

        public Map<String, String> getParams() {
            return params;
        }
    }

    private class CancelJobRequest extends StringRequest {

        private Map<String, String> params;

        CancelJobRequest(String jobApplicationId,
                         String jobPostId,
                         String jobseekerId,
                         String url,
                         Response.Listener<String> listener,
                         Response.ErrorListener errorListener) {

            super(Method.POST, url, listener, errorListener);

            params = new HashMap<>();
            params.put("job_application_id", jobApplicationId);
            params.put("job_post_id", jobPostId);
            params.put("jobseeker_id", jobseekerId);
        }

        public Map<String, String> getParams() {
            return params;
        }
    }
}
