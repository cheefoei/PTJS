package com.TimeToWork.TimeToWork.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import com.TimeToWork.TimeToWork.R;
import java.util.ArrayList;
import com.TimeToWork.TimeToWork.Entity.Location;

/**
 * Created by MelvinTanChunKeat on 3/8/2018.
 */

public class LocationList extends ArrayAdapter<Location> {

    private Context context;
    int resource;

    public LocationList(Context context, int resource, ArrayList<Location> objects){
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        String location = getItem(position).getLocation();

        Location listLocation = new Location(location);

        LayoutInflater inflater = LayoutInflater.from(context);
        convertView = inflater.inflate(resource, parent, false);

        TextView locationList = (TextView) convertView.findViewById(R.id.textView1);
        locationList.setText(location);

        return convertView;
    }
}
