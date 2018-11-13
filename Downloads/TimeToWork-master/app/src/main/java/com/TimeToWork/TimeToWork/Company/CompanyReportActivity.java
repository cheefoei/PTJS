package com.TimeToWork.TimeToWork.Company;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;

import com.TimeToWork.TimeToWork.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class CompanyReportActivity extends AppCompatActivity {

    private String months, years;
    private int month, year;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_report);

        Spinner spinnerMonth = (Spinner) findViewById(R.id.spinner_card_month);
        spinnerMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                months = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Spinner spinnerYear = (Spinner) findViewById(R.id.spinner_card_year);
        spinnerYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                years = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        Button btnShow = (Button) findViewById(R.id.btnShow);
        btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Date date = null;
                try {
                    date = new SimpleDateFormat("MMMM", Locale.ENGLISH).parse(months);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTime(date);

                month = cal.get(Calendar.MONTH) + 1;
                year = Integer.parseInt(years);

                Intent intent = new Intent(CompanyReportActivity.this, PaymentReport.class);
                intent.putExtra("month", month);
                intent.putExtra("year", year);
                startActivity(intent);
            }
        });
    }
}
