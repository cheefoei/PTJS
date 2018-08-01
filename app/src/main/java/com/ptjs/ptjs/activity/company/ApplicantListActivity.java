package com.ptjs.ptjs.activity.company;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.ptjs.ptjs.R;
import com.ptjs.ptjs.adapter.ApplicantListAdapter;

import java.util.ArrayList;
import java.util.List;

public class ApplicantListActivity extends AppCompatActivity {

    private ApplicantListAdapter adapter;
    private List<String> jobseekerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_list);

        jobseekerList = new ArrayList<>();
        adapter = new ApplicantListAdapter(this, jobseekerList);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.rv_applicant_list);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);

        getApplicantList();
    }

    private void getApplicantList() {

        jobseekerList.add("abc");
        adapter.notifyDataSetChanged();
    }
}
