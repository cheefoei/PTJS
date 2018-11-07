package com.TimeToWork.TimeToWork;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import com.TimeToWork.TimeToWork.Control.MaintainFreeTime;
import com.TimeToWork.TimeToWork.Entity.Schedule;
import com.TimeToWork.TimeToWork.R;

public class SetFreeTimePage extends AppCompatActivity {

    private EditText editTextDate, txtFrom, txtTo ;
    private Button btnSave;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private MaintainFreeTime maintainFreeTime = new MaintainFreeTime();
    private String dob, schedule_id, schedule_list_id;
    private ArrayAdapter<String> adapter, adapter1;
    private AlertDialog dialog, dialog1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_free_time_page);
        editTextDate = (EditText) findViewById(R.id.addDate);
        txtFrom = (EditText)findViewById(R.id.addFrom);
        txtTo = (EditText)findViewById(R.id.addTo);
        btnSave = (Button) findViewById(R.id.save);

        String scheduleID = maintainFreeTime.readScheduleID().getSchedule_id();
        int number = Integer.parseInt(scheduleID.substring(2, 7)); // Get the behind 5 digits
        number += 1; // id + 1, Example: 10001 + 1 = 10002
        String word = scheduleID.substring(0, 2); // Get the first two letter 'JR'
        schedule_id = String.valueOf(word + number); // Get the first two letter 'JR' + the behind 5 digits

        String scheduleListID = maintainFreeTime.readScheduleListID().getSchedule_list_id();
        int number1 = Integer.parseInt(scheduleListID.substring(2, 7)); // Get the behind 5 digits
        number1 += 1; // id + 1, Example: 10001 + 1 = 10002
        String word1 = scheduleListID.substring(0, 2); // Get the first two letter 'JR'
        schedule_list_id = String.valueOf(word1 + number1); // Get the first two letter 'JR' + the behind 5 digits

        editTextDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        SetFreeTimePage.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                month += 1;

                String date = day + "/" + month + "/" + year;
                editTextDate.setText(date);
                dob = year + "-" + month + "-" + day;
            }
        };

        ListView listView = new ListView(this);
        ListView listView1 = new ListView(this);

        List<String> data = new ArrayList<>();
        data.add("8:00");
        data.add("9:00");
        data.add("10:00");
        data.add("11:00");
        data.add("12:00");
        data.add("13:00");
        data.add("14:00");
        data.add("15:00");
        data.add("16:00");
        data.add("17:00");
        data.add("18:00");
        data.add("19:00");
        data.add("20:00");
        data.add("21:00");
        data.add("22:00");
        data.add("23:00");
        data.add("24:00");

        //From
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        listView.setAdapter(adapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(SetFreeTimePage.this);
        builder.setCancelable(true);
        builder.setView(listView);

        dialog = builder.create();

        txtFrom.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                dialog.show();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                txtFrom.setText(adapter.getItem(position));
                dialog.dismiss();
            }
        });

        //To
        adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,data);
        listView1.setAdapter(adapter1);

        AlertDialog.Builder builder1 = new AlertDialog.Builder(SetFreeTimePage.this);
        builder1.setCancelable(true);
        builder1.setView(listView1);

        dialog1 = builder1.create();

        txtTo.setOnClickListener(new View.OnClickListener(){

            public void onClick(View v){
                dialog1.show();
            }
        });

        listView1.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                txtTo.setText(adapter1.getItem(position));
                dialog1.dismiss();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String timeFrom = txtFrom.getText().toString();
                String timeTo = txtTo.getText().toString();

                Schedule detail = new Schedule(schedule_id, timeFrom, timeTo);
                maintainFreeTime.insertShedule(detail);

                Schedule details = new Schedule(schedule_id, schedule_list_id, "J10001", dob);
                maintainFreeTime.insertSheduleList(details);

                Intent myIntent = new Intent(SetFreeTimePage.this, ProfilePage.class);
                startActivity(myIntent);
            }
        });
    }
}
