package com.TimeToWork.TimeToWork.NavigationFragment;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.TextView;

import com.TimeToWork.TimeToWork.Adapter.PaymentHistoryAdapter;
import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.CustomClass.CustomVolleyErrorListener;
import com.TimeToWork.TimeToWork.CustomClass.MonthYearPickerDialog;
import com.TimeToWork.TimeToWork.Database.Entity.JobLocation;
import com.TimeToWork.TimeToWork.Database.Entity.JobPost;
import com.TimeToWork.TimeToWork.Database.Entity.Payment;
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
import static com.TimeToWork.TimeToWork.MainApplication.userId;

public class PaymentHistoryFragment extends Fragment {

    private CustomProgressDialog mProgressDialog;
    private SwipeRefreshLayout swipeContainer;
    private TextView tvEmpty;

    private PaymentHistoryAdapter adapter;
    private List<Payment> paymentList;
    private List<JobPost> jobPostList;

    private MonthYearPickerDialog monthYearPickerDialog;
    private int month = 0;
    private int year = 0;

    public PaymentHistoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(getString(R.string.fragment_payment_history));

        // Enable menu in toolbar
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_payment_history, container, false);

        mProgressDialog = new CustomProgressDialog(getActivity());
        tvEmpty = (TextView) view.findViewById(R.id.tv_empty_payment_record);

        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

            @Override
            public void onRefresh() {
                setupPaymentHistory();
            }
        });
        swipeContainer.setColorSchemeResources(R.color.colorAccent);

        paymentList = new ArrayList<>();
        jobPostList = new ArrayList<>();
        adapter = new PaymentHistoryAdapter(getActivity(), paymentList, jobPostList);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_payment_history);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);

        monthYearPickerDialog = new MonthYearPickerDialog();
        monthYearPickerDialog.setListener(
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int selectedYear,
                                          int monthOfYear, int dayOfMonth) {

                        if (month != monthOfYear || year != selectedYear) {
                            month = monthOfYear;
                            year = selectedYear;

                            // Show progress dialog at the beginning
                            mProgressDialog.setMessage("Filtering payment record ...");
                            mProgressDialog.show();

                            setupPaymentHistory();
                        }
                    }
                });
        monthYearPickerDialog.setTitle("Filter Payment History");

        // Show progress dialog at the beginning
        mProgressDialog.setMessage("Getting payment record ...");
        mProgressDialog.show();

        //Getting all payment record
        setupPaymentHistory();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_payment_history, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.date_range) {
            monthYearPickerDialog.show(getActivity().getSupportFragmentManager(),
                    "MonthYearPickerDialog");
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupPaymentHistory() {

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
                            JSONArray jobPostArray = jsonResponse.getJSONArray("PAYMENTHISTORY");

                            for (int i = 0; i < jobPostArray.length(); i++) {

                                JSONObject jsonobject = jobPostArray.getJSONObject(i);

                                Payment payment = new Payment();
                                payment.setId(jsonobject.getString("payment_id"));
                                payment.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
                                        .parse(jsonobject.getString("payment_date")));
                                payment.setAmount(jsonobject.getDouble("payment_amount"));
                                payment.setCardNumber(jsonobject.getString("payment_card_number"));

                                JobLocation jobLocation = new JobLocation();
                                jobLocation.setId(jsonobject.getString("job_location_id"));
                                jobLocation.setName(jsonobject.getString("job_location_name"));
                                jobLocation.setAddress(jsonobject.getString("job_location_address"));

                                JobPost jobPost = new JobPost();
                                jobPost.setId(jsonobject.getString("job_post_id"));
                                jobPost.setJobLocation(jobLocation);
                                jobPost.setTitle(jsonobject.getString("job_post_title"));
                                jobPost.setPostedDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
                                        .parse(jsonobject.getString("job_post_posted_date")));
                                jobPost.setWorkingDate(jsonobject.getString("job_post_working_date"));
                                jobPost.setWorkingTime(jsonobject.getString("job_post_working_timetable"));
                                jobPost.setCategory(jsonobject.getString("job_post_category"));
                                jobPost.setRequirement(jsonobject.getString("job_post_requirement"));
                                jobPost.setDescription(jsonobject.getString("job_post_description"));
                                jobPost.setNote(jsonobject.getString("job_post_note"));
                                jobPost.setWages(jsonobject.getDouble("job_post_wages"));
                                jobPost.setPaymentTerm(jsonobject.getInt("job_post_payment_term"));
                                jobPost.setPositionNumber(jsonobject.getInt("job_post_position_num"));
                                jobPost.setAds(jsonobject.getInt("job_post_isAds") == 1);
                                jobPost.setStatus(jsonobject.getString("job_post_status"));
                                jobPost.setPreferGender(jsonobject.getString("job_post_prefer_gender"));

                                paymentList.add(payment);
                                jobPostList.add(jobPost);

                                tvEmpty.setVisibility(View.GONE);
                            }
                        } else {
                            tvEmpty.setVisibility(View.VISIBLE);
                        }
                        adapter.notifyDataSetChanged();
                        swipeContainer.setRefreshing(false);

                    } else {
                        //If failed, then show alert dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
        PaymentHistoryFragment.FetchPaymentHistoryRequest fetchPaymentHistoryRequest
                = new PaymentHistoryFragment.FetchPaymentHistoryRequest(
                month,
                year,
                root + getString(R.string.url_get_payment_history),
                responseListener,
                errorListener
        );
        mRequestQueue.add(fetchPaymentHistoryRequest);
    }

    private class FetchPaymentHistoryRequest extends StringRequest {

        private Map<String, String> params;

        FetchPaymentHistoryRequest(
                int month,
                int year,
                String url,
                Response.Listener<String> listener,
                Response.ErrorListener errorListener) {
            super(Method.POST, url, listener, errorListener);

            params = new HashMap<>();
            params.put("company_id", userId);
            params.put("month", Integer.toString(month));
            params.put("year", Integer.toString(year));
        }

        public Map<String, String> getParams() {
            return params;
        }
    }
}
