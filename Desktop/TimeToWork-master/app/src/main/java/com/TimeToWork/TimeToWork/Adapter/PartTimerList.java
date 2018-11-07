package com.TimeToWork.TimeToWork.Adapter;

import android.util.Log;
import java.sql.*;
import java.util.ArrayList;
import com.TimeToWork.TimeToWork.Entity.PartTimer;

public class PartTimerList {

    private Connection connect;
    private PreparedStatement stmt;
    private ResultSet rs;

    // RegisterPage
    public void insertJobSeekerDetail(PartTimer partTimerDetails){
        ConnectionHelper connection = new ConnectionHelper(); // Open Connection
        connect = connection.connectionclasss();
        String query = "INSERT INTO jobseeker " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try{
            stmt = connect.prepareStatement(query);
            stmt.setString(1, partTimerDetails.getJobseeker_id());
            stmt.setString(2, partTimerDetails.getJobseeker_name());
            stmt.setString(3, String.valueOf(partTimerDetails.getJobseeker_gender()));
            stmt.setString(4, partTimerDetails.getJobseeker_dob());
            stmt.setString(5, partTimerDetails.getJobseeker_ic());
            stmt.setString(6, partTimerDetails.getJobseeker_address());
            stmt.setString(7, partTimerDetails.getJobseeker_phone_number());
            stmt.setString(8, partTimerDetails.getJobseeker_email());
            stmt.setString(9, null);
            stmt.setString(10, null);
            stmt.setString(11, null);
            stmt.setString(12, null);
            stmt.setString(13, partTimerDetails.getJobseeker_password());
            stmt.setString(14, null);
            stmt.executeUpdate();
            connect.close();
        } catch (SQLException e) {
            Log.e("Error here : ", e.getMessage());
        }
    }

    // Read ID
    public PartTimer readID() {
        ConnectionHelper connection = new ConnectionHelper(); // Open Connection
        connect = connection.connectionclasss();
        String query = "SELECT * FROM Jobseeker" + " ORDER BY jobseeker_id DESC";
        PartTimer partTimer = null;
        try {
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();

            if (rs.next()) {
                partTimer = new PartTimer(rs.getString("jobseeker_id"));
            }
            connect.close();
        } catch (SQLException ex) {

        }
        return partTimer;
    }

