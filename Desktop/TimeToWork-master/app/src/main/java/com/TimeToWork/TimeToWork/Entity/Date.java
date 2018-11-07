package com.TimeToWork.TimeToWork.Entity;

/**
 * Created by MelvinTanChunKeat on 4/8/2018.
 */

public class Date {
    private String timeFrom;
    private String timeTo;
    private String time;

    public Date(String timeFrom, String timeTo) {
        this.timeFrom = timeFrom;
        this.timeTo = timeTo;
    }

    public Date(String time) {
        this.time = time;
    }

    public String getTimeFrom() {
        return timeFrom;
    }

    public String getTimeTo() {
        return timeTo;
    }

    public String getTime() {
        return time;
    }

    public void setTimeFrom(String timeFrom) {
        this.timeFrom = timeFrom;
    }

    public void setTimeTo(String timeTo) {
        this.timeTo = timeTo;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
