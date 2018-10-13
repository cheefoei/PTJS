package com.TimeToWork.TimeToWork;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.Database.CompanyDA;
import com.TimeToWork.TimeToWork.Database.Entity.Company;
import com.TimeToWork.TimeToWork.Database.Entity.Jobseeker;
import com.TimeToWork.TimeToWork.Database.JobseekerDA;
import com.TimeToWork.TimeToWork.NavigationFragment.HomeFragment;
import com.TimeToWork.TimeToWork.NavigationFragment.MyJobFragment;

import static com.TimeToWork.TimeToWork.MainApplication.clearAppData;
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

        navigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        navigation.setOnNavigationItemSelectedListener(this);

        // Adding home fragment into frame as default
        FragmentManager fragmentManager = getSupportFragmentManager();
        mFragment = new HomeFragment();
        mFragmentTransaction = fragmentManager.beginTransaction();
        mFragmentTransaction.add(R.id.content_frame, mFragment, currentFragment);
        mFragmentTransaction.commit();

        MainApplication.root = getString(R.string.url_root);
        mProgressDialog = new CustomProgressDialog(MainActivity.this);

        new ActivityAsyncTask() {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                //Show progress dialog
                mProgressDialog.setMessage("Loading ...");
                mProgressDialog.show();
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
                mProgressDialog.dismiss();
            }
        }.execute();

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

        if (id == R.id.navigation_home && !currentFragment.equals("HOME")) {

            navigation.getMenu().getItem(0).setChecked(true);
            currentFragment = "HOME";

            mFragment = getSupportFragmentManager().findFragmentByTag(currentFragment);
            if (mFragment == null) {
                mFragment = new HomeFragment();
            }
        } else if (id == R.id.navigation_my_job && !currentFragment.equals("MYJOB")) {

            mFragment = new MyJobFragment();
            currentFragment = "MYJOB";

            mFragment = getSupportFragmentManager().findFragmentByTag(currentFragment);
            if (mFragment == null) {
                mFragment = new MyJobFragment();
            }
        } else if (id == R.id.navigation_profile && !currentFragment.equals("PROFILE")) {

            clearAppData(MainActivity.this);
            currentFragment = "PROFILE";
        }

        mFragmentTransaction = getSupportFragmentManager().beginTransaction();
        mFragmentTransaction.replace(R.id.content_frame, mFragment, currentFragment);
        mFragmentTransaction.commit();

        return false;
    }

    private void readUserData() {

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
            //If not yet log in, navigate to LoginActivity
            Intent intent = new Intent(this, LoginOptionActivity.class);
            startActivity(intent);
            finish();
        } else {
            if (jobseeker != null) {
//                if (jobseeker.getImg() != null && !jobseeker.getImg().equals("")) {
//                    byte[] decodedString = Base64.decode(jobseeker.getImg(), Base64.DEFAULT);
//                    Bitmap decodedByte = BitmapFactory
//                            .decodeByteArray(decodedString, 0, decodedString.length);
//                    imgProfile.setImageBitmap(decodedByte);
//                }
//                tvUsername.setText(jobseeker.getName());
                navigation.inflateMenu(R.menu.menu_navigation_jobseeker);
                userType = "Jobseeker";
                userId = jobseeker.getId();
            } else {
//                if (company.getImg() != null && !company.getImg().equals("")) {
//                    byte[] decodedString = Base64.decode(company.getImg(), Base64.DEFAULT);
//                    Bitmap decodedByte = BitmapFactory
//                            .decodeByteArray(decodedString, 0, decodedString.length);
//                    imgProfile.setImageBitmap(decodedByte);
//                }
//                tvUsername.setText(company.getName());
                navigation.inflateMenu(R.menu.menu_navigation_company);
                userType = "Company";
                userId = company.getId();
            }
        }
    }

    private abstract class ActivityAsyncTask extends AsyncTask<String, String, String> {

        private ActivityAsyncTask() {
        }
    }
}
