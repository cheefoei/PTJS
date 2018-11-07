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

import java.util.ArrayList;
import com.TimeToWork.TimeToWork.Control.MaintainReport;
import com.TimeToWork.TimeToWork.Entity.Report;

public class IncomeSummaryReport extends AppCompatActivity {

    LinearLayout mChartLayout;
    TableLayout mTableLayout;
    Report report;
    ArrayList<Report> list = new ArrayList<>();
    ArrayList<String> arrayList = new ArrayList<>();
    private MaintainReport MReport = new MaintainReport();
    private Intent intent;
    private int month, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_summary_report);

        intent = getIntent();
        month = intent.getExtras().getInt("month");
        year = intent.getExtras().getInt("year");

        ListView listView = (ListView) findViewById(R.id.listView);

        list = MReport.getIncomeReport("J10001", month, year);

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.income_report, R.id.report, arrayList);

        for(int i = 0; i < list.size(); i++)
        {
            arrayList.add(i+1 + "                   " + list.get(i).getCompany_name() + "          " + list.get(i).getJob() + "         " + list.get(i).getDate() + "                " + list.get(i).getSalary());
            listView.setAdapter(adapter);
        }

    }

}
