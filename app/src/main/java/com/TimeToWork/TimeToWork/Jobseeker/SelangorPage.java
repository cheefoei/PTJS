package com.TimeToWork.TimeToWork.Jobseeker;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.Database.Control.MaintainJobseeker;
import com.TimeToWork.TimeToWork.Database.Entity.Jobseeker;
import com.TimeToWork.TimeToWork.R;

import static com.TimeToWork.TimeToWork.MainApplication.userId;

public class SelangorPage extends AppCompatActivity {

    private MaintainJobseeker maintainJobseeker = new MaintainJobseeker();

    private CustomProgressDialog mProgressDialog;
    private Handler handler;

    private ArrayList<String> selectedItems = new ArrayList<>();
    private Jobseeker getLocation = new Jobseeker();
    private String selectedLocation = "";
    private String locationList[] = {"Bukit Raja", "Bukit Tinggi", "Klang", "Setia Alam", "Shah Alam"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selangor_page);

        mProgressDialog = new CustomProgressDialog(this);
        handler = new Handler();

        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_view_preferred_job, R.id.txt_lan, locationList);
        listView.setAdapter(adapter);

        getLocation = maintainJobseeker.getJobseekerDetail(userId);

        String locations = getLocation.getPreferred_location();

        String[] location = locations.split(",");

        getLocation = maintainJobseeker.getJobseekerDetail(userId);

        for(int i =0; i<location.length;i++)
        {
            selectedItems.add(location[i]);
        }

        for(int i = 0; i<selectedItems.size();i++)
        {
            String a = selectedItems.get(i);
            for(int j = 0; j <5; j++)
            {
                if(a.equals(locationList[j])){
                    listView.setItemChecked(j,true);
                }
            }

        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                String selectedItem = ((TextView) view).getText().toString();
                if (selectedItems.contains(selectedItem)) {
                    selectedItems.remove(selectedItem); //uncheck item
                } else {
                    selectedItems.add(selectedItem); //check item
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.done) {
            updatePreferredJob();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updatePreferredJob() {

        //Show progress dialog
        mProgressDialog.setMessage("Remembering your preferred locations …");
        mProgressDialog.show();

        new Thread(new Runnable() {

            @Override
            public void run() {

                for (int i = 0; i < selectedItems.size(); i++) {
                    if (selectedLocation.equals("")) {
                        selectedLocation = selectedItems.get(i);
                    } else {
                        selectedLocation += "," + selectedItems.get(i);
                    }
                }
                Jobseeker jobseeker = new Jobseeker();
                jobseeker.setId(userId);
                jobseeker.setPreferred_job(null);
                jobseeker.setPreferred_location(selectedLocation);

                maintainJobseeker.updatePreferredLocation(jobseeker);

                handler.post(new Runnable() {

                    @Override
                    public void run() {

                        mProgressDialog.dismiss();

                        AlertDialog.Builder builder
                                = new AlertDialog.Builder(SelangorPage.this)
                                .setTitle("Successful")
                                .setMessage("We remembered your preferred locations.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });
                        builder.show();
                    }
                });
            }
        }).start();
    }
}
