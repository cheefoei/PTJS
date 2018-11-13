package com.example.melvintanchunkeat.fyp;

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
import Control.MaintainReport;
import Entity.Report;

public class PaymentReport extends AppCompatActivity {

    LinearLayout mChartLayout;
    TableLayout mTableLayout;
    TextView title1, title2;
    Report report;
    ArrayList<Report> list = new ArrayList<>();
    ArrayList<String> arrayList = new ArrayList<>();
    private MaintainReport MReport = new MaintainReport();
    private Intent intent;
    private int month, year;
    private String months;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_report);

        title1 = (TextView)findViewById(R.id.title);
        title2 = (TextView)findViewById(R.id.title2);

        intent = getIntent();
        month = intent.getExtras().getInt("month");
        year = intent.getExtras().getInt("year");

        ListView listView = (ListView) findViewById(R.id.listView);

        list = MReport.getWorkerReport("C10002", month, year);

        if(month == 1)
        {
            months = "JANUARY";
        }
        else if(month == 2)
        {
            months = "FEBRUARY";
        }
        else if(month == 3)
        {
            months = "MARCH";
        }
        else if(month == 4)
        {
            months = "APRIL";
        }
        else if(month == 5)
        {
            months = "MAY";
        }
        else if(month == 6)
        {
            months = "JUNE";
        }
        else if(month == 7)
        {
            months = "JULY";
        }
        else if(month == 8)
        {
            months = "AUGUST";
        }
        else if(month == 9)
        {
            months = "SEPTEMBER";
        }
        else if(month == 10)
        {
            months = "OCTOBER";
        }
        else if(month == 11)
        {
            months = "NOVEMBER";
        }
        else if(month == 12)
        {
            months = "DECEMBER";
        }

        title1.setText(list.get(0).getCompany_name());

        title2.setText("Payment Summary Report for " + months + "  " + year);

        ArrayAdapter adapter = new ArrayAdapter(this, R.layout.income_report, R.id.report, arrayList);

        for(int i = 0; i < list.size(); i++)
        {
            arrayList.add(i+1 + "                   " + list.get(i).getId() + "             " + list.get(i).getJobseeker_name() + "         " + list.get(i).getJob() + "         " + list.get(i).getDate() + "         " + list.get(i).getPaymentAmount());
            listView.setAdapter(adapter);
        }

    }

}
