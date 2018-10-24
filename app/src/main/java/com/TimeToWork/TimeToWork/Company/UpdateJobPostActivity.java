package com.TimeToWork.TimeToWork.Company;

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
import com.TimeToWork.TimeToWork.Database.Entity.JobLocation;
import com.TimeToWork.TimeToWork.Database.Entity.WorkingSchedule;
import com.TimeToWork.TimeToWork.Database.Entity.WorkingTime;
import com.TimeToWork.TimeToWork.R;
import com.TimeToWork.TimeToWork.Database.Entity.JobPost;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

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

        jobPost = (JobPost) getIntent().getSerializableExtra("JOBPOST");
        jobLocation = (JobLocation) getIntent().getSerializableExtra("JOBLOCATION");

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
//                    checkLocationExists();
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
            etWages.setText(String.format(Locale.getDefault(), "RM %.2f", jobPost.getWages()));
            etPosition.setText(jobPost.getPositionNumber());
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
            } else {
                List<String> terms = Arrays.asList(getResources().
                        getStringArray(R.array.array_payment_term));
                for (int i = 0; i < terms.size(); i++) {
                    int t = Integer.parseInt(terms.get(i).substring(0, 2));
                    if (t == jobPost.getPaymentTerm()) {
                        mSpinnerPaymentTerm.setSelection(i);
                    }
                }
            }

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

}
