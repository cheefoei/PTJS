package com.TimeToWork.TimeToWork.Adapter;

import android.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.TimeToWork.TimeToWork.Entity.JobApplication;
import com.TimeToWork.TimeToWork.Entity.PartTimer;

/**
 * Created by MelvinTanChunKeat on 11/10/2018.
 */

public class Notification {

    private Connection connect;
    private PreparedStatement stmt;
    private ResultSet rs;

    public Notification() {
        /*try
        {
            ConnectionHelper connection = new ConnectionHelper();
            connect = connection.connectionclasss();
        } catch(Exception e){
            Log.e("error here : ", e.getMessage());
        }*/
    }

    public ArrayList<JobApplication> getJobApplicationDetails() throws SQLException {
        ConnectionHelper connection = new ConnectionHelper();
        connect = connection.connectionclasss();
        ArrayList<JobApplication> jobApplicationArrayList = new ArrayList<JobApplication>();
        JobApplication jobApplication = null;
        String query = "SELECT * FROM JobApplication";
        try {
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {

                jobApplicationArrayList.add(new JobApplication(rs.getString("job_application_id"), rs.getString("job_application_date"), rs.getString("job_application_status")));
            }

        } catch (SQLException ex) {
            ex.getMessage();
        } finally {
            if (!connect.isClosed()){
                connect.close();
            }
        }


        return jobApplicationArrayList;
    }

    public JobApplication updateJobApplication(JobApplication jobApplication) throws SQLException {
        ConnectionHelper connection = new ConnectionHelper();
        connect = connection.connectionclasss();
        String query = "UPDATE JobApplication" + " SET job_application_status = ? WHERE job_application_id = ?";

        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, jobApplication.getJob_application_status());
            stmt.setString(2, jobApplication.getJob_application_id());
            stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.getMessage();
        }finally {
            if (!connect.isClosed()){
                connect.close();
            }
        }
        return jobApplication;
    }
}
