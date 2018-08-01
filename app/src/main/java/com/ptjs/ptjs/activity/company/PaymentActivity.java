package com.ptjs.ptjs.activity.company;

import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ptjs.ptjs.R;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Button btnPayment = (Button) findViewById(R.id.btn_make_payment);
        btnPayment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder
                        = new AlertDialog.Builder(PaymentActivity.this, R.style.DialogTheme)
                        .setTitle("Successful")
                        .setMessage("Your payment is made.")
                        .setPositiveButton("OK", null);
                builder.show();
            }
        });
    }
}
