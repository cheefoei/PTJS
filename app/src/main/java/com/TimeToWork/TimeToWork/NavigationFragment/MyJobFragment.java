package com.TimeToWork.TimeToWork.NavigationFragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.TimeToWork.TimeToWork.Jobseeker.JobApplicationFragment;
import com.TimeToWork.TimeToWork.R;

import java.util.ArrayList;
import java.util.List;

public class MyJobFragment extends Fragment {

    public MyJobFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(getString(R.string.fragment_my_job));

        // Enable menu in toolbar
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_job, container, false);

        Fragment sentJobFragment = new JobApplicationFragment();
        Fragment approvedJobFragment = new JobApplicationFragment();
        Fragment rejectedJobFragment = new JobApplicationFragment();
        Fragment completedJobFragment = new JobApplicationFragment();
        Fragment cancelledJobFragment = new JobApplicationFragment();

        ViewPagerAdapter viewPagerAdapter
                = new ViewPagerAdapter(getChildFragmentManager()); //**
        viewPagerAdapter.addFragment(sentJobFragment, "Sent");
        viewPagerAdapter.addFragment(approvedJobFragment, "Approved");
        viewPagerAdapter.addFragment(completedJobFragment, "Completed");
        viewPagerAdapter.addFragment(rejectedJobFragment, "Rejected");
        viewPagerAdapter.addFragment(cancelledJobFragment, "Cancelled");

        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewpager_my_job);
        mViewPager.setOffscreenPageLimit(4);
        mViewPager.setAdapter(viewPagerAdapter);

        TabLayout mTabLayout = (TabLayout) view.findViewById(R.id.tab_job_status);
        mTabLayout.setupWithViewPager(mViewPager);

        return view;
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
