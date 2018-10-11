package com.TimeToWork.TimeToWork;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.CustomClass.CustomVolleyErrorListener;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

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
                mProgressDialog.toggleProgressDialog();
                Toast.makeText(ForgotPasswordActivity.this, response, Toast.LENGTH_LONG).show();
            }
        };

        String url = null;
        if (userType.equals("Company")) {
            url = getString(R.string.url_jobseeker_login);
        } else if (userType.equals("Jobseeker")) {
            url = getString(R.string.url_jobseeker_login);
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
        mRequestQueue.add(recoverPasswordRequest);
    }

    private class RecoverPasswordRequest extends StringRequest {

        private Map<String, String> params;

        RecoverPasswordRequest(String forgotEmail,
                               String url,
                               Response.Listener<String> listener,
                               Response.ErrorListener errorListener) {
            super(Method.GET, url, listener, errorListener);
            params = new HashMap<>();
            params.put("forgotEmail", forgotEmail);
        }

        public Map<String, String> getParams() {
            return params;
        }
    }
}
