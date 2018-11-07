package com.TimeToWork.TimeToWork.Entity;

public class Company {
    private String company_id, company_name, company_address, company_email, company_phone_number, company_rating, company_status, company_password;
    private String company_img;

    public Company() {
    }

    public Company(String company_id, String company_name, String company_address, String company_email, String company_phone_number, String company_rating, String company_status, String company_password, String company_img) {
        this.company_id = company_id;
        this.company_name = company_name;
        this.company_address = company_address;
        this.company_email = company_email;
        this.company_phone_number = company_phone_number;
        this.company_rating = company_rating;
        this.company_status = company_status;
        this.company_password = company_password;
        this.company_img = company_img;
    }

    public Company(String company_id, String company_name, String company_phone_number, String company_email, String company_address, String company_img) {
        this.company_id = company_id;
        this.company_name = company_name;
        this.company_address = company_address;
        this.company_email = company_email;
        this.company_phone_number = company_phone_number;
        this.company_img = company_img;
    }

    public Company(String company_email, String company_phone_number) {
        this.company_email = company_email;
        this.company_phone_number = company_phone_number;
    }

    public Company(String company_id) {
        this.company_id = company_id;
    }

    public String getCompany_id() {
        return company_id;
    }

    public void setCompany_id(String company_id) {
        this.company_id = company_id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getCompany_address() {
        return company_address;
    }

    public void setCompany_address(String company_address) {
        this.company_address = company_address;
    }

    public String getCompany_email() {
        return company_email;
    }

    public void setCompany_email(String company_email) {
        this.company_email = company_email;
    }

    public String getCompany_phone_number() {
        return company_phone_number;
    }

    public void setCompany_phone_number(String company_phone_number) {
        this.company_phone_number = company_phone_number;
    }

    public String getCompany_rating() {
        return company_rating;
    }

    public void setCompany_rating(String company_rating) {
        this.company_rating = company_rating;
    }

    public String getCompany_status() {
        return company_status;
    }

    public void setCompany_status(String company_status) {
        this.company_status = company_status;
    }

    public String getCompany_password() {
        return company_password;
    }

    public void setCompany_password(String company_password) {
        this.company_password = company_password;
    }

    public String getCompany_img() {
        return company_img;
    }

    public void setCompany_img(String company_img) {
        this.company_img = company_img;
    }
}
