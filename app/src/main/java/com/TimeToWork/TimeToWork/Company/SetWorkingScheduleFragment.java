package com.TimeToWork.TimeToWork.Company;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import com.TimeToWork.TimeToWork.Adapter.WorkingDateAdapter;
import com.TimeToWork.TimeToWork.Database.Entity.WorkingSchedule;
import com.TimeToWork.TimeToWork.Database.Entity.WorkingTime;
import com.TimeToWork.TimeToWork.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SetWorkingScheduleFragment extends DialogFragment {

    private EditText etStartWorkingTime, etEndWorkingTime,
            etStartBreakTime, etEndBreakTime;

    private WorkingDateAdapter adapter;
    private List<Date> workingDateList = new ArrayList<>();
    private WorkingTime workingTime;

    private DatePickerDialog datePickerDialog;

    private Calendar calendar = Calendar.getInstance();

    private OnCallbackReceived mCallback;

    public SetWorkingScheduleFragment() {
    }

    public static SetWorkingScheduleFragment newSetWorkingScheduleFragment() {
        return new SetWorkingScheduleFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_set_working_schedule, container, false);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.baseline_close_white_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        toolbar.setTitle("Set Working Schedule");

        etStartWorkingTime = (EditText) rootView.findViewById(R.id.et_start_working_time);
        etStartWorkingTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showTimePicker(etStartWorkingTime);
            }
        });

        etEndWorkingTime = (EditText) rootView.findViewById(R.id.et_end_working_time);
        etEndWorkingTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showTimePicker(etEndWorkingTime);
            }
        });

        etStartBreakTime = (EditText) rootView.findViewById(R.id.et_start_break_time);
        etStartBreakTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showTimePicker(etStartBreakTime);
            }
        });

        etEndBreakTime = (EditText) rootView.findViewById(R.id.et_end_break_time);
        etEndBreakTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showTimePicker(etEndBreakTime);
            }
        });

        // Get Current Date
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        try {
                            Date date = new SimpleDateFormat("dd MM yyyy", Locale.ENGLISH)
                                    .parse((dayOfMonth + " " + (monthOfYear + 1) + " " + year));

                            if (workingDateList.contains(date)) {

                                AlertDialog.Builder builder
                                        = new AlertDialog.Builder(getActivity(), R.style.DialogStyle)
                                        .setTitle("Error")
                                        .setMessage("This date already added.")
                                        .setPositiveButton("OK", null);
                                builder.show();
                            } else {

                                workingDateList.add(date);
                                Collections.sort(workingDateList);
                                adapter.notifyDataSetChanged();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        Button btnAdd = (Button) rootView.findViewById(R.id.btn_add_date);
        btnAdd.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });

        adapter = new WorkingDateAdapter(workingDateList);

        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);

        RecyclerView mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rv_working_date);
        mRecyclerView.setLayoutManager(llm);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);

        Button btnDone = (Button) rootView.findViewById(R.id.btn_done_schedule);
        btnDone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                workingTime = new WorkingTime();
                workingTime.setStartWorkingTime(etStartWorkingTime.getText().toString());
                workingTime.setEndWorkingTime(etEndWorkingTime.getText().toString());
                workingTime.setStartBreakTime1(etStartBreakTime.getText().toString());
                workingTime.setEndBreakTime1(etEndBreakTime.getText().toString());

                if (isValid()) {
                    mCallback.updateWorkingSchedule(workingDateList, workingTime);
                    dismiss();
                }
            }
        });

        WorkingSchedule workingSchedule = (WorkingSchedule) getArguments().getSerializable("SCHEDULE");
        if (workingSchedule != null) {

            workingTime = workingSchedule.getWorkingTime();
            if (workingTime != null) {
                etStartWorkingTime.setText(workingTime.getStartWorkingTime());
                etEndWorkingTime.setText(workingTime.getEndWorkingTime());
                etStartBreakTime.setText(workingTime.getStartBreakTime1());
                etEndBreakTime.setText(workingTime.getEndBreakTime1());
            }
            if (workingSchedule.getWorkingDateList() != null) {
                workingDateList.addAll(workingSchedule.getWorkingDateList());
            }
        }
        adapter.notifyDataSetChanged();

        return rootView;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onStart() {

        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        try {
            mCallback = (OnCallbackReceived) context;
        } catch (ClassCastException ignored) {
        }
    }

    @Override
    public void onDetach() {

        mCallback = null;
        super.onDetach();
    }

    private void showTimePicker(final EditText editText) {

        // Get Current Time
        int mHour = calendar.get(Calendar.HOUR_OF_DAY);
        int mMinute = calendar.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        String hour;
                        String min;

                        if (hourOfDay == 0) {
                            hour = "00";
                        } else if (hourOfDay < 10) {
                            hour = "0" + hourOfDay;
                        } else {
                            hour = Integer.toString(hourOfDay);
                        }
                        if (minute == 0) {
                            min = "00";
                        } else if (minute < 10) {
                            min = "0" + minute;
                        } else {
                            min = Integer.toString(minute);
                        }
                        editText.setText(hour + ":" + min);
                        editText.clearFocus();
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    private boolean isValid() {

        boolean isValid = true;
        String errorMessage = "";

        if (etStartWorkingTime.getText().toString().equals("") ||
                etEndWorkingTime.getText().toString().equals("") ||
                etStartBreakTime.getText().toString().equals("") ||
                etEndBreakTime.getText().toString().equals("")) {
            errorMessage += "Please fill up time. \n";
            isValid = false;
        }

        if (workingDateList.isEmpty()) {
            errorMessage += "Please select a working date. \n";
            isValid = false;
        }

        if (!isValid) {
            AlertDialog.Builder builder
                    = new AlertDialog.Builder(getActivity(), R.style.DialogStyle)
                    .setTitle("Error")
                    .setMessage(errorMessage)
                    .setPositiveButton("OK", null);
            builder.show();
        }

        return isValid;
    }

    interface OnCallbackReceived {

        void updateWorkingSchedule(List<Date> workingDates,
                                   WorkingTime workingTime);
    }
}
