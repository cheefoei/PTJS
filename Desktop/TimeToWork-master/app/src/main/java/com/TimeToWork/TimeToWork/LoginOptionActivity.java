package com.TimeToWork.TimeToWork;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginOptionActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_option);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        intent = new Intent(LoginOptionActivity.this, LoginActivity.class);

        Button btnJobseeker = (Button) findViewById(R.id.btn_iamjobseeker);
        btnJobseeker.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MainApplication.userType = "Jobseeker";
                intent.putExtra("jobseeker", "Jobseeker");
                startActivityForResult(intent, 0);
            }
        });

        Button btnCompany = (Button) findViewById(R.id.btn_iamcompany);
        btnCompany.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                MainApplication.userType = "Company";
                intent.putExtra("company", "Company");
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        if (resultCode == 1) {
            if (requestCode == 0) {
                if (data.getBooleanExtra("Success", false)) {
                    Intent intent = new Intent(LoginOptionActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }
    }
}
