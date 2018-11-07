package com.TimeToWork.TimeToWork;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;
import com.TimeToWork.TimeToWork.Control.MaintainReport;
import com.TimeToWork.TimeToWork.Entity.Report;

public class WorkerRatingReport extends AppCompatActivity {

    LinearLayout mChartLayout;
    TableLayout mTableLayout;
    Report report;
    ArrayList<Report> list = new ArrayList<>();
    ArrayList<String> arrayList = new ArrayList<>();
    private MaintainReport MReport = new MaintainReport();
    private Intent intent;
    private int month, year;
    private static DecimalFormat df2 = new DecimalFormat(".##");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_worker_rating_report);

        ListView listView = (ListView) findViewById(R.id.listView);

        list = MReport.getWorkerReport("C10001");

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.income_report, R.id.report, arrayList);

        for(int i = 0; i < list.size(); i++)
        {
            double rate = Double.parseDouble(list.get(i).getRate());
            arrayList.add(i+1 + "                   " + list.get(i).getWorkerName() + "          " + df2.format(rate) + "         " + list.get(i).getJob());
            listView.setAdapter(adapter);
        }

    }

}
