package com.TimeToWork.TimeToWork.Adapter;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.TimeToWork.TimeToWork.Database.Entity.Review;
import com.TimeToWork.TimeToWork.R;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.reviewViewHolder> {

    private List<Review> reviewList;
    private String showRole;

    public ReviewAdapter(List<Review> reviewList, String showRole) {
        this.reviewList = reviewList;
        this.showRole = showRole;
    }

    @Override
    public reviewViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_review_list, viewGroup, false);
        return new reviewViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(reviewViewHolder reviewViewHolder, int i) {

        Review review = reviewList.get(i);

        if (showRole.equals("Company")) {

            reviewViewHolder.tvUserName.setText(review.getCompany().getName());
            if (review.getCompany().getImg() != null && !review.getCompany().getImg().equals("")
                    && !review.getCompany().getImg().equals("null")) {
                byte[] decodedString = Base64.decode(review.getCompany().getImg(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory
                        .decodeByteArray(decodedString, 0, decodedString.length);
                reviewViewHolder.imgProfile.setImageBitmap(decodedByte);
            }
        } else if (showRole.equals("Jobseeker")) {

            reviewViewHolder.tvUserName.setText(review.getJobseeker().getName());
            if (review.getJobseeker().getImg() != null && !review.getJobseeker().getImg().equals("")
                    && !review.getJobseeker().getImg().equals("null")) {
                byte[] decodedString = Base64.decode(review.getJobseeker().getImg(), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory
                        .decodeByteArray(decodedString, 0, decodedString.length);
                reviewViewHolder.imgProfile.setImageBitmap(decodedByte);
            }
        }
        reviewViewHolder.tvDate.setText(new SimpleDateFormat("yyyy-MM-dd",
                Locale.ENGLISH).format(review.getDate()));
        reviewViewHolder.tvTag.setText(review.getTag());
        reviewViewHolder.tvComment.setText(review.getComment());
        reviewViewHolder.ratingBar.setRating((float) review.getStarValue());
    }

    @Override
    public int getItemCount() {
        return reviewList.size();
    }

    public void clear() {

        int size = reviewList.size();
        reviewList.clear();
        notifyItemRangeRemoved(0, size);
    }

    class reviewViewHolder extends RecyclerView.ViewHolder {

        TextView tvUserName, tvDate, tvTag, tvComment;
        ImageView imgProfile;
        RatingBar ratingBar;

        reviewViewHolder(View view) {

            super(view);
            tvUserName = (TextView) view.findViewById(R.id.tv_user_name);
            tvDate = (TextView) view.findViewById(R.id.tv_review_date);
            tvTag = (TextView) view.findViewById(R.id.tv_review_tag);
            tvComment = (TextView) view.findViewById(R.id.tv_review_comment);
            imgProfile = (ImageView) view.findViewById(R.id.img_profile);
            ratingBar = (RatingBar) view.findViewById(R.id.rate_bar);
        }
    }
}
