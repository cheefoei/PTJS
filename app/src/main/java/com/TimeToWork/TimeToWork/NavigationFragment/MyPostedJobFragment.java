package com.TimeToWork.TimeToWork.NavigationFragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.TimeToWork.TimeToWork.Company.PostedJobListFragment;
import com.TimeToWork.TimeToWork.R;

import java.util.ArrayList;
import java.util.List;

public class MyPostedJobFragment extends Fragment {

    public MyPostedJobFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(getString(R.string.fragment_posted_job));

        // Enable menu in toolbar
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_posted_job, container, false);

        Fragment fullJobFragment = new PostedJobListFragment();
        Fragment completedJobFragment = new PostedJobListFragment();
        Fragment closedJobFragment = new PostedJobListFragment();

        ViewPagerAdapter viewPagerAdapter
                = new ViewPagerAdapter(getChildFragmentManager()); //**
        viewPagerAdapter.addFragment(fullJobFragment, "Full Slot");
        viewPagerAdapter.addFragment(completedJobFragment, "Completed");
        viewPagerAdapter.addFragment(closedJobFragment, "Unavailable");

        ViewPager mViewPager = (ViewPager) view.findViewById(R.id.viewpager_posted_job);
        mViewPager.setOffscreenPageLimit(2);
        mViewPager.setAdapter(viewPagerAdapter);

        TabLayout mTabLayout = (TabLayout) view.findViewById(R.id.tab_job_status);
        mTabLayout.setupWithViewPager(mViewPager);

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_help, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.help) {

            String message = "<b>Full Slot</b>: When the number of approved applicant matches " +
                    "the position number of job, the system will make the job to this status.<br/><br/>" +
                    "<b>Completed</b>: After you clicked 'Complete Job' button, means you made " +
                    "the job to 'Completed' status. The job is allowed for the jobseeker and " +
                    "employer to provide review.<br/><br/>" +
                    "<b>Unavailable</b>: After you made the job unavailable or the working date of the job " +
                    "already after current date, the system will make the job to " +
                    "'Unavailable' status. The jobseeker cannot apply the job.";

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            //noinspection deprecation
            builder.setTitle("Learn More")
                    .setMessage(Html.fromHtml(message))
                    .setPositiveButton("Close", null)
                    .create()
                    .show();
        }
        return super.onOptionsItemSelected(item);
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
            if (getPageTitle(position).toString().equals("Full Slot")) {
                bundle.putString("STATUS", "Full");
            } else {
                bundle.putString("STATUS", getPageTitle(position).toString());
            }
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
