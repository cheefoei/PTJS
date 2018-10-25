package com.TimeToWork.TimeToWork.Company;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.TimeToWork.TimeToWork.Database.Entity.JobPost;
import com.TimeToWork.TimeToWork.R;

public class PaymentActivity extends AppCompatActivity {

    private JobPost jobPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        jobPost = (JobPost) getIntent().getSerializableExtra("JOBPOST");

        Button btnPayment = (Button) findViewById(R.id.btn_make_payment);
        btnPayment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder
                        = new AlertDialog.Builder(PaymentActivity.this, R.style.DialogStyle)
                        .setTitle("Successful")
                        .setMessage("Your payment is made.")
                        .setPositiveButton("OK", null);
                builder.show();
            }
        });
    }
}
