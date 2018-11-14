package com.TimeToWork.TimeToWork.NavigationFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.TimeToWork.TimeToWork.ChangePasswordActivity;
import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.Database.Control.MaintainJobseeker;
import com.TimeToWork.TimeToWork.Database.Entity.Jobseeker;
import com.TimeToWork.TimeToWork.Database.JobseekerDA;
import com.TimeToWork.TimeToWork.Jobseeker.JobseekerDetailActivity;
import com.TimeToWork.TimeToWork.Jobseeker.JobseekerMyReviewActivity;
import com.TimeToWork.TimeToWork.Jobseeker.JobseekerReportActivity;
import com.TimeToWork.TimeToWork.Jobseeker.PreferredJobActivity;
import com.TimeToWork.TimeToWork.Jobseeker.PreferredLocationActivity;
import com.TimeToWork.TimeToWork.Jobseeker.SetFreeTimeActivity;
import com.TimeToWork.TimeToWork.Jobseeker.ViewFreeTimeActivity;
import com.TimeToWork.TimeToWork.R;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.TimeToWork.TimeToWork.MainApplication.clearAppData;
import static com.TimeToWork.TimeToWork.MainApplication.mRequestQueue;
import static com.TimeToWork.TimeToWork.MainApplication.root;
import static com.TimeToWork.TimeToWork.MainApplication.userId;

public class JobseekerProfileFragment extends Fragment {

    private CustomProgressDialog mProgressDialog;
    private Handler handler;

    private TextView textViewName, txtViewSalary, txtViewCompleted, ratingValue;
    private ImageView imageView;
    private RatingBar ratingBar;

    private Jobseeker jobseeker;

    private MaintainJobseeker maintainJobseeker = new MaintainJobseeker();

    private DatabaseReference databaseRef;

    public JobseekerProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(getString(R.string.fragment_profile));

