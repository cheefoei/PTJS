package com.TimeToWork.TimeToWork.Control;

import java.sql.SQLException;
import java.util.ArrayList;

import com.TimeToWork.TimeToWork.Adapter.Notification;
import com.TimeToWork.TimeToWork.Entity.JobApplication;
import java.util.ArrayList;

/**
 * Created by MelvinTanChunKeat on 11/10/2018.
 */

public class MaintainNotification {

    private Notification notificationList = new Notification();

    public ArrayList<JobApplication> getJobApplicationDetails() throws SQLException {
        ArrayList<JobApplication> jobApplicationArrayList = notificationList.getJobApplicationDetails();
        return jobApplicationArrayList;
    }

    public void updateJobApplication(JobApplication jobApplication) throws SQLException {
        notificationList.updateJobApplication(jobApplication);
    }
}
