package com.TimeToWork.TimeToWork.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.TimeToWork.TimeToWork.Database.Entity.Date;
import com.TimeToWork.TimeToWork.R;

import java.util.List;

/*
 * Created by MelvinTanChunKeat on 3/8/2018.
 */

public class TimeArrayAdapter extends ArrayAdapter<Date> {

    private Context context;
    private int resource;

    public TimeArrayAdapter(Context context, int resource, List<Date> dateList) {

        super(context, resource, dateList);
        this.context = context;
        this.resource = resource;
    }

    @SuppressLint("ViewHolder")
    @Override
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        Date date = getItem(position);

        if (date != null) {
            String timeFrom = date.getTimeFrom();
            String timeTo = date.getTimeTo();

            String time = timeFrom + " ~ " + timeTo;

//        Date listTime = new Date(time);

            TextView timeList = (TextView) convertView.findViewById(R.id.textView1);
            timeList.setText(time);
        }
        return convertView;
    }
}
