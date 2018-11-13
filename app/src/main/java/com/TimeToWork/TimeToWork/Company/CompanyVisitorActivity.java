package com.TimeToWork.TimeToWork.Company;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.TimeToWork.TimeToWork.Database.Entity.Company;
import com.TimeToWork.TimeToWork.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CompanyVisitorActivity extends AppCompatActivity
        implements CompanyReviewFragment.TagTotalNumberCallBack {

    private LinearLayout layoutRating, layoutReview;
    private TabLayout mTabLayout;

    private List<TextView> tags;
    private List<String> tagStrings;
    private int totalReview = 0;

    private Company company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_visitor);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setElevation(0);
        }
        setTitle("");
        company = (Company) getIntent().getSerializableExtra("COMPANY");

        layoutRating = (LinearLayout) findViewById(R.id.layout_profile_rating);
        layoutReview = (LinearLayout) findViewById(R.id.layout_review_tag);

        TextView tvJobseekerName = (TextView) findViewById(R.id.tv_company_name);
        tvJobseekerName.setText(company.getName());

        TextView tvRating = (TextView) findViewById(R.id.tv_company_rating);
        tvRating.setText(String.valueOf(company.getRating()));

        ImageView imgProfile = (ImageView) findViewById(R.id.img_company);
        if (company.getImg() != null && !company.getImg().equals("")
                && !company.getImg().equals("null")) {
            byte[] decodedString = Base64.decode(company.getImg(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory
                    .decodeByteArray(decodedString, 0, decodedString.length);
            imgProfile.setImageBitmap(decodedByte);
        }

        RatingBar ratingBar = (RatingBar) findViewById(R.id.rate_bar_company);
        ratingBar.setRating((float) company.getRating());

        tagStrings = Arrays.asList(
                getResources().getStringArray(R.array.array_review_tag_jobseeker));

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

        Fragment companyInfoFragment = new CompanyInfoFragment();
        Fragment companyReviewFragment = new CompanyReviewFragment();

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFragment(companyInfoFragment, "INFORMATION");
        viewPagerAdapter.addFragment(companyReviewFragment, "REVIEW");

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
            bundle.putSerializable("COMPANY", company);
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
