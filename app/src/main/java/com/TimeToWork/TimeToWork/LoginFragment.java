package com.TimeToWork.TimeToWork;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.CustomClass.CustomVolleyErrorListener;
import com.TimeToWork.TimeToWork.Database.Entity.Jobseeker;
import com.TimeToWork.TimeToWork.Database.JobseekerDA;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.microsoft.azure.documentdb.DocumentClient;

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

public class LoginFragment extends Fragment {

    private CustomProgressDialog mProgressDialog;
    private EditText etEmail, etPassword;
    private String email, password;

    public LoginFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        TextView tvForgot = (TextView) view.findViewById(R.id.tv_forgot);
        tvForgot.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                View dialogView = View.inflate(getContext(), R.layout.dialog_forgot_password, null);
                final EditText etForgotEmail = (EditText) dialogView.findViewById(R.id.et_email);

                final AlertDialog dialogRateJob = new AlertDialog.Builder(getContext(), R.style.DialogStyle)
                        .setTitle("Recover Password")
                        .setView(dialogView)
                        .setPositiveButton("Submit", null)
                        .setNegativeButton("Cancel", null)
                        .setCancelable(false)
                        .create();

                dialogRateJob.setOnShowListener(new DialogInterface.OnShowListener() {

                    @Override
                    public void onShow(DialogInterface dialogInterface) {

                        Button button = dialogRateJob.getButton(AlertDialog.BUTTON_POSITIVE);
                        button.setOnClickListener(new View.OnClickListener() {

                            @Override
                            public void onClick(View view) {

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
                });

                dialogRateJob.show();
            }
        });

        mProgressDialog = new CustomProgressDialog(getActivity());
        etEmail = (EditText) view.findViewById(R.id.et_email);
        etPassword = (EditText) view.findViewById(R.id.et_password);

        Button btnLogin = (Button) view.findViewById(R.id.btn_login);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    attemptLogin();
                }
            }
        });

//        Button btnRegister = (Button) view.findViewById(R.id.btn_register);
//        btnRegister.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                RegisterFragment registerFragment = new RegisterFragment();
//                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                fragmentTransaction.setCustomAnimations(
//                        R.anim.trans_fragment_enter,
//                        R.anim.trans_fragment_exit,
//                        R.anim.trans_fragment_pop_enter,
//                        R.anim.trans_fragment_pop_exit
//                );
//                fragmentTransaction.replace(R.id.login_container, registerFragment);
//                fragmentTransaction.addToBackStack(null); // Press back key to go back
//                fragmentTransaction.commit();
//            }
//        });

        return view;
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

                    } else {
                        //If failed, then show alert dialog
                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    builder.setMessage(e.getMessage())
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    getActivity().finish();
                                }
                            })
                            .create()
                            .show();

                } catch (ParseException ignored) {
                }
            }
        };

        CustomVolleyErrorListener errorListener
                = new CustomVolleyErrorListener(getActivity(), mProgressDialog, mRequestQueue);
        LoginFragment.LoginRequest loginRequest = new LoginFragment.LoginRequest(
                email,
                getEncryptedPassword(),
                getString(R.string.url_login),
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
        JobseekerDA jobseekerDA = new JobseekerDA(getActivity());
        //Saving jobseeker data into sqlite database
        Jobseeker j = jobseekerDA.insertJobseeker(jobseeker);
        //Closing sqlite database
        jobseekerDA.close();

        if (j != null) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            getActivity().startActivity(intent);
            getActivity().finish();
        }
    }

    private void attemptRecoverPassword(final String forgotEmail) {

        //Show progress dialog
        mProgressDialog.setMessage(getString(R.string.progress_loading));
        mProgressDialog.toggleProgressDialog();

        final Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                mProgressDialog.toggleProgressDialog();
                Toast.makeText(getActivity(), response, Toast.LENGTH_LONG).show();
            }
        };

        CustomVolleyErrorListener errorListener
                = new CustomVolleyErrorListener(getActivity(), mProgressDialog, mRequestQueue);
        LoginFragment.RecoverPasswordRequest recoverPasswordRequest = new LoginFragment.RecoverPasswordRequest(
                getString(R.string.url_recover_password) + "&forgotEmail=" + forgotEmail,
                responseListener,
                errorListener
        );
        mRequestQueue.add(recoverPasswordRequest);
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

    private class RecoverPasswordRequest extends StringRequest {

        RecoverPasswordRequest(String url,
                               Response.Listener<String> listener,
                               Response.ErrorListener errorListener) {
            super(Method.GET, url, listener, errorListener);
        }
    }

}
