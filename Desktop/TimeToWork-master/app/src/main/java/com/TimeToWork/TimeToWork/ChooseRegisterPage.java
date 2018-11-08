package com.TimeToWork.TimeToWork;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChooseRegisterPage extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_register_page);

        Button shwJobseeker = (Button) findViewById(R.id.btnJobseeker);
        shwJobseeker.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent intent = new Intent(ChooseRegisterPage.this, RegistrationPage.class);
                startActivity(intent);
            }
        });

        Button shwCompany = (Button) findViewById(R.id.btnCompany);
        shwCompany.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent myIntent = new Intent(ChooseRegisterPage.this, CompanyRegistrationPage.class);
                startActivity(myIntent);

            }
        });
    }
}
