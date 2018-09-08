package com.TimeToWork.TimeToWork.activity.company;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.TimeToWork.TimeToWork.R;

public class PostNewJobActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_job_post);

        Button btnPost = (Button) findViewById(R.id.btn_post_new_job);
        btnPost.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder
                        = new AlertDialog.Builder(PostNewJobActivity.this, R.style.DialogTheme)
                        .setTitle("Job is posted successfully")
                        .setMessage("Make advertisement for this job post? ")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                Intent intent = new Intent(PostNewJobActivity.this, PaymentActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", null);
                builder.show();
            }
        });
    }
}
