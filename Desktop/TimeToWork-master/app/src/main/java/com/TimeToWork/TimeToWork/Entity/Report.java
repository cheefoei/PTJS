package com.TimeToWork.TimeToWork.Entity;

/**
 * Created by MelvinTanChunKeat on 5/11/2018.
 */

public class Report {

    String company_name, job, date, workerName, rate;
    double salary;

    public Report(String company_name, String job, String date, double salary) {
        this.company_name = company_name;
        this.job = job;
        this.date = date;
        this.salary = salary;
    }

    public Report(String company_name, String date, double salary) {
        this.company_name = company_name;
        this.job = job;
        this.date = date;
        this.salary = salary;
    }

    public String getWorkerName() {
        return workerName;
    }

    public void setWorkerName(String workerName) {
        this.workerName = workerName;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public Report(String job, String workerName, String rate) {
        this.job = job;
        this.workerName = workerName;
        this.rate = rate;

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
