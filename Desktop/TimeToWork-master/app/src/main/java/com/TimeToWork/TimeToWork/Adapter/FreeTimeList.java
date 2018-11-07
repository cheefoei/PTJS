package com.TimeToWork.TimeToWork.Adapter;

import android.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.TimeToWork.TimeToWork.Entity.Schedule;

/**
 * Created by MelvinTanChunKeat on 11/10/2018.
 */

public class FreeTimeList {
    private Connection connect;
    private PreparedStatement stmt;
    private ResultSet rs;

    public FreeTimeList() {
        try
        {
            ConnectionHelper connection = new ConnectionHelper();
            connect = connection.connectionclasss();
        } catch(Exception e){
            Log.e("error here : ", e.getMessage());
        }
    }

    public void insertShedule(Schedule schedule) { //Insert Schedule
        String query = "INSERT INTO schedule " + "VALUES (?, ?, ?)";

        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, schedule.getSchedule_id());
            stmt.setString(2, schedule.getTimeFrom());
            stmt.setString(3, schedule.getTimeTo());
            stmt.executeUpdate();
        } catch (SQLException e) {
            Log.e("error here : ", e.getMessage());
        }
    }

    public void insertSheduleList(Schedule schedule) {//Insert ScheduleList

        String query = "INSERT INTO schedulelist " + "VALUES (?, ?, ?, ?)";
        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, schedule.getSchedule_list_id());
            stmt.setString(2, schedule.getSchedule_list_date());
            stmt.setString(3, schedule.getJobseeker_id());
            stmt.setString(4, schedule.getSchedule_id());
            stmt.executeUpdate();
            connect.close();
        } catch (SQLException e) {
            Log.e("error here : ", e.getMessage());
        }
    }

    public ArrayList<Schedule> getScheduleDetail() {
        ArrayList<Schedule> getScheduleDetail = new ArrayList<Schedule>();
        Schedule schedule = null;
        String query = "SELECT S.schedule_id, S.schedule_time_from, S.schedule_time_to, L.schedule_list_id, L.schedule_date, L.jobseeker_id FROM schedule S, schedulelist L WHERE S.schedule_id = L.schedule_id";
        try {
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {

                getScheduleDetail.add(new Schedule(rs.getString("schedule_id"), rs.getString("schedule_list_id"), rs.getString("jobseeker_id"), rs.getString("schedule_time_from"), rs.getString("schedule_time_to"), rs.getString("schedule_date")));
            }

        } catch (SQLException ex) {
            ex.getMessage();
        }

        return getScheduleDetail;
    }

    public Schedule updateFreeTime(Schedule schedule) {
        String query = "UPDATE schedule" + " SET schedule_time_from = ?, schedule_time_to = ? WHERE schedule_id = ?";

        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, schedule.getTimeFrom());
            stmt.setString(2, schedule.getTimeTo());
            stmt.setString(3, schedule.getSchedule_id());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return schedule;
    }

    public Schedule deleteSchedule(Schedule schedule) {
        String query = "Delete FROM schedule" + " WHERE schedule_id = ?";

        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, schedule.getSchedule_id());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return schedule;
    }
    public Schedule deleteScheduleList(Schedule schedule) {
        String query = "Delete FROM schedulelist" + " WHERE schedule_list_id = ?";

        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, schedule.getSchedule_list_id());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return schedule;
    }

    // Read ID
    public Schedule readScheduleID() {
        ConnectionHelper connection = new ConnectionHelper(); // Open Connection
        connect = connection.connectionclasss();
        String query = "SELECT * FROM schedule ORDER BY schedule_id DESC";
        Schedule schedule = null;
        try {
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();

            if (rs.next()) {
                schedule = new Schedule(rs.getString("schedule_id"),null);
            }
        } catch (SQLException ex) {

        }
        return schedule;
    }

    // Read ID
    public Schedule readScheduleListID() {
        ConnectionHelper connection = new ConnectionHelper(); // Open Connection
        connect = connection.connectionclasss();
        String query = "SELECT * FROM schedulelist ORDER BY schedule_list_id DESC";
        Schedule schedule = null;
        try {
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();

            if (rs.next()) {
                schedule = new Schedule(null,rs.getString("schedule_list_id"));
            }
        } catch (SQLException ex) {

        }
        return schedule;
    }
}
