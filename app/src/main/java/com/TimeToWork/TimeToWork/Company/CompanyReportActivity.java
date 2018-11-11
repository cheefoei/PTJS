package com.TimeToWork.TimeToWork.Company;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.TimeToWork.TimeToWork.R;

public class CompanyReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_report);

        Button btnWorkerRatingReport = (Button) findViewById(R.id.btnReport1);
        Button btnCompanyRatingReport = (Button) findViewById(R.id.btnReport2);

        btnWorkerRatingReport.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanyReportActivity.this, WorkerRatingReportActivity.class);
                startActivity(intent);
            }
        });

        btnCompanyRatingReport.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanyReportActivity.this, WorkerRatingReportActivity.class);
                startActivity(intent);
            }
        });
    }
}
