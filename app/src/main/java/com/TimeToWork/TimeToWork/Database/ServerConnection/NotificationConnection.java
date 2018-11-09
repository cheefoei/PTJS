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

    public List<JobApplication> getJobApplicationDetails() throws SQLException {

        ConnectionHelper connection = new ConnectionHelper();
        connect = connection.connectionClass();

        List<JobApplication> jobApplicationArrayList = new ArrayList<>();
        String query = "SELECT * FROM JobApplication";

        try {
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {

                JobApplication jobApplication = new JobApplication();
                jobApplication.setId(rs.getString("job_application_id"));
                jobApplication.setDate(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
                        .parse(rs.getString("job_application_date")));
                jobApplication.setStatus(rs.getString("job_application_status"));

                jobApplicationArrayList.add(jobApplication);
            }
        } catch (SQLException ex) {
            ex.getMessage();
        } catch (ParseException e) {
            e.printStackTrace();
        } finally {
            if (!connect.isClosed()){
                connect.close();
            }
        }
        return jobApplicationArrayList;
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
