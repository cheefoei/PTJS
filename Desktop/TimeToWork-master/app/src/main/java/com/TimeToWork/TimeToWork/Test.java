package com.TimeToWork.TimeToWork;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.ArrayList;

import com.TimeToWork.TimeToWork.Control.MaintainNotification;
import com.TimeToWork.TimeToWork.Entity.JobApplication;
import com.TimeToWork.TimeToWork.Entity.PartTimer;

public class Test extends AppCompatActivity {

    private MaintainNotification MJobApplication = new MaintainNotification();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        /*Button registerShowDialog = (Button) findViewById(R.id.btnShowDialog);
        registerShowDialog.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                View dialogView = View.inflate(Test.this, R.layout.activity_registration_page, null);
                AlertDialog.Builder builder = new AlertDialog.Builder(Test.this)
                        .setTitle("Register")
                        .setView(dialogView)
                        .setPositiveButton("Register", null)
                        .setNegativeButton("Cancel", null);
                builder.show();
            }
        });*/

        Button registerJobSeeker = (Button) findViewById(R.id.btnShowDialog);
        registerJobSeeker.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent myIntent = new Intent(Test.this, RegistrationPage.class);
                startActivity(myIntent);
            }
        });


        Button showProfile = (Button) findViewById(R.id.btnProfile);
        showProfile.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent myIntent = new Intent(Test.this, ProfilePage.class);
                startActivity(myIntent);

            }
        });

        Button showNotification = (Button) findViewById(R.id.btnNotification);
        showNotification.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                try {
                    notification();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });

        /*Button showCalendar = (Button) findViewById(R.id.btnCalendar);
        showCalendar.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent myIntent = new Intent(Test.this, CalendarPage.class);
                startActivity(myIntent);
            }
        });*/

        Button showSetFreeTime = (Button) findViewById(R.id.btnSetFreeTime);
        showSetFreeTime.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent myIntent = new Intent(Test.this, ChangePasswordPage.class);
                startActivity(myIntent);
            }
        });

        Button registerCompany = (Button) findViewById(R.id.btnRegCompany);
        registerCompany.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent myIntent = new Intent(Test.this, CompanyRegistrationPage.class);
                startActivity(myIntent);
            }
        });

        Button profileCompany = (Button) findViewById(R.id.btnProfileCompany);
        profileCompany.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent myIntent = new Intent(Test.this, CompanyProfilePage.class);
                startActivity(myIntent);
            }
        });

        Button login = (Button) findViewById(R.id.btn_login);
        login.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent myIntent = new Intent(Test.this, LoginActivity.class);
                startActivity(myIntent);
            }
        });
    }

    public void notification() throws SQLException {

        ArrayList<JobApplication> jobApplicationArrayList = MJobApplication.getJobApplicationDetails();
        for(int i = 0; i < jobApplicationArrayList.size(); i++)
        {
            if(jobApplicationArrayList.get(i).getJob_application_id().equals("JA10004"))
            {
                if(jobApplicationArrayList.get(i).getJob_application_status().equals("Sent"))
                {
                    NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                            .setDefaults(NotificationCompat.DEFAULT_ALL)
                            .setSmallIcon(R.drawable.notification)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.notification))
                            .setContentTitle("Message")
                            .setContentText("Sent");
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(1,notificationBuilder.build());
                }
                else if(jobApplicationArrayList.get(i).getJob_application_status().equals("Reject"))
                {
                    NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                            .setDefaults(NotificationCompat.DEFAULT_ALL)
                            .setSmallIcon(R.drawable.notification)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.notification))
                            .setContentTitle("Message")
                            .setContentText("Reject");
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(1,notificationBuilder.build());
                }
                else if(jobApplicationArrayList.get(i).getJob_application_status().equals("Approved"))
                {
                    NotificationCompat.Builder notificationBuilder = (NotificationCompat.Builder) new NotificationCompat.Builder(this)
                            .setDefaults(NotificationCompat.DEFAULT_ALL)
                            .setSmallIcon(R.drawable.notification)
                            .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.notification))
                            .setContentTitle("Message")
                            .setContentText("Approved");
                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    notificationManager.notify(1,notificationBuilder.build());
                }
            }
        }

    }
}