    // Check the Jobseeker IC
    public Boolean checkIc(String jobseeker_ic) {
        ConnectionHelper connection = new ConnectionHelper();
        connect = connection.connectionclasss();
        boolean chkIC = true;
        PartTimer partTimer = null;
        String query = "SELECT jobseeker_ic FROM Jobseeker WHERE jobseeker_ic = ?";
        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, jobseeker_ic);
            rs = stmt.executeQuery();
            if(rs.next() == false)
            {
                chkIC = true;
            }
            else
            {
                chkIC = false;
            }
            connect.close();
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return chkIC;
    }

    // Check the Jobseeker Phone Number
    public Boolean checkPhoneNum(String jobseeker_phone_number) {
        ConnectionHelper connection = new ConnectionHelper();
        connect = connection.connectionclasss();
        boolean chkPhoneNum = true;
        String query = "SELECT jobseeker_phone_number FROM Jobseeker WHERE jobseeker_phone_number = ?";
        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, jobseeker_phone_number);
            rs = stmt.executeQuery();
            if(rs.next() == false)
            {
                chkPhoneNum = true;
            }
            else
            {
                chkPhoneNum = false;
            }
            connect.close();
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return chkPhoneNum;
    }

    // Check the Jobseeker Email
    public Boolean checkEmail(String jobseeker_email) {
        ConnectionHelper connection = new ConnectionHelper();
        connect = connection.connectionclasss();
        boolean chkEmail = true;
        String query = "SELECT jobseeker_email FROM Jobseeker WHERE jobseeker_email = ?";
        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, jobseeker_email);
            rs = stmt.executeQuery();
            if(rs.next() == false)
            {
                chkEmail = true;
            }
            else
            {
                chkEmail = false;
            }
            connect.close();
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return chkEmail;
    }

    // Get the Jobseeker Details
    public ArrayList<PartTimer> getPartTimerDetails(String jobseeker_id) {
        ConnectionHelper connection = new ConnectionHelper();
        connect = connection.connectionclasss();
        ArrayList<PartTimer> partTimerArrayList = new ArrayList<PartTimer>();
        PartTimer partTimer = null;
        String query = "SELECT * FROM Jobseeker WHERE jobseeker_id = ?";
        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, jobseeker_id);
            rs = stmt.executeQuery();

            while (rs.next()) {

                partTimerArrayList.add(new PartTimer(rs.getString("jobseeker_id"), rs.getString("jobseeker_name"), rs.getString("jobseeker_dob"), rs.getString("jobseeker_ic"), rs.getString("jobseeker_address"), rs.getString("jobseeker_phone_number"), rs.getString("jobseeker_email"), rs.getString("jobseeker_img"), rs.getString("jobseeker_gender").charAt(0)));
            }
            connect.close();
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return partTimerArrayList;
    }

    // Update the Jobseeker Profile Details
    public PartTimer updateJobSeekerDetail(PartTimer partTimer) {
        ConnectionHelper connection = new ConnectionHelper();
        connect = connection.connectionclasss();
        String query = "UPDATE Jobseeker" + " SET jobseeker_name = ?, jobseeker_dob = ?, jobseeker_ic = ?, jobseeker_address = ?, jobseeker_phone_number = ?, jobseeker_email = ?, jobseeker_img = ?, jobseeker_gender = ? WHERE jobseeker_id = ?";

        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, partTimer.getJobseeker_name());
            stmt.setString(2, partTimer.getJobseeker_dob());
            stmt.setString(3, partTimer.getJobseeker_ic());
            stmt.setString(4, partTimer.getJobseeker_address());
            stmt.setString(5, partTimer.getJobseeker_phone_number());
            stmt.setString(6, partTimer.getJobseeker_email());
            stmt.setString(7, partTimer.getJobseeker_img());
            stmt.setString(8, String.valueOf(partTimer.getJobseeker_gender()));
            stmt.setString(9, "J10001");
            stmt.executeUpdate();
            connect.close();

        } catch (SQLException ex) {
            ex.getMessage();
        }
        return partTimer;
    }

    // Update Jobseeker Job Details
    public PartTimer updatePreferredJob(PartTimer partTimer) {
        ConnectionHelper connection = new ConnectionHelper();
        connect = connection.connectionclasss();
        String query = "UPDATE Jobseeker" + " SET jobseeker_preferred_job = ? WHERE jobseeker_id = ?";

        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, partTimer.getJobseeker_preferred_job());
            stmt.setString(2, partTimer.getJobseeker_id());
            stmt.executeUpdate();

        } catch (SQLException ex) {
            ex.getMessage();
        }
        return partTimer;
    }

    // Update Jobseeker Location Details
    public PartTimer updatePreferredLocation(PartTimer partTimer) {
        ConnectionHelper connection = new ConnectionHelper();
        connect = connection.connectionclasss();
        String query = "UPDATE Jobseeker" + " SET jobseeker_preferred_location = ? WHERE jobseeker_id = ?";

        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, partTimer.getJobseeker_preferred_location());
            stmt.setString(2, partTimer.getJobseeker_id());
            stmt.executeUpdate();
            connect.close();

        } catch (SQLException ex) {
            ex.getMessage();
        }
        return partTimer;
    }

    // Change Jobseeker Password Details
    public PartTimer updatePassword(PartTimer partTimer) {
        ConnectionHelper connection = new ConnectionHelper(); // Open Connection
        connect = connection.connectionclasss();
        String query = "UPDATE Jobseeker" + " SET jobseeker_password = ? WHERE jobseeker_id = ?";

        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, partTimer.getJobseeker_password());
            stmt.setString(2, partTimer.getJobseeker_id());
            stmt.executeUpdate();
            connect.close();

        } catch (SQLException ex) {
            ex.getMessage();
        }
        return partTimer;
    }

    // Get the Jobseeker Earn and Completed
    public ArrayList<PartTimer> getPartTimerProfile(String jobseeker_id) {
        ConnectionHelper connection = new ConnectionHelper();
        connect = connection.connectionclasss();
        ArrayList<PartTimer> partTimerArrayList = new ArrayList<PartTimer>();
        PartTimer partTimer = null;
        String query = "SELECT jobseeker_name, COUNT(s.jobseeker_id)" +
                ", Sum(p.payment_amount)\n" +
                "From payment p, jobpost j, jobapplication a, jobseeker s\n" +
                "Where p.job_post_id = j.job_post_id AND\n" +
                "j.job_post_id = a.job_post_id AND\n" +
                "a.jobseeker_id = s.jobseeker_id AND\n" +
                "a.job_application_status = 'Sent' AND\n" +
                "s.jobseeker_id = ?;";
        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, jobseeker_id);
            rs = stmt.executeQuery();

            while (rs.next()) {

                partTimerArrayList.add(new PartTimer(rs.getString("jobseeker_name"), rs.getInt("Sum(p.payment_amount)"), rs.getInt("COUNT(s.jobseeker_id)")));
            }
            connect.close();
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return partTimerArrayList;
    }
}