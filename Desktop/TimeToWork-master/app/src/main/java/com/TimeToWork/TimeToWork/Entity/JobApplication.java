package com.TimeToWork.TimeToWork.Entity;

/**
 * Created by MelvinTanChunKeat on 11/10/2018.
 */

public class JobApplication {
    String job_application_id, job_application_date, job_application_status;

    public JobApplication(String job_application_id, String job_application_date, String job_application_status) {
        this.job_application_id = job_application_id;
        this.job_application_date = job_application_date;
        this.job_application_status = job_application_status;
    }

    public JobApplication() {
    }

    public String getJob_application_id() {
        return job_application_id;
    }

    public void setJob_application_id(String job_application_id) {
        this.job_application_id = job_application_id;
    }

    public String getJob_application_date() {
        return job_application_date;
    }

    public void setJob_application_date(String job_application_date) {
        this.job_application_date = job_application_date;
    }

    public String getJob_application_status() {
        return job_application_status;
    }

    public void setJob_application_status(String job_application_status) {
        this.job_application_status = job_application_status;
    }
}
