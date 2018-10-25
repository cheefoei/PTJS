package com.TimeToWork.TimeToWork.Jobseeker;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.CustomClass.CustomVolleyErrorListener;
import com.TimeToWork.TimeToWork.Database.Entity.Review;
import com.TimeToWork.TimeToWork.R;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.TimeToWork.TimeToWork.MainApplication.mRequestQueue;
import static com.TimeToWork.TimeToWork.MainApplication.root;

public class JobseekerViewReviewActivity extends AppCompatActivity {

    private CustomProgressDialog mProgressDialog;
    private Review review;

    private List<String> tagStrings;
    private List<TextView> tags;

    private RatingBar ratingBar;
    private TextView tvComment;
    private TextView tvDate;

    private LinearLayout layoutView;
    private LinearLayout layoutUpdate;
    private EditText etReview;
    private Button btnUpdate;

    private SimpleDateFormat dateFormat;
    private int currentSelectedTag = -1;
    private boolean isEditable = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobseeker_view_review);

        mProgressDialog = new CustomProgressDialog(JobseekerViewReviewActivity.this);
        review = (Review) getIntent().getSerializableExtra("REVIEW");

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

                    if (isEditable) {
                        changeTagBackground(finalI);
                    }
                }
            });

            if (review.getTag().equals(tagStrings.get(i))) {
                changeTagBackground(finalI);
                currentSelectedTag = finalI;
            }
        }

        ratingBar = (RatingBar) findViewById(R.id.rate_bar_review);
        ratingBar.setRating((float) review.getStarValue());

        tvComment = (TextView) findViewById(R.id.tv_comment);
        tvComment.setText(review.getComment());

        dateFormat = new SimpleDateFormat("dd MMMM yyyy, HH:mm:ss", Locale.ENGLISH);
        tvDate = (TextView) findViewById(R.id.tv_review_date);
        tvDate.setText(String.format("Written on %s", dateFormat.format(review.getDate())));
    }

    @Override
    public void onBackPressed() {

        if (isEditable) {
            nonEditableUI();
            isEditable = false;
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_view_review, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.review_update) {

            isEditable = true;
            setupEditableUI();
        } else if (id == R.id.review_delete) {

            AlertDialog.Builder builder = new AlertDialog.Builder(JobseekerViewReviewActivity.this);
            builder.setTitle("Confirmation")
                    .setMessage("Confirm to remove this review? You cannot recover this review after performed this action.")
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            removeReview();
                        }
                    })
                    .setNegativeButton("No", null)
                    .create()
                    .show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void changeTagBackground(int index) {

        if (currentSelectedTag != index) {
            if (currentSelectedTag >= 0) {
                tags.get(currentSelectedTag).setBackground(getDrawable(R.drawable.bg_review_tag_null));
                tags.get(currentSelectedTag).setTextColor(
                        ContextCompat.getColor(JobseekerViewReviewActivity.this, R.color.colorTextPrimary));
                tags.get(index).setBackground(getDrawable(R.drawable.bg_review_tag_accent));
                tags.get(index).setTextColor(
                        ContextCompat.getColor(JobseekerViewReviewActivity.this, R.color.colorWhite));
            } else {
                tags.get(index).setBackground(getDrawable(R.drawable.bg_review_tag_accent));
                tags.get(index).setTextColor(
                        ContextCompat.getColor(JobseekerViewReviewActivity.this, R.color.colorWhite));
            }
            currentSelectedTag = index;
        }
    }

    private void setupEditableUI() {

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Updating Job Review");
        }

        layoutView = (LinearLayout) findViewById(R.id.layout_view_comment);
        layoutView.setVisibility(View.GONE);

        layoutUpdate = (LinearLayout) findViewById(R.id.layout_update_comment);
        layoutUpdate.setVisibility(View.VISIBLE);

        ratingBar.setIsIndicator(false);

        etReview = (EditText) findViewById(R.id.et_job_review);
        etReview.setText(review.getComment());

        btnUpdate = (Button) findViewById(R.id.btn_update_review);
        btnUpdate.setVisibility(View.VISIBLE);
        btnUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (etReview.getText().toString().equals("")) {
                    etReview.setError(getString(R.string.error_required_field));
                } else {
                    updateReview();
                }
            }
        });
    }

    private void nonEditableUI() {

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(getString(R.string.activity_view_review));
        }

        layoutView.setVisibility(View.VISIBLE);
        layoutUpdate.setVisibility(View.GONE);

        ratingBar.setIsIndicator(true);
        btnUpdate.setVisibility(View.VISIBLE);
    }

    private void updateReview() {

        // Show progress dialog
        mProgressDialog.setMessage("Update your review ...");
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(JobseekerViewReviewActivity.this);
                    builder.setTitle(title)
                            .setMessage(jsonResponse.getString("msg"))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    tvComment.setText(etReview.getText().toString());
                                    tvDate.setText(String.format("Written on %s", dateFormat.format(new Date())));
                                    isEditable = false;
                                    nonEditableUI();
                                }
                            })
                            .create()
                            .show();

                } catch (JSONException e) {

                    e.printStackTrace();
                    //To close progress dialog
                    mProgressDialog.toggleProgressDialog();
                    //If exception, then show alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(JobseekerViewReviewActivity.this);
                    builder.setMessage(e.getMessage())
                            .setPositiveButton("OK", null)
                            .create()
                            .show();
                }
            }
        };

        CustomVolleyErrorListener errorListener
                = new CustomVolleyErrorListener(JobseekerViewReviewActivity.this, mProgressDialog, mRequestQueue);

        JobseekerViewReviewActivity.UpdateReviewRequest updateReviewRequest = new JobseekerViewReviewActivity.UpdateReviewRequest(
                etReview.getText().toString(),
                tagStrings.get(currentSelectedTag),
                Float.toString(ratingBar.getRating()),
                root + getString(R.string.url_update_review_by_jobseeker),
                responseListener,
                errorListener
        );
        updateReviewRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(updateReviewRequest);
    }

    private void removeReview() {

        // Show progress dialog
        mProgressDialog.setMessage("Removing your review …");
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(JobseekerViewReviewActivity.this);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(JobseekerViewReviewActivity.this);
                    builder.setMessage(e.getMessage())
                            .setPositiveButton("OK", null)
                            .create()
                            .show();
                }
            }
        };

        CustomVolleyErrorListener errorListener
                = new CustomVolleyErrorListener(JobseekerViewReviewActivity.this, mProgressDialog, mRequestQueue);

        JobseekerViewReviewActivity.DeleteReviewRequest deleteReviewRequest = new JobseekerViewReviewActivity.DeleteReviewRequest(
                root + getString(R.string.url_delete_review_by_jobseeker),
                responseListener,
                errorListener
        );
        deleteReviewRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        mRequestQueue.add(deleteReviewRequest);
    }

    private class UpdateReviewRequest extends StringRequest {

        private Map<String, String> params;

        UpdateReviewRequest(
                String comment,
                String tag,
                String starValue,
                String url,
                Response.Listener<String> listener,
                Response.ErrorListener errorListener) {
            super(Method.POST, url, listener, errorListener);

            params = new HashMap<>();
            params.put("review_id", review.getId());
            params.put("comment", comment);
            params.put("tag", tag);
            params.put("star_value", starValue);
        }

        public Map<String, String> getParams() {
            return params;
        }
    }

    private class DeleteReviewRequest extends StringRequest {

        private Map<String, String> params;

        DeleteReviewRequest(
                String url,
                Response.Listener<String> listener,
                Response.ErrorListener errorListener) {
            super(Method.POST, url, listener, errorListener);

            params = new HashMap<>();
            params.put("review_id", review.getId());
        }

        public Map<String, String> getParams() {
            return params;
        }
    }
}
