package com.TimeToWork.TimeToWork;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.TimeToWork.TimeToWork.Control.MaintainPartTimer;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.TimeToWork.TimeToWork.Adapter.PartTimerList;
import com.TimeToWork.TimeToWork.Entity.PartTimer;
import java.util.ArrayList;
import android.util.Base64;

public class RegistrationPage extends AppCompatActivity {

    private MaintainPartTimer MPartTimer = new MaintainPartTimer();
    private EditText editTextName, editTextIC, editTextDob, editTextPhoneNumber, editTextEmail, editTextAddress, editTextPassword, editTextConfirmPassword;
    private RadioGroup radioGroup;
    private Button btnOkay;
    private Toast errorToast, successfulToast;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private String dob;
    private String ICFormat = "\\d{6}\\-\\d{2}\\-\\d{4}";
    private String emailFormat = "^(.+)@(.+)$";
    private String nameFormat = "[a-zA-Z_]+";
    private String phoneNumFormat = "[0-9]+";
    private Pattern pattern = null;
    private Matcher matcher = null;
    private DatabaseReference databaseRef;
    private ArrayList<PartTimer> partTimerArrayList;
    private Boolean chkIC, chkPhoneNum, chkEmail;
    private static MessageDigest md;
    byte[] passBytes;

    // Show Okay Button (Upper Right)
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_registration_page);
        databaseRef = FirebaseDatabase.getInstance().getReference("jobseeker");
        editTextName = (EditText) findViewById(R.id.name);
        editTextIC = (EditText) findViewById(R.id.ic);
        radioGroup = (RadioGroup) findViewById(R.id.gender);
        editTextPhoneNumber = (EditText) findViewById(R.id.phoneNumber);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextAddress = (EditText) findViewById(R.id.address);
        editTextPassword = (EditText) findViewById(R.id.password);
        editTextConfirmPassword = (EditText) findViewById(R.id.confirmPassword);
        editTextDob = (EditText) findViewById(R.id.dob);

        btnOkay = (Button) findViewById(R.id.btnOk);
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
                        RegistrationPage.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        dateSetListener,
                        year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });
    }

    private void registerJobSeeker(){
        String id = MPartTimer.readID().getJobseeker_id();
        int number = Integer.parseInt(id.substring(1, 6)); // Get the behind 5 digits
        number += 1; // id + 1, Example: 10001 + 1 = 10002
        String word = id.substring(0, 1); // Get the first two letter 'JR'
        String jobseeker_id = String.valueOf(word + number); // Get the first two letter 'JR' + the behind 5 digits
        String name = editTextName.getText().toString().trim();
        String ic = editTextIC.getText().toString().trim();
        int rgID = radioGroup.getCheckedRadioButtonId();
        char gender;
        if(rgID == 1)
        {
            gender = 'M';
        } else
        {
            gender = 'F';
        }
        String phoneNum = editTextPhoneNumber.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String pass = editTextPassword.getText().toString().trim();
        String confirmPass = editTextConfirmPassword.getText().toString().trim();
        pattern = Pattern.compile(nameFormat);
        matcher = pattern.matcher(name);

        if (!matcher.matches()) {
            errorToast = Toast.makeText(RegistrationPage.this, "Name cannot be numberic", Toast.LENGTH_LONG);
            errorToast.show();
        }
        else
        {
            pattern = Pattern.compile(ICFormat);
            matcher = pattern.matcher(ic);

            if(!matcher.matches())
            {
                errorToast = Toast.makeText(RegistrationPage.this, "Invalid IC Format", Toast.LENGTH_LONG);
                errorToast.show();
            }
            else
            {
                if(checkIc() == false) {
                    errorToast = Toast.makeText(RegistrationPage.this, "IC Has Been Used.", Toast.LENGTH_LONG);
                    errorToast.show();
                }
                else
                {
                    pattern = Pattern.compile(phoneNumFormat);
                    matcher = pattern.matcher(phoneNum);
                    if(!matcher.matches())
                    {
                        errorToast = Toast.makeText(RegistrationPage.this, "Invalid Phone Number Format", Toast.LENGTH_LONG);
                        errorToast.show();
                    }
                    else
                    {
                        if(checkPhoneNumber() == false) {
                            errorToast = Toast.makeText(RegistrationPage.this, "Phone Number Has Been Used.", Toast.LENGTH_LONG);
                            errorToast.show();
                        }
                        else
                        {
                            pattern = Pattern.compile(emailFormat);
                            matcher = pattern.matcher(email);
                            if(!matcher.matches())
                            {
                                errorToast = Toast.makeText(RegistrationPage.this, "Invalid Email Format", Toast.LENGTH_LONG);
                                errorToast.show();
                            }
                            else
                            {
                                if(checkEmail() == false) {
                                    errorToast = Toast.makeText(RegistrationPage.this, "Email Has Been Used.", Toast.LENGTH_LONG);
                                    errorToast.show();
                                }
                                else
                                {
                                    if(pass.length() >= 8)
                                    {
                                        if(Character.isUpperCase(pass.charAt(0)))
                                        {
                                            if(pass.equals(confirmPass))
                                            {
                                                String converted = Base64.encodeToString(pass.toString().getBytes(), Base64.DEFAULT);

                                                PartTimer detail = new PartTimer(jobseeker_id, name, gender, dob, ic, address, phoneNum, email, null, null, null, null, converted, null);
                                                MPartTimer.insertJobSeekerDetail(detail);
                                                databaseRef.child(jobseeker_id).child("jobseeker_img").setValue("");
                                                Intent intent = new Intent(RegistrationPage.this, Test.class);
                                                startActivity(intent);
                                                successfulToast = Toast.makeText(RegistrationPage.this, "Success Registered.", Toast.LENGTH_LONG);
                                                successfulToast.show();
                                            }
                                            else
                                            {
                                                errorToast = Toast.makeText(RegistrationPage.this, "Not match with password and confirm password", Toast.LENGTH_LONG);
                                                errorToast.show();
                                            }
                                        }
                                        else
                                        {
                                            errorToast = Toast.makeText(RegistrationPage.this, "Invalid Password Format", Toast.LENGTH_LONG);
                                            errorToast.show();
                                        }
                                    }
                                    else
                                    {
                                        errorToast = Toast.makeText(RegistrationPage.this, "Invalid Password Format", Toast.LENGTH_LONG);
                                        errorToast.show();
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private Boolean checkIc()
    {
        chkIC = MPartTimer.checkIc(editTextIC.getText().toString());
        return chkIC;
    }

    private Boolean checkPhoneNumber()
    {
        chkPhoneNum = MPartTimer.checkPhoneNum(editTextPhoneNumber.getText().toString());
        return chkPhoneNum;
    }

    private Boolean checkEmail()
    {
        chkEmail = MPartTimer.checkEmail(editTextEmail.getText().toString());
        return chkEmail;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.btnOk) {
            registerJobSeeker();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}