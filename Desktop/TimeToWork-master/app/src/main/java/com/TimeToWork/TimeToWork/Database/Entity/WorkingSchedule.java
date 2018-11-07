package com.TimeToWork.TimeToWork.Database.Entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class WorkingSchedule implements Serializable {

    private List<Date> workingDateList;
    private WorkingTime workingTime;

    public WorkingSchedule() {
    }

    public List<Date> getWorkingDateList() {
        return workingDateList;
    }

    public void setWorkingDateList(List<Date> workingDateList) {
        this.workingDateList = workingDateList;
    }

    public WorkingTime getWorkingTime() {
        return workingTime;
    }

    public void setWorkingTime(WorkingTime workingTime) {
        this.workingTime = workingTime;
    }
}
