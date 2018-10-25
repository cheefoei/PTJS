package com.TimeToWork.TimeToWork.Database.Entity;

import java.io.Serializable;
import java.util.Date;

public class Payment implements Serializable {

    private String id;
    private Date date;
    private double amount;

    public Payment() {
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

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}