package com.TimeToWork.TimeToWork.CustomClass;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class CustomProgressDialog extends ProgressDialog {

    private Context context;

    public CustomProgressDialog(Context context) {
        super(context);
        this.context = context;
        //Disable all possible cancelable
        setCanceledOnTouchOutside(false);
        setCancelable(false);
    }

    public void toggleProgressDialog() {

        //Check if no view has focus:
        View view = ((Activity) context).getCurrentFocus();
        if (view != null) {
            //To hide soft keyboard
            InputMethodManager imm
                    = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        //Then toggle the progress dialog
        if (isShowing()) {
            dismiss();
        } else {
            show();
        }
    }
}
