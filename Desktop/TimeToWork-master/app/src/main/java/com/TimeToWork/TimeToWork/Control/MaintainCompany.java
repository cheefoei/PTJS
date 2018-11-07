package com.TimeToWork.TimeToWork.Control;

import com.TimeToWork.TimeToWork.Adapter.CompanyList;
import com.TimeToWork.TimeToWork.Entity.Company;
import java.sql.SQLException;
import java.util.ArrayList;

public class MaintainCompany {

    private CompanyList companyList = new CompanyList();

    // Registration Company Page
    public void insertCompanyDetail(Company company){
        companyList.insertCompanyDetail(company);
    }

    public Company readID(){
        return companyList.readID();
    }

    public ArrayList<Company> getCompanyDetails(String company_id) {
        ArrayList<Company> companyArrayLister = companyList.getCompanyDetails(company_id);
        return companyArrayLister;
    }

    public Boolean checkPhoneNum(String jobseeker_phone_number) {
        Boolean chkPhoneNum = companyList.checkPhoneNum(jobseeker_phone_number);
        return chkPhoneNum;
    }

    public Boolean checkEmail(String jobseeker_email) {
        Boolean chkEmail = companyList.checkEmail(jobseeker_email);
        return chkEmail;
    }

    public ArrayList<Company> getAllCompanyDetails() {
        ArrayList<Company> companyArrayLister = companyList.getAllCompanyDetails();
        return companyArrayLister;
    }

    public void updateCompanyDetail(Company company) {
        companyList.updateCompanyDetail(company);
    }

    public void updatePassword(Company company) {
        companyList.updatePassword(company);
    }
}
