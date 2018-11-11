package com.TimeToWork.TimeToWork.Database.ServerConnection;

import android.util.Log;

import com.TimeToWork.TimeToWork.Database.Entity.Jobseeker;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class JobseekerConnection {

    private Connection connect;
    private PreparedStatement stmt;
    private ResultSet rs;

    // RegisterPage
    public boolean insertJobSeekerDetail(Jobseeker jobseeker) {

        ConnectionHelper connection = new ConnectionHelper(); // Open Connection
        connect = connection.connectionClass();
        String query = "INSERT INTO jobseeker " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, jobseeker.getId());
            stmt.setString(2, jobseeker.getName());
            stmt.setString(3, String.valueOf(jobseeker.getGender()));
            stmt.setString(4, new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                    .format(jobseeker.getDob()));
            stmt.setString(5, jobseeker.getIc());
            stmt.setString(6, jobseeker.getAddress());
            stmt.setString(7, jobseeker.getPhone_number());
            stmt.setString(8, jobseeker.getEmail());
            stmt.setString(9, null);
            stmt.setString(10, null);
            stmt.setString(11, null);
            stmt.setDouble(12, jobseeker.getRating());
            stmt.setString(13, jobseeker.getPassword());
            stmt.setString(14, null);
            stmt.executeUpdate();

            connect.close();
            return true;
        } catch (SQLException e) {

            Log.e("Error here : ", e.getMessage());
            return false;
        }
    }

    // Read ID
    public String getJobseekerLastId() {

        ConnectionHelper connection = new ConnectionHelper(); // Open Connection
        connect = connection.connectionClass();

        String query = "SELECT * FROM Jobseeker" + " ORDER BY jobseeker_id DESC";
        String id = null;

        try {
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();

            if (rs.next()) {
                id = rs.getString("jobseeker_id");
            }
            connect.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return id;
    }

    // Check the Jobseeker IC
    public Boolean checkIc(String jobseeker_ic) {

        ConnectionHelper connection = new ConnectionHelper();
        connect = connection.connectionClass();

        boolean chkIC = true;
        String query = "SELECT jobseeker_ic FROM Jobseeker WHERE jobseeker_ic = ?";
        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, jobseeker_ic);

            rs = stmt.executeQuery();
            chkIC = !rs.next();

            connect.close();
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return chkIC;
    }

    // Check the Jobseeker Phone Number
    public Boolean checkPhoneNum(String jobseeker_phone_number) {

        ConnectionHelper connection = new ConnectionHelper();
        connect = connection.connectionClass();

        boolean chkPhoneNum = true;
        String query = "SELECT jobseeker_phone_number FROM Jobseeker WHERE jobseeker_phone_number = ?";

        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, jobseeker_phone_number);

            rs = stmt.executeQuery();
            chkPhoneNum = !rs.next();

            connect.close();
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return chkPhoneNum;
    }

    // Check the Jobseeker Email
    public Boolean checkEmail(String jobseeker_email) {

        ConnectionHelper connection = new ConnectionHelper();
        connect = connection.connectionClass();

        boolean chkEmail = true;
        String query = "SELECT jobseeker_email FROM Jobseeker WHERE jobseeker_email = ?";

        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, jobseeker_email);

            rs = stmt.executeQuery();
            chkEmail = !rs.next();

            connect.close();
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return chkEmail;
    }

    // Get the Jobseeker Details
    public Jobseeker getJobseekerDetail(String jobseekerId) {

        ConnectionHelper connection = new ConnectionHelper();
        connect = connection.connectionClass();

        Jobseeker jobseeker = null;
        String query = "SELECT * FROM Jobseeker WHERE jobseeker_id = ?";

        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, jobseekerId);
            rs = stmt.executeQuery();

            while (rs.next()) {

                jobseeker = new Jobseeker();
                jobseeker.setId(rs.getString("jobseeker_id"));
                jobseeker.setName(rs.getString("jobseeker_name"));
                jobseeker.setDob(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                        .parse(rs.getString("jobseeker_dob")));
                jobseeker.setIc(rs.getString("jobseeker_ic"));
                jobseeker.setAddress(rs.getString("jobseeker_address"));
                jobseeker.setPhone_number(rs.getString("jobseeker_phone_number"));
                jobseeker.setEmail(rs.getString("jobseeker_email"));
                jobseeker.setImg(rs.getString("jobseeker_img"));
                jobseeker.setGender(rs.getString("jobseeker_gender").charAt(0));
            }
            connect.close();
        } catch (SQLException ex) {
            ex.getMessage();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return jobseeker;
    }

    // Update the Jobseeker Profile Details
    public Jobseeker updateJobseekerDetail(Jobseeker jobseeker) {

        ConnectionHelper connection = new ConnectionHelper();
        connect = connection.connectionClass();
        String query = "UPDATE Jobseeker" + " SET jobseeker_name = ?, jobseeker_dob = ?, " +
                "jobseeker_ic = ?, jobseeker_address = ?, jobseeker_phone_number = ?, " +
                "jobseeker_email = ?, jobseeker_img = ?, jobseeker_gender = ? WHERE jobseeker_id = ?";

        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, jobseeker.getName());
            stmt.setString(2, new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                    .format(jobseeker.getDob()));
            stmt.setString(3, jobseeker.getIc());
            stmt.setString(4, jobseeker.getAddress());
            stmt.setString(5, jobseeker.getPhone_number());
            stmt.setString(6, jobseeker.getEmail());
            stmt.setString(7, jobseeker.getImg());
            stmt.setString(8, String.valueOf(jobseeker.getGender()));
            stmt.setString(9, jobseeker.getId());
            stmt.executeUpdate();
            connect.close();
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return jobseeker;
    }

    // Update Jobseeker Job Details
    public Jobseeker updatePreferredJob(Jobseeker jobseeker) {

        ConnectionHelper connection = new ConnectionHelper();
        connect = connection.connectionClass();
        String query = "UPDATE Jobseeker" + " SET jobseeker_preferred_job = ? " +
                "WHERE jobseeker_id = ?";

        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, jobseeker.getPreferred_job());
            stmt.setString(2, jobseeker.getId());
            stmt.executeUpdate();
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return jobseeker;
    }

    // Update Jobseeker Location Details
    public Jobseeker updatePreferredLocation(Jobseeker jobseeker) {

        ConnectionHelper connection = new ConnectionHelper();
        connect = connection.connectionClass();
        String query = "UPDATE Jobseeker" + " SET jobseeker_preferred_location = ? WHERE jobseeker_id = ?";

        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, jobseeker.getPreferred_location());
            stmt.setString(2, jobseeker.getId());
            stmt.executeUpdate();
            connect.close();
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return jobseeker;
    }

    // Change Jobseeker Password Details
    public Jobseeker updatePassword(Jobseeker jobseeker) {

        ConnectionHelper connection = new ConnectionHelper(); // Open Connection
        connect = connection.connectionClass();
        String query = "UPDATE Jobseeker" + " SET jobseeker_password = ? WHERE jobseeker_id = ?";

        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, jobseeker.getPassword());
            stmt.setString(2, jobseeker.getId());
            stmt.executeUpdate();
            connect.close();
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return jobseeker;
    }

    // Get the Jobseeker Earn and Completed
    public List<Object> getJobseekerProfile(String jobseekerId) {

        ConnectionHelper connection = new ConnectionHelper();
        connect = connection.connectionClass();

        List<Object> jobseekerList = new ArrayList<>();

        String query = "SELECT jobseeker_name, jobseeker_rating, COUNT(a.jobseeker_id) AS total_completed " +
                ", Sum(p.payment_amount) AS amount " +
                "From payment p, jobpost j, jobapplication a, jobseeker s " +
                "Where p.job_post_id = j.job_post_id AND " +
                "j.job_post_id = a.job_post_id AND " +
                "a.jobseeker_id = s.jobseeker_id AND " +
                "a.job_application_status = 'Completed' AND " +
                "s.jobseeker_id = ?;";
        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, jobseekerId);
            rs = stmt.executeQuery();

            while (rs.next()) {

                Jobseeker jobseeker = new Jobseeker();
                jobseeker.setName(rs.getString("jobseeker_name"));
                jobseeker.setRating(rs.getDouble("jobseeker_rating"));

                jobseekerList.add(0, jobseeker);
                jobseekerList.add(1, rs.getDouble("amount"));
                jobseekerList.add(2, rs.getInt("total_completed"));
            }
            connect.close();
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return jobseekerList;
    }
}