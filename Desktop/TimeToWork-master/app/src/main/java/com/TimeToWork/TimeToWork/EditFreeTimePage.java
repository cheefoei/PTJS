package com.TimeToWork.TimeToWork;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import java.util.ArrayList;
import java.util.List;
import com.TimeToWork.TimeToWork.Control.MaintainFreeTime;
import com.TimeToWork.TimeToWork.Entity.Schedule;

public class EditFreeTimePage extends AppCompatActivity {

    private MaintainFreeTime maintainFreeTime = new MaintainFreeTime();
    private String timeFrom, timeTo;
    private ArrayAdapter<String> adapter, adapter1;
    private EditText txtFrom, txtTo;
    private Button btnSave;
    private Intent intent;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_free_time_page);
        txtFrom = (EditText)findViewById(R.id.addFrom);
        txtTo = (EditText)findViewById(R.id.addTo);
        btnSave = (Button)findViewById(R.id.save);

        intent = getIntent();
        timeFrom = intent.getExtras().getString("timeFrom");
        timeTo = intent.getExtras().getString("timeTo");

        txtFrom.setText(timeFrom);
        txtTo.setText(timeTo);

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

        AlertDialog.Builder builder = new AlertDialog.Builder(EditFreeTimePage.this);
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

        AlertDialog.Builder builder1 = new AlertDialog.Builder(EditFreeTimePage.this);
        builder1.setCancelable(true);
        builder1.setView(listView1);

        final AlertDialog dialog1 = builder1.create();

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

                Schedule scheduleDetail = null;
                scheduleDetail = new Schedule("SL10001", timeFrom, timeTo);
                maintainFreeTime.updateFreeTime(scheduleDetail);
                Intent intent = new Intent(EditFreeTimePage.this, ProfilePage.class);
                startActivity(intent);
            }
        });

    }
}
