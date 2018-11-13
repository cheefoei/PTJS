package com.TimeToWork.TimeToWork.Jobseeker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.Database.Entity.Jobseeker;
import com.TimeToWork.TimeToWork.R;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.TimeToWork.TimeToWork.MainApplication.mRequestQueue;
import static com.TimeToWork.TimeToWork.MainApplication.root;

public class JobseekerInfoFragment extends Fragment {

    private CustomProgressDialog mProgressDialog;
    private TextView tvGender, tvIdentityNumber, tvBirthday,
            tvAddress, tvPhoneNumber, tvEmail;

    private Jobseeker jobseeker;

    public JobseekerInfoFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_jobseeker_info, container, false);

        mProgressDialog = new CustomProgressDialog(getActivity());
        jobseeker = (Jobseeker) getArguments().getSerializable("JOBSEEKER");

        tvGender = (TextView) view.findViewById(R.id.tv_jobseeker_gender);
        tvIdentityNumber = (TextView) view.findViewById(R.id.tv_jobseeker_ic);
        tvBirthday = (TextView) view.findViewById(R.id.tv_jobseeker_dob);
        tvAddress = (TextView) view.findViewById(R.id.tv_jobseeker_address);
        tvPhoneNumber = (TextView) view.findViewById(R.id.tv_jobseeker_phone);
        tvEmail = (TextView) view.findViewById(R.id.tv_jobseeker_email);

        getJobseekerData();

        return view;
    }

    private void getJobseekerData() {

        //Show progress dialog
        mProgressDialog.setMessage("Loading …");
        mProgressDialog.show();

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {

                        //Change jobseeker JSON to Array
                        JSONObject jobseekerObject = jsonResponse.getJSONObject("JOBSEEKER");
                        //Assign jobseeker data
                        jobseeker.setName(jobseekerObject.getString("jobseeker_name"));
                        jobseeker.setGender(jobseekerObject.getString("jobseeker_gender").charAt(0));
                        jobseeker.setDob(new SimpleDateFormat("yyyy-MM-dd",
                                Locale.ENGLISH).parse(jobseekerObject.getString("jobseeker_dob")));
                        jobseeker.setIc(jobseekerObject.getString("jobseeker_ic"));
                        jobseeker.setAddress(jobseekerObject.getString("jobseeker_address"));
                        jobseeker.setPhone_number(jobseekerObject.getString("jobseeker_phone_number"));
                        jobseeker.setEmail(jobseekerObject.getString("jobseeker_email"));
                        jobseeker.setExperience(jobseekerObject.getString("jobseeker_experience"));

                        if (jobseeker.getGender() == 'M') {
                            tvGender.setText(R.string.string_male);
                        } else if (jobseeker.getGender() == 'F') {
                            tvGender.setText(R.string.string_female);
                        }

                        DateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);
                        String dob = sdf.format(jobseeker.getDob());
                        int age = getAge(jobseeker.getDob());

                        tvBirthday.setText(dob + " (" + age + " years old)");
                        tvIdentityNumber.setText(jobseeker.getIc());
                        tvAddress.setText(jobseeker.getAddress());
                        tvPhoneNumber.setText(jobseeker.getPhone_number());
                        tvEmail.setText(jobseeker.getEmail());
                    }
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }
                mProgressDialog.dismiss();
            }
        };

        GetJobseekerRequest getJobseekerRequest = new GetJobseekerRequest(
                root + getString(R.string.url_get_jobseeker),
                responseListener,
                null
        );
        mRequestQueue.add(getJobseekerRequest);
    }

    private int getAge(Date dateOfBirth) {

        Calendar today = Calendar.getInstance();
        Calendar birthDate = Calendar.getInstance();

        birthDate.setTime(dateOfBirth);
        if (birthDate.after(today)) {
            throw new IllegalArgumentException("You don't exist yet");
        }
        int todayYear = today.get(Calendar.YEAR);
        int birthDateYear = birthDate.get(Calendar.YEAR);
        int todayDayOfYear = today.get(Calendar.DAY_OF_YEAR);
        int birthDateDayOfYear = birthDate.get(Calendar.DAY_OF_YEAR);
        int todayMonth = today.get(Calendar.MONTH);
        int birthDateMonth = birthDate.get(Calendar.MONTH);
        int todayDayOfMonth = today.get(Calendar.DAY_OF_MONTH);
        int birthDateDayOfMonth = birthDate.get(Calendar.DAY_OF_MONTH);
        int age = todayYear - birthDateYear;

        // If birth date is greater than today's date (after 2 days adjustment of leap year)
        // then decrement age one year
        if ((birthDateDayOfYear - todayDayOfYear > 3) || (birthDateMonth > todayMonth)) {
            age--;
            // If birth date and today's date are of same month and birth day of month is greater
            // than todays day of month then decrement age
        } else if ((birthDateMonth == todayMonth) && (birthDateDayOfMonth > todayDayOfMonth)) {
            age--;
        }
        return age;
    }

    private class GetJobseekerRequest extends StringRequest {

        private Map<String, String> params;

        GetJobseekerRequest(String url,
                            Response.Listener<String> listener,
                            Response.ErrorListener errorListener) {
            super(Method.POST, url, listener, errorListener);

            params = new HashMap<>();
            params.put("id", jobseeker.getId());
        }

        public Map<String, String> getParams() {
            return params;
        }
    }
}
