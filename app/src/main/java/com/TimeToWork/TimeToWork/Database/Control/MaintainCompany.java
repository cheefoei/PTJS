package com.TimeToWork.TimeToWork.Database.Control;

import com.TimeToWork.TimeToWork.Database.Entity.Company;
import com.TimeToWork.TimeToWork.Database.ServerConnection.CompanyConnection;

import java.util.List;

public class MaintainCompany {

    private CompanyConnection companyConnection = new CompanyConnection();

    // Registration Company Page
    public boolean insertCompanyDetail(Company company) {
        return companyConnection.insertCompanyDetail(company);
    }

    public String getCompanyLastId() {
        return companyConnection.getCompanyLastId();
    }

    public Company getCompanyDetail(String companyId) {
        return companyConnection.getCompanyDetail(companyId);
    }

    public Boolean checkPhoneNum(String company_phone_number) {
        return companyConnection.checkPhoneNum(company_phone_number);
    }

    public Boolean checkEmail(String company_email) {
        return companyConnection.checkEmail(company_email);
    }

    public List<Company> getAllCompanyDetails() {
        return companyConnection.getAllCompanyDetails();
    }

    public void updateCompanyDetail(Company company) {
        companyConnection.updateCompanyDetail(company);
    }

    public void updatePassword(Company company) {
        companyConnection.updatePassword(company);
    }
}
