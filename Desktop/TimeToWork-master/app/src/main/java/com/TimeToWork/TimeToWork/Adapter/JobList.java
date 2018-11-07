package com.TimeToWork.TimeToWork.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import com.TimeToWork.TimeToWork.Entity.Job;
import com.TimeToWork.TimeToWork.R;

/**
 * Created by MelvinTanChunKeat on 3/8/2018.
 */

public class JobList extends ArrayAdapter<Job> {

    private Context context;
    int resource;

    public  JobList(Context context, int resource, ArrayList<Job> objects){
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        String job = getItem(position).getJob();

        Job listJob = new Job(job);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView jobList = (TextView) convertView.findViewById(R.id.textView1);
        jobList.setText(job);

        return convertView;
    }
}
