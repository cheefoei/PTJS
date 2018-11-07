package com.TimeToWork.TimeToWork;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import java.util.ArrayList;
import com.TimeToWork.TimeToWork.Adapter.LocationList;
import com.TimeToWork.TimeToWork.Entity.Location;

public class PreferredLocationPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferred_location_page);
        ListView listView = (ListView) findViewById(R.id.listView);

        Location listLocation1 = new Location("Johor");
        Location listLocation2 = new Location("Kedah");
        Location listLocation3 = new Location("Kelantan");
        Location listLocation4 = new Location("Kuala Lumpur");
        Location listLocation5 = new Location("Negeri Sembilan");
        Location listLocation6 = new Location("Pahang");
        Location listLocation7 = new Location("Perak");
        Location listLocation8 = new Location("Perlis");
        Location listLocation9 = new Location("Penang");
        Location listLocation10 = new Location("Sabah");
        Location listLocation11 = new Location("Sarawak");
        Location listLocation12 = new Location("Selangor");
        Location listLocation13 = new Location("Terengganu");

        ArrayList<Location> locationList = new ArrayList<>();
        locationList.add(listLocation1);
        locationList.add(listLocation2);
        locationList.add(listLocation3);
        locationList.add(listLocation4);
        locationList.add(listLocation5);
        locationList.add(listLocation6);
        locationList.add(listLocation7);
        locationList.add(listLocation8);
        locationList.add(listLocation9);
        locationList.add(listLocation10);
        locationList.add(listLocation11);
        locationList.add(listLocation12);
        locationList.add(listLocation13);

        LocationList adapter = new LocationList(this, R.layout.adapter_view_job_layout, locationList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(position == 11){
                    Intent myIntent = new Intent(PreferredLocationPage.this, SelangorPage.class);
                    startActivity(myIntent);
            }
        }

    });
    }
}

