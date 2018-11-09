package com.TimeToWork.TimeToWork.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.TimeToWork.TimeToWork.Database.Entity.JobPost;

import java.util.List;


/**
 * Created by MelvinTanChunKeat on 3/8/2018.
 */

public class JobArrayAdapter extends ArrayAdapter<JobPost> {

    private Context context;
    private int resource;

    public JobArrayAdapter(Context context, int resource, List<JobPost> jobPostList){

        super(context, resource, jobPostList);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent){

//        convertView = LayoutInflater.from(context).inflate(resource, parent, false);
//
//        JobPost jobPost = getItem(position);
//
//        TextView jobList = (TextView) convertView.findViewById(R.id.textView1);
//        jobList.setText(jobPost.getTitle());

        return convertView;
    }
}
