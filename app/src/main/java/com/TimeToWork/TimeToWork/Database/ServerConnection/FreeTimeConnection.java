package com.TimeToWork.TimeToWork.Database.ServerConnection;

import android.util.Log;

import com.TimeToWork.TimeToWork.Database.Entity.Schedule;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
 * Created by MelvinTanChunKeat on 11/10/2018.
 */

public class FreeTimeConnection {

    private Connection connect;
    private PreparedStatement stmt;
    private ResultSet rs;

    public FreeTimeConnection() {
        try {
            ConnectionHelper connection = new ConnectionHelper();
            connect = connection.connectionClass();
        } catch (Exception e) {
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

    public List<Schedule> getScheduleDetail() {

        List<Schedule> getScheduleDetail = new ArrayList<>();

        String query = "SELECT S.schedule_id, S.schedule_time_from, S.schedule_time_to, " +
                "L.schedule_list_id, L.schedule_date, L.jobseeker_id " +
                "FROM schedule S, schedulelist L WHERE S.schedule_id = L.schedule_id";
        try {
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {

                Schedule schedule = new Schedule(
                        rs.getString("schedule_id"),
                        rs.getString("schedule_list_id"),
                        rs.getString("jobseeker_id"),
                        rs.getString("schedule_time_from"),
                        rs.getString("schedule_time_to"),
                        rs.getString("schedule_date")
                );
                getScheduleDetail.add(schedule);
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return getScheduleDetail;
    }

    public Schedule updateFreeTime(Schedule schedule) {

        String query = "UPDATE schedule" + " SET schedule_time_from = ?, schedule_time_to = ? " +
                "WHERE schedule_id = ?";

        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, schedule.getTimeFrom());
            stmt.setString(2, schedule.getTimeTo());
            stmt.setString(3, schedule.getSchedule_id());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
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
            ex.printStackTrace();
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
            ex.printStackTrace();
        }
        return schedule;
    }

    // Read ID
    public String getScheduleLastId() {

        ConnectionHelper connection = new ConnectionHelper(); // Open Connection
        connect = connection.connectionClass();

        String query = "SELECT * FROM schedule ORDER BY schedule_id DESC";
        String scheduleId = null;

        try {
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();

            if (rs.next()) {
                scheduleId = rs.getString("schedule_id");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return scheduleId;
    }

    // Read ID
    public String getScheduleListLastId() {

        ConnectionHelper connection = new ConnectionHelper(); // Open Connection
        connect = connection.connectionClass();

        String query = "SELECT * FROM schedulelist ORDER BY schedule_list_id DESC";
        String scheduleListId = null;

        try {
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();

            if (rs.next()) {
                scheduleListId = rs.getString("schedule_list_id");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return scheduleListId;
    }
}
