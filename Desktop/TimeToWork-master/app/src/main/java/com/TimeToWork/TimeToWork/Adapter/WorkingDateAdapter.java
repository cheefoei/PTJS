package com.TimeToWork.TimeToWork.Adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.TimeToWork.TimeToWork.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class WorkingDateAdapter extends RecyclerView.Adapter<WorkingDateAdapter.WorkingDateViewHolder> {

    private List<Date> workingDates;

    public WorkingDateAdapter(List<Date> workingDates) {
        this.workingDates = workingDates;
    }

    @Override
    public WorkingDateViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.recycler_view_working_date, viewGroup, false);
        return new WorkingDateViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(WorkingDateViewHolder workingDateViewHolder, int i) {

        final int index = i;
        Date date = workingDates.get(i);

        workingDateViewHolder.workingDate.setText(
                new SimpleDateFormat("EEEE, dd MMMM yyyy", Locale.ENGLISH).format(date));

        workingDateViewHolder.btnRemove.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                workingDates.remove(index);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return workingDates.size();
    }


    class WorkingDateViewHolder extends RecyclerView.ViewHolder {

        TextView workingDate;
        Button btnRemove;

        WorkingDateViewHolder(View view) {

            super(view);
            workingDate = (TextView) view.findViewById(R.id.tv_working_date);
            btnRemove = (Button) view.findViewById(R.id.btn_remove_date);
        }
    }
}
