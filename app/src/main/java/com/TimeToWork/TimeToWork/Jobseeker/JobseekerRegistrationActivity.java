package com.TimeToWork.TimeToWork.Jobseeker;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.Database.Control.MaintainJobseeker;
import com.TimeToWork.TimeToWork.Database.Entity.Jobseeker;
import com.TimeToWork.TimeToWork.R;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JobseekerRegistrationActivity extends AppCompatActivity {

    private MaintainJobseeker maintainJobseeker = new MaintainJobseeker();
    private EditText editTextName, editTextIC, editTextDob,
            editTextPhoneNumber, editTextEmail, editTextAddress,
            editTextPassword, editTextConfirmPassword;
    private RadioGroup radioGroup;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    private String name, ic, dob, phoneNum, email, address, pass;
    private char gender;

    private CustomProgressDialog mProgressDialog;
    private Handler handler;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_jobseeker_registration);

        mProgressDialog = new CustomProgressDialog(JobseekerRegistrationActivity.this);
        handler = new Handler();

        editTextName = (EditText) findViewById(R.id.name);
        editTextIC = (EditText) findViewById(R.id.ic);
        radioGroup = (RadioGroup) findViewById(R.id.gender);
        editTextPhoneNumber = (EditText) findViewById(R.id.phoneNumber);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextAddress = (EditText) findViewById(R.id.address);
        editTextPassword = (EditText) findViewById(R.id.password);
        editTextConfirmPassword = (EditText) findViewById(R.id.confirmPassword);
        editTextDob = (EditText) findViewById(R.id.dob);
        databaseRef = FirebaseDatabase.getInstance().getReference("jobseeker");

        editTextDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                dateSetListener = new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month += 1;
                        String date = day + "/" + month + "/" + year;
                        dob = year + "-" + month + "-" + day;
                        editTextDob.setText(date);
                    }
                };

                DatePickerDialog dialog = new DatePickerDialog(
                        JobseekerRegistrationActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,
                        year, month, day);
                if (dialog.getWindow() != null) {
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                }
                dialog.show();
            }
        });
    }

    // Show Okay Button (Upper Right)
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_done, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.done) {

            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {

                    final boolean validIc = checkIc();
                    final boolean   validPhoneNumber = checkPhoneNumber();
                    final boolean   validEmail = checkEmail();

                    handler.post(new Runnable() {

                        @Override
                        public void run() {

                            mProgressDialog.dismiss();

                            registerJobseeker();

                        }
                    });
                }
            });

            if (isValid()) {
                mProgressDialog.setMessage("Verifying your data …");
                mProgressDialog.show();
                thread.start();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void registerJobseeker() {

        int rgID = radioGroup.getCheckedRadioButtonId();

        if(rgID == 1)
        {
            gender = 'M';

        } else
        {
            gender = 'F';
        }

        mProgressDialog.setMessage("Registering account …");
        mProgressDialog.show();

        new Thread(new Runnable() {

            @Override
            public void run() {

                String id = maintainJobseeker.getJobseekerLastId();

                int number = Integer.parseInt(id.substring(1, 6)); // Get the behind 5 digits
                number += 1; // id + 1, Example: 10001 + 1 = 10002
                String word = id.substring(0, 1); // Get the first letter 'J'
                String jobseekerId = String.valueOf(word + number); // Get the first letter 'J' + the behind 5 digits
                String encryptedPassword = getEncryptedPassword();

                Jobseeker jobseeker = new Jobseeker();
                jobseeker.setId(jobseekerId);
                jobseeker.setName(name);
                jobseeker.setGender(gender);
                jobseeker.setIc(ic);
                jobseeker.setAddress(address);
                jobseeker.setPhone_number(phoneNum);
                jobseeker.setEmail(email);
                jobseeker.setRating(0.0);
                jobseeker.setPassword(encryptedPassword);

                databaseRef.child(jobseekerId).child("jobseeker_img").setValue("");

                try {
                    jobseeker.setDob(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(dob));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                final boolean success = maintainJobseeker.insertJobSeekerDetail(jobseeker);

                handler.post(new Runnable() {

                    @Override
                    public void run() {

                        mProgressDialog.dismiss();
                        AlertDialog.Builder builder =
                                new AlertDialog.Builder(JobseekerRegistrationActivity.this);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                finish();
                            }
                        });

                        if (success) {
                            builder.setMessage("Success Registered.");
                        } else {
                            builder.setMessage("Error occurred.");
                        }
                        builder.create()
                                .show();
                    }
                });
            }
        }).start();
    }

    private boolean isValid() {

        boolean valid = true;

        name = editTextName.getText().toString().trim();
        ic = editTextIC.getText().toString().trim();
        phoneNum = editTextPhoneNumber.getText().toString().trim();
        email = editTextEmail.getText().toString().trim();
        address = editTextAddress.getText().toString().trim();
        pass = editTextPassword.getText().toString().trim();
        String confirmPass = editTextConfirmPassword.getText().toString().trim();

        String nameFormat = "[a-zA-Z_]+";
        Pattern pattern = Pattern.compile(nameFormat);
        Matcher matcher = pattern.matcher(name);
        if (name.equals("")) {
            editTextName.setError("Cannot be empty");
            valid = false;
        }else{
            if (!matcher.matches()) {
                editTextName.setError("Name cannot be numeric");
                valid = false;
            }
        }

        String ICFormat = "\\d{6}-\\d{2}-\\d{4}";
        pattern = Pattern.compile(ICFormat);
        matcher = pattern.matcher(ic);

        if (ic.equals("")) {
            editTextIC.setError("Cannot be empty");
            valid = false;
        }else{
            if (!matcher.matches()) {
                editTextIC.setError("Invalid IC Format");
                valid = false;
            }
            else {
                if (!checkIc()) {
                    editTextIC.setError("IC Has Been Used.");
                    valid = false;
                }
            }
        }


        String phoneNumFormat = "\\d{3}-\\d{7}";
        pattern = Pattern.compile(phoneNumFormat);
        matcher = pattern.matcher(phoneNum);
        if (phoneNum.equals("")) {
            editTextPhoneNumber.setError("Cannot be empty");
            valid = false;
        }else {
            if (!matcher.matches()) {
                editTextPhoneNumber.setError("Invalid Phone Number Format");
                valid = false;
            } else {
                if (!checkPhoneNumber()) {
                    editTextPhoneNumber.setError("Phone Number Has Been Used.");
                    valid = false;
                }
            }
        }

        String emailFormat = "^(.+)@(.+)$";
        pattern = Pattern.compile(emailFormat);
        matcher = pattern.matcher(email);
        if (email.equals("")) {
            editTextEmail.setError("Cannot be empty");
            valid = false;
        }else {
            if (!matcher.matches()) {
                editTextEmail.setError("Invalid Email Format");
                valid = false;
            } else {
                if (!checkEmail()) {
                    editTextEmail.setError("Email Has Been Used.");
                    valid = false;
                }
            }
        }

        if (address.equals("")) {
            editTextAddress.setError("Cannot be empty");
            valid = false;
        }

        if (pass.equals("")) {
            editTextPassword.setError("Cannot be empty");
            valid = false;
        }else {
            if (confirmPass.equals("")) {
                editTextConfirmPassword.setError("Cannot be empty");
                valid = false;
            }else
            {
                if (pass.length() >= 8) {
                    if (Character.isUpperCase(pass.charAt(0))) {
                        if (!pass.equals(confirmPass)) {
                            editTextPassword.setError("Not match with password and confirm password");
                            valid = false;
                        }
                    } else {
                        editTextPassword.setError("Invalid Password Format");
                        valid = false;
                    }
                } else {
                    editTextPassword.setError("Invalid Password Format");
                    valid = false;
                }
            }

        }

        mProgressDialog.dismiss();

        return valid;
    }

    private Boolean checkIc() {
        return maintainJobseeker.checkIc(editTextIC.getText().toString());
    }

    private Boolean checkPhoneNumber() {
        return maintainJobseeker.checkPhoneNum(editTextPhoneNumber.getText().toString());
    }

    private Boolean checkEmail() {
        return maintainJobseeker.checkEmail(editTextEmail.getText().toString());
    }

    private String getEncryptedPassword() {

        String encryptedPassword = null;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(pass.getBytes());
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