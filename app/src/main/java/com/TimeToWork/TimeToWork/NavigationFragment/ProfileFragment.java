package com.TimeToWork.TimeToWork.NavigationFragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.Database.Control.MaintainJobseeker;
import com.TimeToWork.TimeToWork.Database.Entity.Jobseeker;
import com.TimeToWork.TimeToWork.Database.JobseekerDA;
import com.TimeToWork.TimeToWork.R;

import java.util.List;
import java.util.Locale;

import static com.TimeToWork.TimeToWork.MainApplication.clearAppData;
import static com.TimeToWork.TimeToWork.MainApplication.userId;

public class ProfileFragment extends Fragment {

    private CustomProgressDialog mProgressDialog;
    private Handler handler;

    private TextView textViewName, txtViewSalary, txtViewCompleted;
    private ImageView imageView;

    private Jobseeker jobseeker;

    private MaintainJobseeker maintainJobseeker = new MaintainJobseeker();

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(getString(R.string.fragment_profile));

        // Enable menu in toolbar
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        mProgressDialog = new CustomProgressDialog(getActivity());
        handler = new Handler();

        textViewName = (TextView) view.findViewById(R.id.name);
        txtViewSalary = (TextView) view.findViewById(R.id.salary);
        txtViewCompleted = (TextView) view.findViewById(R.id.completed);
        imageView = (ImageView) view.findViewById(R.id.imgUser);

        TextView showPersonalDetails = (TextView) view.findViewById(R.id.pd);
        showPersonalDetails.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
//                Intent myIntent = new Intent(getActivity(), PersonalDetails.class);
//                startActivity(myIntent);
            }
        });

        TextView showPreferredJob = (TextView) view.findViewById(R.id.pj);
        showPreferredJob.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
//                Intent myIntent = new Intent(getActivity(), PreferredJobPage.class);
//                startActivity(myIntent);
            }
        });

        TextView showPreferredLocation = (TextView) view.findViewById(R.id.pl);
        showPreferredLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
//                Intent myIntent = new Intent(getActivity(), PreferredLocationPage.class);
//                startActivity(myIntent);
            }
        });

        TextView showSetFreeTime = (TextView) view.findViewById(R.id.sft);
        showSetFreeTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
//                Intent myIntent = new Intent(getActivity(), SetFreeTimePage.class);
//                startActivity(myIntent);
            }
        });

        TextView showCalendar = (TextView) view.findViewById(R.id.eft);
        showCalendar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
//                Intent myIntent = new Intent(getActivity(), CalendarPage.class);
//                startActivity(myIntent);
            }
        });

        TextView changePassword = (TextView) view.findViewById(R.id.cp);
        changePassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
//                Intent myIntent = new Intent(getActivity(), ChangePasswordPage.class);
//                startActivity(myIntent);
            }
        });

        TextView report = (TextView) view.findViewById(R.id.report);
        report.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
//                Intent myIntent = new Intent(getActivity(), JobseekerReport.class);
//                startActivity(myIntent);
            }
        });

        // Get profile detail
        getJobseekerProfile();

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
            clearAppData(getActivity());
        }
        return super.onOptionsItemSelected(item);
    }

    private void getJobseekerProfile() {

        //Show progress dialog
        mProgressDialog.setMessage("Loading …");
        mProgressDialog.show();

        new Thread(new Runnable() {

            @Override
            public void run() {

                List<Object> profileList = maintainJobseeker.getJobseekerProfile(userId);
                final double amount = (Double) profileList.get(1);
                final int totalCompleted = (Integer) profileList.get(2);

                //Reading jobseeker data
                JobseekerDA jobseekerDA = new JobseekerDA(getActivity());
                jobseeker = jobseekerDA.getJobseekerData();
                //Close jobseeker database
                jobseekerDA.close();

                handler.post(new Runnable() {

                    @Override
                    public void run() {

                        textViewName.setText(jobseeker.getName());
                        txtViewSalary.setText(String.format(Locale.ENGLISH, "RM %.2f", amount));
                        txtViewCompleted.setText(String.valueOf(totalCompleted));

                        if (jobseeker.getImg() != null && !jobseeker.getImg().equals("")
                                && !jobseeker.getImg().equals("null")) {
                            byte[] decodedString = Base64.decode(jobseeker.getImg(), Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory
                                    .decodeByteArray(decodedString, 0, decodedString.length);
                            imageView.setImageBitmap(decodedByte);
                        }
                        mProgressDialog.dismiss();
                    }
                });
            }
        }).start();
    }
}
