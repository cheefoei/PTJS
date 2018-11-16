package com.TimeToWork.TimeToWork.Database.Entity;

import java.io.Serializable;

/**
 * Created by MelvinTanChunKeat on 5/11/2018.
 */

public class Report implements Serializable{

    String company_name, job, date, id, jobseeker_name;
    double salary, paymentAmount;

    public Report(String company_name, String job, String date, double salary) {
        this.company_name = company_name;
        this.job = job;
        this.date = date;
        this.salary = salary;
    }

    public Report() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJobseeker_name() {
        return jobseeker_name;
    }

    public void setJobseeker_name(String jobseeker_name) {
        this.jobseeker_name = jobseeker_name;
    }

    public double getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }
}
