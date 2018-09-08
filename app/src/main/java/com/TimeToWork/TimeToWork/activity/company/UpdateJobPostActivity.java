package com.TimeToWork.TimeToWork.activity.company;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.TimeToWork.TimeToWork.R;
import com.TimeToWork.TimeToWork.Database.Entity.Job;

import java.util.Locale;

public class UpdateJobPostActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_job_post);

        Job job = (Job) getIntent().getSerializableExtra("JOB");

        EditText etJobTitle = (EditText) findViewById(R.id.et_job_title);
        EditText etLocation = (EditText) findViewById(R.id.et_job_location);
        EditText etWorkingDate = (EditText) findViewById(R.id.et_job_working_date);
        EditText etWages = (EditText) findViewById(R.id.et_wages);
        EditText etRequirement = (EditText) findViewById(R.id.et_job_requirement);
        EditText etDescription = (EditText) findViewById(R.id.et_job_description);
        EditText etNote = (EditText) findViewById(R.id.et_job_note);

        etJobTitle.setText(job.getTitle());
        etLocation.setText(job.getWorkplace());
        etWorkingDate.setText("30 July 2018");
        etWages.setText(String.format(Locale.getDefault(), "RM %.2f", job.getWages()));
        etRequirement.setText(job.getRequirement());
        etDescription.setText(job.getDescription());
        etNote.setText(job.getNote());

    }
}
