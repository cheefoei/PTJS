package com.TimeToWork.TimeToWork.Company;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.TimeToWork.TimeToWork.Database.Entity.Report;
import com.TimeToWork.TimeToWork.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MelvinTanChunKeat on 15/11/2018.
 */

public class PaymentListAdapter extends ArrayAdapter<Report> {

    private LayoutInflater mInflater;
    private ArrayList<Report> reports;
    private int mViewResourceId;
    List<Report> reportList = new ArrayList<>();

    public PaymentListAdapter(Context context, int textViewResourceId, ArrayList<Report> reports) {
        super(context, textViewResourceId, reports);
        this.reports = reports;
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mViewResourceId = textViewResourceId;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = mInflater.inflate(mViewResourceId, null);

        Report report = reports.get(position);

        if (report != null) {
            TextView no = (TextView) convertView.findViewById(R.id.no);
            TextView name = (TextView) convertView.findViewById(R.id.name);
            TextView job = (TextView) convertView.findViewById(R.id.job);
            TextView date = (TextView) convertView.findViewById(R.id.date);
            TextView salary = (TextView) convertView.findViewById(R.id.salary);
            if (no != null) {
                no.setText(String.valueOf(position+1));
            }

            if (job != null) {
                job.setText((report.getJob()));
            }
            if (date != null) {
                date.setText((report.getDate()));
            }
            if (salary != null) {
                salary.setText(String.valueOf(report.getSalary()));
            }

        }

        return convertView;
    }
}