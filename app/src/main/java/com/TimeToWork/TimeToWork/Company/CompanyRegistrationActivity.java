package com.TimeToWork.TimeToWork.Company;

import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;

import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.Database.Control.MaintainCompany;
import com.TimeToWork.TimeToWork.Database.Entity.Company;
import com.TimeToWork.TimeToWork.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CompanyRegistrationActivity extends AppCompatActivity {

    private MaintainCompany maintainCompany = new MaintainCompany();
    private EditText editTextName, editTextAddress, editTextEmail,
            editTextPhoneNumber, editTextPassword, editTextConfirmPassword;

    private String name, phoneNum, email, address, pass;

    private CustomProgressDialog mProgressDialog;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_registration);

        mProgressDialog = new CustomProgressDialog(CompanyRegistrationActivity.this);
        handler = new Handler();

        editTextName = (EditText) findViewById(R.id.name);
        editTextAddress = (EditText) findViewById(R.id.address);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPhoneNumber = (EditText) findViewById(R.id.phoneNumber);
        editTextPassword = (EditText) findViewById(R.id.password);
        editTextConfirmPassword = (EditText) findViewById(R.id.confirmPassword);
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_done, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.done) {

            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {

                    handler.post(new Runnable() {

                        @Override
                        public void run() {

                            if (isValid()) {
                                mProgressDialog.dismiss();
                                checkPhoneNumber();
                            } else {
                                mProgressDialog.dismiss();
                            }
                        }
                    });
                }
            });
            mProgressDialog.setMessage("Verifying your company data …");
            mProgressDialog.show();
            thread.start();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void registerCompany() {

        new RegisterAsyncTask() {

            CustomProgressDialog mProgressDialog;

            @Override
            protected void onPreExecute() {

                mProgressDialog = new CustomProgressDialog(CompanyRegistrationActivity.this);
                mProgressDialog.setMessage("Registering account  …");
                mProgressDialog.show();
                super.onPreExecute();
            }

            @Override
            protected Boolean doInBackground(String... params) {

                String id = maintainCompany.getCompanyLastId();
                int number = Integer.parseInt(id.substring(1, 6)); // Get the behind 5 digits
                number += 1; // id + 1, Example: 10001 + 1 = 10002
                String word = id.substring(0, 1); // Get the first letter 'C'
                String companyId = String.valueOf(word + number); // Get the first letter 'C' + the behind 5 digits
                String encryptedPassword = getEncryptedPassword();

                Company company = new Company();
                company.setId(companyId);
                company.setName(name);
                company.setAddress(address);
                company.setPhone_number(phoneNum);
                company.setEmail(email);
                company.setRating(0.0);
                company.setPassword(encryptedPassword);

                return maintainCompany.insertCompanyDetail(company);
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {

                AlertDialog.Builder builder = new AlertDialog.Builder(CompanyRegistrationActivity.this);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                if (aBoolean) {
                    builder.setMessage("Success Registered.");
                } else {
                    builder.setMessage("Error occurred.");
                }
                builder.create()
                        .show();

                super.onPostExecute(aBoolean);
            }
        }.execute();
    }

    private boolean isValid() {

        CustomProgressDialog mProgressDialog = new CustomProgressDialog(this);
        mProgressDialog.setMessage("Verifying your data  …");
        mProgressDialog.show();

        boolean valid = true;

        name = editTextName.getText().toString().trim();
        address = editTextAddress.getText().toString().trim();
        email = editTextEmail.getText().toString().trim();
        phoneNum = editTextPhoneNumber.getText().toString().trim();
        pass = editTextPassword.getText().toString().trim();
        String confirmPass = editTextConfirmPassword.getText().toString().trim();

        Pattern pattern;
        Matcher matcher;

        if (name.equals("")) {
            editTextName.setError("Cannot be empty");
            valid = false;
        }

        if (address.equals("")) {
            editTextAddress.setError("Cannot be empty");
            valid = false;
        }

        String phoneNumFormat = "\\d{2}-\\d{8}";
        pattern = Pattern.compile(phoneNumFormat);
        matcher = pattern.matcher(phoneNum);
        if (phoneNum.equals("")) {
            editTextPhoneNumber.setError("Cannot be empty");
            valid = false;
        } else {
            if (!matcher.matches()) {
                editTextPhoneNumber.setError("Invalid Phone Number Format");
                valid = false;
            }
        }

        String emailFormat = "^(.+)@(.+)$";
        pattern = Pattern.compile(emailFormat);
        matcher = pattern.matcher(email);
        if (email.equals("")) {
            editTextEmail.setError("Cannot be empty");
            valid = false;
        } else {
            if (!matcher.matches()) {
                editTextEmail.setError("Invalid Email Format");
                valid = false;
            }
        }

        if (pass.equals("")) {
            editTextPassword.setError("Cannot be empty");
            valid = false;
        } else {
            if (confirmPass.equals("")) {
                editTextConfirmPassword.setError("Cannot be empty");
                valid = false;
            } else {
                if (pass.length() >= 8) {
                    if (Character.isUpperCase(pass.charAt(0))) {
                        if (!pass.equals(confirmPass)) {
                            editTextPassword.setError("Not match with password and confirm password");
                            valid = false;
                        }
                    } else {
                        editTextPassword.setError("Invalid Password Format. Eg.A1234567");
                        valid = false;
                    }
                } else {
                    editTextPassword.setError("Invalid Password Format. Eg.A1234567");
                    valid = false;
                }
            }
        }
        mProgressDialog.dismiss();

        return valid;
    }

    private void checkPhoneNumber() {

        mProgressDialog.setMessage("Checking your phone number …");
        mProgressDialog.show();

        new Thread(new Runnable() {

            @Override
            public void run() {

                final boolean result = maintainCompany.checkPhoneNum(editTextPhoneNumber.getText().toString());

                handler.post(new Runnable() {

                    @Override
                    public void run() {

                        mProgressDialog.dismiss();
                        if (!result) {
                            editTextPhoneNumber.setError("Phone Number Has Been Used.");
                        } else {
                            checkEmail();
                        }
                    }
                });
            }
        }).start();
    }

    private void checkEmail() {

        mProgressDialog.setMessage("Checking your email address …");
        mProgressDialog.show();

        new Thread(new Runnable() {

            @Override
            public void run() {

                final boolean result = maintainCompany.checkEmail(editTextEmail.getText().toString());

                handler.post(new Runnable() {

                    @Override
                    public void run() {

                        mProgressDialog.dismiss();
                        if (!result) {
                            editTextEmail.setError("Email Has Been Used.");
                        } else {
                            registerCompany();
                        }
                    }
                });
            }
        }).start();
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

    private abstract class RegisterAsyncTask extends AsyncTask<String, Boolean, Boolean> {
    }
}
