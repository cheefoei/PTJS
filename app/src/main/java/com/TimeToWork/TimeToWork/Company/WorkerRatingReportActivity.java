package com.TimeToWork.TimeToWork.Company;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.Database.Control.MaintainReport;
import com.TimeToWork.TimeToWork.Database.Entity.Report;
import com.TimeToWork.TimeToWork.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import static com.TimeToWork.TimeToWork.MainApplication.userId;

public class WorkerRatingReportActivity extends AppCompatActivity {

    private MaintainReport maintainReport;

    private CustomProgressDialog mProgressDialog;
    private Handler handler;

//    LinearLayout mChartLayout;
//    TableLayout mTableLayout;

//    private Report report;
    private List<Report> reportList = new ArrayList<>();
    private List<String> arrayList = new ArrayList<>();

    //    private int month, year;
    private static DecimalFormat df2 = new DecimalFormat(".##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_rating_report);

        mProgressDialog = new CustomProgressDialog(this);
        handler = new Handler();

        //Show progress dialog
        mProgressDialog.setMessage("Loading …");
        mProgressDialog.show();

        new Thread(new Runnable() {

            @Override
            public void run() {

                maintainReport = new MaintainReport();
                reportList = maintainReport.getWorkerReport(userId);

                handler.post(new Runnable() {

                    @Override
                    public void run() {

                        ArrayAdapter<String> adapter = new ArrayAdapter<>(WorkerRatingReportActivity.this,
                                R.layout.list_view_income_report, R.id.report, arrayList);

                        ListView listView = (ListView) findViewById(R.id.list_view_report);
                        listView.setAdapter(adapter);

                        for (int i = 0; i < reportList.size(); i++) {
                            double rate = Double.parseDouble(reportList.get(i).getRate());
                            arrayList.add(i + 1 + "                   "
                                    + reportList.get(i).getWorkerName() + "          "
                                    + df2.format(rate) + "         "
                                    + reportList.get(i).getJob());
                            adapter.notifyDataSetChanged();
                        }
                        mProgressDialog.dismiss();
                    }
                });
            }
        }).start();
    }

}
