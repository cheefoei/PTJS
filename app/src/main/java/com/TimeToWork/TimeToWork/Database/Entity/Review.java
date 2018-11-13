package com.TimeToWork.TimeToWork.Database.Entity;

import java.io.Serializable;
import java.util.Date;

public class Review implements Serializable {

    private String id;
    private Date date;
    private String comment;
    private String tag;
    private double starValue;
    private Jobseeker jobseeker;
    private Company company;

    public Review() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public double getStarValue() {
        return starValue;
    }

    public void setStarValue(double starValue) {
        this.starValue = starValue;
    }

    public Jobseeker getJobseeker() {
        return jobseeker;
    }

    public void setJobseeker(Jobseeker jobseeker) {
        this.jobseeker = jobseeker;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}
