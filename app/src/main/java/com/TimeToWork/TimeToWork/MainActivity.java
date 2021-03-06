package com.TimeToWork.TimeToWork;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.Database.CompanyDA;
import com.TimeToWork.TimeToWork.Database.Entity.Company;
import com.TimeToWork.TimeToWork.Database.Entity.Jobseeker;
import com.TimeToWork.TimeToWork.Database.JobseekerDA;
import com.TimeToWork.TimeToWork.NavigationFragment.CompanyHomeFragment;
import com.TimeToWork.TimeToWork.NavigationFragment.CompanyProfileFragment;
import com.TimeToWork.TimeToWork.NavigationFragment.JobseekerHomeFragment;
import com.TimeToWork.TimeToWork.NavigationFragment.MyAppliedJobFragment;
import com.TimeToWork.TimeToWork.NavigationFragment.PaymentHistoryFragment;
import com.TimeToWork.TimeToWork.NavigationFragment.MyPostedJobFragment;
import com.TimeToWork.TimeToWork.NavigationFragment.JobseekerProfileFragment;

import static com.TimeToWork.TimeToWork.MainApplication.latitude;
import static com.TimeToWork.TimeToWork.MainApplication.longitude;
import static com.TimeToWork.TimeToWork.MainApplication.userId;
import static com.TimeToWork.TimeToWork.MainApplication.userType;

public class MainActivity extends AppCompatActivity
        implements BottomNavigationView.OnNavigationItemSelectedListener {

    private BottomNavigationView navigation;
    private FragmentTransaction mFragmentTransaction;
    private Fragment mFragment;
    private CustomProgressDialog mProgressDialog;

    private String currentFragment = "HOME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainApplication.root = getString(R.string.url_root);
        mProgressDialog = new CustomProgressDialog(MainActivity.this);

        new ActivityAsyncTask() {

            @Override
            protected void onPreExecute() {

                super.onPreExecute();

                navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
                navigation.setOnNavigationItemSelectedListener(MainActivity.this);

                //Show progress dialog
                mProgressDialog.setMessage("Loading ...");
                mProgressDialog.show();
            }

            @Override
            protected Boolean doInBackground(String... params) {

                MainApplication.setRequestQueue(MainActivity.this);
                readUserData();
                // Get current location if user is jobseeker
                if (userType != null) {
                    if (userType.equals("Jobseeker")) {
                        getCurrentLocation();
                    }
                }
                return null;
            }

            @Override
            protected void onPostExecute(Boolean b) {

                super.onPostExecute(b);
                //Close progress dialog
                mProgressDialog.dismiss();

                if (userId != null) {

                    if (userType.equals("Jobseeker")) {
                        // Setup navigation menu for different users
                        navigation.inflateMenu(R.menu.menu_navigation_jobseeker);
                        // Adding home fragment into frame as default
                        mFragment = new JobseekerHomeFragment();
                    } else if (userType.equals("Company")) {
                        // Setup navigation menu for different users
                        navigation.inflateMenu(R.menu.menu_navigation_company);
                        // Adding home fragment into frame as default
                        mFragment = new CompanyHomeFragment();
                    }
                    mFragmentTransaction = getSupportFragmentManager().beginTransaction();
                    mFragmentTransaction.replace(R.id.content_frame, mFragment, currentFragment);
                    mFragmentTransaction.commitAllowingStateLoss();
                }
            }
        }.execute();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                getCurrentLocation();
                break;
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        mProgressDialog.dismiss();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        item.setChecked(true);

        if (id == R.id.navigation_jobseeker_home && !currentFragment.equals("HOME")) {

            navigation.getMenu().getItem(0).setChecked(true);
            currentFragment = "HOME";

            mFragment = getSupportFragmentManager().findFragmentByTag(currentFragment);
            if (mFragment == null) {
                mFragment = new JobseekerHomeFragment();
            }
        } else if (id == R.id.navigation_company_home && !currentFragment.equals("HOME")) {

            navigation.getMenu().getItem(0).setChecked(true);
            currentFragment = "HOME";

            mFragment = getSupportFragmentManager().findFragmentByTag(currentFragment);
            if (mFragment == null) {
                mFragment = new CompanyHomeFragment();
            }
        } else if (id == R.id.navigation_my_job && !currentFragment.equals("MYJOB")) {

//            mFragment = new MyAppliedJobFragment();
            currentFragment = "MYJOB";

            mFragment = getSupportFragmentManager().findFragmentByTag(currentFragment);
            if (mFragment == null) {
                mFragment = new MyAppliedJobFragment();
            }
        } else if (id == R.id.navigation_posted_job && !currentFragment.equals("POSTEDJOB")) {

//            mFragment = new MyPostedJobFragment();
            currentFragment = "POSTEDJOB";

            mFragment = getSupportFragmentManager().findFragmentByTag(currentFragment);
            if (mFragment == null) {
                mFragment = new MyPostedJobFragment();
            }
        } else if (id == R.id.navigation_payment_history && !currentFragment.equals("PAYMENT")) {

//            mFragment = new PaymentHistoryFragment();
            currentFragment = "PAYMENT";

            mFragment = getSupportFragmentManager().findFragmentByTag(currentFragment);
            if (mFragment == null) {
                mFragment = new PaymentHistoryFragment();
            }
        } else if (id == R.id.navigation_profile && !currentFragment.equals("PROFILE")) {

            currentFragment = "PROFILE";
            mFragment = getSupportFragmentManager().findFragmentByTag(currentFragment);

//            mFragment = new JobseekerProfileFragment();
            if (mFragment == null) {
                if (userType.equals("Jobseeker")) {
                    mFragment = new JobseekerProfileFragment();
                } else if (userType.equals("Company")) {
                    mFragment = new CompanyProfileFragment();
                }
            }
        }

        mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.content_frame, mFragment, currentFragment);
        mFragmentTransaction.commit();

        return false;
    }

    private void readUserData() {

        mProgressDialog.setMessage("Loading ...");
        mProgressDialog.show();

        //Reading jobseeker data
        JobseekerDA jobseekerDA = new JobseekerDA(this);
        Jobseeker jobseeker = jobseekerDA.getJobseekerData();
        //Close jobseeker database
        jobseekerDA.close();

        //Reading company data
        CompanyDA companyDA = new CompanyDA(this);
        Company company = companyDA.getCompanyData();
        //Close company database
        companyDA.close();

        if (jobseeker == null && company == null) {
            // Close progress dialog
            mProgressDialog.dismiss();
            //If not yet log in, navigate to LoginActivity
            Intent intent = new Intent(this, LoginOptionActivity.class);
            startActivity(intent);
            finish();
        } else {
            if (jobseeker != null) {
                userType = "Jobseeker";
                userId = jobseeker.getId();
            } else {
                userType = "Company";
                userId = company.getId();
            }
        }
        // Close progress dialog
        mProgressDialog.dismiss();
    }

    private void getCurrentLocation() {

        LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {

            Location location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            if (location != null) {
                latitude = location.getLatitude();
                longitude = location.getLongitude();
            } else {
                latitude = Double.NaN;
                longitude = Double.NaN;
            }
        }
    }

    private abstract class ActivityAsyncTask extends AsyncTask<String, Boolean, Boolean> {

        private ActivityAsyncTask() {
        }
    }
}
