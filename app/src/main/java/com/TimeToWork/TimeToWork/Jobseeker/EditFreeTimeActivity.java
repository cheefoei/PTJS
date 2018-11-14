package com.TimeToWork.TimeToWork.Jobseeker;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.Database.Control.MaintainFreeTime;
import com.TimeToWork.TimeToWork.Database.Entity.Schedule;
import com.TimeToWork.TimeToWork.R;

import java.util.ArrayList;
import java.util.List;

public class EditFreeTimeActivity extends AppCompatActivity {

    private MaintainFreeTime maintainFreeTime;

    private CustomProgressDialog mProgressDialog;
    private Handler handler;

    private ArrayAdapter<String> adapterFrom, adapterTo;
    private EditText txtFrom, txtTo;
    private String id;

    private AlertDialog dialogFrom, dialogTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_free_time);

        mProgressDialog = new CustomProgressDialog(this);
        handler = new Handler();

        txtFrom = (EditText) findViewById(R.id.addFrom);
        txtTo = (EditText) findViewById(R.id.addTo);

        id = getIntent().getExtras().getString("id");
        String timeFrom = getIntent().getExtras().getString("timeFrom");
        String timeTo = getIntent().getExtras().getString("timeTo");

        txtFrom.setText(timeFrom);
        txtTo.setText(timeTo);

        List<String> timeList = new ArrayList<>();
        for (int i = 8; i < 25; i++) {
            timeList.add(i + ":00");
        }

        //From
        adapterFrom = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, timeList);
        ListView listViewFrom = new ListView(this);
        listViewFrom.setAdapter(adapterFrom);
        listViewFrom.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                txtFrom.setText(adapterFrom.getItem(position));
                dialogFrom.dismiss();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setView(listViewFrom);

        dialogFrom = builder.create();
        txtFrom.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dialogFrom.show();
            }
        });

        //To
        adapterTo = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, timeList);
        ListView listViewTo = new ListView(this);
        listViewTo.setAdapter(adapterTo);
        listViewTo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                txtTo.setText(adapterTo.getItem(position));
                dialogTo.dismiss();
            }
        });

        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setCancelable(true);
        builder1.setView(listViewTo);

        dialogTo = builder1.create();
        txtTo.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dialogTo.show();
            }
        });

        Button btnSave = (Button) findViewById(R.id.save);
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                setSchedule();
            }
        });

    }

    private void setSchedule() {

        //Show progress dialog
        mProgressDialog.setMessage("Setting your free time …");
        mProgressDialog.show();

        new Thread(new Runnable() {

            @Override
            public void run() {

                String timeFrom = txtFrom.getText().toString();
                String timeTo = txtTo.getText().toString();

                Schedule scheduleDetail = new Schedule(id, timeFrom, timeTo);

                maintainFreeTime = new MaintainFreeTime();
                maintainFreeTime.updateFreeTime(scheduleDetail);

                handler.post(new Runnable() {

                    @Override
                    public void run() {

                        mProgressDialog.dismiss();

                        AlertDialog.Builder builder
                                = new AlertDialog.Builder(EditFreeTimeActivity.this)
                                .setTitle("Successful")
                                .setMessage("Your free time is set.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        setResult(RESULT_OK);
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
