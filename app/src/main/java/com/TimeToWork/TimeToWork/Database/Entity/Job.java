package com.TimeToWork.TimeToWork.Database.Entity;

import java.io.Serializable;
import java.util.Date;

public class Job implements Serializable {

    private String id;
    private String title;
    private String companyId;
    private String workplace;
    private Date workingDate;
    private double wages;
    private String requirement;
    private String description;
    private String note;

    public Job() {
    }

    public Job(String id, String title, String companyId, String workplace, Date workingDate, double wages, String requirement, String description, String note) {
        this.id = id;
        this.title = title;
        this.companyId = companyId;
        this.workplace = workplace;
        this.workingDate = workingDate;
        this.wages = wages;
        this.requirement = requirement;
        this.description = description;
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getWorkplace() {
        return workplace;
    }

    public void setWorkplace(String workplace) {
        this.workplace = workplace;
    }

    public Date getWorkingDate() {
        return workingDate;
    }

    public void setWorkingDate(Date workingDate) {
        this.workingDate = workingDate;
    }

    public double getWages() {
        return wages;
    }

    public void setWages(double wages) {
        this.wages = wages;
    }

    public String getRequirement() {
        return requirement;
    }

    public void setRequirement(String requirement) {
        this.requirement = requirement;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
