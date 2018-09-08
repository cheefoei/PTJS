package com.TimeToWork.TimeToWork;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.Database.Entity.Jobseeker;
import com.TimeToWork.TimeToWork.Database.JobseekerDA;
import com.TimeToWork.TimeToWork.NaviFragment.HomeFragment;
import com.TimeToWork.TimeToWork.NaviFragment.MyJobFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentTransaction mFragmentTransaction;
    private Fragment mFragment;
    private TextView tvUsername;
    private ImageView imgProfile;

    private String currentFragment = "HOME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                drawer,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );
        drawer.addDrawerListener(toggle);
        toggle.syncState(); //Show animation of hamburger

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View navigationHeader = navigationView.getHeaderView(0);
        tvUsername = (TextView) navigationHeader.findViewById(R.id.tv_username);
        imgProfile = (ImageView) navigationHeader.findViewById(R.id.img_profile);

        // Adding home fragment into frame as default
        FragmentManager fragmentManager = getSupportFragmentManager();
        mFragment = new HomeFragment();
        mFragmentTransaction = fragmentManager.beginTransaction();
        mFragmentTransaction.add(R.id.content_frame, mFragment);
        mFragmentTransaction.commit();

        new ActivityAsyncTask() {

            CustomProgressDialog mProgressDialog
                    = new CustomProgressDialog(MainActivity.this);

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //Show progress dialog
                mProgressDialog.setMessage("Loading ...");
                mProgressDialog.toggleProgressDialog();
            }

            @Override
            protected String doInBackground(String... params) {

                MainApplication.setRequestQueue(MainActivity.this);
                readUserData();
                return null;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                //Close progress dialog
                mProgressDialog.toggleProgressDialog();
            }
        }.execute();

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_home && !currentFragment.equals("HOME")) {

            mFragment = new HomeFragment();
            currentFragment = "HOME";
        } else if (id == R.id.nav_my_job && !currentFragment.equals("JOB")) {

            mFragment = new MyJobFragment();
            currentFragment = "JOB";
        }

        mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.content_frame, mFragment);
        mFragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void readUserData() {

        //Reading jobseeker data
        JobseekerDA jobseekerDA = new JobseekerDA(this);
        Jobseeker jobseeker = jobseekerDA.getJobseekerData();
        //Close jobseeker database
        jobseekerDA.close();

        if (jobseeker == null) {
            //If not yet log in, navigate to LoginActivity
            Intent intent = new Intent(this, LoginRegisterActivity.class);
            startActivity(intent);
            finish();
        } else {
            if (jobseeker.getImg() != null && !jobseeker.getImg().equals("")) {
                byte[] decodedString = Base64.decode(jobseeker.getImg(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory
                        .decodeByteArray(decodedString, 0, decodedString.length);
                imgProfile.setImageBitmap(decodedByte);
            }
            tvUsername.setText(jobseeker.getName());
        }
    }

    private abstract class ActivityAsyncTask extends AsyncTask<String, String, String> {

        private ActivityAsyncTask() {
        }
    }
}
