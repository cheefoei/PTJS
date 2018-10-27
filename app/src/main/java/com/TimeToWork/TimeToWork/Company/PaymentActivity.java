package com.TimeToWork.TimeToWork.Company;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.TimeToWork.TimeToWork.CustomClass.CustomProgressDialog;
import com.TimeToWork.TimeToWork.CustomClass.CustomVolleyErrorListener;
import com.TimeToWork.TimeToWork.Database.Entity.JobPost;
import com.TimeToWork.TimeToWork.R;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.TimeToWork.TimeToWork.MainApplication.mRequestQueue;
import static com.TimeToWork.TimeToWork.MainApplication.root;

public class PaymentActivity extends AppCompatActivity {

    private EditText etCardNumber, etCVV, etHolderName;
    private Spinner mSpinnerMonth, mSpinnerYear;
    private TextView tvCardType;

    private CustomProgressDialog mProgressDialog;

    private JobPost jobPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        mProgressDialog = new CustomProgressDialog(this);

        jobPost = (JobPost) getIntent().getSerializableExtra("JOBPOST");

        etCVV = (EditText) findViewById(R.id.et_card_cvv);
        etHolderName = (EditText) findViewById(R.id.et_card_holder_name);

        mSpinnerMonth = (Spinner) findViewById(R.id.spinner_card_month);
        mSpinnerYear = (Spinner) findViewById(R.id.spinner_card_year);
        tvCardType = (TextView) findViewById(R.id.tv_card_type);

        etCardNumber = (EditText) findViewById(R.id.et_card_number);
        etCardNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (etCardNumber.getText().toString().length() > 0) {
                    String num = etCardNumber.getText().toString().substring(0, 1);
                    switch (num) {
                        case "4":
                            tvCardType.setText(getString(R.string.string_visa_card));
                            break;
                        case "5":
                            tvCardType.setText(getString(R.string.string_master_card));
                            break;
                        default:
                            tvCardType.setText(getString(R.string.string_not_card));
                            break;
                    }
                }
            }
        });

        Button btnPayment = (Button) findViewById(R.id.btn_make_payment);
        btnPayment.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(isValid()){
                    makeNewPayment();
                }
            }
        });
    }

    private boolean isValid() {

        boolean isValid = true;

        if (etCardNumber.getText().toString().equals("")) {
            etCardNumber.setError(getString(R.string.error_required_field));
            isValid = false;
        }
        if (etCVV.getText().toString().equals("")) {
            etCVV.setError(getString(R.string.error_required_field));
            isValid = false;
        }
        if (etHolderName.getText().toString().equals("")) {
            etHolderName.setError(getString(R.string.error_required_field));
            isValid = false;
        }
        if (tvCardType.getText().toString().equals(getString(R.string.string_not_card))) {

            AlertDialog.Builder builder
                    = new AlertDialog.Builder(PaymentActivity.this, R.style.DialogStyle)
                    .setTitle("Error")
                    .setMessage("Your credit card is not supported now")
                    .setPositiveButton("OK", null);
            builder.show();
            isValid = false;
        }

        return isValid;
    }

    private void makeNewPayment() {

        // Show progress dialog
        mProgressDialog.setMessage("Making payment …");
        mProgressDialog.toggleProgressDialog();

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {

                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");

                    AlertDialog.Builder builder
                            = new AlertDialog.Builder(PaymentActivity.this, R.style.DialogStyle);

                    if (success) {
                        builder.setTitle("Successful");
                    } else {
                        builder.setTitle("Failed");
                    }

                    //To close progress dialog
                    mProgressDialog.toggleProgressDialog();
                    //Show alert dialog
                    builder.setMessage(jsonResponse.getString("msg"))
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    finish();
                                }
                            })
                            .create()
                            .show();

                } catch (JSONException e) {

                    e.printStackTrace();
                    //To close progress dialog
                    mProgressDialog.toggleProgressDialog();
                    //If exception, then show alert dialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(PaymentActivity.this);
                    builder.setMessage(e.getMessage())
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    PaymentActivity.this.finish();
                                }
                            })
                            .create()
                            .show();

                }
            }
        };

        CustomVolleyErrorListener errorListener
                = new CustomVolleyErrorListener(this, mProgressDialog, mRequestQueue);

        PaymentActivity.NewPaymentRequest newPaymentRequest = new PaymentActivity.NewPaymentRequest(
                root + getString(R.string.url_make_new_payment),
                responseListener,
                errorListener
        );
        mRequestQueue.add(newPaymentRequest);
    }

    private class NewPaymentRequest extends StringRequest {

        private Map<String, String> params;

        NewPaymentRequest(
                String url,
                Response.Listener<String> listener,
                Response.ErrorListener errorListener) {
            super(Method.POST, url, listener, errorListener);

            params = new HashMap<>();
            params.put("job_post_id", jobPost.getId());
            params.put("amount", "5");
        }

        public Map<String, String> getParams() {
            return params;
        }
    }

}
