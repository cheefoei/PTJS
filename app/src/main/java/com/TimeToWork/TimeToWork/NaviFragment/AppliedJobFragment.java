package com.TimeToWork.TimeToWork.NaviFragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.TimeToWork.TimeToWork.R;
import com.TimeToWork.TimeToWork.Adapter.AppliedJobAdapter;
import com.TimeToWork.TimeToWork.Database.Entity.Job;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppliedJobFragment extends Fragment {

    private ProgressDialog mProgressDialog;

    private AppliedJobAdapter adapter;
    private List<Job> jobList;

    public AppliedJobFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_applied_job, container, false);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setCancelable(false);

        jobList = new ArrayList<>();
        adapter = new AppliedJobAdapter(getContext(), jobList);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_applied_job);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);

        setupJobList();

        return view;
    }

    private void setupJobList() {

        mProgressDialog.setMessage("Getting your data ...");
        mProgressDialog.show();

        Job job2 = new Job("2", "UPSR Teacher (Homeroom, English & Add-Math) ", "C2", "7, Jalan Malinja 2, Taman Bunga Raya, 53000 Kuala Lumpur, Wilayah Persekutuan Kuala Lumpur", new Date(), 80, "", "", "");

        jobList.add(job2);
        adapter.notifyDataSetChanged();

        mProgressDialog.dismiss();

    }

}
