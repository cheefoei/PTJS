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
import com.TimeToWork.TimeToWork.Company.CompanyMyReviewActivity;
import com.TimeToWork.TimeToWork.Company.CompanyReportActivity;
import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.CustomClass.CustomVolleyErrorListener;
import com.TimeToWork.TimeToWork.Database.CompanyDA;
import com.TimeToWork.TimeToWork.Database.Entity.Company;
import com.TimeToWork.TimeToWork.R;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.TimeToWork.TimeToWork.MainApplication.clearAppData;
import static com.TimeToWork.TimeToWork.MainApplication.mRequestQueue;
import static com.TimeToWork.TimeToWork.MainApplication.root;
import static com.TimeToWork.TimeToWork.MainApplication.userId;

public class CompanyProfileFragment extends Fragment {

    private CustomProgressDialog mProgressDialog;
    private Handler handler;

    private TextView txtViewName, ratingValue;
    private ImageView imageView;
    private RatingBar ratingBar;

    private Company company;

    private DatabaseReference databaseRef;

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
        databaseRef = FirebaseDatabase.getInstance().getReference("company");

        TextView showCompanyDetails = (TextView) view.findViewById(R.id.cd);
        showCompanyDetails.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), CompanyDetailActivity.class);
                startActivity(myIntent);
            }
        });

        TextView myCompanyReview = (TextView) view.findViewById(R.id.mcr);
        myCompanyReview.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent myIntent = new Intent(getActivity(), CompanyMyReviewActivity.class);
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
            databaseRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    String value = (String) dataSnapshot.child(userId).child("company_img").getValue();
                    //decode base64 string to image
                    byte[] imageBytes;
                    imageBytes = Base64.decode(value, Base64.DEFAULT);
                    Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    imageView.setImageBitmap(decodedImage);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

            getCompanyProfile();
        }

        return view;
    }

    @Override
    public void onResume() {

        if (userId != null && company != null) {
            syncCompanyData();
        }
        super.onResume();
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

                //Reading company data
                CompanyDA companyDA = new CompanyDA(getActivity());
                company = companyDA.getCompanyData();
                //Close company database
                companyDA.close();
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

    private void syncCompanyData() {

        //Show progress dialog
        mProgressDialog.setMessage("Loading …");
        mProgressDialog.show();

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    if (success) {

                        //Change company JSON to Array
                        JSONObject companyObject = jsonResponse.getJSONObject("COMPANY");
                        //Create company object
                        Company c = new Company();
                        c.setId(companyObject.getString("company_id"));
                        c.setName(companyObject.getString("company_name"));
                        c.setAddress(companyObject.getString("company_address"));
                        c.setPhone_number(companyObject.getString("company_phone_number"));
                        c.setEmail(companyObject.getString("company_email"));
                        c.setRating(companyObject.getDouble("company_rating"));
                        c.setImg(companyObject.getString("company_img"));

                        //Update jobseeker data
                        CompanyDA companyDA = new CompanyDA(getActivity());
                        companyDA.updateCompanyData(c);
                        //Closing sqlite database
                        companyDA.close();

                        txtViewName.setText(c.getName());
                        if (c.getImg() != null && !c.getImg().equals("") && !c.getImg().equals("null")) {
                            byte[] decodedString = Base64.decode(c.getImg(), Base64.DEFAULT);
                            Bitmap decodedByte = BitmapFactory
                                    .decodeByteArray(decodedString, 0, decodedString.length);
                            imageView.setImageBitmap(decodedByte);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mProgressDialog.dismiss();
            }
        };

        CustomVolleyErrorListener errorListener
                = new CustomVolleyErrorListener(getActivity(), mProgressDialog, mRequestQueue);
        GetCompanyRequest getCompanyRequest = new GetCompanyRequest(
                root + getString(R.string.url_get_company),
                responseListener,
                errorListener
        );
        mRequestQueue.add(getCompanyRequest);
    }

    private class GetCompanyRequest extends StringRequest {

        private Map<String, String> params;

        GetCompanyRequest(String url,
                          Response.Listener<String> listener,
                          Response.ErrorListener errorListener) {
            super(Method.POST, url, listener, errorListener);

            params = new HashMap<>();
            params.put("id", userId);
        }

        public Map<String, String> getParams() {
            return params;
        }
    }

}
