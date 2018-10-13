package com.TimeToWork.TimeToWork.Company;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.TimeToWork.TimeToWork.Database.Entity.JobPost;
import com.TimeToWork.TimeToWork.R;

import java.util.Locale;

public class CompanyJobDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_job_detail);

        JobPost jobPost = (JobPost) getIntent().getSerializableExtra("JOB");

        TextView tvJobTitle = (TextView) findViewById(R.id.tv_job_title);
        tvJobTitle.setText(jobPost.getTitle());

        TextView tvCompanyName = (TextView) findViewById(R.id.tv_company_name);
        tvCompanyName.setText("Connect Dots Sdn Bhd");

        TextView tvWorkplace = (TextView) findViewById(R.id.tv_workplace);
//        tvWorkplace.setText(jobPost.getWorkplace());

        TextView tvWorkingDate = (TextView) findViewById(R.id.tv_working_date);
        tvWorkingDate.setText("30 July 2018");

        TextView tvWages = (TextView) findViewById(R.id.tv_wages);
        tvWages.setText(String.format(Locale.getDefault(), "RM %.2f", jobPost.getWages()));

        TextView tvRequirement = (TextView) findViewById(R.id.tv_job_requirement);
        tvRequirement.setText(jobPost.getRequirement());

        TextView tvDescription = (TextView) findViewById(R.id.tv_job_description);
        tvDescription.setText(jobPost.getDescription());

        TextView tvNote = (TextView) findViewById(R.id.tv_job_note);
        tvNote.setText(jobPost.getNote());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_view_job_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.applicant) {
            Intent i = new Intent(this, ApplicantListActivity.class);
            startActivity(i);

        }
        return super.onOptionsItemSelected(item);
    }
}