        // Enable menu in toolbar
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_jobseeker, container, false);

        mProgressDialog = new CustomProgressDialog(getActivity());
        handler = new Handler();

        textViewName = (TextView) view.findViewById(R.id.name);
        txtViewSalary = (TextView) view.findViewById(R.id.salary);
        txtViewCompleted = (TextView) view.findViewById(R.id.completed);
        ratingValue = (TextView) view.findViewById(R.id.tv_jobseeker_rating);
        ratingBar = (RatingBar) view.findViewById(R.id.rate_bar_jobseeker);
        imageView = (ImageView) view.findViewById(R.id.imgUser);

        databaseRef = FirebaseDatabase.getInstance().getReference("jobseeker");



        TextView showPersonalDetails = (TextView) view.findViewById(R.id.pd);
        showPersonalDetails.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), JobseekerDetailActivity.class);
                startActivity(myIntent);
            }
        });

        TextView myReview = (TextView) view.findViewById(R.id.mr);
        myReview.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), JobseekerMyReviewActivity.class);
                startActivity(myIntent);
            }
        });

        TextView showPreferredJob = (TextView) view.findViewById(R.id.pj);
        showPreferredJob.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), PreferredJobActivity.class);
                startActivity(myIntent);
            }
        });

        TextView showPreferredLocation = (TextView) view.findViewById(R.id.pl);
        showPreferredLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), PreferredLocationActivity.class);
                startActivity(myIntent);
            }
        });

        TextView showSetFreeTime = (TextView) view.findViewById(R.id.sft);
        showSetFreeTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), SetFreeTimeActivity.class);
                startActivity(myIntent);
            }
        });

        TextView showCalendar = (TextView) view.findViewById(R.id.eft);
        showCalendar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), ViewFreeTimeActivity.class);
                startActivity(myIntent);
            }
        });

        TextView changePassword = (TextView) view.findViewById(R.id.cp);
        changePassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(myIntent);
            }
        });

        TextView report = (TextView) view.findViewById(R.id.report);
        report.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), JobseekerReportActivity.class);
                startActivity(myIntent);
            }
        });

        if (userId != null) {
            // Get profile detail
            databaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = (String) dataSnapshot.child(userId).child("jobseeker_img").getValue();
                    //decode base64 string to image
                    byte[] imageBytes;
                    imageBytes = Base64.decode(value, Base64.DEFAULT);
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    imageView.setImageBitmap(decodedImage);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            getJobseekerProfile();
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.logout) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setTitle("Confirmation")
                    .setMessage("Confirm to logout?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            clearAppData(getActivity());

                            AlertDialog.Builder builder
                                    = new AlertDialog.Builder(getActivity())
                                    .setTitle("Successful")
                                    .setMessage("You already logout.")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            getActivity().recreate();
                                        }
                                    });
                            builder.show();
                        }
                    })
                    .setNegativeButton("Cancel", null);
            builder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {

        if (userId != null && jobseeker != null) {
            syncJobseekerData();
        }
        super.onResume();
    }

    private void syncJobseekerData() {

        //Show progress dialog
        //mProgressDialog.setMessage("Loading …");
        //mProgressDialog.show();

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {

                        //Change jobseeker JSON to Array
                        JSONObject jobseekerObject = jsonResponse.getJSONObject("JOBSEEKER");
                        //Create jobseeker object
                        Jobseeker j = new Jobseeker();
                        j.setId(jobseekerObject.getString("jobseeker_id"));
                        j.setName(jobseekerObject.getString("jobseeker_name"));
                        j.setGender(jobseekerObject.getString("jobseeker_gender").charAt(0));
                        j.setDob(new SimpleDateFormat("yyyy-MM-dd",
                                Locale.ENGLISH).parse(jobseekerObject.getString("jobseeker_dob")));
                        j.setIc(jobseekerObject.getString("jobseeker_ic"));
                        j.setAddress(jobseekerObject.getString("jobseeker_address"));
                        j.setPhone_number(jobseekerObject.getString("jobseeker_phone_number"));
                        j.setEmail(jobseekerObject.getString("jobseeker_email"));
                        j.setPreferred_job(jobseekerObject.getString("jobseeker_preferred_job"));
                        j.setPreferred_location(jobseekerObject.getString("jobseeker_preferred_location"));
                        j.setExperience(jobseekerObject.getString("jobseeker_experience"));
                        j.setRating(jobseekerObject.getDouble("jobseeker_rating"));
                        j.setImg(jobseekerObject.getString("jobseeker_img"));

                        //Update jobseeker data
                        JobseekerDA jobseekerDA = new JobseekerDA(getActivity());
                        jobseekerDA.updateJobseekerData(j);
                        jobseekerDA.close();

                        textViewName.setText(j.getName());
                        if (j.getImg() != null && !j.getImg().equals("") && !j.getImg().equals("null")) {
                            byte[] decodedString = Base64.decode(j.getImg(), Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory
                                    .decodeByteArray(decodedString, 0, decodedString.length);
                            imageView.setImageBitmap(decodedByte);
                        }
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

    private void getJobseekerProfile() {

        //Reading jobseeker data
        JobseekerDA jobseekerDA = new JobseekerDA(getActivity());
        jobseeker = jobseekerDA.getJobseekerData();
        //Close jobseeker database
        jobseekerDA.close();

        textViewName.setText(jobseeker.getName());
        ratingBar.setRating((float) jobseeker.getRating());
        ratingValue.setText(String.valueOf(jobseeker.getRating()));

        if (jobseeker.getImg() != null && !jobseeker.getImg().equals("")
                && !jobseeker.getImg().equals("null")) {
            byte[] decodedString = Base64.decode(jobseeker.getImg(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory
                    .decodeByteArray(decodedString, 0, decodedString.length);
            imageView.setImageBitmap(decodedByte);
        }

        new Thread(new Runnable() {

            @Override
            public void run() {

                List<Object> profileList = maintainJobseeker.getJobseekerProfile(userId);
                final double amount = (Double) profileList.get(1);
                final int totalCompleted = (Integer) profileList.get(2);

                handler.post(new Runnable() {

                    @Override
                    public void run() {

                        txtViewSalary.setText(String.format(Locale.ENGLISH, "RM %.2f", amount));
                        txtViewCompleted.setText(String.valueOf(totalCompleted));
                    }
                });
            }
        }).start();
    }

    private class GetJobseekerRequest extends StringRequest {

        private Map<String, String> params;

        GetJobseekerRequest(String url,
                            Response.Listener<String> listener,
                            Response.ErrorListener errorListener) {
            super(Method.POST, url, listener, errorListener);

            params = new HashMap<>();
            params.put("id", userId);
        }

        public Map<String, String> getParams() {
            return params;
        }
    }
}
