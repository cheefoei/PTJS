package com.TimeToWork.TimeToWork.Database.Control;

import com.TimeToWork.TimeToWork.Database.Entity.JobApplication;
import com.TimeToWork.TimeToWork.Database.ServerConnection.NotificationConnection;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by MelvinTanChunKeat on 11/10/2018.
 */

public class MaintainNotification {

    private NotificationConnection notificationConnection = new NotificationConnection();

    public List<JobApplication> getJobApplicationDetails() throws SQLException {
        return notificationConnection.getJobApplicationDetails();
    }

    public void updateJobApplication(JobApplication jobApplication) throws SQLException {
        notificationConnection.updateJobApplication(jobApplication);
    }
}
