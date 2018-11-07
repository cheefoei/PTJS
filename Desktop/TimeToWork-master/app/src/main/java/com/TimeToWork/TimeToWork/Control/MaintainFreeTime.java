package com.TimeToWork.TimeToWork.Control;

import java.util.ArrayList;

import com.TimeToWork.TimeToWork.Adapter.FreeTimeList;
import com.TimeToWork.TimeToWork.Entity.Schedule;


/**
 * Created by MelvinTanChunKeat on 11/10/2018.
 */

public class MaintainFreeTime {

    private FreeTimeList timeList = new FreeTimeList();

    public void insertShedule(Schedule schedule){
        timeList.insertShedule(schedule);
    }

    public void insertSheduleList(Schedule schedule){
        timeList.insertSheduleList(schedule);
    }

    public ArrayList<Schedule> getScheduleDetail() {
        ArrayList<Schedule> scheduleDetail = timeList.getScheduleDetail();
        return scheduleDetail;
    }

    public void updateFreeTime(Schedule schedule) {
        timeList.updateFreeTime(schedule);
    }

    public void deleteSchedule(Schedule schedule) {
        timeList.deleteSchedule(schedule);
    }

    public void deleteScheduleList(Schedule schedule) {
        timeList.deleteScheduleList(schedule);
    }

    public Schedule readScheduleID(){
        return timeList.readScheduleID();
    }

    public Schedule readScheduleListID(){
        return timeList.readScheduleListID();
    }
}
