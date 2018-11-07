package com.TimeToWork.TimeToWork;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.CustomClass.CustomVolleyErrorListener;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.TimeToWork.TimeToWork.MainApplication.mRequestQueue;
import static com.TimeToWork.TimeToWork.MainApplication.root;
import static com.TimeToWork.TimeToWork.MainApplication.userType;

public class ForgotPasswordActivity extends AppCompatActivity {

    private CustomProgressDialog mProgressDialog;
    private EditText etForgotEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        mProgressDialog = new CustomProgressDialog(ForgotPasswordActivity.this);
        etForgotEmail = (EditText) findViewById(R.id.et_email);

        Button btnSubmit = (Button) findViewById(R.id.btn_continue);
        btnSubmit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                String forgotEmail = etForgotEmail.getText().toString();
                if (forgotEmail.equals("")) {
                    etForgotEmail.setError(getString(R.string.error_required_field));
                } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(forgotEmail).matches()) {
                    etForgotEmail.setError(getString(R.string.error_email_invalid));
                } else {
                    attemptRecoverPassword(forgotEmail);
                }
            }
        });

    }

    private void attemptRecoverPassword(final String forgotEmail) {

        //Show progress dialog
        mProgressDialog.setMessage(getString(R.string.progress_loading));
        mProgressDialog.toggleProgressDialog();

        final Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e("JSON", response);

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordActivity.this);
                    if (success) {
                        builder.setTitle("Success")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Intent intent = new Intent(ForgotPasswordActivity.this, ResetPasswordActivity.class);
                                        intent.putExtra("EMAIL", forgotEmail);
                                        startActivity(intent);
                                        finish();
                                    }
                                });
                    } else {
                        builder.setPositiveButton("OK", null)
                                .setTitle("Failed");
                    }
                    //If failed, then show alert dialog
                    builder.setMessage(jsonResponse.getString("message"))
                            .create()
                            .show();

                    //To close progress dialog
                    mProgressDialog.toggleProgressDialog();

                } catch (JSONException e) {

                    e.printStackTrace();
                    //To close progress dialog
                    mProgressDialog.toggleProgressDialog();
                    //If exception, then show alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPasswordActivity.this);
                    builder.setMessage(e.getMessage())
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    ForgotPasswordActivity.this.finish();
                                }
                            })
                            .create()
                            .show();
                }
            }
        };

        String url = null;
        if (userType.equals("Company")) {
            url = getString(R.string.url_company_recovery);
        } else if (userType.equals("Jobseeker")) {
            url = getString(R.string.url_jobseeker_recovery);
        }

        CustomVolleyErrorListener errorListener
                = new CustomVolleyErrorListener(ForgotPasswordActivity.this, mProgressDialog, mRequestQueue);
        ForgotPasswordActivity.RecoverPasswordRequest recoverPasswordRequest
                = new ForgotPasswordActivity.RecoverPasswordRequest(
                forgotEmail,
                root + url,
                responseListener,
                errorListener
        );
        recoverPasswordRequest.setRetryPolicy(new DefaultRetryPolicy(
                30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue.add(recoverPasswordRequest);
    }

    private class RecoverPasswordRequest extends StringRequest {

        private Map<String, String> params;

        RecoverPasswordRequest(String forgotEmail,
                               String url,
                               Response.Listener<String> listener,
                               Response.ErrorListener errorListener) {
            super(Method.POST, url, listener, errorListener);
            params = new HashMap<>();
            params.put("email", forgotEmail);
        }

        public Map<String, String> getParams() {
            return params;
        }
    }
}
