package com.TimeToWork.TimeToWork;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import com.TimeToWork.TimeToWork.Control.MaintainCompany;
import com.TimeToWork.TimeToWork.Entity.Company;

public class CompanyProfilePage extends AppCompatActivity {

    private TextView txtViewName;
    private ImageView imageView;
    private DatabaseReference databaseRef;
    private ArrayList<Company> companyArrayList;
    private MaintainCompany MCompany = new MaintainCompany();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_profile_page);
        txtViewName = (TextView) findViewById(R.id.name);
        imageView = (ImageView) findViewById(R.id.imgUser);
        databaseRef = FirebaseDatabase.getInstance().getReference("company");

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = (String) dataSnapshot.child("J10001").child("jobseeker_img").getValue();
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

        getPartTimerDetails();


        TextView showCompanyDetails = (TextView) findViewById(R.id.cd);
        showCompanyDetails.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent myIntent = new Intent(CompanyProfilePage.this, CompanyDetails.class);
                startActivity(myIntent);
            }
        });


        TextView changePassword = (TextView) findViewById(R.id.cp);
        changePassword.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent myIntent = new Intent(CompanyProfilePage.this, ChangePasswordPage.class);
                startActivity(myIntent);
            }
        });

        TextView report = (TextView) findViewById(R.id.report);
        report.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                Intent myIntent = new Intent(CompanyProfilePage.this, CompanyReport.class);
                startActivity(myIntent);
            }
        });
    }

    private void getPartTimerDetails() {

        companyArrayList = MCompany.getCompanyDetails("C10001");

        for (int i = 0; i < companyArrayList.size(); i++) {

            txtViewName.setText(companyArrayList.get(i).getCompany_name());
        }
    }
}
