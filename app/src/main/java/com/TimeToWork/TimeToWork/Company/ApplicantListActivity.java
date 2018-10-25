package com.TimeToWork.TimeToWork.Company;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.TimeToWork.TimeToWork.Database.Entity.JobPost;
import com.TimeToWork.TimeToWork.R;

import java.util.ArrayList;
import java.util.List;

public class ApplicantListActivity extends AppCompatActivity {

    private JobPost jobPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applicant_list);

        jobPost = (JobPost) getIntent().getSerializableExtra("JOBPOST");

        Fragment sentFragment = new ApplicantFragment();
        Fragment approvedFragment = new ApplicantFragment();
        Fragment rejectedFragment = new ApplicantFragment();

        ApplicantListActivity.ViewPagerAdapter viewPagerAdapter
                = new ApplicantListActivity.ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(sentFragment, "Sent");
        viewPagerAdapter.addFragment(approvedFragment, "Approved");
        viewPagerAdapter.addFragment(rejectedFragment, "Rejected");

        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager_applicant);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(viewPagerAdapter);

        TabLayout mTabLayout = (TabLayout) findViewById(R.id.tab_application_status);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private class ViewPagerAdapter extends FragmentPagerAdapter {

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        private void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public Fragment getItem(int position) {

            Bundle bundle = new Bundle();
            bundle.putString("STATUS", getPageTitle(position).toString());
            bundle.putSerializable("JOBPOST", jobPost);
            Fragment fragment = mFragmentList.get(position);
            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
