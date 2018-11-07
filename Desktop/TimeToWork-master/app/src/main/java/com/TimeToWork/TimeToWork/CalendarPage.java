package com.TimeToWork.TimeToWork;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CalendarView;
import com.baoyz.swipemenulistview.SwipeMenu;
import com.baoyz.swipemenulistview.SwipeMenuCreator;
import com.baoyz.swipemenulistview.SwipeMenuItem;
import com.baoyz.swipemenulistview.SwipeMenuListView;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import com.TimeToWork.TimeToWork.Adapter.TimeList;
import com.TimeToWork.TimeToWork.Control.MaintainFreeTime;
import com.TimeToWork.TimeToWork.Entity.Date;
import com.TimeToWork.TimeToWork.Entity.Schedule;

public class CalendarPage extends AppCompatActivity {

    private MaintainFreeTime maintainFreeTime = new MaintainFreeTime();
    private CalendarView calendarView;
    private ArrayList<Date> timeList = new ArrayList<>();
    private SwipeMenuListView listView;
    private TimeList adapter;
    private ArrayList<Schedule> scheduleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar_page);

        calendarView = (CalendarView) findViewById(R.id.calendarView);
        listView = (SwipeMenuListView) findViewById(R.id.listView);
        adapter = new TimeList(this, R.layout.adapter_view_job_layout, timeList);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener(){

            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                String date = dayOfMonth + "/" + (month + 1) + "/" + year;
                if(date.charAt(0) != 0)
                {
                    date = "0" + dayOfMonth + "/" + (month + 1) + "/" + year;
                }

                scheduleList = maintainFreeTime.getScheduleDetail();
                for(int i = 0; i < scheduleList.size(); i++)
                {
                    if(scheduleList.get(i).getJobseeker_id().equals("J10001"))
                    {
                        DateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy");
                        DateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");

                        String inputText = scheduleList.get(i).getSchedule_list_date();
                        try {
                            java.util.Date dates = inputFormat.parse(inputText);
                            String outputText = outputFormat.format(dates);
                            if(date.equals(outputText))
                            {
                                Date listTime = new Date(scheduleList.get(i).getTimeFrom(),scheduleList.get(i).getTimeTo());
                                timeList.add(listTime);

                                listView.setAdapter(adapter);

                                SwipeMenuCreator creator = new SwipeMenuCreator() {

                                    @Override
                                    public void create(SwipeMenu menu) {
                                        // create "open" item
                                        SwipeMenuItem openItem = new SwipeMenuItem(
                                                getApplicationContext());
                                        // set item background
                                        openItem.setBackground(new ColorDrawable(Color.rgb(0x00, 0x66,
                                                0xff)));
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
                                        deleteItem.setIcon(R.drawable.delete);
                                        // add to menu
                                        menu.addMenuItem(deleteItem);
                                    }
                                };

                                listView.setMenuCreator(creator);

                                listView.setOnMenuItemClickListener(new SwipeMenuListView.OnMenuItemClickListener() {
                                    @Override
                                    public boolean onMenuItemClick(int position, SwipeMenu menu, int index) {
                                        switch (index) {
                                            case 0:
                                                String timeFrom = timeList.get(position).getTimeFrom();
                                                String timeTo = timeList.get(position).getTimeTo();
                                                Intent myIntent = new Intent(getApplicationContext(), EditFreeTimePage.class);
                                                myIntent.putExtra("timeFrom",timeFrom);
                                                myIntent.putExtra("timeTo",timeTo);
                                                startActivity(myIntent);
                                                break;
                                            case 1:
                                                Schedule scheduleDetails = null;
                                                scheduleDetails = new Schedule(null, "JS10001", null, null);
                                                maintainFreeTime.deleteScheduleList(scheduleDetails);

                                                Schedule scheduleDetail = null;
                                                scheduleDetail = new Schedule("SL10001", null, null);
                                                maintainFreeTime.deleteSchedule(scheduleDetail);

                                                Intent intent = new Intent(CalendarPage.this, Test.class);
                                                startActivity(intent);
                                        }
                                        // false : close the menu; true : not close the menu
                                        return false;
                                    }
                                });
                            }
                        } catch (ParseException e) {
                            Log.e("Error here : ", e.getMessage());
                        }
                    }
                }
            }
        });

    }
}
