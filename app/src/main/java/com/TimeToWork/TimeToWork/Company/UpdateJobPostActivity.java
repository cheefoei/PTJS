package com.TimeToWork.TimeToWork.Company;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.CustomClass.CustomVolleyErrorListener;
import com.TimeToWork.TimeToWork.Database.Entity.JobLocation;
import com.TimeToWork.TimeToWork.Database.Entity.WorkingSchedule;
import com.TimeToWork.TimeToWork.Database.Entity.WorkingTime;
import com.TimeToWork.TimeToWork.R;
import com.TimeToWork.TimeToWork.Database.Entity.JobPost;
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

import java.text.ParseException;
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
import static com.google.android.gms.location.places.ui.PlaceAutocomplete.MODE_FULLSCREEN;

public class UpdateJobPostActivity extends AppCompatActivity implements
        OwnLocationFragment.OnCallbackReceived, SetWorkingScheduleFragment.OnCallbackReceived {

    private EditText etTitle, etLocation, etWages, etPosition,
            etRequirement, etDescription, etNote;
    private Spinner mSpinnerCategory, mSpinnerPaymentTerm, mSpinnerPreferGender;
    private Button btnSetSchedule;

    private List<Date> workingDateList;
    private WorkingTime workingTime;

    private JobPost jobPost;
    private JobLocation jobLocation;

    private CustomProgressDialog mProgressDialog;

    private int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_job_post);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        jobPost = (JobPost) getIntent().getSerializableExtra("JOBPOST");
        jobLocation = (JobLocation) getIntent().getSerializableExtra("JOBLOCATION");

        Log.e("hfhfhf", jobLocation.getId());

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
                    Intent intent = new PlaceAutocomplete.IntentBuilder(MODE_FULLSCREEN)
                            .setFilter(autocompleteFilter)
                            .build(UpdateJobPostActivity.this);
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

        Button btnUpdate = (Button) findViewById(R.id.btn_update_job_post);
        btnUpdate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (isValid()) {
                    checkLocationExists();
                }
            }
        });

        fillInForm();
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

    private void fillInForm() {

        if (jobPost != null && jobLocation != null) {

            etTitle.setText(jobPost.getTitle());
            etLocation.setText(jobLocation.getName());
            etWages.setText(String.format(Locale.getDefault(), "%.0f", jobPost.getWages()));
            etPosition.setText(String.format(Locale.getDefault(), "%d", jobPost.getPositionNumber()));
            etRequirement.setText(jobPost.getRequirement());
            etDescription.setText(jobPost.getDescription());
            etNote.setText(jobPost.getNote());

            List<String> categories = Arrays.asList(getResources().
                    getStringArray(R.array.array_category));
            for (int i = 0; i < categories.size(); i++) {
                if (categories.get(i).equals(jobPost.getCategory())) {
                    mSpinnerCategory.setSelection(i);
                }
            }

            if (jobPost.getPaymentTerm() == 0) {
                mSpinnerPaymentTerm.setSelection(0);
            } else if (jobPost.getPaymentTerm() == 7) {
                mSpinnerPaymentTerm.setSelection(1);
            } else {
                List<String> terms = Arrays.asList(getResources().
                        getStringArray(R.array.array_payment_term));
                for (int i = 2; i < terms.size(); i++) {
                    int t = Integer.parseInt(terms.get(i).substring(0, 2));
                    if (t == jobPost.getPaymentTerm()) {
                        mSpinnerPaymentTerm.setSelection(i);
                    }
                }
            }

            if (jobPost.getPreferGender() != null) {
                switch (jobPost.getPreferGender()) {
                    case "":
                        mSpinnerPreferGender.setSelection(0);
                        break;
                    case "M":
                        mSpinnerPreferGender.setSelection(1);
                        break;
                    case "F":
                        mSpinnerPreferGender.setSelection(2);
                        break;
                }
            }

            try {
                JSONObject workingDateJSON = new JSONObject(jobPost.getWorkingDate());
                int len = workingDateJSON.names().length();
                workingDateList = new ArrayList<>();

                for (int i = 1; i <= len; i++) {
                    Date date = new SimpleDateFormat("ddMMyyyy", Locale.ENGLISH)
                            .parse(workingDateJSON.getString("wd" + i));
                    workingDateList.add(date);
                }
            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            }

            try {
                JSONObject workingTimeJSON = new JSONObject(jobPost.getWorkingTime());

                workingTime = new WorkingTime();
                workingTime.setStartWorkingTime(workingTimeJSON.getString("startWorkTime"));
                workingTime.setEndWorkingTime(workingTimeJSON.getString("endWorkTime"));
                workingTime.setStartBreakTime1(workingTimeJSON.getString("startBreakTime1"));
                workingTime.setEndBreakTime1(workingTimeJSON.getString("endBreakTime1"));
                workingTime.setStartBreakTime2(workingTimeJSON.getString("startBreakTime2"));
                workingTime.setEndBreakTime2(workingTimeJSON.getString("endBreakTime2"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            btnSetSchedule.setText(R.string.string_change);
        }
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
                        jobLocation.setId(jsonResponse.getString("job_location_id"));
                        updateJobPost();
                    } else {
                        //If failed, then show alert dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateJobPostActivity.this);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(UpdateJobPostActivity.this);
                    builder.setMessage(e.getMessage())
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    UpdateJobPostActivity.this.finish();
                                }
                            })
                            .create()
                            .show();
                }
            }
        };

        CustomVolleyErrorListener errorListener
                = new CustomVolleyErrorListener(this, mProgressDialog, mRequestQueue);

        UpdateJobPostActivity.CheckJobLocationRequest checkJobLocationRequest
                = new UpdateJobPostActivity.CheckJobLocationRequest(
                jobLocation,
                root + getString(R.string.url_check_job_location_exists),
                responseListener,
                errorListener
        );
        mRequestQueue.add(checkJobLocationRequest);
    }

    private void updateJobPost() {

        // Show progress dialog
        mProgressDialog.setMessage("Updating your job post …");
        mProgressDialog.toggleProgressDialog();

        final Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    AlertDialog.Builder builder
                            = new AlertDialog.Builder(UpdateJobPostActivity.this, R.style.DialogStyle);

                    if (success) {
                        builder.setTitle("Successful");
                    } else {
                        builder.setTitle("Failed");
                    }
                    //To close progress dialog
                    mProgressDialog.toggleProgressDialog();
                    //Show alert dialog
                    builder.setMessage(jsonResponse.getString("msg"))
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Intent intent = new Intent(UpdateJobPostActivity.this, CompanyJobDetailActivity.class);
                                    intent.putExtra("JOBPOST", jobPost);
                                    intent.putExtra("JOBLOCATION", jobLocation);
                                    startActivity(intent);
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(UpdateJobPostActivity.this);
                    builder.setMessage(e.getMessage())
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    UpdateJobPostActivity.this.finish();
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

        jobPost.setJobLocation(jobLocation);
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
        } else if (mSpinnerPaymentTerm.getSelectedItemPosition() == 1) {
            jobPost.setPaymentTerm(7);
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

        UpdateJobPostActivity.UpdateJobPostRequest updateJobPostRequest
                = new UpdateJobPostActivity.UpdateJobPostRequest(
                jobPost,
                root + getString(R.string.url_update_job_post),
                responseListener,
                errorListener
        );
        mRequestQueue.add(updateJobPostRequest);
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

    private class UpdateJobPostRequest extends StringRequest {

        private Map<String, String> params;

        UpdateJobPostRequest(
                JobPost jobPost,
                String url,
                Response.Listener<String> listener,
                Response.ErrorListener errorListener) {
            super(Method.POST, url, listener, errorListener);

            params = new HashMap<>();
            params.put("job_post_id", jobPost.getId());
            params.put("location_id", jobPost.getJobLocation().getId());
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
