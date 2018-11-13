package com.TimeToWork.TimeToWork.Company;

import android.app.Dialog;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;

import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.Database.Entity.JobLocation;
import com.TimeToWork.TimeToWork.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class OwnLocationFragment extends DialogFragment {

    private EditText etLocationName, etAddress;

    private JobLocation jobLocation = new JobLocation();

    private OnCallbackReceived mCallback;

    public OwnLocationFragment() {
    }

    public static OwnLocationFragment newOwnLocationFragment() {
        return new OwnLocationFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_own_location, container, false);

        Toolbar toolbar = (Toolbar) rootView.findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.baseline_close_white_24);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        toolbar.setTitle("Use My Location");

        etAddress = (EditText) rootView.findViewById(R.id.et_address);
        etLocationName = (EditText) rootView.findViewById(R.id.et_location_name);
        etLocationName.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {

                if (!hasFocus) {

                    CustomProgressDialog mProgressDialog = new CustomProgressDialog(getActivity());
                    mProgressDialog.toggleProgressDialog();

                    String locationName = etLocationName.getText().toString();
                    Geocoder geoCoder = new Geocoder(getActivity(), Locale.getDefault());
                    try {
                        List<Address> addressList = geoCoder.getFromLocationName(locationName, 1);

                        if (!addressList.isEmpty()) {
                            double latitude = (addressList.get(0).getLatitude());
                            double longitude = (addressList.get(0).getLongitude());
                            String address = addressList.get(0).getAddressLine(0);

                            jobLocation.setName(locationName);
                            jobLocation.setAddress(address);
                            jobLocation.setLatitude(latitude);
                            jobLocation.setLongitude(longitude);

                            etAddress.setText(jobLocation.getAddress());
                        } else {
                            etAddress.setText(null);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    mProgressDialog.toggleProgressDialog();
                }
            }
        });

        Button btnDone = (Button) rootView.findViewById(R.id.btn_done_location);
        btnDone.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                mCallback.updateLocation(jobLocation);
                dismiss();
            }
        });


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

    interface OnCallbackReceived {

        void updateLocation(JobLocation jobLocation);
    }
}
