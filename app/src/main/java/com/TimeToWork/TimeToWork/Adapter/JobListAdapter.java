package com.TimeToWork.TimeToWork.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.TimeToWork.TimeToWork.CustomClass.CategoryTagView;
import com.TimeToWork.TimeToWork.Database.Entity.Company;
import com.TimeToWork.TimeToWork.Database.Entity.JobLocation;
import com.TimeToWork.TimeToWork.Database.Entity.JobPost;
import com.TimeToWork.TimeToWork.Jobseeker.ViewJobDetailActivity;
import com.TimeToWork.TimeToWork.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class JobListAdapter extends RecyclerView.Adapter<JobListAdapter.JobListViewHolder> implements Filterable {

    private Context mContext;
    private List<JobPost> jobPostList;
    private List<JobPost> originalJobPostList;

    public JobListAdapter(Context mContext,
                          List<JobPost> jobPostList,
                          List<JobPost> originalJobPostList) {

        this.mContext = mContext;
        this.jobPostList = jobPostList;
        this.originalJobPostList = originalJobPostList;
    }

    @Override
    public JobListViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_job_list, viewGroup, false);
        return new JobListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final JobListViewHolder jobListViewHolder, int i) {

        final JobPost jobPost = jobPostList.get(i);
        final JobLocation jobLocation = jobPost.getJobLocation();
        final Company company = jobPost.getCompany();

        try {
            JSONObject workingDateObject = new JSONObject(jobPost.getWorkingDate());

            Date startWorkingDate = new SimpleDateFormat("ddMMyyyy", Locale.ENGLISH)
                    .parse(workingDateObject.getString("wd1"));
            SimpleDateFormat newDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.ENGLISH);

            String workingDate = newDateFormat.format(startWorkingDate);

            int j = workingDateObject.names().length();
            if (j != 0) {

                Date endWorkingDate = new SimpleDateFormat("ddMMyyyy", Locale.ENGLISH)
                        .parse(workingDateObject.getString("wd" + j));

                workingDate = workingDate + " - " + newDateFormat.format(endWorkingDate);
            }
            jobListViewHolder.workingDate.setText(workingDate);

        } catch (JSONException | ParseException e) {
            e.printStackTrace();
        }

        jobListViewHolder.companyName.setText(company.getName());
        jobListViewHolder.ratingText.setText(String.format(Locale.ENGLISH, "%.1f", company.getRating()));
        jobListViewHolder.rateBarCompany.setRating((float) company.getRating());
        jobListViewHolder.title.setText(jobPost.getTitle());
        jobListViewHolder.workplace.setText(jobLocation.getName());
        jobListViewHolder.wages.setText(String.format(Locale.getDefault(), "RM %.2f", jobPost.getWages()));
        jobListViewHolder.categoryTagView.setText(jobPost.getCategory());

        if (jobLocation.getDistance() != 99999999) {
            jobListViewHolder.distance.setVisibility(View.VISIBLE);
            jobListViewHolder.distance.setText(String.format("%s km", jobLocation.getDistance()));
        } else {
            jobListViewHolder.distance.setVisibility(View.INVISIBLE);
        }
        if (jobPost.getPaymentTerm() > 7) {
            jobListViewHolder.tagFastPayment.setVisibility(View.GONE);
        } else {
            jobListViewHolder.tagFastPayment.setVisibility(View.VISIBLE);
        }
        if (!jobPost.isAds()) {
            jobListViewHolder.tagRecommended.setVisibility(View.GONE);
        } else {
            jobListViewHolder.tagRecommended.setVisibility(View.VISIBLE);
        }

        if (company.getImg() != null) {
            byte[] decodedString = Base64.decode(company.getImg(), Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory
                    .decodeByteArray(decodedString, 0, decodedString.length);
            jobListViewHolder.img.setImageBitmap(decodedByte);
        }

        jobListViewHolder.layoutJob.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, ViewJobDetailActivity.class);
                intent.putExtra("JOB", jobPost);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return jobPostList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {

            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.equals("")) {
                    jobPostList = originalJobPostList;
                } else {
                    List<JobPost> searchedList = new ArrayList<>();
                    for (JobPost jobPost : originalJobPostList) {
                        if (jobPost.getId().toLowerCase().contains(charString.toLowerCase())) {
                            searchedList.add(jobPost);
                        }
                        if (jobPost.getTitle().toLowerCase().contains(charString.toLowerCase())) {
                            searchedList.add(jobPost);
                        }
                        if (jobPost.getCompany().getName().toLowerCase().contains(charString.toLowerCase())) {
                            searchedList.add(jobPost);
                        }
                    }
                    jobPostList.clear();
                    jobPostList.addAll(searchedList);
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = jobPostList;

                return filterResults;
            }

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {

                jobPostList = (ArrayList<JobPost>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    public void clear() {

        int size = jobPostList.size();
        jobPostList.clear();
        notifyItemRangeRemoved(0, size);
    }

    class JobListViewHolder extends RecyclerView.ViewHolder {

        LinearLayout layoutJob;
        ImageView img;
        TextView title, companyName, workplace, workingDate, wages, ratingText, distance;
        TextView tagFastPayment, tagRecommended;
        CategoryTagView categoryTagView;
        RatingBar rateBarCompany;

        JobListViewHolder(View view) {

            super(view);
            layoutJob = (LinearLayout) view.findViewById(R.id.layout_job);
            img = (CircleImageView) view.findViewById(R.id.img_company);
            title = (TextView) view.findViewById(R.id.tv_job_title);
            companyName = (TextView) view.findViewById(R.id.tv_company_name);
            workplace = (TextView) view.findViewById(R.id.tv_workplace);
            workingDate = (TextView) view.findViewById(R.id.tv_working_date);
            wages = (TextView) view.findViewById(R.id.tv_wages);
            ratingText = (TextView) view.findViewById(R.id.tv_company_rating);
            distance = (TextView) view.findViewById(R.id.tv_job_distance);
            tagFastPayment = (TextView) view.findViewById(R.id.tag_fast_payment);
            tagRecommended = (TextView) view.findViewById(R.id.tag_recommended);
            categoryTagView = (CategoryTagView) view.findViewById(R.id.tag_category);
            rateBarCompany = (RatingBar) view.findViewById(R.id.rate_bar_company);
        }
    }
}
