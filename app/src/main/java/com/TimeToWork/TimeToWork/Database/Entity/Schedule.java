package com.TimeToWork.TimeToWork.Database.Entity;

import java.io.Serializable;

/**
 * Created by MelvinTanChunKeat on 11/10/2018.
 */

public class Schedule implements Serializable {

    private String schedule_id;
    private String schedule_list_id;
    private String jobseeker_id;
    private String timeFrom;
    private String timeTo;
    private String schedule_list_date;

    public Schedule(String schedule_id, String timeFrom, String timeTo) {

        this.schedule_id = schedule_id;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
    }

    public Schedule(String schedule_id,
                    String schedule_list_id,
                    String jobseeker_id,
                    String schedule_list_date) {

        this.schedule_id = schedule_id;
        this.schedule_list_id = schedule_list_id;
        this.jobseeker_id = jobseeker_id;
        this.schedule_list_date = schedule_list_date;
    }

    public Schedule(String schedule_id,
                    String schedule_list_id,
                    String jobseeker_id,
                    String timeFrom,
                    String timeTo,
                    String schedule_list_date) {

        this.schedule_id = schedule_id;
        this.schedule_list_id = schedule_list_id;
        this.jobseeker_id = jobseeker_id;
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
        this.schedule_list_date = schedule_list_date;
    }

    public Schedule(String schedule_id, String schedule_list_id) {
        this.schedule_id = schedule_id;
        this.schedule_list_id = schedule_list_id;
    }

    public String getSchedule_id() {
        return schedule_id;
    }

    public void setSchedule_id(String schedule_id) {
        this.schedule_id = schedule_id;
    }

    public String getSchedule_list_id() {
        return schedule_list_id;
    }

    public void setSchedule_list_id(String schedule_list_id) {
        this.schedule_list_id = schedule_list_id;
    }

    public String getJobseeker_id() {
        return jobseeker_id;
    }

    public void setJobseeker_id(String jobseeker_id) {
        this.jobseeker_id = jobseeker_id;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }

    public String getSchedule_list_date() {
        return schedule_list_date;
    }

    public void setSchedule_list_date(String schedule_list_date) {
        this.schedule_list_date = schedule_list_date;
    }
}
