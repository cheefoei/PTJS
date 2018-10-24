package com.TimeToWork.TimeToWork.Database.Entity;

import java.io.Serializable;

public class WorkingTime implements Serializable {

    private String startWorkingTime;
    private String endWorkingTime;
    private String startBreakTime1;
    private String startBreakTime2;
    private String endBreakTime1;
    private String endBreakTime2;

    public WorkingTime() {
    }

    public String getStartWorkingTime() {
        return startWorkingTime;
    }

    public void setStartWorkingTime(String startWorkingTime) {
        this.startWorkingTime = startWorkingTime;
    }

    public String getEndWorkingTime() {
        return endWorkingTime;
    }

    public void setEndWorkingTime(String endWorkingTime) {
        this.endWorkingTime = endWorkingTime;
    }

    public String getStartBreakTime1() {
        return startBreakTime1;
    }

    public void setStartBreakTime1(String startBreakTime1) {
        this.startBreakTime1 = startBreakTime1;
    }

    public String getStartBreakTime2() {
        return startBreakTime2;
    }

    public void setStartBreakTime2(String startBreakTime2) {
        this.startBreakTime2 = startBreakTime2;
    }

    public String getEndBreakTime1() {
        return endBreakTime1;
    }

    public void setEndBreakTime1(String endBreakTime1) {
        this.endBreakTime1 = endBreakTime1;
    }

    public String getEndBreakTime2() {
        return endBreakTime2;
    }

    public void setEndBreakTime2(String endBreakTime2) {
        this.endBreakTime2 = endBreakTime2;
    }
}
