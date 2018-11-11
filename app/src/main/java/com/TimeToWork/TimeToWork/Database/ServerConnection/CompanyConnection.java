package com.TimeToWork.TimeToWork.Database.ServerConnection;

import android.util.Log;

import com.TimeToWork.TimeToWork.Database.Entity.Company;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class CompanyConnection {

    private ConnectionHelper connection;
    private Connection connect;
    private PreparedStatement stmt;
    private ResultSet rs;

    public CompanyConnection() {
        this.connection = new ConnectionHelper(); // Open Connection
    }

    // RegisterPage
    public boolean insertCompanyDetail(Company company) {

        connect = connection.connectionClass();
        String query = "INSERT INTO Company " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, company.getId());
            stmt.setString(2, company.getName());
            stmt.setString(3, company.getAddress());
            stmt.setString(4, company.getPhone_number());
            stmt.setString(5, company.getEmail());
            stmt.setDouble(6, company.getRating());
            stmt.setString(7, company.getPassword());
            stmt.setString(8, null);
            stmt.executeUpdate();

            connect.close();
            return true;
        } catch (SQLException e) {
            Log.e("Error here : ", e.getMessage());
            return false;
        }
    }

    // Read ID
    public String getCompanyLastId() {

        connect = connection.connectionClass();

        String query = "SELECT * FROM Company" + " ORDER BY company_id DESC";
        String id = null;

        try {
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();

            if (rs.next()) {
                id = rs.getString("company_id");
            }
            connect.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return id;
    }

    // Get the Company Details
    public Company getCompanyDetail(String companyId) {

        connect = connection.connectionClass();

        Company company = null;
        String query = "SELECT * FROM Company WHERE company_id = ?";

        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, companyId);
            rs = stmt.executeQuery();

            while (rs.next()) {

                company = new Company();
                company.setId(rs.getString("company_ID"));
                company.setName(rs.getString("company_name"));
                company.setName(rs.getString("company_name"));
                company.setPhone_number(rs.getString("company_phone_number"));
                company.setEmail(rs.getString("company_email"));
                company.setAddress(rs.getString("company_address"));
                company.setImg(rs.getString("company_img"));
            }
            connect.close();
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return company;
    }

    // Get all the Company Details
    public ArrayList<Company> getAllCompanyDetails() {

        connect = connection.connectionClass();

        ArrayList<Company> companyArrayList = new ArrayList<>();
        String query = "SELECT company_phone_num, company_email FROM Company";

        try {
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {

                Company company = new Company();
                company.setId(rs.getString("company_ID"));
                company.setName(rs.getString("company_name"));
                company.setName(rs.getString("company_name"));
                company.setPhone_number(rs.getString("company_phone_number"));
                company.setEmail(rs.getString("company_email"));
                company.setAddress(rs.getString("company_address"));
                company.setImg(rs.getString("company_img"));

                companyArrayList.add(company);
            }
            connect.close();
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return companyArrayList;
    }

    // Update the Company Profile Details
    public Company updateCompanyDetail(Company company) {

        connect = connection.connectionClass();

        String query = "UPDATE Company" + " SET company_name = ?, company_address = ?, company_email = ?, " +
                "company_phone_number = ?, company_img = ? WHERE company_id = ?";

        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, company.getName());
            stmt.setString(2, company.getAddress());
            stmt.setString(3, company.getEmail());
            stmt.setString(4, company.getPhone_number());
            stmt.setString(5, null);
            stmt.setString(6, company.getId());
            stmt.executeUpdate();
            connect.close();
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return company;
    }

    // Change Company Password Details
    public Company updatePassword(Company company) {

        connect = connection.connectionClass();
        String query = "UPDATE Company" + " SET company_password = ? WHERE company_id = ?";

        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, company.getPassword());
            stmt.setString(2, company.getId());
            stmt.executeUpdate();

            connect.close();
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return company;
    }

    // Check the Company Phone Number
    public Boolean checkPhoneNum(String company_phone_number) {

        connect = connection.connectionClass();
        boolean chkPhoneNum = true;

        String query = "SELECT company_phone_number FROM Jobseeker WHERE company_phone_number = ?";
        try {

            stmt = connect.prepareStatement(query);
            stmt.setString(1, company_phone_number);

            rs = stmt.executeQuery();
            chkPhoneNum = !rs.next();

            connect.close();
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return chkPhoneNum;
    }

    // Check the Company Email
    public Boolean checkEmail(String company_email) {

        connect = connection.connectionClass();
        boolean chkEmail = true;
        String query = "SELECT company_email FROM Jobseeker WHERE company_email = ?";

        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, company_email);

            rs = stmt.executeQuery();
            chkEmail = !rs.next();

            connect.close();
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return chkEmail;
    }
}
