package com.TimeToWork.TimeToWork.Company;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.Database.Control.MaintainCompany;
import com.TimeToWork.TimeToWork.Database.Entity.Company;
import com.TimeToWork.TimeToWork.R;

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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.TimeToWork.TimeToWork.MainApplication.userId;

public class CompanyDetailActivity extends AppCompatActivity {

    private MaintainCompany maintainCompany = new MaintainCompany();

    private CustomProgressDialog mProgressDialog;
    private Handler handler;

    private EditText editTextName, editTextEmail, editTextPhoneNumber, editTextAddress;
    private ImageView imageView;

    private static final int RESULT_LOAD_IMAGE = 5;
    private String encodedImage, name, phoneNum, email, address;

    private Company company;

    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_detail);

        databaseRef = FirebaseDatabase.getInstance().getReference("company");

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

        mProgressDialog = new CustomProgressDialog(this);
        handler = new Handler();

        imageView = (ImageView) findViewById(R.id.imgUser);
        editTextName = (EditText) findViewById(R.id.name);
        editTextPhoneNumber = (EditText) findViewById(R.id.phoneNumber);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextAddress = (EditText) findViewById(R.id.address);

        Button saveButton = (Button) findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    updateCompanyDetail();
                }
            }
        });

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        getCompanyDetail();
    }

    private void getCompanyDetail() {

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

                            editTextName.setText(company.getName());
                            editTextEmail.setText(company.getEmail());
                            editTextPhoneNumber.setText(company.getPhone_number());
                            editTextAddress.setText(company.getAddress());

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

    private void openGallery() {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) // Crop Image and Convert Image to Basse 64.
    {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            CropImage.activity(selectedImage).setGuidelines(
                    CropImageView.Guidelines.ON).start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {

                Uri resultImage = result.getUri();

                Bitmap originBitmap = null;
                InputStream imageStream;

                try {
                    imageStream = getContentResolver().openInputStream(resultImage);
                    originBitmap = BitmapFactory.decodeStream(imageStream);
                } catch (FileNotFoundException e) {
                    Log.e("error here : ", e.getMessage());
                }

                if (originBitmap != null) {
                    this.imageView.setImageBitmap(originBitmap);
                    Bitmap image = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    image.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
                    encodedImage = Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT);
                }
                imageView.setImageURI(resultImage);

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Log.e("Error", "CropImage Error");
            }
        }
    }

    private void updateCompanyDetail() {

        //Show progress dialog
        mProgressDialog.setMessage("Saving your data …");
        mProgressDialog.show();

        new Thread(new Runnable() {

            @Override
            public void run() {

                try {
                    company = new Company();
                    company.setId(userId);
                    company.setName(name);
                    company.setAddress(address);
                    company.setEmail(email);
                    company.setPhone_number(phoneNum);
                    company.setImg(encodedImage);

                    maintainCompany.updateCompanyDetail(company);

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("company").child(userId).child("company_img");
                    databaseReference.setValue(encodedImage);
                } catch (Exception e) {
                    Log.e("Error here : ", e.getMessage());
                }

                handler.post(new Runnable() {

                    @Override
                    public void run() {

                        mProgressDialog.dismiss();

                        AlertDialog.Builder builder
                                = new AlertDialog.Builder(CompanyDetailActivity.this)
                                .setTitle("Successful")
                                .setMessage("Your data is saved.")
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        finish();
                                    }
                                });
                        builder.show();
                    }
                });
            }
        }).start();
    }

    private boolean isValid() {

        CustomProgressDialog mProgressDialog = new CustomProgressDialog(this);
        mProgressDialog.setMessage("Verifying your data  …");
        mProgressDialog.show();

        boolean valid = true;

        name = editTextName.getText().toString().trim();
        address = editTextAddress.getText().toString().trim();
        email = editTextEmail.getText().toString().trim();
        phoneNum = editTextPhoneNumber.getText().toString().trim();

        String nameFormat = "[a-zA-Z_]+";
        Pattern pattern = Pattern.compile(nameFormat);
        Matcher matcher = pattern.matcher(name);

        if (name.equals("")) {
            editTextName.setError("Cannot be empty");
            valid = false;
        }else{
            if (!matcher.matches()) {
                editTextName.setError("Name cannot be numeric");
                valid = false;
            }
        }

        if (address.equals("")) {
            editTextAddress.setError("Cannot be empty");
            valid = false;
        }

        String phoneNumFormat = "\\d{2}-\\d{8}";
        pattern = Pattern.compile(phoneNumFormat);
        matcher = pattern.matcher(phoneNum);
        if (phoneNum.equals("")) {
            editTextPhoneNumber.setError("Cannot be empty");
            valid = false;
        }else {
            if (!matcher.matches()) {
                editTextPhoneNumber.setError("Invalid Phone Number Format");
                valid = false;
            } else {
                if (!checkPhoneNumber()) {
                    editTextPhoneNumber.setError("Phone Number Has Been Used.");
                    valid = false;
                }
            }
        }

        mProgressDialog.dismiss();

        return valid;
    }

    private Boolean checkPhoneNumber() {
        return maintainCompany.checkPhoneNum(editTextPhoneNumber.getText().toString());
    }

    private Boolean checkEmail() {
        return maintainCompany.checkEmail(editTextEmail.getText().toString());
    }
}