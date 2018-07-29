package com.ptjs.ptjs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.ptjs.ptjs.db.entity.Job;

import java.util.Locale;

public class JobDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_job_detail);

        Job job = (Job) getIntent().getSerializableExtra("JOB");

        TextView tvJobTitle = (TextView) findViewById(R.id.tv_job_title);
        tvJobTitle.setText(job.getTitle());

        TextView tvCompanyName = (TextView) findViewById(R.id.tv_company_name);
        tvCompanyName.setText("Connect Dots Sdn Bhd");

        TextView tvWorkplace = (TextView) findViewById(R.id.tv_workplace);
        tvWorkplace.setText(job.getWorkplace());

        TextView tvWorkingDate = (TextView) findViewById(R.id.tv_working_date);
        tvWorkingDate.setText("30 July 2018");

        TextView tvWages = (TextView) findViewById(R.id.tv_wages);
        tvWages.setText(String.format(Locale.getDefault(), "RM %.2f", job.getWages()));

        TextView tvRequirement = (TextView) findViewById(R.id.tv_job_requirement);
        tvRequirement.setText(job.getRequirement());

        TextView tvDescription = (TextView) findViewById(R.id.tv_job_description);
        tvDescription.setText(job.getDescription());

        TextView tvNote = (TextView) findViewById(R.id.tv_job_note);
        tvNote.setText(job.getNote());
    }
}
