package com.TimeToWork.TimeToWork;

import android.content.Intent;
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
import android.view.MenuItem;

import com.TimeToWork.TimeToWork.NaviFragment.HomeFragment;
import com.TimeToWork.TimeToWork.NaviFragment.MyJobFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private FragmentTransaction mFragmentTransaction;
    private Fragment mFragment;

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

        // Adding home fragment into frame as default
        FragmentManager fragmentManager = getSupportFragmentManager();
        mFragment = new HomeFragment();
        mFragmentTransaction = fragmentManager.beginTransaction();
        mFragmentTransaction.add(R.id.content_frame, mFragment);
        mFragmentTransaction.commit();

        Intent i = new Intent(this, LoginRegisterActivity.class);
        startActivity(i);
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
}
