package com.TimeToWork.TimeToWork.Adapter;

import android.util.Log;
import java.sql.*;
import java.util.ArrayList;

import com.TimeToWork.TimeToWork.Entity.Company;

public class CompanyList {

    private Connection connect;
    private PreparedStatement stmt;
    private ResultSet rs;

    // RegisterPage
    public void insertCompanyDetail(Company companyDetails){
        ConnectionHelper connection = new ConnectionHelper(); // Open Connection
        connect = connection.connectionclasss();
        String query = "INSERT INTO Company " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try{
            stmt = connect.prepareStatement(query);
            stmt.setString(1, companyDetails.getCompany_id());
            stmt.setString(2, companyDetails.getCompany_name());
            stmt.setString(3, companyDetails.getCompany_address());
            stmt.setString(4, companyDetails.getCompany_email());
            stmt.setString(5, companyDetails.getCompany_phone_number());
            stmt.setString(6, companyDetails.getCompany_rating());
            stmt.setString(7, companyDetails.getCompany_status());
            stmt.setString(8, companyDetails.getCompany_password());
            stmt.setString(9, null);
            stmt.executeUpdate();
            connect.close();
        } catch (SQLException e) {
            Log.e("Error here : ", e.getMessage());
        }
    }

    // Read ID
    public Company readID() {
        ConnectionHelper connection = new ConnectionHelper(); // Open Connection
        connect = connection.connectionclasss();
        String query = "SELECT * FROM Company" + " ORDER BY company_id DESC";
        Company companyID = null;
        try {
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();

            if (rs.next()) {
                companyID = new Company(rs.getString("company_id"));
            }
            connect.close();
        } catch (SQLException ex) {

        }
        return companyID;
    }

    // Get the Company Details
    public ArrayList<Company> getCompanyDetails(String company_id) {
        ConnectionHelper connection = new ConnectionHelper();
        connect = connection.connectionclasss();
        ArrayList<Company> companyArrayList = new ArrayList<Company>();
        Company company = null;
        String query = "SELECT * FROM Company WHERE company_id = ?";
        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, company_id);
            rs = stmt.executeQuery();

            while (rs.next()) {

                companyArrayList.add(new Company(rs.getString("company_ID"), rs.getString("company_name"), rs.getString("company_phone_number"), rs.getString("company_email"), rs.getString("company_address"), rs.getString("company_img")));
            }
            connect.close();
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return companyArrayList;
    }

    // Get all the Company Details
    public ArrayList<Company> getAllCompanyDetails() {
        ConnectionHelper connection = new ConnectionHelper();
        connect = connection.connectionclasss();
        ArrayList<Company> companyArrayList = new ArrayList<Company>();
        Company company = null;
        String query = "SELECT company_phone_num, company_email FROM Company";
        try {
            stmt = connect.prepareStatement(query);
            rs = stmt.executeQuery();

            while (rs.next()) {

                companyArrayList.add(new Company(rs.getString("company_phone_number"), rs.getString("company_email")));
            }
            connect.close();
        } catch (SQLException ex) {
            ex.getMessage();
        }
        return companyArrayList;
    }

    // Update the Company Profile Details
    public Company updateCompanyDetail(Company company) {
        ConnectionHelper connection = new ConnectionHelper();
        connect = connection.connectionclasss();
        String query = "UPDATE Company" + " SET company_name = ?, company_address = ?, company_email = ?, company_phone_number = ?, company_img = ? WHERE company_id = ?";

        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, company.getCompany_name());
            stmt.setString(2, company.getCompany_address());
            stmt.setString(3, company.getCompany_email());
            stmt.setString(4, company.getCompany_phone_number());
            stmt.setString(5, null);
            stmt.setString(6, company.getCompany_id());
            stmt.executeUpdate();
            connect.close();

        } catch (SQLException ex) {
            ex.getMessage();
        }
        return company;
    }

    // Change Company Password Details
    public Company updatePassword(Company company) {
        ConnectionHelper connection = new ConnectionHelper(); // Open Connection
        connect = connection.connectionclasss();
        String query = "UPDATE Company" + " SET company_password = ? WHERE company_id = ?";

        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, company.getCompany_password());
            stmt.setString(2, company.getCompany_id());
            stmt.executeUpdate();
            connect.close();

        } catch (SQLException ex) {
            ex.getMessage();
        }
        return company;
    }

    // Check the Company Phone Number
    public Boolean checkPhoneNum(String company_phone_number) {
        ConnectionHelper connection = new ConnectionHelper();
        connect = connection.connectionclasss();
        boolean chkPhoneNum = true;
        String query = "SELECT company_phone_number FROM Jobseeker WHERE company_phone_number = ?";
        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, company_phone_number);
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

    // Check the Company Email
    public Boolean checkEmail(String company_email) {
        ConnectionHelper connection = new ConnectionHelper();
        connect = connection.connectionclasss();
        boolean chkEmail = true;
        String query = "SELECT company_email FROM Jobseeker WHERE company_email = ?";
        try {
            stmt = connect.prepareStatement(query);
            stmt.setString(1, company_email);
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
}
