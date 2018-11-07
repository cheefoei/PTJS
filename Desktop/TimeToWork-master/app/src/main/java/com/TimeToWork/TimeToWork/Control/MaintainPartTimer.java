package com.TimeToWork.TimeToWork.Control;

import com.TimeToWork.TimeToWork.Adapter.PartTimerList;
import com.TimeToWork.TimeToWork.Entity.PartTimer;
import java.sql.SQLException;
import java.util.ArrayList;

public class MaintainPartTimer {

    private PartTimerList partTimeList = new PartTimerList();

    // RegistrationPage
    public void insertJobSeekerDetail(PartTimer partTimer){
        partTimeList.insertJobSeekerDetail(partTimer);
    }

    public PartTimer readID(){
        return partTimeList.readID();
    }

    public ArrayList<PartTimer> getPartTimerDetails(String jobseeker_id) {
        ArrayList<PartTimer> partTimerArrayLister = partTimeList.getPartTimerDetails(jobseeker_id);
        return partTimerArrayLister;
    }

    public Boolean checkIc(String jobseeker_ic) {
        Boolean chkIC = partTimeList.checkIc(jobseeker_ic);
        return chkIC;
    }

    public Boolean checkPhoneNum(String jobseeker_phone_number) {
        Boolean chkPhoneNum = partTimeList.checkPhoneNum(jobseeker_phone_number);
        return chkPhoneNum;
    }

    public Boolean checkEmail(String jobseeker_email) {
        Boolean chkEmail = partTimeList.checkEmail(jobseeker_email);
        return chkEmail;
    }

    public void updateJobSeekerDetails(PartTimer partTimer) {
        partTimeList.updateJobSeekerDetail(partTimer);
    }

    public void updatePreferredJob(PartTimer partTimer) {
        partTimeList.updatePreferredJob(partTimer);
    }

    public void updatePreferredLocation(PartTimer partTimer) {
        partTimeList.updatePreferredLocation(partTimer);
    }

    public void updatePassword(PartTimer partTimer) {
        partTimeList.updatePassword(partTimer);
    }

    public ArrayList<PartTimer> getPartTimerProfile(String jobseeker_id) {
        ArrayList<PartTimer> partTimerArrayLister = partTimeList.getPartTimerProfile(jobseeker_id);
        return partTimerArrayLister;
    }

}
