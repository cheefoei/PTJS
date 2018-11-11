package com.TimeToWork.TimeToWork.NavigationFragment;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.TimeToWork.TimeToWork.ChangePasswordActivity;
import com.TimeToWork.TimeToWork.Company.CompanyDetailActivity;
import com.TimeToWork.TimeToWork.Company.CompanyReportActivity;
import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.Database.Control.MaintainCompany;
import com.TimeToWork.TimeToWork.Database.Entity.Company;
import com.TimeToWork.TimeToWork.R;

import static com.TimeToWork.TimeToWork.MainApplication.clearAppData;
import static com.TimeToWork.TimeToWork.MainApplication.userId;

public class CompanyProfileFragment extends Fragment {

    private CustomProgressDialog mProgressDialog;
    private Handler handler;

    private TextView txtViewName, ratingValue;
    private ImageView imageView;
    private RatingBar ratingBar;

    private Company company;

    private MaintainCompany maintainCompany = new MaintainCompany();

    public CompanyProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getActivity().setTitle(getString(R.string.fragment_profile));

        // Enable menu in toolbar
        setHasOptionsMenu(true);

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile_company, container, false);

        mProgressDialog = new CustomProgressDialog(getActivity());
        handler = new Handler();

        txtViewName = (TextView) view.findViewById(R.id.name);
        ratingValue = (TextView) view.findViewById(R.id.tv_company_rating);
        ratingBar = (RatingBar) view.findViewById(R.id.rate_bar_company);
        imageView = (ImageView) view.findViewById(R.id.imgUser);

        TextView showCompanyDetails = (TextView) view.findViewById(R.id.cd);
        showCompanyDetails.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), CompanyDetailActivity.class);
                startActivity(myIntent);
            }
        });

        TextView changePassword = (TextView) view.findViewById(R.id.cp);
        changePassword.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(myIntent);
            }
        });

        TextView report = (TextView) view.findViewById(R.id.report);
        report.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), CompanyReportActivity.class);
                startActivity(myIntent);
            }
        });

        if (userId != null) {
            // Get profile detail
            getCompanyProfile();
        }

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.menu_profile, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.logout) {

            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                    .setTitle("Confirmation")
                    .setMessage("Confirm to logout?")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            clearAppData(getActivity());

                            AlertDialog.Builder builder
                                    = new AlertDialog.Builder(getActivity())
                                    .setTitle("Successful")
                                    .setMessage("You already logout.")
                                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            getActivity().recreate();
                                        }
                                    });
                            builder.show();
                        }
                    })
                    .setNegativeButton("Cancel", null);
            builder.show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getCompanyProfile() {

        //Show progress dialog
        mProgressDialog.setMessage("Loading …");
        mProgressDialog.show();

        new Thread(new Runnable() {

            @Override
            public void run() {

                company = maintainCompany.getCompanyDetail(userId);

                handler.post(new Runnable() {

                    @Override
                    public void run() {

                        if (company != null) {

                            txtViewName.setText(company.getName());
                            ratingBar.setRating((float) company.getRating());
                            ratingValue.setText(String.valueOf(company.getRating()));

                            if (company.getImg() != null && !company.getImg().equals("")
                                    && !company.getImg().equals("null")) {
                                byte[] decodedString = Base64.decode(company.getImg(), Base64.DEFAULT);
                                Bitmap decodedByte = BitmapFactory
                                        .decodeByteArray(decodedString, 0, decodedString.length);
                                imageView.setImageBitmap(decodedByte);
                            }
                        }
                        mProgressDialog.dismiss();
                    }
                });
            }
        }).start();
    }
}
