package com.TimeToWork.TimeToWork.Jobseeker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.TimeToWork.TimeToWork.Adapter.LocationArrayAdapter;
import com.TimeToWork.TimeToWork.Database.Entity.JobLocation;
import com.TimeToWork.TimeToWork.R;

import java.util.ArrayList;
import java.util.List;

public class PreferredLocationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferred_location);

        ListView listView = (ListView) findViewById(R.id.list_view_preferred_location);

        JobLocation listLocation1 = new JobLocation("Johor");
        JobLocation listLocation2 = new JobLocation("Kedah");
        JobLocation listLocation3 = new JobLocation("Kelantan");
        JobLocation listLocation4 = new JobLocation("Kuala Lumpur");
        JobLocation listLocation5 = new JobLocation("Negeri Sembilan");
        JobLocation listLocation6 = new JobLocation("Pahang");
        JobLocation listLocation7 = new JobLocation("Perak");
        JobLocation listLocation8 = new JobLocation("Perlis");
        JobLocation listLocation9 = new JobLocation("Penang");
        JobLocation listLocation10 = new JobLocation("Sabah");
        JobLocation listLocation11 = new JobLocation("Sarawak");
        JobLocation listLocation12 = new JobLocation("Selangor");
        JobLocation listLocation13 = new JobLocation("Terengganu");

        List<JobLocation> jobLocationList = new ArrayList<>();
        jobLocationList.add(listLocation1);
        jobLocationList.add(listLocation2);
        jobLocationList.add(listLocation3);
        jobLocationList.add(listLocation4);
        jobLocationList.add(listLocation5);
        jobLocationList.add(listLocation6);
        jobLocationList.add(listLocation7);
        jobLocationList.add(listLocation8);
        jobLocationList.add(listLocation9);
        jobLocationList.add(listLocation10);
        jobLocationList.add(listLocation11);
        jobLocationList.add(listLocation12);
        jobLocationList.add(listLocation13);

        LocationArrayAdapter adapter = new LocationArrayAdapter(
                this, R.layout.list_view_preferred_location, jobLocationList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 11) {
                    Intent myIntent = new Intent(PreferredLocationActivity.this, SelangorPage.class);
                    startActivity(myIntent);
                }
            }

        });
    }
}

