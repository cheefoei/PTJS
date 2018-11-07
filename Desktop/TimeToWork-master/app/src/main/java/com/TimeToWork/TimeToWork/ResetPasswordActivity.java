package com.TimeToWork.TimeToWork;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.CustomClass.CustomVolleyErrorListener;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import static com.TimeToWork.TimeToWork.MainApplication.mRequestQueue;
import static com.TimeToWork.TimeToWork.MainApplication.root;
import static com.TimeToWork.TimeToWork.MainApplication.userType;

public class ResetPasswordActivity extends AppCompatActivity {

    private CustomProgressDialog mProgressDialog;
    private EditText etCode, etPassword, etConfirmPassword;
    private String email;

    private boolean isValidated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        email = getIntent().getStringExtra("EMAIL");
        mProgressDialog = new CustomProgressDialog(ResetPasswordActivity.this);

        etCode = (EditText) findViewById(R.id.et_code);
        etPassword = (EditText) findViewById(R.id.et_password);
        etConfirmPassword = (EditText) findViewById(R.id.et_confirm_password);

        Button btnReset = (Button) findViewById(R.id.btn_reset_password);
        btnReset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (isValidated) {
                    if (isValid()) {
                        resetPassword();
                    }
                } else {
                    if (!etCode.getText().toString().equals("")) {
                        checkVerifyCode();
                    } else {
                        etCode.setError(getString(R.string.error_required_field));
                    }
                }
            }
        });
    }

    private boolean isValid() {

        boolean isValid = true;

        String password = etPassword.getText().toString();
        String confirmPassword = etConfirmPassword.getText().toString();

        if (password.equals("")) {
            etPassword.setError(getString(R.string.error_required_field));
            isValid = false;
        }
        if (confirmPassword.equals("")) {
            etConfirmPassword.setError(getString(R.string.error_required_field));
            isValid = false;
        }
        if (!password.equals("") && !confirmPassword.equals("")) {
            if (!password.equals(confirmPassword)) {
                etPassword.setError(getString(R.string.error_password_not_match));
                etConfirmPassword.setError(getString(R.string.error_password_not_match));
                isValid = false;
            }
        }

        return isValid;
    }

    private String getEncryptedPassword() {

        String encryptedPassword = null;

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(etPassword.getText().toString().getBytes());
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

    private void checkVerifyCode() {

        //Show progress dialog
        mProgressDialog.setMessage("Checking verify code …");
        mProgressDialog.toggleProgressDialog();

        final Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean exists = jsonResponse.getBoolean("exists");

                    if (exists) {
                        etCode.setVisibility(View.GONE);
                        etPassword.setVisibility(View.VISIBLE);
                        etConfirmPassword.setVisibility(View.VISIBLE);
                        isValidated = true;
                    } else {
                        //If exists, then show alert dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                        builder.setPositiveButton("OK", null)
                                .setTitle("Failed")
                                .setMessage("Wrong verify code.")
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                    builder.setMessage(e.getMessage())
                            .setPositiveButton("OK", null)
                            .create()
                            .show();
                }
            }
        };

        CustomVolleyErrorListener errorListener
                = new CustomVolleyErrorListener(this, mProgressDialog, mRequestQueue);
        ResetPasswordActivity.VerifyCodeRequest verifyCodeRequest = new ResetPasswordActivity.VerifyCodeRequest(
                email,
                etCode.getText().toString(),
                root + getString(R.string.url_check_verify_code),
                responseListener,
                errorListener
        );
        mRequestQueue.add(verifyCodeRequest);
    }

    private void resetPassword() {

        //Show progress dialog
        mProgressDialog.setMessage("Resetting password …");
        mProgressDialog.toggleProgressDialog();

        final Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);

                    if (success) {
                        builder.setTitle("Success")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });
                    } else {
                        builder.setPositiveButton("OK", null)
                                .setTitle("Failed");
                    }
                    //To close progress dialog
                    mProgressDialog.toggleProgressDialog();
                    // Show message from server
                    builder.setMessage(jsonResponse.getString("msg"))
                            .create()
                            .show();
                } catch (JSONException e) {

                    e.printStackTrace();
                    //To close progress dialog
                    mProgressDialog.toggleProgressDialog();
                    //If exception, then show alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(ResetPasswordActivity.this);
                    builder.setMessage(e.getMessage())
                            .setPositiveButton("OK", null)
                            .create()
                            .show();
                }
            }
        };

        String newPassword = getEncryptedPassword();
        String url = null;
        if (userType.equals("Company")) {
            url = getString(R.string.url_company_update_password);
        } else if (userType.equals("Jobseeker")) {
            url = getString(R.string.url_jobseeker_update_password);
        }

        CustomVolleyErrorListener errorListener
                = new CustomVolleyErrorListener(this, mProgressDialog, mRequestQueue);
        ResetPasswordActivity.ResetPasswordRequest resetPasswordRequest = new ResetPasswordActivity.ResetPasswordRequest(
                email,
                newPassword,
                root + url,
                responseListener,
                errorListener
        );
        mRequestQueue.add(resetPasswordRequest);
    }

    private class VerifyCodeRequest extends StringRequest {

        private Map<String, String> params;

        VerifyCodeRequest(
                String email,
                String code,
                String url,
                Response.Listener<String> listener,
                Response.ErrorListener errorListener) {
            super(Method.POST, url, listener, errorListener);
            params = new HashMap<>();
            params.put("user_email", email);
            params.put("code", code);
        }

        public Map<String, String> getParams() {
            return params;
        }
    }

    private class ResetPasswordRequest extends StringRequest {

        private Map<String, String> params;

        ResetPasswordRequest(
                String email,
                String password,
                String url,
                Response.Listener<String> listener,
                Response.ErrorListener errorListener) {
            super(Method.POST, url, listener, errorListener);
            params = new HashMap<>();
            params.put("email", email);
            params.put("new_password", password);
        }

        public Map<String, String> getParams() {
            return params;
        }
    }
}
