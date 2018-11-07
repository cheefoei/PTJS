package com.TimeToWork.TimeToWork.Adapter;

import android.util.Log;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.TimeToWork.TimeToWork.Entity.Report;

/**
 * Created by MelvinTanChunKeat on 5/11/2018.
 */

public class ReportList {

    private Connection connect;
    private PreparedStatement stmt;
    private ResultSet rs;

    // Read Income Report
    public ArrayList<Report> getIncomeReport(String jobseeker_id, int month, int year) {
        ConnectionHelper connection = new ConnectionHelper(); // Open Connection
        connect = connection.connectionclasss();
        ArrayList<Report> reportList = new ArrayList<Report>();

        String query = "Select s.jobseeker_id, c.company_name, j.job_post_category, date(p.payment_date), p.payment_amount From company c, jobpost j, jobseeker s, jobapplication a, payment p Where c.company_id = j.company_id AND j.job_post_id = a.job_post_id AND a.jobseeker_id = s.jobseeker_id AND j.job_post_id = p.job_post_id AND s.jobseeker_id = ? AND MONTH(p.payment_date) = ? AND YEAR(p.payment_date) = ?";
        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, jobseeker_id);
            stmt.setInt(2, month);
            stmt.setInt(3, year);
            rs = stmt.executeQuery();

            while (rs.next()) {
                reportList.add(new Report(rs.getString("company_name"), rs.getString("job_post_category"),rs.getString("date(p.payment_date)"),rs.getDouble("payment_amount")));
            }
            connect.close();
        } catch (SQLException ex) {
            ex.getMessage();

        }
        return reportList;
    }

    // Read Income Report
    public ArrayList<Report> getWorkerReport(String company_id) {
        ConnectionHelper connection = new ConnectionHelper(); // Open Connection
        connect = connection.connectionclasss();
        ArrayList<Report> reportList = new ArrayList<Report>();

        String query = "SELECT jobseeker_name, jobseeker_preferred_job, AVG(jobseeker_rating) From company c, jobpost j, jobseeker s, jobapplication a Where c.company_id = j.company_id AND j.job_post_id = a.job_post_id AND a.jobseeker_id = s.jobseeker_id AND c.company_name = 'Connect Dots Sdn Bhd'";
        try {
            stmt = connect.prepareStatement(query);
            //stmt.setString(1, company_id);
            rs = stmt.executeQuery();

            while (rs.next()) {
                reportList.add(new Report(rs.getString("jobseeker_preferred_job"), rs.getString("jobseeker_name"), rs.getString("AVG(jobseeker_rating)")));
            }
            connect.close();
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return reportList;
    }
}
