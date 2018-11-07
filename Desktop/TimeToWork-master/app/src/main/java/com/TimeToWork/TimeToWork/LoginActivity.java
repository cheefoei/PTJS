package com.TimeToWork.TimeToWork;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.CustomClass.CustomVolleyErrorListener;
import com.TimeToWork.TimeToWork.Database.CompanyDA;
import com.TimeToWork.TimeToWork.Database.Entity.Company;
import com.TimeToWork.TimeToWork.Database.Entity.Jobseeker;
import com.TimeToWork.TimeToWork.Database.JobseekerDA;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.TimeToWork.TimeToWork.MainApplication.mRequestQueue;
import static com.TimeToWork.TimeToWork.MainApplication.root;
import static com.TimeToWork.TimeToWork.MainApplication.userType;

public class LoginActivity extends AppCompatActivity {

    private CustomProgressDialog mProgressDialog;
    private EditText etEmail, etPassword;
    private String email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mProgressDialog = new CustomProgressDialog(LoginActivity.this);
        etEmail = (EditText) findViewById(R.id.et_email);
        etPassword = (EditText) findViewById(R.id.et_password);

        Button btnLogin = (Button) findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    attemptLogin();
                }
            }
        });

        TextView tvForgot = (TextView) findViewById(R.id.tv_forgot);
        tvForgot.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(intent);
            }
        });

        Button btnRegister = (Button) findViewById(R.id.btn_register);
        btnRegister.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(LoginActivity.this, ChooseRegisterPage.class);
                startActivity(myIntent);
            }
        });

    }

    private boolean isValid() {

        boolean isValid = true;

        email = etEmail.getText().toString();
        password = etPassword.getText().toString();

        if (password.equals("")) {
            etPassword.setError(getString(R.string.error_required_field));
            isValid = false;
        }

        if (email.equals("")) {
            etEmail.setError(getString(R.string.error_required_field));
            isValid = false;
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError(getString(R.string.error_email_invalid));
            isValid = false;
        }

        return isValid;
    }

    private void attemptLogin() {

        //Show progress dialog
        mProgressDialog.setMessage(getString(R.string.progress_logging_in));
        mProgressDialog.toggleProgressDialog();

        final Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {

                        if (userType.equals("Jobseeker")) {

                            //Change jobseeker JSON to Array
                            JSONObject jobseekerObject = jsonResponse.getJSONObject("JOBSEEKER");
                            //Create jobseeker object
                            Jobseeker jobseeker = new Jobseeker();
                            jobseeker.setId(jobseekerObject.getString("jobseeker_id"));
                            jobseeker.setName(jobseekerObject.getString("jobseeker_name"));
                            jobseeker.setGender(jobseekerObject.getString("jobseeker_gender").charAt(0));
                            jobseeker.setDob(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(jobseekerObject.getString("jobseeker_dob")));
                            jobseeker.setIc(jobseekerObject.getString("jobseeker_ic"));
                            jobseeker.setAddress(jobseekerObject.getString("jobseeker_address"));
                            jobseeker.setPhone_number(jobseekerObject.getString("jobseeker_phone_number"));
                            jobseeker.setEmail(jobseekerObject.getString("jobseeker_email"));
                            jobseeker.setPreferred_job(jobseekerObject.getString("jobseeker_preferred_job"));
                            jobseeker.setPreferred_location(jobseekerObject.getString("jobseeker_preferred_location"));
                            jobseeker.setExperience(jobseekerObject.getString("jobseeker_experience"));
                            jobseeker.setRating(jobseekerObject.getDouble("jobseeker_rating"));
                            jobseeker.setImg(jobseekerObject.getString("jobseeker_img"));

                            //Save jobseeker data
                            saveJobseekerData(jobseeker);

                        } else if (userType.equals("Company")) {

                            //Change company JSON to Array
                            JSONObject companyObject = jsonResponse.getJSONObject("COMPANY");
                            //Create company object
                            Company company = new Company();
                            company.setId(companyObject.getString("company_id"));
                            company.setName(companyObject.getString("company_name"));
                            company.setAddress(companyObject.getString("company_address"));
                            company.setPhone_number(companyObject.getString("company_phone_number"));
                            company.setEmail(companyObject.getString("company_email"));
                            company.setRating(companyObject.getDouble("company_rating"));
                            company.setImg(companyObject.getString("company_img"));

                            //Save company data
                            saveCompanyData(company);
                        }
                    } else {
                        //If failed, then show alert dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                        builder.setMessage(jsonResponse.getString("error_msg"))
                                .setPositiveButton("OK", null)
                                .create()
                                .show();
                    }
                    //To close progress dialog
                    mProgressDialog.toggleProgressDialog();

                } catch (JSONException e) {

                    e.printStackTrace();
                    //To close progress dialog
                    mProgressDialog.toggleProgressDialog();
                    //If exception, then show alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                    builder.setMessage(e.getMessage())
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    LoginActivity.this.finish();
                                }
                            })
                            .create()
                            .show();

                } catch (ParseException ignored) {
                }
            }
        };

        String url = null;
        if (userType.equals("Company")) {
            url = getString(R.string.url_company_login);
        } else if (userType.equals("Jobseeker")) {
            url = getString(R.string.url_jobseeker_login);
        }

        CustomVolleyErrorListener errorListener
                = new CustomVolleyErrorListener(LoginActivity.this, mProgressDialog, mRequestQueue);
        LoginActivity.LoginRequest loginRequest = new LoginActivity.LoginRequest(
                email,
                getEncryptedPassword(),
                root + url,
                responseListener,
                errorListener
        );
        mRequestQueue.add(loginRequest);
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

    private void saveJobseekerData(Jobseeker jobseeker) {

        //Opening Jobseeker sqlite database
        JobseekerDA jobseekerDA = new JobseekerDA(LoginActivity.this);
        //Saving jobseeker data into sqlite database
        Jobseeker j = jobseekerDA.insertJobseeker(jobseeker);
        //Closing sqlite database
        jobseekerDA.close();

        if (j != null) {
            Intent intent = new Intent();
            intent.putExtra("Success", true);
            setResult(1, intent);
            finish();
        }
    }

    private void saveCompanyData(Company company) {

        //Opening Jobseeker sqlite database
        CompanyDA companyDA = new CompanyDA(LoginActivity.this);
        //Saving jobseeker data into sqlite database
        Company j = companyDA.insertCompany(company);
        //Closing sqlite database
        companyDA.close();

        if (j != null) {
            Intent intent = new Intent();
            intent.putExtra("Success", true);
            setResult(1, intent);
            finish();
        }
    }

    private class LoginRequest extends StringRequest {

        private Map<String, String> params;

        LoginRequest(String email,
                     String password,
                     String url,
                     Response.Listener<String> listener,
                     Response.ErrorListener errorListener) {
            super(Method.POST, url, listener, errorListener);

            params = new HashMap<>();
            params.put("email", email);
            params.put("password", password);
        }

        public Map<String, String> getParams() {
            return params;
        }
    }

}
