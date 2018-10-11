package com.TimeToWork.TimeToWork.Company;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;

import com.TimeToWork.TimeToWork.R;
import com.TimeToWork.TimeToWork.Adapter.JobPostAdapter;
import com.TimeToWork.TimeToWork.Database.Entity.JobPost;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CompanyMainActivity extends AppCompatActivity {

    private ProgressDialog mProgressDialog;

    private JobPostAdapter adapter;
    private List<JobPost> jobPostList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setCancelable(false);

        jobPostList = new ArrayList<>();
        adapter = new JobPostAdapter(this, jobPostList);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rv_job_post);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);

        setupJobPost();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_add_job);
        fab.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                Intent intent = new Intent(CompanyMainActivity.this, PostNewJobActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_company_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setupJobPost() {

        mProgressDialog.setMessage("Getting your job posts ...");
        mProgressDialog.show();

//        JobPost jobPost1 = new JobPost("1", "Part Time Mandarin Interpreter - Work From Home!", "C1", "Suria KLCC, Kuala Lumpur City Centre, City Centre, 50088 Kuala Lumpur, Wilayah Persekutuan Kuala Lumpur", new Date(), 100,
//                "Native or native-like level of proficiency in English and Target language, with a good command of both colloquial and written English and Target ",
//                "Looking to find freelance work online? We are looking for new subtitle translators and captioners to join our team of professionals!",
//                "Inclueded meals");
//        JobPost jobPost2 = new JobPost("2", "UPSR Teacher (Homeroom, English & Add-Math) ", "C2", "7, Jalan Malinja 2, Taman Bunga Raya, 53000 Kuala Lumpur, Wilayah Persekutuan Kuala Lumpur", new Date(), 80, "", "", "");
//
//        jobPostList.add(jobPost1);
//        jobPostList.add(jobPost2);
        adapter.notifyDataSetChanged();

        mProgressDialog.dismiss();

    }

}
