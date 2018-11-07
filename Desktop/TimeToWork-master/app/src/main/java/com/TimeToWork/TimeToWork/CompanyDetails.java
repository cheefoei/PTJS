package com.TimeToWork.TimeToWork;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import org.apache.commons.io.output.ByteArrayOutputStream;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;

import com.TimeToWork.TimeToWork.Control.MaintainCompany;
import com.TimeToWork.TimeToWork.Entity.Company;

public class CompanyDetails extends AppCompatActivity {

    private MaintainCompany MCompany = new MaintainCompany();
    private EditText editTextName, editTextEmail, editTextPhoneNumber, editTextAddress;
    private Button saveButton;
    private ImageView imageView;
    private DatabaseReference databaseRef;
    private ArrayList<Company> companyArrayList;
    private static final int RESULT_LOAD_IMAGE = 5;
    private String encodedImage;
    private Uri resultImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_details);

        databaseRef = FirebaseDatabase.getInstance().getReference("company");
        imageView = (ImageView) findViewById(R.id.imgUser);
        editTextName = (EditText) findViewById(R.id.name);
        editTextPhoneNumber = (EditText) findViewById(R.id.phoneNumber);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextAddress = (EditText) findViewById(R.id.address);
        saveButton = (Button) findViewById(R.id.save);

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = (String) dataSnapshot.child("C10001").child("company_img").getValue();
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

        getCompanyDetail();

        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                updatePartTimerDetails();
            }
        });

        imageView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void getCompanyDetail()
    {
        companyArrayList = MCompany.getCompanyDetails("C10001");
        for(int i = 0; i < companyArrayList.size(); i++)
        {
            editTextName.setText(companyArrayList.get(i).getCompany_name());
            editTextEmail.setText(companyArrayList.get(i).getCompany_email());
            editTextPhoneNumber.setText(companyArrayList.get(i).getCompany_phone_number());
            editTextAddress.setText(companyArrayList.get(i).getCompany_address());

        }
    }

    private void openGallery(){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent,RESULT_LOAD_IMAGE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) // Crop Image and Convert Image to Basse 64.
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==RESULT_LOAD_IMAGE && resultCode==RESULT_OK && data!=null)
        {
            Uri selectedImage = data.getData();
            CropImage.activity(selectedImage).setGuidelines(CropImageView.Guidelines.ON).start(CompanyDetails.this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE)
        {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if(resultCode == RESULT_OK)
            {
                resultImage = result.getUri();

                Bitmap originBitmap = null;
                InputStream imageStream;

                try{
                    imageStream = getContentResolver().openInputStream(resultImage);
                    originBitmap = BitmapFactory.decodeStream(imageStream);
                } catch (FileNotFoundException e) {
                    Log.e("error here : ", e.getMessage());
                }

                if (originBitmap != null)
                {
                    this.imageView.setImageBitmap(originBitmap);
                    Bitmap image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                }

                imageView.setImageURI(resultImage);

            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error = result.getError();
            }
        }
    }

    private void updatePartTimerDetails() {
        String name = editTextName.getText().toString().trim();
        String phoneNum = editTextPhoneNumber.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();

        Company companyDetail = null;
        try {
            companyDetail = new Company("C10001", name, address, email, phoneNum, null);
            MCompany.updateCompanyDetail(companyDetail);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("company").child("C10001").child("company_img");
            databaseReference.setValue(encodedImage);
        } catch (Exception e) {
            Log.e("Error here : ", e.getMessage());
        }
    }
}