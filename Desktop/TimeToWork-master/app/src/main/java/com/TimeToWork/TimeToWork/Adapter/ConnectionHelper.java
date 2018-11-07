package com.TimeToWork.TimeToWork.Adapter;

import android.annotation.SuppressLint;
import android.os.StrictMode;
import android.util.Log;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Created by Alaeddin on 5/21/2017.
 */

public class ConnectionHelper {

    private String IP,DB,DBUserName,DBPassword;


    @SuppressLint("NewApi")
    public Connection connectionclasss()
    {

        // Declaring Server ip, username, database name and password
        DB = "timetowork";
        DBUserName = "timetowork@timetowork";
        DBPassword = "$Password";
        String url = "";
        // Declaring Server ip, username, database name and password


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        java.sql.Connection connection = null;
        String ConnectionURL = null;
        try
        {
            Class.forName("com.mysql.jdbc.Driver");
            url = "jdbc:mysql://timetowork.mysql.database.azure.com:3306/" + DB;
            connection = DriverManager.getConnection(url,DBUserName,DBPassword);

        }
        catch (SQLException se)
        {
            Log.e("error here 1 : ", se.getMessage());
        }
        catch (ClassNotFoundException e)
        {
            Log.e("error here 2 : ", e.getMessage());
        }
        catch (Exception e)
        {
            Log.e("error here 3 : ", e.getMessage());
        }
        return connection;
    }
}