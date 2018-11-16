package com.TimeToWork.TimeToWork.Jobseeker;

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
import android.widget.RadioGroup;

import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.Database.Control.MaintainJobseeker;
import com.TimeToWork.TimeToWork.Database.Entity.Jobseeker;
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
import java.text.SimpleDateFormat;
import java.util.Locale;

import static com.TimeToWork.TimeToWork.MainApplication.userId;

public class JobseekerDetailActivity extends AppCompatActivity {

    private MaintainJobseeker maintainJobseeker = new MaintainJobseeker();

    private CustomProgressDialog mProgressDialog;
    private Handler handler;

    private EditText editTextName, editTextIC, editTextDob,
            editTextPhoneNumber, editTextEmail, editTextAddress;
    private RadioGroup radioGroup;
    private ImageView imageView;

    private Jobseeker jobseeker;
    private String encodedImage;

    private DatabaseReference databaseRef;

    private static final int RESULT_LOAD_IMAGE = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jobseeker_detail);



        mProgressDialog = new CustomProgressDialog(this);
        handler = new Handler();

        editTextName = (EditText) findViewById(R.id.name);
        editTextIC = (EditText) findViewById(R.id.ic);
        radioGroup = (RadioGroup) findViewById(R.id.gender);
        editTextPhoneNumber = (EditText) findViewById(R.id.phoneNumber);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextAddress = (EditText) findViewById(R.id.address);
        editTextDob = (EditText) findViewById(R.id.dob);
        databaseRef = FirebaseDatabase.getInstance().getReference("jobseeker");

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String value = (String) dataSnapshot.child(userId).child("jobseeker_img").getValue();
                encodedImage = value;
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

        Button saveButton = (Button) findViewById(R.id.save);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("jobseeker").child(userId).child("jobseeker_img");
                    databaseReference.setValue(encodedImage);
                    updatePartTimerDetails();

            }
        });

        imageView = (ImageView) findViewById(R.id.imgUser);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        if (userId != null) {
            // Get profile detail
            getJobseekerProfile();
        }
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

    private void openGallery() {

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, RESULT_LOAD_IMAGE);
    }

    private void getJobseekerProfile() {

        //Show progress dialog
        mProgressDialog.setMessage("Loading …");
        mProgressDialog.show();

        new Thread(new Runnable() {

            @Override
            public void run() {

                jobseeker = maintainJobseeker.getJobseekerDetail(userId);

                handler.post(new Runnable() {

                    @Override
                    public void run() {

                        if (jobseeker != null) {

                            editTextName.setText(jobseeker.getName());
                            editTextDob.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                                    .format(jobseeker.getDob()));
                            editTextIC.setText(jobseeker.getIc());
                            editTextEmail.setText(jobseeker.getEmail());
                            editTextPhoneNumber.setText(jobseeker.getPhone_number());
                            editTextAddress.setText(jobseeker.getAddress());
                            if (jobseeker.getGender() == 'M') {
                                radioGroup.check(R.id.male);
                            } else {
                                radioGroup.check(R.id.female);
                            }

                            if (jobseeker.getImg() != null && !jobseeker.getImg().equals("")
                                    && !jobseeker.getImg().equals("null")) {
                                byte[] decodedString = Base64.decode(jobseeker.getImg(), Base64.DEFAULT);
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

    private void updatePartTimerDetails() {

        //Show progress dialog
        mProgressDialog.setMessage("Saving your data …");
        mProgressDialog.show();

        new Thread(new Runnable() {

            @Override
            public void run() {

                String name = editTextName.getText().toString().trim();
                String ic = editTextIC.getText().toString().trim();
                int rgID = radioGroup.getCheckedRadioButtonId();
                char gender;
                if (rgID == 1) {
                    gender = 'M';
                } else {
                    gender = 'F';
                }
                String dob = editTextDob.getText().toString().trim();
                String phoneNum = editTextPhoneNumber.getText().toString().trim();
                String email = editTextEmail.getText().toString().trim();
                String address = editTextAddress.getText().toString().trim();

                try {
                    jobseeker = new Jobseeker();
                    jobseeker.setId(userId);
                    jobseeker.setName(name);
                    jobseeker.setDob(new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH).parse(dob));
                    jobseeker.setIc(ic);
                    jobseeker.setAddress(address);
                    jobseeker.setPhone_number(phoneNum);
                    jobseeker.setEmail(email);
                    jobseeker.setImg(encodedImage);
                    jobseeker.setGender(gender);
                    maintainJobseeker.updateJobSeekerDetails(jobseeker);

                } catch (Exception e) {
                    Log.e("error here : ", e.getMessage());
                }

                handler.post(new Runnable() {

                    @Override
                    public void run() {

                        mProgressDialog.dismiss();

                        AlertDialog.Builder builder
                                = new AlertDialog.Builder(JobseekerDetailActivity.this)
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
}
