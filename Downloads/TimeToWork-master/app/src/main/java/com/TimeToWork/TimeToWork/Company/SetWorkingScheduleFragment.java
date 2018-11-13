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
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import java.util.concurrent.TimeUnit;

public class SetWorkingScheduleFragment extends DialogFragment {

    private EditText etStartWorkingTime, etEndWorkingTime,
            etStartFirstBreakTime, etEndFirstBreakTime,
            etStartSecondBreakTime, etEndSecondBreakTime;
    private Spinner spinnerWorkingHour, spinnerBreakDuration;
    private LinearLayout layoutBreak;

    private List<Date> workingDateList = new ArrayList<>();
    private WorkingDateAdapter adapter;
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

        layoutBreak = (LinearLayout) rootView.findViewById(R.id.layout_break);

        spinnerWorkingHour = (Spinner) rootView.findViewById(R.id.spinner_working_hour);
        spinnerWorkingHour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (!etStartWorkingTime.getText().toString().isEmpty()) {
                    int workingHours = position + 1;
                    int startWorkingHour = Integer.parseInt(
                            etStartWorkingTime.getText().toString().substring(0, 2));
                    int startWorkingMinute = Integer.parseInt(
                            etStartWorkingTime.getText().toString().substring(3, 5));

                    etEndWorkingTime.setText(formattedTime(
                            startWorkingHour + workingHours, startWorkingMinute));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spinnerBreakDuration = (Spinner) rootView.findViewById(R.id.spinner_break_duration);
        spinnerBreakDuration.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    layoutBreak.setVisibility(View.GONE);
                } else {

                    int minute = Integer.parseInt(
                            spinnerBreakDuration.getItemAtPosition(position).toString().substring(0, 2));

                    if (!etStartFirstBreakTime.getText().toString().isEmpty()) {

                        int startFirstBreakHour = Integer.parseInt(
                                etStartFirstBreakTime.getText().toString().substring(0, 2));
                        int startFirstBreakMinute = Integer.parseInt(
                                etStartFirstBreakTime.getText().toString().substring(3, 5));
                        etEndFirstBreakTime.setText(formattedTime(
                                startFirstBreakHour, startFirstBreakMinute + minute));
                    }
                    if (!etStartSecondBreakTime.getText().toString().isEmpty()) {

                        int startSecondBreakHour = Integer.parseInt(
                                etStartSecondBreakTime.getText().toString().substring(0, 2));
                        int startSecondBreakMinute = Integer.parseInt(
                                etStartSecondBreakTime.getText().toString().substring(3, 5));
                        etEndSecondBreakTime.setText(formattedTime(
                                startSecondBreakHour, startSecondBreakMinute + minute));
                    }
                    layoutBreak.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        etStartWorkingTime = (EditText) rootView.findViewById(R.id.et_start_working_time);
        etStartWorkingTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showTimePicker(etStartWorkingTime);
            }
        });

        etStartFirstBreakTime = (EditText) rootView.findViewById(R.id.et_start_first_break_time);
        etStartFirstBreakTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showTimePicker(etStartFirstBreakTime);
            }
        });

        etStartSecondBreakTime = (EditText) rootView.findViewById(R.id.et_start_second_break_time);
        etStartSecondBreakTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showTimePicker(etStartSecondBreakTime);
            }
        });

        etEndWorkingTime = (EditText) rootView.findViewById(R.id.et_end_working_time);
        etEndFirstBreakTime = (EditText) rootView.findViewById(R.id.et_end_first_break_time);
        etEndSecondBreakTime = (EditText) rootView.findViewById(R.id.et_end_second_break_time);

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

                if (spinnerBreakDuration.getSelectedItemPosition() != 0) {
                    workingTime.setStartFirstBreakTime(etStartFirstBreakTime.getText().toString());
                    workingTime.setEndFirstBreakTime(etEndFirstBreakTime.getText().toString());
                    if (!etStartSecondBreakTime.getText().toString().isEmpty()) {
                        workingTime.setStartSecondBreakTime(etStartSecondBreakTime.getText().toString());
                        workingTime.setEndSecondBreakTime(etEndSecondBreakTime.getText().toString());
                    } else {
                        workingTime.setStartSecondBreakTime(null);
                        workingTime.setEndSecondBreakTime(null);
                    }
                } else {
                    workingTime.setStartFirstBreakTime(null);
                    workingTime.setEndFirstBreakTime(null);
                    workingTime.setStartSecondBreakTime(null);
                    workingTime.setEndSecondBreakTime(null);
                }

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

                int startWorkingHour = Integer.parseInt(
                        workingTime.getStartWorkingTime().substring(0, 2));
                int endWorkingHour = Integer.parseInt(
                        workingTime.getEndWorkingTime().substring(0, 2));

                if (endWorkingHour < startWorkingHour) {
                    endWorkingHour += 24;
                }
                int workingHours = endWorkingHour - startWorkingHour;
                spinnerWorkingHour.setSelection(workingHours - 1);

                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm", Locale.ENGLISH);
                try {

                    String startFirstBreak = workingTime.getStartFirstBreakTime();
                    String endFirstBreak = workingTime.getEndFirstBreakTime();
                    String startSecondBreak = workingTime.getStartSecondBreakTime();
                    String endSecondBreak = workingTime.getEndSecondBreakTime();

                    if (startFirstBreak != null && endFirstBreak != null) {

                        etStartFirstBreakTime.setText(startFirstBreak);
                        etEndFirstBreakTime.setText(endFirstBreak);

                        if (startSecondBreak != null && endSecondBreak != null) {
                            etStartSecondBreakTime.setText(startSecondBreak);
                            etEndSecondBreakTime.setText(endSecondBreak);
                        }

                        Date startFirstBreakTime = format.parse("01-01-1970 " + startFirstBreak);
                        Date endFirstBreakTime;

                        int startFirstBreakHour = Integer.parseInt(startFirstBreak.substring(0, 2));
                        int endFirstBreakHour = Integer.parseInt(endFirstBreak.substring(0, 2));

                        if (endFirstBreakHour < startFirstBreakHour) {
                            endFirstBreakTime = format.parse("02-01-1970 " + endFirstBreak);
                        } else {
                            endFirstBreakTime = format.parse("01-01-1970 " + endFirstBreak);
                        }

                        long breakDuration = endFirstBreakTime.getTime() - startFirstBreakTime.getTime();
                        long minute = TimeUnit.MINUTES.convert(breakDuration, TimeUnit.MILLISECONDS);

                        if (minute == 30) {
                            spinnerBreakDuration.setSelection(1);
                        } else if (minute == 45) {
                            spinnerBreakDuration.setSelection(2);
                        } else if (minute == 60) {
                            spinnerBreakDuration.setSelection(3);
                        } else if (minute == 75) {
                            spinnerBreakDuration.setSelection(4);
                        } else if (minute == 90) {
                            spinnerBreakDuration.setSelection(5);
                        }
                    } else {
                        spinnerBreakDuration.setSelection(0);
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
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

                        editText.setText(formattedTime(hourOfDay, minute));
                        editText.clearFocus();

                        if (editText == etStartWorkingTime) {
                            int workingHours = spinnerWorkingHour.getSelectedItemPosition() + 1;
                            etEndWorkingTime.setText(formattedTime(hourOfDay + workingHours, minute));
                        } else if (editText == etStartFirstBreakTime) {
                            int duration = 0;
                            if (spinnerBreakDuration.getSelectedItemPosition() != 0) {
                                duration = Integer.parseInt(
                                        spinnerBreakDuration.getSelectedItem().toString().substring(0, 2));
                            }
                            etEndFirstBreakTime.setText(formattedTime(hourOfDay, minute + duration));
                        } else if (editText == etStartSecondBreakTime) {
                            int duration = 0;
                            if (spinnerBreakDuration.getSelectedItemPosition() != 0) {
                                duration = Integer.parseInt(
                                        spinnerBreakDuration.getSelectedItem().toString().substring(0, 2));
                            }
                            etEndSecondBreakTime.setText(formattedTime(hourOfDay, minute + duration));
                        }
                    }
                }, mHour, mMinute, true);
        timePickerDialog.show();
    }

    private boolean isValid() {

        boolean isValid = true;
        String errorMessage = "";

        if (etStartWorkingTime.getText().toString().equals("") ||
                etEndWorkingTime.getText().toString().equals("")) {
            errorMessage += "Please fill up working time. \n";
            isValid = false;
        }

        if (spinnerBreakDuration.getSelectedItemPosition() != 0) {
            if (etStartFirstBreakTime.getText().toString().equals("") ||
                    etEndFirstBreakTime.getText().toString().equals("")) {
                errorMessage += "Please fill up break time. \n";
                isValid = false;
            }
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

    private String formattedTime(int hourOfDay, int minute) {

        String hour;
        String min;

        if (minute > 59 && minute < 120) {
            minute -= 60;
            hourOfDay++;
        } else if (minute >= 120) {
            minute -= 120;
            hourOfDay += 2;
        }
        if (hourOfDay > 23) {
            hourOfDay -= 24;
        }

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
        return hour + ":" + min;
    }

    interface OnCallbackReceived {

        void updateWorkingSchedule(List<Date> workingDates,
                                   WorkingTime workingTime);
    }
}
