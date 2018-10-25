package com.TimeToWork.TimeToWork.Jobseeker;

import android.content.DialogInterface;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;

import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.CustomClass.CustomVolleyErrorListener;
import com.TimeToWork.TimeToWork.Database.Entity.JobApplication;
import com.TimeToWork.TimeToWork.R;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.TimeToWork.TimeToWork.MainApplication.mRequestQueue;
import static com.TimeToWork.TimeToWork.MainApplication.root;
import static com.TimeToWork.TimeToWork.MainApplication.userId;

public class JobseekerProvideReviewActivity extends AppCompatActivity {

    private CustomProgressDialog mProgressDialog;
    private JobApplication jobApplication;

    private List<String> tagStrings;
    private List<TextView> tags;

    private RatingBar ratingBar;
    private EditText etReview;
    private int currentSelectedTag = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobseeker_provide_review);

        mProgressDialog = new CustomProgressDialog(JobseekerProvideReviewActivity.this);
        jobApplication = (JobApplication) getIntent().getSerializableExtra("JOBAPPLICATION");

        tagStrings = Arrays.asList(getResources().getStringArray(R.array.array_review_tag_jobseeker));

        TextView tvTag1 = (TextView) findViewById(R.id.tv_review_tag_1);
        TextView tvTag2 = (TextView) findViewById(R.id.tv_review_tag_2);
        TextView tvTag3 = (TextView) findViewById(R.id.tv_review_tag_3);
        TextView tvTag4 = (TextView) findViewById(R.id.tv_review_tag_4);

        tags = new ArrayList<>();
        tags.add(tvTag1);
        tags.add(tvTag2);
        tags.add(tvTag3);
        tags.add(tvTag4);

        for (int i = 0; i < tags.size(); i++) {

            final int finalI = i;

            TextView tag = tags.get(i);
            tag.setText(tagStrings.get(i));
            tag.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    changeTagBackground(finalI);
                }
            });
        }

        ratingBar = (RatingBar) findViewById(R.id.rate_bar_review);
        etReview = (EditText) findViewById(R.id.et_job_review);

        Button btnSubmit = (Button) findViewById(R.id.btn_submit_review);
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (etReview.getText().toString().equals("")) {
                    etReview.setError(getString(R.string.error_required_field));
                } else {
                    submitNewReview();
                }
            }
        });
    }

    private void changeTagBackground(int index) {

        if (currentSelectedTag != index) {
            if (currentSelectedTag >= 0) {
                tags.get(currentSelectedTag).setBackground(getDrawable(R.drawable.bg_review_tag_null));
                tags.get(currentSelectedTag).setTextColor(
                        ContextCompat.getColor(JobseekerProvideReviewActivity.this, R.color.colorTextPrimary));
                tags.get(index).setBackground(getDrawable(R.drawable.bg_review_tag_accent));
                tags.get(index).setTextColor(
                        ContextCompat.getColor(JobseekerProvideReviewActivity.this, R.color.colorWhite));
            } else {
                tags.get(index).setBackground(getDrawable(R.drawable.bg_review_tag_accent));
                tags.get(index).setTextColor(
                        ContextCompat.getColor(JobseekerProvideReviewActivity.this, R.color.colorWhite));
            }
            currentSelectedTag = index;
        }
    }

    private void submitNewReview() {

        // Show progress dialog
        mProgressDialog.setMessage("Submitting your review …");
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
                    //If failed, then show alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(JobseekerProvideReviewActivity.this);
                    builder.setTitle(title)
                            .setMessage(jsonResponse.getString("msg"))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    JobseekerProvideReviewActivity.this.finish();
                                }
                            })
                            .create()
                            .show();

                } catch (JSONException e) {

                    e.printStackTrace();
                    //To close progress dialog
                    mProgressDialog.toggleProgressDialog();
                    //If exception, then show alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(JobseekerProvideReviewActivity.this);
                    builder.setMessage(e.getMessage())
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    JobseekerProvideReviewActivity.this.finish();
                                }
                            })
                            .create()
                            .show();

                }
            }
        };

        CustomVolleyErrorListener errorListener
                = new CustomVolleyErrorListener(JobseekerProvideReviewActivity.this, mProgressDialog, mRequestQueue);

        JobseekerProvideReviewActivity.SubmitReviewRequest submitReviewRequest = new JobseekerProvideReviewActivity.SubmitReviewRequest(
                jobApplication.getId(),
                etReview.getText().toString(),
                tagStrings.get(currentSelectedTag),
                Float.toString(ratingBar.getRating()),
                root + getString(R.string.url_submit_review_by_jobseeker),
                responseListener,
                errorListener
        );
        submitReviewRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(submitReviewRequest);
    }

    private class SubmitReviewRequest extends StringRequest {

        private Map<String, String> params;

        SubmitReviewRequest(
                String jobApplicationId,
                String comment,
                String tag,
                String starValue,
                String url,
                Response.Listener<String> listener,
                Response.ErrorListener errorListener) {
            super(Method.POST, url, listener, errorListener);

            params = new HashMap<>();
            params.put("job_application_id", jobApplicationId);
            params.put("jobseeker_id", userId);
            params.put("comment", comment);
            params.put("tag", tag);
            params.put("star_value", starValue);
        }

        public Map<String, String> getParams() {
            return params;
        }
    }
}
