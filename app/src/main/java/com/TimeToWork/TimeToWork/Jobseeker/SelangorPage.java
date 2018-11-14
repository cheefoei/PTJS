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

    ArrayList<String> selectedItems = new ArrayList<>();
    private MaintainJobseeker maintainJobseeker = new MaintainJobseeker();
    private String selectedLocation = "";
    private Button btnOk;
    private Jobseeker partTimerDetail = null;
    private CustomProgressDialog mProgressDialog;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selangor_page);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        String locationList[] = {"Bukit Raja", "Bukit Tinggi", "Klang", "Setia Alam", "Shah Alam"};

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.list_view_preferred_job, R.id.txt_lan, locationList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = ((TextView)view).getText().toString();
                if(selectedItems.contains(selectedItem)){
                    selectedItems.remove(selectedItem); //uncheck item
                }
                else
                    selectedItems.add(selectedItem); //check item
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_done, menu);
        return true;
    }

    private void updatePreferredLocation(){
        //Show progress dialog
        //mProgressDialog.setMessage("Remembering your preferred jobs …");
        //mProgressDialog.show();

        new Thread(new Runnable() {

            @Override
            public void run() {

                for (int i = 0; i < selectedItems.size(); i++) {
                    if (selectedLocation.equals("")) {
                        selectedLocation = selectedItems.get(i);
                    } else {
                        selectedLocation += ", " + selectedItems.get(i);
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
                                .setMessage("We remembered your preferred jobs.")
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

public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.done) {
            updatePreferredLocation();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
