package com.TimeToWork.TimeToWork.Database.Control;

import com.TimeToWork.TimeToWork.Database.Entity.Report;
import com.TimeToWork.TimeToWork.Database.ServerConnection.ReportConnection;

import java.util.List;

/*
 * Created by MelvinTanChunKeat on 5/11/2018.
 */

public class MaintainReport {

    private ReportConnection reportConnection = new ReportConnection();

    public List<Report> getIncomeReport(String jobseeker_id, int month, int year) {
        return reportConnection.getIncomeReport(jobseeker_id, month, year);
    }

    public List<Report> getWorkerReport(String company_id, int month, int year){
        return reportConnection.getWorkerReport(company_id, month, year);
    }
}
