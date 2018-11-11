package com.TimeToWork.TimeToWork.Jobseeker;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;

import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.Database.Control.MaintainFreeTime;
import com.TimeToWork.TimeToWork.Database.Entity.Schedule;
import com.TimeToWork.TimeToWork.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.TimeToWork.TimeToWork.MainApplication.userId;

public class SetFreeTimeActivity extends AppCompatActivity {

    private MaintainFreeTime maintainFreeTime;

    private CustomProgressDialog mProgressDialog;
    private Handler handler;

    private EditText editTextDate, txtFrom, txtTo;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    private String dob, scheduleId, scheduleListId;
    private ArrayAdapter<String> adapterFrom, adapterTo;
    private AlertDialog dialogFrom, dialogTo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_free_time);

        mProgressDialog = new CustomProgressDialog(this);
        handler = new Handler();

        editTextDate = (EditText) findViewById(R.id.addDate);
        txtFrom = (EditText) findViewById(R.id.addFrom);
        txtTo = (EditText) findViewById(R.id.addTo);

        editTextDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(SetFreeTimeActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,
                        year, month, day);

                if (dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialog.show();
                }
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {

                String date = day + "/" + (month + 1) + "/" + year;
                editTextDate.setText(date);
                dob = year + "-" + month + "-" + day;
            }
        };

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

        AlertDialog.Builder builderFrom = new AlertDialog.Builder(this);
        builderFrom.setCancelable(true);
        builderFrom.setView(listViewFrom);

        dialogFrom = builderFrom.create();
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

        AlertDialog.Builder builderTo = new AlertDialog.Builder(this);
        builderTo.setCancelable(true);
        builderTo.setView(listViewTo);

        dialogTo = builderTo.create();
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

        initScheduleId();
    }

    private void initScheduleId() {

        //Show progress dialog
        mProgressDialog.setMessage("Loading …");
        mProgressDialog.show();

        new Thread(new Runnable() {

            @Override
            public void run() {

                maintainFreeTime = new MaintainFreeTime();

                String scheduleLastId = maintainFreeTime.getScheduleLastId();
                int number = Integer.parseInt(scheduleLastId.substring(2, 7)); // Get the behind 5 digits
                number += 1; // id + 1, Example: 10001 + 1 = 10002
                String word = scheduleLastId.substring(0, 2); // Get the first two letter 'JR'
                scheduleId = String.valueOf(word + number); // Get the first two letter 'JR' + the behind 5 digits

                String scheduleListLastId = maintainFreeTime.getScheduleListLastId();
                int number1 = Integer.parseInt(scheduleListLastId.substring(2, 7)); // Get the behind 5 digits
                number1 += 1; // id + 1, Example: 10001 + 1 = 10002
                String word1 = scheduleListLastId.substring(0, 2); // Get the first two letter 'JR'
                scheduleListId = String.valueOf(word1 + number1); // Get the first two letter 'JR' + the behind 5 digits

                handler.post(new Runnable() {

                    @Override
                    public void run() {
                        mProgressDialog.dismiss();
                    }
                });
            }
        }).start();
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

                Schedule detail = new Schedule(scheduleId, timeFrom, timeTo);
                maintainFreeTime.insertShedule(detail);

                Schedule details = new Schedule(scheduleId, scheduleListId, userId, dob);
                maintainFreeTime.insertSheduleList(details);

                handler.post(new Runnable() {

                    @Override
                    public void run() {

                        mProgressDialog.dismiss();

                        AlertDialog.Builder builder
                                = new AlertDialog.Builder(SetFreeTimeActivity.this)
                                .setTitle("Successful")
                                .setMessage("Your free time is set.")
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
