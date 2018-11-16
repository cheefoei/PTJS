package com.TimeToWork.TimeToWork.Database.ServerConnection;

import com.TimeToWork.TimeToWork.Database.Entity.JobApplication;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by MelvinTanChunKeat on 11/10/2018.
 */

public class NotificationConnection {

    private Connection connect;
    private PreparedStatement stmt;
    private ResultSet rs;

    public NotificationConnection() {
        /*try
        {
            ConnectionHelper connection = new ConnectionHelper();
            connect = connection.connectionClass();
        } catch(Exception e){
            Log.e("error here : ", e.getMessage());
        }*/
    }

    public JobApplication getJobApplicationDetails() throws SQLException {

        ConnectionHelper connection = new ConnectionHelper();
        connect = connection.connectionClass();

        JobApplication jobApplication = null;
        String query = "SELECT company_name,job_post_title  FROM JobApplication a, jobpost j, company c WHERE a.job_post_id = j.job_post_id AND\n" +
                "j.company_id = c.company_id AND job_application_status = 'Approved' Order BY job_application_date DESC";

        try {
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();

            if (rs.next()) {
                jobApplication = new JobApplication();
                jobApplication.setId(rs.getString("company_name"));
                jobApplication.setStatus(rs.getString("job_post_title"));
            }

        } catch (SQLException ex) {
            ex.getMessage();
        } finally {
            if (!connect.isClosed()){
                connect.close();
            }
        }
        return jobApplication;
    }

    public JobApplication updateJobApplication(JobApplication jobApplication) throws SQLException {

        ConnectionHelper connection = new ConnectionHelper();
        connect = connection.connectionClass();

        String query = "UPDATE JobApplication" + " SET job_application_status = ? " +
                "WHERE job_application_id = ?";

        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, jobApplication.getStatus());
            stmt.setString(2, jobApplication.getId());
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
