package com.TimeToWork.TimeToWork.Database.Control;

import com.TimeToWork.TimeToWork.Database.Entity.Schedule;
import com.TimeToWork.TimeToWork.Database.ServerConnection.FreeTimeConnection;

import java.util.List;

/*
 * Created by MelvinTanChunKeat on 11/10/2018.
 */

public class MaintainFreeTime {

    private FreeTimeConnection freeTimeConnection = new FreeTimeConnection();

    public void insertShedule(Schedule schedule) {
        freeTimeConnection.insertShedule(schedule);
    }

    public void insertSheduleList(Schedule schedule) {
        freeTimeConnection.insertSheduleList(schedule);
    }

    public List<Schedule> getScheduleDetail() {
        return freeTimeConnection.getScheduleDetail();
    }

    public void updateFreeTime(Schedule schedule) {
        freeTimeConnection.updateFreeTime(schedule);
    }

    public void deleteSchedule(Schedule schedule) {
        freeTimeConnection.deleteSchedule(schedule);
    }

    public void deleteScheduleList(Schedule schedule) {
        freeTimeConnection.deleteScheduleList(schedule);
    }

    public String getScheduleLastId() {
        return freeTimeConnection.getScheduleLastId();
    }

    public String getScheduleListLastId() {
        return freeTimeConnection.getScheduleListLastId();
    }
}
