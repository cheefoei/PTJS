package com.TimeToWork.TimeToWork.CustomClass;

import android.app.Activity;
import android.app.ProgressDialog;
import android.support.v7.app.AlertDialog;

import com.TimeToWork.TimeToWork.R;
import com.android.volley.NoConnectionError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;

public class CustomVolleyErrorListener implements Response.ErrorListener {

    private Activity mActivity;
    private ProgressDialog mProgressDialog;
    private RequestQueue mRequestQueue;

    public CustomVolleyErrorListener(Activity mActivity,
                                     ProgressDialog mProgressDialog,
                                     RequestQueue mRequestQueue) {
        this.mActivity = mActivity;
        this.mProgressDialog = mProgressDialog;
        this.mRequestQueue = mRequestQueue;
    }

    @Override
    public void onErrorResponse(VolleyError error) {

        String errorMessage;

        //To cancel all volley requests
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(this);
        }
        //To close progress dialog
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
        //Define error messages
        if (error instanceof TimeoutError) {
            errorMessage = mActivity.getResources().getString(R.string.error_connection_timeout);
        } else if (error instanceof NoConnectionError) {
            errorMessage = mActivity.getResources().getString(R.string.error_no_connection);
        } else if (error instanceof ServerError) {
            errorMessage = mActivity.getResources().getString(R.string.error_server_connection);
        } else {
            errorMessage = mActivity.getResources().getString(R.string.error_general);
            errorMessage += "\n" + error.getMessage();
        }
        //To show error alert dialog
        AlertDialog errorDialog = new AlertDialog.Builder(mActivity)
                .setTitle("Error")
                .setMessage(errorMessage)
                .setPositiveButton("OK", null)
                .setCancelable(false)
                .create();
        errorDialog.show();
    }
}