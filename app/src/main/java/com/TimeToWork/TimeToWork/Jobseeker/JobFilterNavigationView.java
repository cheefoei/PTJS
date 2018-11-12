package com.TimeToWork.TimeToWork.Jobseeker;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.TimeToWork.TimeToWork.R;

import java.util.Calendar;
import java.util.Locale;

public class JobFilterNavigationView extends Fragment {

    private LinearLayout layoutManualFilter;
    private EditText etStartDate, etEndDate;
    private EditText etStartTime, etEndTime;
    private EditText etLocation;
    private Spinner spinnerPaymentTerm, spinnerCategory;
    private SeekBar seekBarWages;
    private Button btnSubmitFilter, btnClearFilter;

    private Calendar calendar = Calendar.getInstance();
    private DatePickerDialog startDatePickerDialog;
    private DatePickerDialog endDatePickerDialog;
    private TimePickerDialog startTimePickerDialog;
    private TimePickerDialog endTimePickerDialog;

    private String[] option = {"01012018", "31122099", "00:00", "23:59", "", "", "0", "60"};

    private OnFilterOptionChangeListener onFilterOptionChangeListener;

    public JobFilterNavigationView() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.navigation_view_filter, container, false);

        layoutManualFilter = (LinearLayout) view.findViewById(R.id.layout_manual_filter);

        etStartDate = (EditText) view.findViewById(R.id.et_working_date_start);
        etEndDate = (EditText) view.findViewById(R.id.et_working_date_end);

        // Get Current Date
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        startDatePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerListener(etStartDate), mYear, mMonth, mDay);
        startDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        etStartDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startDatePickerDialog.show();
            }
        });

        endDatePickerDialog = new DatePickerDialog(getActivity(),
                new DatePickerListener(etEndDate), mYear, mMonth, mDay);
        endDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

        etEndDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                endDatePickerDialog.show();
            }
        });

        etStartTime = (EditText) view.findViewById(R.id.et_working_time_start);
        etEndTime = (EditText) view.findViewById(R.id.et_working_time_end);

        // Get Current Time
        int mHour = calendar.get(Calendar.HOUR_OF_DAY);
        int mMinute = calendar.get(Calendar.MINUTE);

        startTimePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerListener(etStartTime), mHour, mMinute, true);
        etStartTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                startTimePickerDialog.show();
            }
        });

        endTimePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerListener(etEndTime), mHour, mMinute, true);
        etEndTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                endTimePickerDialog.show();
            }
        });

        etLocation = (EditText) view.findViewById(R.id.et_location_name);
        spinnerPaymentTerm = (Spinner) view.findViewById(R.id.spinner_payment_term);
        spinnerCategory = (Spinner) view.findViewById(R.id.spinner_category);

        final TextView tvWagesValue = (TextView) view.findViewById(R.id.tv_wages_value);

        seekBarWages = (SeekBar) view.findViewById(R.id.seekbar_wages);
        seekBarWages.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvWagesValue.setText(String.format(Locale.ENGLISH, "minimum RM %d", progress));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        btnSubmitFilter = (Button) view.findViewById(R.id.btn_submit_filter);
        btnSubmitFilter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if (!etStartDate.getText().toString().equals("") &&
                        !etEndDate.getText().toString().equals("")) {

                    option[0] = etStartDate.getText().toString();
                    option[1] = etEndDate.getText().toString();
                }

                if (!etStartTime.getText().toString().equals("") &&
                        !etEndTime.getText().toString().equals("")) {

                    option[2] = etStartTime.getText().toString();
                    option[3] = etEndTime.getText().toString();
                }

                option[4] = etLocation.getText().toString();
                option[5] = spinnerCategory.getSelectedItem().toString();

                if (seekBarWages.getProgress() >= 0) {
                    option[6] = String.format(Locale.ENGLISH, "%d", seekBarWages.getProgress());
                }

                String paymentTerm = "0";
                if (spinnerPaymentTerm.getSelectedItemPosition() != 0) {
                    paymentTerm = spinnerPaymentTerm.getSelectedItem().toString().substring(0, 2);
                }
                option[7] = paymentTerm;

                onFilterOptionChangeListener.OnFilterOptionChange(option);
            }
        });

        btnClearFilter = (Button) view.findViewById(R.id.btn_clear_filter);
        btnClearFilter.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                // Set default option
                option[0] = "01012018";
                option[1] = "31122099";
                option[2] = "00:00";
                option[3] = "23:59";
                option[4] = "";
                option[5] = "";
                option[6] = "0";
                option[7] = "60";
                onFilterOptionChangeListener.OnFilterOptionChange(option);
            }
        });

        Switch switchPersonalFilter = (Switch) view.findViewById(R.id.switch_schedule);
        switchPersonalFilter.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    layoutManualFilter.setVisibility(View.GONE);
                    btnClearFilter.setVisibility(View.GONE);
                    btnSubmitFilter.setVisibility(View.GONE);
                } else {
                    layoutManualFilter.setVisibility(View.VISIBLE);
                    btnClearFilter.setVisibility(View.VISIBLE);
                    btnSubmitFilter.setVisibility(View.VISIBLE);
                }
                onFilterOptionChangeListener.OnSwitchPersonalFilterChange(isChecked);
            }
        });

        TextView tvLearnMore = (TextView) view.findViewById(R.id.tv_learn_more);
        tvLearnMore.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder
                        = new AlertDialog.Builder(getActivity())
                        .setTitle("Learn More")
                        .setMessage("TimeToWork provides advanced filter function that based on preferred job category," +
                                " job location and time schedule you set before. ")
                        .setPositiveButton("That's great", null);
                builder.show();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Context context) {

        super.onAttach(context);
        onFilterOptionChangeListener = (OnFilterOptionChangeListener) getParentFragment();
    }

    @Override
    public void onDetach() {

        super.onDetach();
        onFilterOptionChangeListener = null;
    }

    private class DatePickerListener implements DatePickerDialog.OnDateSetListener {

        private EditText editText;

        private DatePickerListener(EditText editText) {
            this.editText = editText;
        }

        @Override
        public void onDateSet(DatePicker view, int year,
                              int monthOfYear, int dayOfMonth) {

            monthOfYear = monthOfYear + 1;
            String dateStr = "";

            if (dayOfMonth < 10) {
                dateStr += "0" + dayOfMonth + "";
            } else {
                dateStr += dayOfMonth + "";
            }
            if (monthOfYear < 10) {
                dateStr += "0" + monthOfYear + "";
            } else {
                dateStr += monthOfYear + "";
            }
            dateStr += year;
            editText.setText(dateStr);
        }
    }

    private class TimePickerListener implements TimePickerDialog.OnTimeSetListener {

        private EditText editText;

        private TimePickerListener(EditText editText) {
            this.editText = editText;
        }

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
    }

    public interface OnFilterOptionChangeListener {

        void OnFilterOptionChange(String option[]);

        void OnSwitchPersonalFilterChange(boolean isChecked);
    }
}
