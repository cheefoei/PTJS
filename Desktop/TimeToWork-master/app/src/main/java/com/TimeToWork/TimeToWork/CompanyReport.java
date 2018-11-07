package com.TimeToWork.TimeToWork;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

public class CompanyReport extends AppCompatActivity{

    private Button btnReport1, btnReport2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_report);
        btnReport1 = (Button) findViewById(R.id.btnReport1);
        btnReport2 = (Button) findViewById(R.id.btnReport2);

        btnReport1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanyReport.this, WorkerRatingReport.class);
                startActivity(intent);
            }
        });

        btnReport2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CompanyReport.this, WorkerRatingReport.class);
                startActivity(intent);
            }
        });


    }
}
