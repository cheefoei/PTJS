package com.TimeToWork.TimeToWork;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.Database.CompanyDA;
import com.TimeToWork.TimeToWork.Database.Control.MaintainCompany;
import com.TimeToWork.TimeToWork.Database.Control.MaintainJobseeker;
import com.TimeToWork.TimeToWork.Database.Entity.Company;
import com.TimeToWork.TimeToWork.Database.Entity.Jobseeker;
import com.TimeToWork.TimeToWork.Database.JobseekerDA;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import static com.TimeToWork.TimeToWork.MainApplication.userType;

public class ChangePasswordActivity extends AppCompatActivity {

    private MaintainJobseeker maintainJobseeker = new MaintainJobseeker();
    private MaintainCompany maintainCompany = new MaintainCompany();

    private CustomProgressDialog mProgressDialog;
    private Handler handler;

    private EditText editTextPassword;
    private String password;
//    private String confirmPassword, id, word;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        mProgressDialog = new CustomProgressDialog(this);
        handler = new Handler();

        editTextPassword = (EditText) findViewById(R.id.password);
//        EditText editTextConfirmPassword = (EditText) findViewById(R.id.confirmPassword);

        Button btnSave = (Button) findViewById(R.id.save);
        btnSave.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                changeUserPassword();
            }
        });
    }

    private void changeUserPassword() {

        //Show progress dialog
        mProgressDialog.setMessage("Changing your password …");
        mProgressDialog.show();

        new Thread(new Runnable() {

            @Override
            public void run() {

                password = editTextPassword.getText().toString().trim();
//                String confirmPassword = editTextPassword.getText().toString().trim();

                if (userType.equals("Jobseeker")) {

                    //Reading jobseeker data
                    JobseekerDA jobseekerDA = new JobseekerDA(ChangePasswordActivity.this);
                    Jobseeker jobseeker = jobseekerDA.getJobseekerData();
                    //Close jobseeker database
                    jobseekerDA.close();

                    jobseeker.setPassword(getEncryptedPassword());
                    maintainJobseeker.updatePassword(jobseeker);

                } else if (userType.equals("Company")) {

                    //Reading company data
                    CompanyDA companyDA = new CompanyDA(ChangePasswordActivity.this);
                    Company company = companyDA.getCompanyData();
                    //Close company database
                    companyDA.close();

                    company.setPassword(getEncryptedPassword());
                    maintainCompany.updatePassword(company);
                }

                handler.post(new Runnable() {

                    @Override
                    public void run() {

                        mProgressDialog.dismiss();

                        AlertDialog.Builder builder
                                = new AlertDialog.Builder(ChangePasswordActivity.this)
                                .setTitle("Successful")
                                .setMessage("Your password is changed.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });
                        builder.show();
                    }
                });
            }
        }).start();
    }

    private String getEncryptedPassword() {

        String encryptedPassword = null;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte aByte : bytes) {
                sb.append(String.format("%02X", aByte));
            }
            encryptedPassword = sb.toString();
        } catch (NoSuchAlgorithmException exc) {
            exc.printStackTrace();
        }

        return encryptedPassword;
    }

}
