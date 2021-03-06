package com.TimeToWork.TimeToWork.Jobseeker;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.TimeToWork.TimeToWork.Database.Entity.Jobseeker;
import com.TimeToWork.TimeToWork.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class JobseekerVisitorActivity extends AppCompatActivity
        implements JobseekerReviewFragment.TagTotalNumberCallBack {

    private LinearLayout layoutRating, layoutReview;
    private TabLayout mTabLayout;

    private List<TextView> tags;
    private List<String> tagStrings;
    private int totalReview = 0;

    private Jobseeker jobseeker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobseeker_visitor);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
        }
        setTitle("");
        jobseeker = (Jobseeker) getIntent().getSerializableExtra("JOBSEEKER");

        layoutRating = (LinearLayout) findViewById(R.id.layout_profile_rating);
        layoutReview = (LinearLayout) findViewById(R.id.layout_review_tag);

        TextView tvJobseekerName = (TextView) findViewById(R.id.tv_jobseeker_name);
        tvJobseekerName.setText(jobseeker.getName());

        TextView tvRating = (TextView) findViewById(R.id.tv_jobseeker_rating);
        tvRating.setText(String.valueOf(jobseeker.getRating()));

        ImageView imgProfile = (ImageView) findViewById(R.id.img_jobseeker);
        if (jobseeker.getImg() != null && !jobseeker.getImg().equals("")
                && !jobseeker.getImg().equals("null")) {
            byte[] decodedString = Base64.decode(jobseeker.getImg(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory
                    .decodeByteArray(decodedString, 0, decodedString.length);
            imgProfile.setImageBitmap(decodedByte);
        }

        RatingBar ratingBar = (RatingBar) findViewById(R.id.rate_bar_jobseeker);
        ratingBar.setRating((float) jobseeker.getRating());

        tagStrings = Arrays.asList(
                getResources().getStringArray(R.array.array_review_tag_company));

        TextView tvTag1 = (TextView) findViewById(R.id.tv_review_tag_1);
        TextView tvTag2 = (TextView) findViewById(R.id.tv_review_tag_2);
        TextView tvTag3 = (TextView) findViewById(R.id.tv_review_tag_3);
        TextView tvTag4 = (TextView) findViewById(R.id.tv_review_tag_4);

        tags = new ArrayList<>();
        tags.add(tvTag1);
        tags.add(tvTag2);
        tags.add(tvTag3);
        tags.add(tvTag4);

        for (int i = 0; i < tags.size(); i++) {
            TextView tag = tags.get(i);
            tag.setText(tagStrings.get(i));
        }

        Fragment jobseekerInfoFragment = new JobseekerInfoFragment();
        Fragment jobseekerReviewFragment = new JobseekerReviewFragment();

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(jobseekerInfoFragment, "INFORMATION");
        viewPagerAdapter.addFragment(jobseekerReviewFragment, "REVIEW");

        ViewPager mViewPager = (ViewPager) findViewById(R.id.viewpager_visitor);
        mViewPager.setOffscreenPageLimit(1);
        mViewPager.setAdapter(viewPagerAdapter);

        mTabLayout = (TabLayout) findViewById(R.id.tab_visitor);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                int index = tab.getPosition();
                if (index == 0) {
                    layoutRating.setVisibility(View.VISIBLE);
                    layoutReview.setVisibility(View.GONE);
                } else if (index == 1) {
                    layoutRating.setVisibility(View.GONE);
                    layoutReview.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        });
    }

    @Override
    public void setTagTotalNumber(int[] tagTotalNumber) {

        for (int i = 0; i < tags.size(); i++) {
            TextView tv = tags.get(i);
            tv.setText(tagStrings.get(i) + " (" + tagTotalNumber[i] + ")");
            totalReview += tagTotalNumber[i];
        }
        if (mTabLayout.getTabAt(1) != null) {
            //noinspection ConstantConditions
            mTabLayout.getTabAt(1).setText("REVIEW (" + totalReview + ")");
        }
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
            bundle.putSerializable("JOBSEEKER", jobseeker);
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
