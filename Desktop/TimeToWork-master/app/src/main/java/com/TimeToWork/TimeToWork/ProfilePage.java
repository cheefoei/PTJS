package com.TimeToWork.TimeToWork;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import com.TimeToWork.TimeToWork.Control.MaintainPartTimer;
import com.TimeToWork.TimeToWork.Entity.PartTimer;

public class ProfilePage extends AppCompatActivity {

    public ProfilePage() {
    }

    private TextView textViewName, txtViewSalary, txtViewCompleted;
    private ImageView imageView;
    private DatabaseReference databaseRef;
    private ArrayList<PartTimer> partTimerArrayList;
    private MaintainPartTimer MPartTimer = new MaintainPartTimer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        textViewName = (TextView) findViewById(R.id.name);
        txtViewSalary = (TextView) findViewById(R.id.salary);
        txtViewCompleted = (TextView) findViewById(R.id.completed);
        imageView = (ImageView) findViewById(R.id.imgUser);
        databaseRef = FirebaseDatabase.getInstance().getReference("jobseeker");

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = (String) dataSnapshot.child("J10001").child("jobseeker_img").getValue();
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

        getPartTimerDetails();

        TextView showPersonalDetails = (TextView) findViewById(R.id.pd);
        showPersonalDetails.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent myIntent = new Intent(ProfilePage.this, PersonalDetails.class);
                startActivity(myIntent);
            }
        });

        TextView showPreferredJob = (TextView) findViewById(R.id.pj);
        showPreferredJob.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent myIntent = new Intent(ProfilePage.this, PreferredJobPage.class);
                startActivity(myIntent);
            }
        });

        TextView showPreferredLocation = (TextView) findViewById(R.id.pl);
        showPreferredLocation.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent myIntent = new Intent(ProfilePage.this, PreferredLocationPage.class);
                startActivity(myIntent);
            }
        });

        TextView showSetFreeTime = (TextView) findViewById(R.id.sft);
        showSetFreeTime.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent myIntent = new Intent(ProfilePage.this, SetFreeTimePage.class);
                startActivity(myIntent);
            }
        });

        TextView showCalendar = (TextView) findViewById(R.id.eft);
        showCalendar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent myIntent = new Intent(ProfilePage.this, CalendarPage.class);
                startActivity(myIntent);
            }
        });

        TextView changePassword = (TextView) findViewById(R.id.cp);
        changePassword.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent myIntent = new Intent(ProfilePage.this, ChangePasswordPage.class);
                startActivity(myIntent);
            }
        });

        TextView report = (TextView) findViewById(R.id.report);
        report.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent myIntent = new Intent(ProfilePage.this, JobseekerReport.class);
                startActivity(myIntent);
            }
        });
    }

    private void getPartTimerDetails() {

        partTimerArrayList = MPartTimer.getPartTimerProfile("J10001");

        for (int i = 0; i < partTimerArrayList.size(); i++) {
            textViewName.setText(partTimerArrayList.get(i).getJobseeker_name());
            txtViewSalary.setText("RM " + String.valueOf(partTimerArrayList.get(i).getSalary()));
            txtViewCompleted.setText(String.valueOf(partTimerArrayList.get(i).getCompleted()));
        }
    }
}
