package com.TimeToWork.TimeToWork;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import com.TimeToWork.TimeToWork.Control.MaintainCompany;
import com.TimeToWork.TimeToWork.Entity.Company;

public class CompanyRegistrationPage extends AppCompatActivity {

    private MaintainCompany MCompany = new MaintainCompany();
    private EditText editTextName, editTextAddress, editTextEmail, editTextPhoneNumber, editTextPassword, editTextConfirmPassword;
    private String emailFormat = "^(.+)@(.+)$";
    private String nameFormat = "[a-zA-Z_]+";
    private String phoneNumFormat = "[0-9]+";
    private Pattern pattern = null;
    private Matcher matcher = null;
    private Toast errorToast, successfulToast;
    private ArrayList<Company> companyArrayList;
    private DatabaseReference databaseRef;
    private Boolean chkPhoneNum, chkEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_registration_page);

        databaseRef = FirebaseDatabase.getInstance().getReference("company");
        editTextName = (EditText) findViewById(R.id.name);
        editTextAddress = (EditText) findViewById(R.id.address);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPhoneNumber = (EditText) findViewById(R.id.phoneNumber);
        editTextPassword = (EditText) findViewById(R.id.password);
        editTextConfirmPassword = (EditText) findViewById(R.id.confirmPassword);
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }

    private void registerCompany() {
        String id = MCompany.readID().getCompany_id();
        int number = Integer.parseInt(id.substring(1, 6)); // Get the behind 5 digits
        number += 1; // id + 1, Example: 10001 + 1 = 10002
        String word = id.substring(0, 1); // Get the first two letter 'JR'
        String company_ID = String.valueOf(word + number); // Get the first two letter 'JR' + the behind 5 digits
        String name = editTextName.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String phoneNum = editTextPhoneNumber.getText().toString().trim();
        String pass = editTextPassword.getText().toString().trim();
        String confirmPass = editTextConfirmPassword.getText().toString().trim();
        pattern = Pattern.compile(nameFormat);
        matcher = pattern.matcher(name);
        if (!matcher.matches()) {
            errorToast = Toast.makeText(CompanyRegistrationPage.this, "Name cannot be numberic", Toast.LENGTH_LONG);
            errorToast.show();
        }
        else
        {
            pattern = Pattern.compile(emailFormat);
            matcher = pattern.matcher(email);

            if(!matcher.matches())
            {
                errorToast = Toast.makeText(CompanyRegistrationPage.this, "Invalid Email Format", Toast.LENGTH_LONG);
                errorToast.show();
            }
            else
            {
                if(checkEmail() == false) {
                    errorToast = Toast.makeText(CompanyRegistrationPage.this, "Email Has Been Used.", Toast.LENGTH_LONG);
                    errorToast.show();
                }
                else
                {
                    pattern = Pattern.compile(phoneNumFormat);
                    matcher = pattern.matcher(phoneNum);
                    if(!matcher.matches())
                    {
                        errorToast = Toast.makeText(CompanyRegistrationPage.this, "Invalid Phone Number Format", Toast.LENGTH_LONG);
                        errorToast.show();
                    }
                    else
                    {
                        if(checkPhoneNumber() == false) {
                            errorToast = Toast.makeText(CompanyRegistrationPage.this, "Phone Number Has Been Used.", Toast.LENGTH_LONG);
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
                                        Company detail = new Company(company_ID, name, address, email, phoneNum, null, null, pass, null);
                                        MCompany.insertCompanyDetail(detail);
                                        databaseRef.child(company_ID).child("company_img").setValue("");
                                        Intent intent = new Intent(CompanyRegistrationPage.this, Test.class);
                                        startActivity(intent);
                                        successfulToast = Toast.makeText(CompanyRegistrationPage.this, "Success Registered.", Toast.LENGTH_LONG);
                                        successfulToast.show();
                                    }
                                    else
                                    {
                                        errorToast = Toast.makeText(CompanyRegistrationPage.this, "Not match with password and confirm password", Toast.LENGTH_LONG);
                                        errorToast.show();
                                    }
                                }
                                else
                                {
                                    errorToast = Toast.makeText(CompanyRegistrationPage.this, "Invalid Password Format", Toast.LENGTH_LONG);
                                    errorToast.show();
                                }
                            }
                            else
                            {
                                errorToast = Toast.makeText(CompanyRegistrationPage.this, "Invalid Password Format", Toast.LENGTH_LONG);
                                errorToast.show();
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.btnOk) {
            registerCompany();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Boolean checkPhoneNumber()
    {
        chkPhoneNum = MCompany.checkPhoneNum(editTextPhoneNumber.getText().toString());
        return chkPhoneNum;
    }

    private Boolean checkEmail()
    {
        chkEmail = MCompany.checkEmail(editTextEmail.getText().toString());
        return chkEmail;
    }
}
