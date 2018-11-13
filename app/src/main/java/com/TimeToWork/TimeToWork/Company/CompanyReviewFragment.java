package com.TimeToWork.TimeToWork.Company;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.TimeToWork.TimeToWork.Adapter.ReviewAdapter;
import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.CustomClass.CustomVolleyErrorListener;
import com.TimeToWork.TimeToWork.Database.Entity.Company;
import com.TimeToWork.TimeToWork.Database.Entity.Jobseeker;
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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.TimeToWork.TimeToWork.MainApplication.mRequestQueue;
import static com.TimeToWork.TimeToWork.MainApplication.root;

public class CompanyReviewFragment extends Fragment {

    private CustomProgressDialog mProgressDialog;

    private ReviewAdapter adapter;
    private List<Review> reviewList;
    private int[] totalTagList = {0, 0, 0, 0};

    private Company company;

    private TagTotalNumberCallBack mCallback;

    public CompanyReviewFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_company_review, container, false);

        mProgressDialog = new CustomProgressDialog(getActivity());
        company = (Company) getArguments().getSerializable("COMPANY");

        reviewList = new ArrayList<>();
        adapter = new ReviewAdapter(reviewList);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_review_list);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);

        getReviewList();

        return view;
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        try {
            mCallback = (TagTotalNumberCallBack) context;
        } catch (ClassCastException ignored) {
        }
    }

    @Override
    public void onDetach() {

        mCallback = null;
        super.onDetach();
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

                                Jobseeker jobseeker = new Jobseeker();
                                jobseeker.setName(jsonobject.getString("jobseeker_name"));
                                jobseeker.setImg(jsonobject.getString("jobseeker_img"));

                                Review review = new Review();
                                review.setId(jsonobject.getString("review_id"));
                                review.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
                                        .parse(jsonobject.getString("review_date")));
                                review.setStarValue(jsonobject.getDouble("review_star_value"));
                                review.setTag(jsonobject.getString("review_tag"));
                                review.setComment(jsonobject.getString("review_comment"));
                                review.setComment(jsonobject.getString("review_comment"));
                                review.setJobseeker(jobseeker);

                                switch (review.getTag()) {
                                    case "good company":
                                        totalTagList[0]++;
                                        break;
                                    case "high wages":
                                        totalTagList[1]++;
                                        break;
                                    case "training provided":
                                        totalTagList[2]++;
                                        break;
                                    case "helpful":
                                        totalTagList[3]++;
                                        break;
                                }
                                reviewList.add(review);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        //If failed, then show alert dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setMessage(jsonResponse.getString("msg"))
                                .setPositiveButton("OK", null)
                                .create()
                                .show();
                    }
                    mCallback.setTagTotalNumber(totalTagList);
                    //To close progress dialog
                    mProgressDialog.dismiss();

                } catch (JSONException | ParseException e) {

                    e.printStackTrace();
                    //To close progress dialog
                    mProgressDialog.toggleProgressDialog();
                    //If exception, then show alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(e.getMessage())
                            .setPositiveButton("OK", null)
                            .create()
                            .show();
                }
            }
        };

        CustomVolleyErrorListener errorListener
                = new CustomVolleyErrorListener(getActivity(), mProgressDialog, mRequestQueue);
        GetReviewRequest fetchJobApplicationRequest = new GetReviewRequest(
                root + getString(R.string.url_get_review_of_company),
                responseListener,
                errorListener
        );
        mRequestQueue.add(fetchJobApplicationRequest);
    }

    private class GetReviewRequest extends StringRequest {

        private Map<String, String> params;

        GetReviewRequest(
                String url,
                Response.Listener<String> listener,
                Response.ErrorListener errorListener) {
            super(Method.POST, url, listener, errorListener);

            params = new HashMap<>();
            params.put("company_id", company.getId());
        }

        public Map<String, String> getParams() {
            return params;
        }
    }

    interface TagTotalNumberCallBack {
        void setTagTotalNumber(int[] tagTotalNumber);
    }
}
