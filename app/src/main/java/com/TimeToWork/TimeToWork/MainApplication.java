package com.TimeToWork.TimeToWork;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.TimeToWork.TimeToWork.Database.DatabaseHelper;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class MainApplication extends Application {

    public static String userType;
    public static String userId;
    public static String root;
    public static RequestQueue mRequestQueue;

    public static void setRequestQueue(Context context) {

        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(context);
        }
    }

    public static void clearAppData(Context context) {

        //Removing app data
        context.deleteDatabase(DatabaseHelper.DATABASE_NAME);
        //Reset all user static variable
        userType = userId = null;
    }

    public static boolean isConnected(Context context) {

        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
