package com.TimeToWork.TimeToWork.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.TimeToWork.TimeToWork.Database.Entity.Date;

import java.util.List;

/**
 * Created by MelvinTanChunKeat on 3/8/2018.
 */

public class TimeArrayAdapter extends ArrayAdapter<Date> {

    private Context context;
    private int resource;

    public TimeArrayAdapter(Context context, int resource, List<Date> dateList){

        super(context, resource, dateList);
        this.context = context;
        this.resource = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent){

//        String timeFrom = getItem(position).getTimeFrom();
//        String timeTo = getItem(position).getTimeTo();
//
//        String time = timeFrom + " ~ " + timeTo;
//
//        Date listTime = new Date(time);
//
//        LayoutInflater inflater = LayoutInflater.from(context);
//        convertView = inflater.inflate(resource, parent, false);
//
//        TextView timeList = (TextView) convertView.findViewById(R.id.textView1);
//        timeList.setText(time);

        return convertView;
    }
}
