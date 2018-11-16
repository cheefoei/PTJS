package com.TimeToWork.TimeToWork.Database.ServerConnection;

import com.TimeToWork.TimeToWork.Database.Entity.Report;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/*
 * Created by MelvinTanChunKeat on 5/11/2018.
 */

public class ReportConnection {

    private Connection connect;
    private PreparedStatement stmt;
    private ResultSet rs;

    // Read Income Report
    public List<Report> getIncomeReport(String jobseeker_id, int month, int year) {

        ConnectionHelper connection = new ConnectionHelper(); // Open Connection
        connect = connection.connectionClass();

        List<Report> reportList = new ArrayList<>();
        String query = "Select c.company_name, j.job_post_category, " +
                "date(p.payment_date), p.payment_amount " +
                "From company c, jobpost j, jobseeker s, jobapplication a, payment p " +
                "Where c.company_id = j.company_id AND j.job_post_id = a.job_post_id " +
                "AND a.jobseeker_id = s.jobseeker_id AND j.job_post_id = p.job_post_id " +
                "AND s.jobseeker_id = ? AND MONTH(p.payment_date) = ? AND YEAR(p.payment_date) = ?";

        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, jobseeker_id);
            stmt.setInt(2, month);
            stmt.setInt(3, year);

            rs = stmt.executeQuery();
            while (rs.next()) {
                reportList.add(new Report(
                        rs.getString("company_name"),
                        rs.getString("job_post_category"),
                        rs.getString("date(p.payment_date)"),
                        rs.getDouble("payment_amount"))
                );
            }
            connect.close();
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return reportList;
    }

    // Read Payment Report
    public ArrayList<Report> getWorkerReport(String company_id, int month, int year) {
        ConnectionHelper connection = new ConnectionHelper(); // Open Connection
        connect = connection.connectionClass();
        ArrayList<Report> reportList = new ArrayList<Report>();

        String query = "Select company_name, j.job_post_category, date(p.payment_date), SUM(p.payment_amount) From company c, jobpost j, jobseeker s, jobapplication a, payment p Where c.company_id = j.company_id AND j.job_post_id = a.job_post_id AND a.jobseeker_id = s.jobseeker_id AND j.job_post_id = p.job_post_id AND c.company_id = ? AND MONTH(p.payment_date) = ? AND YEAR(p.payment_date) = ? GROUP BY j.job_post_category";
        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, company_id);
            stmt.setInt(2, month);
            stmt.setInt(3, year);
            rs = stmt.executeQuery();

            while (rs.next()) {
                reportList.add(new Report(rs.getString("company_name"), rs.getString("j.job_post_category"), rs.getString("date(p.payment_date)"), rs.getDouble("SUM(p.payment_amount)")));
            }
            connect.close();
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return reportList;
    }
}
