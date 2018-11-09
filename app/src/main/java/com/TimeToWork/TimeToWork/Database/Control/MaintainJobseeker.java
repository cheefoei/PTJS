package com.TimeToWork.TimeToWork.Database.Control;

import com.TimeToWork.TimeToWork.Database.Entity.Jobseeker;
import com.TimeToWork.TimeToWork.Database.ServerConnection.JobseekerConnection;

import java.util.List;

public class MaintainJobseeker {

    private JobseekerConnection jobseekerConnection = new JobseekerConnection();

    // RegistrationPage
    public boolean insertJobSeekerDetail(Jobseeker jobseeker) {
        return jobseekerConnection.insertJobSeekerDetail(jobseeker);
    }

    public String getJobseekerLastId() {
        return jobseekerConnection.getJobseekerLastId();
    }

    public List<Jobseeker> getJobseekerDetails(String jobseeker_id) {
        return jobseekerConnection.getJobseekerDetails(jobseeker_id);
    }

    public Boolean checkIc(String jobseeker_ic) {
        return jobseekerConnection.checkIc(jobseeker_ic);
    }

    public Boolean checkPhoneNum(String jobseeker_phone_number) {
        return jobseekerConnection.checkPhoneNum(jobseeker_phone_number);
    }

    public Boolean checkEmail(String jobseeker_email) {
        return jobseekerConnection.checkEmail(jobseeker_email);
    }

    public void updateJobSeekerDetails(Jobseeker jobseeker) {
        jobseekerConnection.updateJobseekerDetail(jobseeker);
    }

    public void updatePreferredJob(Jobseeker jobseeker) {
        jobseekerConnection.updatePreferredJob(jobseeker);
    }

    public void updatePreferredLocation(Jobseeker jobseeker) {
        jobseekerConnection.updatePreferredLocation(jobseeker);
    }

    public void updatePassword(Jobseeker jobseeker) {
        jobseekerConnection.updatePassword(jobseeker);
    }

    public List<Jobseeker> getJobseekerProfile(String jobseeker_id) {
        return jobseekerConnection.getJobseekerProfile(jobseeker_id);
    }

}
