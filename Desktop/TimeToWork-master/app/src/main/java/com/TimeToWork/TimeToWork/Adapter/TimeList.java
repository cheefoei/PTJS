package com.TimeToWork.TimeToWork.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.TimeToWork.TimeToWork.R;
import java.util.ArrayList;
import com.TimeToWork.TimeToWork.Entity.Date;

/**
 * Created by MelvinTanChunKeat on 3/8/2018.
 */

public class TimeList extends ArrayAdapter<Date> {

    private Context context;
    int resource;

    public TimeList(Context context, int resource, ArrayList<Date> objects){
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        String timeFrom = getItem(position).getTimeFrom();
        String timeTo = getItem(position).getTimeTo();

        String time = timeFrom + " ~ " + timeTo;

        Date listTime = new Date(time);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView timeList = (TextView) convertView.findViewById(R.id.textView1);
        timeList.setText(time);

        return convertView;
    }
}
