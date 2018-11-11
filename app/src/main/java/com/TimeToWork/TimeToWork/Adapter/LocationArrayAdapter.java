package com.TimeToWork.TimeToWork.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.TimeToWork.TimeToWork.Database.Entity.JobLocation;
import com.TimeToWork.TimeToWork.R;

import java.util.List;

/*
 * Created by MelvinTanChunKeat on 3/8/2018.
 */

public class LocationArrayAdapter extends ArrayAdapter<JobLocation> {

    private Context context;
    private int resource;

    public LocationArrayAdapter(Context context, int resource, List<JobLocation> jobLocationList) {

        super(context, resource, jobLocationList);
        this.context = context;
        this.resource = resource;
    }

    @SuppressLint("ViewHolder")
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        JobLocation jobLocation = getItem(position);

        if (jobLocation != null) {
            TextView locationList = (TextView) convertView.findViewById(R.id.textView1);
            locationList.setText(jobLocation.getName());
        }
        return convertView;
    }
}
