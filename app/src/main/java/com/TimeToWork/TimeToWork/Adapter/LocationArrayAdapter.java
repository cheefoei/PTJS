package com.TimeToWork.TimeToWork.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.TimeToWork.TimeToWork.Database.Entity.JobLocation;

import java.util.List;

/**
 * Created by MelvinTanChunKeat on 3/8/2018.
 */

public class LocationArrayAdapter extends ArrayAdapter<JobLocation> {

    private Context context;
    int resource;

    public LocationArrayAdapter(Context context, int resource, List<JobLocation> jobLocationList){

        super(context, resource, jobLocationList);
        this.context = context;
        this.resource = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent){

//        JobLocation jobLocation = getItem(position);
//
//        LayoutInflater inflater = LayoutInflater.from(context);
//        convertView = inflater.inflate(resource, parent, false);
//
//        TextView locationList = (TextView) convertView.findViewById(R.id.textView1);
//        locationList.setText(jobLocation.getName());

        return convertView;
    }
}
