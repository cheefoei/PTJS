package com.TimeToWork.TimeToWork.Database.Entity;

import java.io.Serializable;

public class WorkingTime implements Serializable {

    private String startWorkingTime;
    private String endWorkingTime;
    private String startFirstBreakTime;
    private String startSecondBreakTime;
    private String endFirstBreakTime;
    private String endSecondBreakTime;

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

    public String getStartFirstBreakTime() {
        return startFirstBreakTime;
    }

    public void setStartFirstBreakTime(String startFirstBreakTime) {
        this.startFirstBreakTime = startFirstBreakTime;
    }

    public String getStartSecondBreakTime() {
        return startSecondBreakTime;
    }

    public void setStartSecondBreakTime(String startSecondBreakTime) {
        this.startSecondBreakTime = startSecondBreakTime;
    }

    public String getEndFirstBreakTime() {
        return endFirstBreakTime;
    }

    public void setEndFirstBreakTime(String endFirstBreakTime) {
        this.endFirstBreakTime = endFirstBreakTime;
    }

    public String getEndSecondBreakTime() {
        return endSecondBreakTime;
    }

    public void setEndSecondBreakTime(String endSecondBreakTime) {
        this.endSecondBreakTime = endSecondBreakTime;
    }
}
