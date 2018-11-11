package com.TimeToWork.TimeToWork.Jobseeker;

import android.content.DialogInterface;
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
import android.widget.ListView;
import android.widget.TextView;

import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.Database.Control.MaintainJobseeker;
import com.TimeToWork.TimeToWork.Database.Entity.Jobseeker;
import com.TimeToWork.TimeToWork.R;

import java.util.ArrayList;

import static com.TimeToWork.TimeToWork.MainApplication.userId;

public class PreferredJobActivity extends AppCompatActivity {

    private MaintainJobseeker maintainJobseeker = new MaintainJobseeker();

    private CustomProgressDialog mProgressDialog;
    private Handler handler;

    private ArrayList<String> selectedItems = new ArrayList<>();
    private String selectedJob = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferred_job);

        mProgressDialog = new CustomProgressDialog(this);
        handler = new Handler();

        ListView listView = (ListView) findViewById(R.id.list_view_preferred_job);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

//        String jobList[] = {"Event & Promotion", "Restaurants & Cafes", "Fashion & Retail", "Administration", "Hotel & Tourism",
//                "Beauty & Healthcare", "Education", "Sales & Marketing", "Transportation & Logistic"};

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.array_category, R.layout.list_view_preferred_job);
        listView.setAdapter(adapter);

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
        mProgressDialog.setMessage("Remembering your preferred jobs …");
        mProgressDialog.show();

        new Thread(new Runnable() {

            @Override
            public void run() {

                for (int i = 0; i < selectedItems.size(); i++) {
                    if (selectedJob.equals("")) {
                        selectedJob = selectedItems.get(i);
                    } else {
                        selectedJob += ", " + selectedItems.get(i);
                    }
                }
                Jobseeker jobseeker = new Jobseeker();
                jobseeker.setId(userId);
                jobseeker.setPreferred_job(selectedJob);
                jobseeker.setPreferred_location(null);

                maintainJobseeker.updatePreferredJob(jobseeker);

                handler.post(new Runnable() {

                    @Override
                    public void run() {

                        mProgressDialog.dismiss();

                        AlertDialog.Builder builder
                                = new AlertDialog.Builder(PreferredJobActivity.this)
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
}
