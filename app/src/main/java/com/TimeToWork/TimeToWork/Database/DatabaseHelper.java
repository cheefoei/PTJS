package com.TimeToWork.TimeToWork.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    //Column of jobseeker table
    static final String TABLE_JOBSEEKER = "jobseeker";
    static final String COLUMN_JOBSEEKER_ID = "jobseeker_id";
    static final String COLUMN_JOBSEEKER_NAME = "jobseeker_name";
    static final String COLUMN_JOBSEEKER_GENDER = "jobseeker_gender";
    static final String COLUMN_JOBSEEKER_DOB = "jobseeker_dob";
    static final String COLUMN_JOBSEEKER_IC = "jobseeker_ic";
    static final String COLUMN_JOBSEEKER_ADDRESS = "jobseeker_address";
    static final String COLUMN_JOBSEEKER_PHONE = "jobseeker_phone_number";
    static final String COLUMN_JOBSEEKER_EMAIL = "jobseeker_email";
    static final String COLUMN_JOBSEEKER_PREFERRED_JOB = "jobseeker_preferred_job";
    static final String COLUMN_JOBSEEKER_PREFERRED_LOC = "jobseeker_preferred_location";
    static final String COLUMN_JOBSEEKER_EXPERIENCE = "jobseeker_experience";
    static final String COLUMN_JOBSEEKER_RATING = "jobseeker_rating";
    static final String COLUMN_JOBSEEKER_IMG = "jobseeker_img";
    private static final String SQL_CREATE_TABLE_JOBSEEKER = "CREATE TABLE " + TABLE_JOBSEEKER + "("
            + COLUMN_JOBSEEKER_ID + " TEXT NOT NULL PRIMARY KEY, "
            + COLUMN_JOBSEEKER_NAME + " TEXT NOT NULL, "
            + COLUMN_JOBSEEKER_GENDER + " TEXT NOT NULL, "
            + COLUMN_JOBSEEKER_DOB + " INTEGER NOT NULL, "
            + COLUMN_JOBSEEKER_IC + " TEXT NOT NULL, "
            + COLUMN_JOBSEEKER_ADDRESS + " TEXT NOT NULL, "
            + COLUMN_JOBSEEKER_PHONE + " TEXT NOT NULL, "
            + COLUMN_JOBSEEKER_EMAIL + " TEXT NOT NULL, "
            + COLUMN_JOBSEEKER_PREFERRED_JOB + " TEXT, "
            + COLUMN_JOBSEEKER_PREFERRED_LOC + " TEXT, "
            + COLUMN_JOBSEEKER_EXPERIENCE + " TEXT, "
            + COLUMN_JOBSEEKER_RATING + " REAL, "
            + COLUMN_JOBSEEKER_IMG + " TEXT "
            + ");";
    //Column of company table
    static final String TABLE_COMPANY = "company";
    static final String COLUMN_COMPANY_ID = "company_id";
    static final String COLUMN_COMPANY_NAME = "company_name";
    static final String COLUMN_COMPANY_ADDRESS = "company_address";
    static final String COLUMN_COMPANY_PHONE = "company_phone_number";
    static final String COLUMN_COMPANY_EMAIL = "company_email";
    static final String COLUMN_COMPANY_RATING = "company_rating";
    static final String COLUMN_COMPANY_IMG = "company_img";
    private static final String SQL_CREATE_TABLE_COMPANY = "CREATE TABLE " + TABLE_COMPANY + "("
            + COLUMN_COMPANY_ID + " TEXT NOT NULL PRIMARY KEY, "
            + COLUMN_COMPANY_NAME + " TEXT NOT NULL, "
            + COLUMN_COMPANY_ADDRESS + " TEXT NOT NULL, "
            + COLUMN_COMPANY_PHONE + " TEXT NOT NULL, "
            + COLUMN_COMPANY_EMAIL + " TEXT NOT NULL, "
            + COLUMN_COMPANY_RATING + " REAL, "
            + COLUMN_COMPANY_IMG + " TEXT "
            + ");";

    public static final String DATABASE_NAME = "TimeToWork.db";
    private static final int DATABASE_VERSION = 1;

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_JOBSEEKER);
        db.execSQL(SQL_CREATE_TABLE_COMPANY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Drop the tables
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_JOBSEEKER);
        db.execSQL("DROP TABLE IF EXISTS " + SQL_CREATE_TABLE_COMPANY);
        //Recreate the tables
        onCreate(db);
    }
}
