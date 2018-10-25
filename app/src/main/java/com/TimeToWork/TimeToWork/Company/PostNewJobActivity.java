package com.TimeToWork.TimeToWork.Company;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.CustomClass.CustomVolleyErrorListener;
import com.TimeToWork.TimeToWork.Database.Entity.JobLocation;
import com.TimeToWork.TimeToWork.Database.Entity.JobPost;
import com.TimeToWork.TimeToWork.Database.Entity.WorkingSchedule;
import com.TimeToWork.TimeToWork.Database.Entity.WorkingTime;
import com.TimeToWork.TimeToWork.R;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.TimeToWork.TimeToWork.MainApplication.mRequestQueue;
import static com.TimeToWork.TimeToWork.MainApplication.root;
import static com.TimeToWork.TimeToWork.MainApplication.userId;
import static com.google.android.gms.location.places.ui.PlaceAutocomplete.*;

public class PostNewJobActivity extends AppCompatActivity implements
        OwnLocationFragment.OnCallbackReceived, SetWorkingScheduleFragment.OnCallbackReceived {

    private EditText etTitle, etLocation, etWages, etPosition,
            etRequirement, etDescription, etNote;
    private Spinner mSpinnerCategory, mSpinnerPaymentTerm, mSpinnerPreferGender;
    private Button btnSetSchedule;

    private List<Date> workingDateList;
    private WorkingTime workingTime;

    private JobLocation jobLocation;

    private CustomProgressDialog mProgressDialog;

    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job_post);

        mProgressDialog = new CustomProgressDialog(this);

        etTitle = (EditText) findViewById(R.id.et_job_title);
        etWages = (EditText) findViewById(R.id.et_wages);
        etPosition = (EditText) findViewById(R.id.et_position_number);
        etRequirement = (EditText) findViewById(R.id.et_job_requirement);
        etDescription = (EditText) findViewById(R.id.et_job_description);
        etNote = (EditText) findViewById(R.id.et_job_note);

        mSpinnerCategory = (Spinner) findViewById(R.id.spinner_category);
        mSpinnerPaymentTerm = (Spinner) findViewById(R.id.spinner_payment_term);
        mSpinnerPreferGender = (Spinner) findViewById(R.id.spinner_prefer_gender);

        etLocation = (EditText) findViewById(R.id.et_job_location);
        etLocation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AutocompleteFilter autocompleteFilter = new AutocompleteFilter.Builder()
                        .setTypeFilter(Place.TYPE_COUNTRY)
                        .setCountry("MY")
                        .build();
                try {
                    Intent intent = new IntentBuilder(MODE_FULLSCREEN)
                            .setFilter(autocompleteFilter)
                            .build(PostNewJobActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
                } catch (GooglePlayServicesRepairableException |
                        GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        TextView tvOwnAddress = (TextView) findViewById(R.id.tv_own_location);
        tvOwnAddress.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                OwnLocationFragment ownLocationFragment = OwnLocationFragment.newOwnLocationFragment();
                ownLocationFragment.show(transaction, "dialog");
            }
        });

        btnSetSchedule = (Button) findViewById(R.id.btn_set_schedule);
        btnSetSchedule.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                WorkingSchedule workingSchedule = new WorkingSchedule();
                workingSchedule.setWorkingDateList(workingDateList);
                workingSchedule.setWorkingTime(workingTime);

                Bundle bundle = new Bundle();
                bundle.putSerializable("SCHEDULE", workingSchedule);

                SetWorkingScheduleFragment setWorkingScheduleFragment
                        = SetWorkingScheduleFragment.newSetWorkingScheduleFragment();
                setWorkingScheduleFragment.setArguments(bundle);
                setWorkingScheduleFragment.show(transaction, "dialog");
            }
        });

        Button btnPost = (Button) findViewById(R.id.btn_post_new_job);
        btnPost.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (isValid()) {
                    checkLocationExists();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {

                Place place = PlaceAutocomplete.getPlace(this, data);
                jobLocation = new JobLocation();
                jobLocation.setName(place.getName().toString());
                jobLocation.setAddress(place.getAddress().toString());
                jobLocation.setLatitude(place.getLatLng().latitude);
                jobLocation.setLongitude(place.getLatLng().longitude);

                etLocation.setText(jobLocation.getName());
                etLocation.clearFocus();
                etLocation.setError(null);

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
                Toast.makeText(this, status.getStatusMessage(), Toast.LENGTH_LONG).show();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(this, "Canceled to select location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void updateWorkingSchedule(List<Date> workingDates, WorkingTime workingTime) {

        this.workingDateList = workingDates;
        this.workingTime = workingTime;

        if (!workingDates.isEmpty() && workingTime != null) {
            btnSetSchedule.setText(R.string.string_change);
        }
    }

    @Override
    public void updateLocation(JobLocation jobLocation) {

        this.jobLocation = jobLocation;
        etLocation.setText(jobLocation.getName());
        etLocation.setError(null);
    }

    private boolean isValid() {

        boolean isValid = true;

        if (etTitle.getText().toString().equals("")) {
            etTitle.setError(getString(R.string.error_required_field));
            isValid = false;
        }
        if (etLocation.getText().toString().equals("")) {
            etLocation.setError(getString(R.string.error_required_field));
            isValid = false;
        }
        if (etWages.getText().toString().equals("")) {
            etWages.setError(getString(R.string.error_required_field));
            isValid = false;
        }
        if (etRequirement.getText().toString().equals("")) {
            etRequirement.setError(getString(R.string.error_required_field));
            isValid = false;
        }
        if (etDescription.getText().toString().equals("")) {
            etDescription.setError(getString(R.string.error_required_field));
            isValid = false;
        }

        if (workingDateList == null || workingTime == null) {
            AlertDialog.Builder builder
                    = new AlertDialog.Builder(this, R.style.DialogStyle)
                    .setTitle("Error")
                    .setMessage("Please set working schedule for this job.")
                    .setPositiveButton("OK", null);
            builder.show();
            isValid = false;
        }

        return isValid;
    }

    private void checkLocationExists() {

        // Show progress dialog
        mProgressDialog.setMessage("Verifying job location …");
        mProgressDialog.toggleProgressDialog();

        final Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    //To close progress dialog
                    mProgressDialog.toggleProgressDialog();

                    if (success) {
                        String locationId = jsonResponse.getString("job_location_id");
                        createNewJobPost(locationId);
                    } else {
                        //If failed, then show alert dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(PostNewJobActivity.this);
                        builder.setTitle("Error")
                                .setMessage(jsonResponse.getString("msg"))
                                .setPositiveButton("OK", null)
                                .create()
                                .show();
                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                    //To close progress dialog
                    mProgressDialog.toggleProgressDialog();
                    //If exception, then show alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(PostNewJobActivity.this);
                    builder.setMessage(e.getMessage())
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    PostNewJobActivity.this.finish();
                                }
                            })
                            .create()
                            .show();
                }
            }
        };

        CustomVolleyErrorListener errorListener
                = new CustomVolleyErrorListener(this, mProgressDialog, mRequestQueue);

        PostNewJobActivity.CheckJobLocationRequest checkJobLocationRequest
                = new PostNewJobActivity.CheckJobLocationRequest(
                jobLocation,
                root + getString(R.string.url_check_job_location_exists),
                responseListener,
                errorListener
        );
        mRequestQueue.add(checkJobLocationRequest);
    }

    private void createNewJobPost(String locationId) {

        // Show progress dialog
        mProgressDialog.setMessage("Posting new job …");
        mProgressDialog.toggleProgressDialog();

        final Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    AlertDialog.Builder builder
                            = new AlertDialog.Builder(PostNewJobActivity.this, R.style.DialogStyle);

                    if (success) {
                        builder.setTitle("Successful")
                                .setMessage("Job post is posted successfully. Do you want to make advertisement for this job post? ")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intent = new Intent(PostNewJobActivity.this, PaymentActivity.class);
                                        startActivity(intent);
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        PostNewJobActivity.this.finish();
                                    }
                                })
                                .create();
                    } else {
                        builder.setTitle("Failed")
                                .setMessage(jsonResponse.getString("msg"))
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        PostNewJobActivity.this.finish();
                                    }
                                })
                                .create();
                    }

                    //To close progress dialog
                    mProgressDialog.toggleProgressDialog();
                    //Show alert dialog
                    builder.show();

                } catch (JSONException e) {

                    e.printStackTrace();
                    //To close progress dialog
                    mProgressDialog.toggleProgressDialog();
                    //If exception, then show alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(PostNewJobActivity.this);
                    builder.setMessage(e.getMessage())
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    PostNewJobActivity.this.finish();
                                }
                            })
                            .create()
                            .show();

                }
            }
        };

        CustomVolleyErrorListener errorListener
                = new CustomVolleyErrorListener(this, mProgressDialog, mRequestQueue);

        JSONObject workingDateJSON = new JSONObject();
        SimpleDateFormat newDateFormat = new SimpleDateFormat("ddMMyyyy", Locale.ENGLISH);
        try {
            for (int i = 0; i < workingDateList.size(); i++) {
                Date date = workingDateList.get(i);
                workingDateJSON.put("wd" + (i + 1), newDateFormat.format(date));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONObject workingTimeJSON = new JSONObject();
        try {
            workingTimeJSON.put("startWorkTime", workingTime.getStartWorkingTime());
            workingTimeJSON.put("endWorkTime", workingTime.getEndWorkingTime());
            workingTimeJSON.put("startBreakTime1", workingTime.getStartBreakTime1());
            workingTimeJSON.put("endBreakTime1", workingTime.getEndBreakTime1());
            workingTimeJSON.put("startBreakTime2", workingTime.getStartBreakTime2());
            workingTimeJSON.put("endBreakTime2", workingTime.getEndBreakTime2());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JobPost jobPost = new JobPost();
        jobPost.setLocation_id(locationId);
        jobPost.setTitle(etTitle.getText().toString());
        jobPost.setWorkingDate(workingDateJSON.toString());
        jobPost.setWorkingTime(workingTimeJSON.toString());
        jobPost.setCategory(mSpinnerCategory.getSelectedItem().toString());
        jobPost.setRequirement(etRequirement.getText().toString());
        jobPost.setDescription(etDescription.getText().toString());
        jobPost.setNote(etNote.getText().toString());
        jobPost.setWages(Integer.parseInt(etWages.getText().toString()));
        jobPost.setPositionNumber(Integer.parseInt(etPosition.getText().toString()));

        if (mSpinnerPaymentTerm.getSelectedItemPosition() == 0) {
            jobPost.setPaymentTerm(0);
        } else {
            jobPost.setPaymentTerm(Integer.parseInt(
                    mSpinnerPaymentTerm.getSelectedItem().toString().substring(0, 2)));
        }

        if (mSpinnerPreferGender.getSelectedItemPosition() == 0) {
            jobPost.setPreferGender("");
        } else if (mSpinnerPreferGender.getSelectedItemPosition() == 1) {
            jobPost.setPreferGender("M");
        } else if (mSpinnerPreferGender.getSelectedItemPosition() == 2) {
            jobPost.setPreferGender("F");
        }

        PostNewJobActivity.CreateNewJobPostRequest createNewJobPostRequest
                = new PostNewJobActivity.CreateNewJobPostRequest(
                jobPost,
                root + getString(R.string.url_create_job_post),
                responseListener,
                errorListener
        );
        mRequestQueue.add(createNewJobPostRequest);
    }

    private class CheckJobLocationRequest extends StringRequest {

        private Map<String, String> params;

        CheckJobLocationRequest(
                JobLocation jobLocation,
                String url,
                Response.Listener<String> listener,
                Response.ErrorListener errorListener) {
            super(Method.POST, url, listener, errorListener);

            params = new HashMap<>();
            params.put("job_location_name", jobLocation.getName());
            params.put("job_location_address", jobLocation.getAddress());
            params.put("job_location_lat", Double.toString(jobLocation.getLatitude()));
            params.put("job_location_long", Double.toString(jobLocation.getLongitude()));
        }

        public Map<String, String> getParams() {
            return params;
        }
    }

    private class CreateNewJobPostRequest extends StringRequest {

        private Map<String, String> params;

        CreateNewJobPostRequest(
                JobPost jobPost,
                String url,
                Response.Listener<String> listener,
                Response.ErrorListener errorListener) {
            super(Method.POST, url, listener, errorListener);

            params = new HashMap<>();
            params.put("company_id", userId);
            params.put("location_id", jobPost.getLocation_id());
            params.put("job_post_title", jobPost.getTitle());
            params.put("job_post_working_date", jobPost.getWorkingDate());
            params.put("job_post_working_timetable", jobPost.getWorkingTime());
            params.put("job_post_category", jobPost.getCategory());
            params.put("job_post_requirement", jobPost.getRequirement());
            params.put("job_post_description", jobPost.getDescription());
            params.put("job_post_note", jobPost.getNote());
            params.put("job_post_wages", Double.toString(jobPost.getWages()));
            params.put("job_post_payment_term", Integer.toString(jobPost.getPaymentTerm()));
            params.put("job_post_position_num", Integer.toString(jobPost.getPositionNumber()));
            params.put("job_post_prefer_gender", jobPost.getPreferGender());
        }

        public Map<String, String> getParams() {
            return params;
        }
    }
}
