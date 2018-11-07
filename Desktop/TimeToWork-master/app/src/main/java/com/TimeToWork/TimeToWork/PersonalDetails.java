package com.TimeToWork.TimeToWork;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import org.apache.commons.io.output.ByteArrayOutputStream;

import com.TimeToWork.TimeToWork.R;
import com.google.firebase.database.ValueEventListener;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import com.TimeToWork.TimeToWork.Control.MaintainPartTimer;
import com.TimeToWork.TimeToWork.Entity.PartTimer;

public class PersonalDetails extends AppCompatActivity {

    private MaintainPartTimer MPartTimer = new MaintainPartTimer();
    private EditText editTextName, editTextIC, editTextDob, editTextPhoneNumber, editTextEmail, editTextAddress;
    private RadioGroup radioGroup;
    private Button saveButton;
    private ImageView imageView;
    private Uri resultImage;
    private static final int RESULT_LOAD_IMAGE = 5;
    private String encodedImage;
    private DatabaseReference databaseRef;
    private ArrayList<PartTimer> partTimerArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_details);

        databaseRef = FirebaseDatabase.getInstance().getReference("jobseeker");
        editTextName = (EditText) findViewById(R.id.name);
        editTextIC = (EditText) findViewById(R.id.ic);
        radioGroup = (RadioGroup) findViewById(R.id.gender);
        editTextPhoneNumber = (EditText) findViewById(R.id.phoneNumber);
        editTextEmail = (EditText) findViewById(R.id.email);
        editTextAddress = (EditText) findViewById(R.id.address);
        editTextDob = (EditText) findViewById(R.id.dob);
        saveButton = (Button) findViewById(R.id.save);
        imageView = (ImageView) findViewById(R.id.imgUser);

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
            CropImage.activity(selectedImage).setGuidelines(CropImageView.Guidelines.ON).start(PersonalDetails.this);
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

    private void getPartTimerDetails()
    {

        partTimerArrayList = MPartTimer.getPartTimerDetails("J10001");

        for(int i = 0; i < partTimerArrayList.size(); i++) {
            editTextName.setText(partTimerArrayList.get(i).getJobseeker_name());
            editTextDob.setText(partTimerArrayList.get(i).getJobseeker_dob());
            editTextIC.setText(partTimerArrayList.get(i).getJobseeker_ic());
            editTextEmail.setText(partTimerArrayList.get(i).getJobseeker_email());
            editTextPhoneNumber.setText(partTimerArrayList.get(i).getJobseeker_phone_number());
            editTextAddress.setText(partTimerArrayList.get(i).getJobseeker_address());
            if (partTimerArrayList.get(i).getJobseeker_gender() == 'M') {
                radioGroup.check(R.id.male);
            } else {
                radioGroup.check(R.id.female);
            }

        }
    }

    private void updatePartTimerDetails()
    {
        String name = editTextName.getText().toString().trim();
        String ic = editTextIC.getText().toString().trim();
        int rgID = radioGroup.getCheckedRadioButtonId();
        char gender;
        if(rgID == 1)
        {
            gender = 'M';
        } else
        {
            gender = 'F';
        }
        String dob = editTextDob.getText().toString().trim();
        String phoneNum = editTextPhoneNumber.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String address = editTextAddress.getText().toString().trim();

        PartTimer partTimerDetail = null;

        try {
            partTimerDetail = new PartTimer("J10001", name, dob, ic, address, phoneNum, email, encodedImage, gender);
            MPartTimer.updateJobSeekerDetails(partTimerDetail);
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("jobseeker").child("J10001").child("jobseeker_img");
            databaseReference.setValue(encodedImage);
        } catch (Exception e) {
            Log.e("error here : ", e.getMessage());
        }
    }
}
