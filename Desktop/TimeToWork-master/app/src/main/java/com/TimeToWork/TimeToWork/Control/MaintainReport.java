package com.TimeToWork.TimeToWork.Control;

import java.util.ArrayList;

import com.TimeToWork.TimeToWork.Adapter.ReportList;
import com.TimeToWork.TimeToWork.Entity.Report;

/**
 * Created by MelvinTanChunKeat on 5/11/2018.
 */

public class MaintainReport {

    private ReportList reportList = new ReportList();

    public ArrayList<Report> getIncomeReport(String jobseeker_id, int month, int year){
        return reportList.getIncomeReport(jobseeker_id, month, year);
    }

    public ArrayList<Report> getWorkerReport(String company_id){
        return reportList.getWorkerReport(company_id);
    }
}
