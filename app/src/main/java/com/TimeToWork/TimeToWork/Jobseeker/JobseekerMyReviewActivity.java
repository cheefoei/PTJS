package com.TimeToWork.TimeToWork.Jobseeker;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.TimeToWork.TimeToWork.Adapter.ReviewAdapter;
import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.CustomClass.CustomVolleyErrorListener;
import com.TimeToWork.TimeToWork.Database.Entity.Company;
import com.TimeToWork.TimeToWork.Database.Entity.Review;
import com.TimeToWork.TimeToWork.R;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.TimeToWork.TimeToWork.MainApplication.mRequestQueue;
import static com.TimeToWork.TimeToWork.MainApplication.root;
import static com.TimeToWork.TimeToWork.MainApplication.userId;

public class JobseekerMyReviewActivity extends AppCompatActivity {

    private CustomProgressDialog mProgressDialog;

    private List<TextView> tags;
    private List<String> tagStrings;
    private int totalReview = 0;

    private ReviewAdapter adapter;
    private List<Review> reviewList;
    private int[] totalTagList = {0, 0, 0, 0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobseeker_my_review);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
        }
        mProgressDialog = new CustomProgressDialog(this);

        tagStrings = Arrays.asList(
                getResources().getStringArray(R.array.array_review_tag_company));

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
            TextView tag = tags.get(i);
            tag.setText(tagStrings.get(i));
        }

        reviewList = new ArrayList<>();
        adapter = new ReviewAdapter(reviewList, "Company");

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rv_review_list);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);

        getReviewList();
    }

    private void getReviewList() {

        // Show progress dialog
        mProgressDialog.setMessage("Loading …");
        mProgressDialog.toggleProgressDialog();

        final Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    int total = jsonResponse.getInt("total");

                    if (success) {

                        // Clear all item inside recycler view
                        adapter.clear();

                        if (total != 0) {

                            // Convert job post list to json array
                            JSONArray jobPostArray = jsonResponse.getJSONArray("REVIEW");

                            for (int i = 0; i < jobPostArray.length(); i++) {

                                JSONObject jsonobject = jobPostArray.getJSONObject(i);

                                Company company = new Company();
                                company.setName(jsonobject.getString("company_name"));
                                company.setImg(jsonobject.getString("company_img"));

                                Review review = new Review();
                                review.setId(jsonobject.getString("review_id"));
                                review.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
                                        .parse(jsonobject.getString("review_date")));
                                review.setStarValue(jsonobject.getDouble("review_star_value"));
                                review.setTag(jsonobject.getString("review_tag"));
                                review.setComment(jsonobject.getString("review_comment"));
                                review.setComment(jsonobject.getString("review_comment"));
                                review.setCompany(company);

                                switch (review.getTag()) {
                                    case "hardworking":
                                        totalTagList[0]++;
                                        break;
                                    case "punctual":
                                        totalTagList[1]++;
                                        break;
                                    case "open-mind":
                                        totalTagList[2]++;
                                        break;
                                    case "satisfied":
                                        totalTagList[3]++;
                                        break;
                                }
                                reviewList.add(review);
                            }
                        }

                        for (int i = 0; i < tags.size(); i++) {
                            TextView tv = tags.get(i);
                            tv.setText(tagStrings.get(i) + " (" + totalTagList[i] + ")");
                            totalReview += totalTagList[i];
                        }
                        JobseekerMyReviewActivity.this.setTitle(
                                JobseekerMyReviewActivity.this.getTitle() + " (" + totalReview + ")");
                        adapter.notifyDataSetChanged();

                    } else {
                        //If failed, then show alert dialog
                        AlertDialog.Builder builder
                                = new AlertDialog.Builder(JobseekerMyReviewActivity.this);
                        builder.setMessage(jsonResponse.getString("msg"))
                                .setPositiveButton("OK", null)
                                .create()
                                .show();
                    }
                    //To close progress dialog
                    mProgressDialog.dismiss();

                } catch (JSONException | ParseException e) {

                    e.printStackTrace();
                    //To close progress dialog
                    mProgressDialog.toggleProgressDialog();
                    //If exception, then show alert dialog
                    AlertDialog.Builder builder
                            = new AlertDialog.Builder(JobseekerMyReviewActivity.this);
                    builder.setMessage(e.getMessage())
                            .setPositiveButton("OK", null)
                            .create()
                            .show();
                }
            }
        };

        CustomVolleyErrorListener errorListener
                = new CustomVolleyErrorListener(this, mProgressDialog, mRequestQueue);
        GetReviewRequest getReviewRequest = new GetReviewRequest(
                root + getString(R.string.url_get_review_of_jobseeker),
                responseListener,
                errorListener
        );
        mRequestQueue.add(getReviewRequest);
    }

    private class GetReviewRequest extends StringRequest {

        private Map<String, String> params;

        GetReviewRequest(
                String url,
                Response.Listener<String> listener,
                Response.ErrorListener errorListener) {
            super(Method.POST, url, listener, errorListener);

            params = new HashMap<>();
            params.put("jobseeker_id", userId);
        }

        public Map<String, String> getParams() {
            return params;
        }
    }
}
