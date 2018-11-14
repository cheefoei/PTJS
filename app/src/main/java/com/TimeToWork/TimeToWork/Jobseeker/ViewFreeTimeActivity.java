package com.TimeToWork.TimeToWork.Jobseeker;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CalendarView;

import com.TimeToWork.TimeToWork.Adapter.TimeArrayAdapter;
import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.Database.Control.MaintainFreeTime;
import com.TimeToWork.TimeToWork.Database.Entity.Date;
import com.TimeToWork.TimeToWork.Database.Entity.Schedule;
import com.TimeToWork.TimeToWork.R;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import static com.TimeToWork.TimeToWork.MainApplication.userId;

public class ViewFreeTimeActivity extends AppCompatActivity {

    private MaintainFreeTime maintainFreeTime = new MaintainFreeTime();

    private CustomProgressDialog mProgressDialog;
    private Handler handler;

    private List<Date> timeList = new ArrayList<>();
    private List<Schedule> scheduleList;
    private TimeArrayAdapter adapter;

    private String date, id, id2;
    private int no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_free_time);

        mProgressDialog = new CustomProgressDialog(this);
        handler = new Handler();

        CalendarView calendarView = (CalendarView) findViewById(R.id.calendarView);

        adapter = new TimeArrayAdapter(this, R.layout.list_view_free_time, timeList);
        SwipeMenuCreator creator = new SwipeMenuCreator() {

            @Override
            public void create(SwipeMenu menu) {

                // create "open" item
                SwipeMenuItem openItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                openItem.setBackground(new ColorDrawable(Color.rgb(
                        0x00, 0x66, 0xff)));
                // set item width
                openItem.setWidth(170);
                // set item title
                openItem.setTitle("Edit");
                // set item title fontsize
                openItem.setTitleSize(18);
                // set item title font color
                openItem.setTitleColor(Color.WHITE);
                // add to menu
                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(
                        getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(0xF9,
                        0x3F, 0x25)));
                // set item width
                deleteItem.setWidth(170);
                // set a icon
                deleteItem.setIcon(R.drawable.baseline_delete_white_24);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };

        SwipeMenuListView swipeMenuListView = (SwipeMenuListView) findViewById(R.id.list_view_free_time);
        swipeMenuListView.setAdapter(adapter);
        swipeMenuListView.setMenuCreator(creator);
        swipeMenuListView.setOnMenuItemClickListener(new OnFreeTimeItemClickListener());

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(@NonNull CalendarView view,
                                            int year, int month, int dayOfMonth) {

                date = dayOfMonth + "/" + (month + 1) + "/" + year;
                //if (date.charAt(0) != 0) {
                   // date = "0" + dayOfMonth + "/" + (month + 1) + "/" + year;
                //}
                initScheduleDetail();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 9487) {
            if (resultCode == RESULT_OK) {
                initScheduleDetail();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initScheduleDetail() {

        //Show progress dialog
        mProgressDialog.setMessage("Loading …");
        mProgressDialog.show();

        new Thread(new Runnable() {

            @Override
            public void run() {

                scheduleList = maintainFreeTime.getScheduleDetail();
                timeList.clear();

                handler.post(new Runnable() {

                    @Override
                    public void run() {

                        for (int i = 0; i < scheduleList.size(); i++) {

                            if (scheduleList.get(i).getJobseeker_id().equals(userId)) {
                                no = i;

                                DateFormat outputFormat = new SimpleDateFormat(
                                        "dd/MM/yyyy", Locale.ENGLISH);
                                DateFormat inputFormat = new SimpleDateFormat(
                                        "yyyy-MM-dd", Locale.ENGLISH);

                                String inputText = scheduleList.get(i).getSchedule_list_date();
                                try {

                                    java.util.Date dates = inputFormat.parse(inputText);
                                    String outputText = outputFormat.format(dates);

                                    if (date.equals(outputText)) {

                                        Date listTime = new Date(scheduleList.get(i).getTimeFrom(),
                                                scheduleList.get(i).getTimeTo());
                                        timeList.add(listTime);
                                    }
                                } catch (ParseException e) {
                                    Log.e("Error here : ", e.getMessage());
                                }
                            }
                        }
                        adapter.notifyDataSetChanged();
                        mProgressDialog.dismiss();
                    }
                });
            }
        }).start();
    }

    private void deleteSchedule() {

        //Show progress dialog
        mProgressDialog.setMessage("Removing free time …");
        mProgressDialog.show();

        new Thread(new Runnable() {

            @Override
            public void run() {

                id = scheduleList.get(no).getSchedule_id();
                id2 = scheduleList.get(no).getSchedule_list_id();
                Schedule scheduleDetails = new Schedule(null, id2, null, null);
                maintainFreeTime.deleteScheduleList(scheduleDetails);

                Schedule scheduleDetail = new Schedule(id, null, null);
                maintainFreeTime.deleteSchedule(scheduleDetail);

                handler.post(new Runnable() {

                    @Override
                    public void run() {

                        mProgressDialog.dismiss();

                        AlertDialog.Builder builder
                                = new AlertDialog.Builder(ViewFreeTimeActivity.this)
                                .setTitle("Successful")
                                .setMessage("Your free time is removed.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        initScheduleDetail();
                                    }
                                });
                        builder.show();
                    }
                });
            }
        }).start();
    }

    private class OnFreeTimeItemClickListener implements SwipeMenuListView.OnMenuItemClickListener {

        @Override
        public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {

            switch (index) {

                case 0:
                    id = scheduleList.get(no).getSchedule_id();
                    String timeFrom = timeList.get(position).getTimeFrom();
                    String timeTo = timeList.get(position).getTimeTo();

                    Intent intent = new Intent(ViewFreeTimeActivity.this, EditFreeTimeActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("timeFrom", timeFrom);
                    intent.putExtra("timeTo", timeTo);
                    startActivityForResult(intent, 9487);

                    break;
                case 1:
                    deleteSchedule();
                    break;
            }
            // false : close the menu; true : not close the menu
            return false;
        }
    }
}
