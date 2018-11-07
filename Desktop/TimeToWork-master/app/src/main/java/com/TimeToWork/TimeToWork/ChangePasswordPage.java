package com.TimeToWork.TimeToWork;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.TimeToWork.TimeToWork.Control.MaintainCompany;
import com.TimeToWork.TimeToWork.Control.MaintainPartTimer;
import com.TimeToWork.TimeToWork.Entity.Company;
import com.TimeToWork.TimeToWork.Entity.PartTimer;

public class ChangePasswordPage extends AppCompatActivity {

    private EditText editTextPassword, editTextConfirmPassword;
    private MaintainPartTimer MPartTimer = new MaintainPartTimer();
    private MaintainCompany MCompany = new MaintainCompany();
    private Button btnSave;
    private String password, confirmPassword, id, word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password_page);

        editTextPassword = (EditText) findViewById(R.id.password);
        editTextConfirmPassword = (EditText) findViewById(R.id.confirmPassword);
        btnSave = (Button) findViewById(R.id.save);

        final char gender = Character.MIN_VALUE;

        id = "J10001";
        word = id.substring(0, 1); // Get the first two letter 'JR'

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                password = editTextPassword.getText().toString().trim();
                confirmPassword = editTextPassword.getText().toString().trim();
                if(word.equals("J"))
                {
                    PartTimer partTimerDetail = null;
                    partTimerDetail = new PartTimer("J10001", "", gender, "", "", "", "", "", "", "", "", "", password, null);
                    MPartTimer.updatePassword(partTimerDetail);
                    Intent intent = new Intent(ChangePasswordPage.this, ProfilePage.class);
                    startActivity(intent);
                }
                else
                {
                    Company companyDetail = null;
                    companyDetail = new Company("C10001", "", "", "", "", "", "", password, null);
                    MCompany.updatePassword(companyDetail);
                    Intent intent = new Intent(ChangePasswordPage.this, ProfilePage.class);
                    startActivity(intent);
                }

            }
        });


    }
}
