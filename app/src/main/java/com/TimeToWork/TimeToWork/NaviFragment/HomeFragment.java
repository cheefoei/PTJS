package com.TimeToWork.TimeToWork.NaviFragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.TimeToWork.TimeToWork.activity.company.CompanyMainActivity;
import com.TimeToWork.TimeToWork.adapter.JobListAdapter;
import com.TimeToWork.TimeToWork.db.entity.Job;
import com.TimeToWork.TimeToWork.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HomeFragment extends Fragment {

    private ProgressDialog mProgressDialog;

    private JobListAdapter adapter;
    private List<Job> jobList;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(getString(R.string.fragment_home));

        // Enable menu in toolbar
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mProgressDialog = new ProgressDialog(getActivity());
        mProgressDialog.setCancelable(false);

        jobList = new ArrayList<>();
        adapter = new JobListAdapter(getContext(), jobList);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.rv_job_list);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);

        setupJobList();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_home, menu);

        SearchView searchView = ((SearchView) menu.findItem(R.id.search).getActionView());
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setQueryHint("Search job");

        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.notification) {
            Intent i = new Intent(getActivity(), CompanyMainActivity.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupJobList() {

        mProgressDialog.setMessage("Getting your data ...");
        mProgressDialog.show();

        Job job1 = new Job("1", "Part Time Mandarin Interpreter - Work From Home!", "C1", "Suria KLCC, Kuala Lumpur City Centre, City Centre, 50088 Kuala Lumpur, Wilayah Persekutuan Kuala Lumpur", new Date(), 100,
                "Native or native-like level of proficiency in English and Target language, with a good command of both colloquial and written English and Target ",
                "Looking to find freelance work online? We are looking for new subtitle translators and captioners to join our team of professionals!",
                "Inclueded meals");
        Job job2 = new Job("2", "UPSR Teacher (Homeroom, English & Add-Math) ", "C2", "7, Jalan Malinja 2, Taman Bunga Raya, 53000 Kuala Lumpur, Wilayah Persekutuan Kuala Lumpur", new Date(), 80, "", "", "");

        jobList.add(job1);
        jobList.add(job2);
        adapter.notifyDataSetChanged();

        mProgressDialog.dismiss();

    }

}
