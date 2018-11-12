package com.TimeToWork.TimeToWork.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.TimeToWork.TimeToWork.Database.Entity.Jobseeker;

import java.util.Date;

import static com.TimeToWork.TimeToWork.MainApplication.userId;

public class JobseekerDA {

    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDatabaseHelper;
    private String[] mAllColumns = {
            DatabaseHelper.COLUMN_JOBSEEKER_ID,
            DatabaseHelper.COLUMN_JOBSEEKER_NAME,
            DatabaseHelper.COLUMN_JOBSEEKER_GENDER,
            DatabaseHelper.COLUMN_JOBSEEKER_DOB,
            DatabaseHelper.COLUMN_JOBSEEKER_IC,
            DatabaseHelper.COLUMN_JOBSEEKER_ADDRESS,
            DatabaseHelper.COLUMN_JOBSEEKER_PHONE,
            DatabaseHelper.COLUMN_JOBSEEKER_EMAIL,
            DatabaseHelper.COLUMN_JOBSEEKER_PREFERRED_JOB,
            DatabaseHelper.COLUMN_JOBSEEKER_PREFERRED_LOC,
            DatabaseHelper.COLUMN_JOBSEEKER_EXPERIENCE,
            DatabaseHelper.COLUMN_JOBSEEKER_RATING,
            DatabaseHelper.COLUMN_JOBSEEKER_IMG
    };

    public JobseekerDA(Context mContext) {

        mDatabaseHelper = new DatabaseHelper(mContext);
        //Open database
        try {
            mDatabase = mDatabaseHelper.getWritableDatabase();
        } catch (SQLException e) {
            Log.e(e.getClass().getName(), e.getMessage());
        }
    }

    public Jobseeker insertJobseeker(Jobseeker jobseeker) {

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_JOBSEEKER_ID, jobseeker.getId());
        values.put(DatabaseHelper.COLUMN_JOBSEEKER_NAME, jobseeker.getName());
        values.put(DatabaseHelper.COLUMN_JOBSEEKER_GENDER, Character.toString(jobseeker.getGender()));
        values.put(DatabaseHelper.COLUMN_JOBSEEKER_DOB, jobseeker.getDob().getTime());
        values.put(DatabaseHelper.COLUMN_JOBSEEKER_IC, jobseeker.getIc());
        values.put(DatabaseHelper.COLUMN_JOBSEEKER_ADDRESS, jobseeker.getAddress());
        values.put(DatabaseHelper.COLUMN_JOBSEEKER_PHONE, jobseeker.getPhone_number());
        values.put(DatabaseHelper.COLUMN_JOBSEEKER_EMAIL, jobseeker.getEmail());
        values.put(DatabaseHelper.COLUMN_JOBSEEKER_PREFERRED_JOB, jobseeker.getPreferred_job());
        values.put(DatabaseHelper.COLUMN_JOBSEEKER_PREFERRED_LOC, jobseeker.getPreferred_location());
        values.put(DatabaseHelper.COLUMN_JOBSEEKER_EXPERIENCE, jobseeker.getExperience());
        values.put(DatabaseHelper.COLUMN_JOBSEEKER_RATING, jobseeker.getRating());
        values.put(DatabaseHelper.COLUMN_JOBSEEKER_IMG, jobseeker.getImg());

        mDatabase.insert(DatabaseHelper.TABLE_JOBSEEKER, null, values);

        return getJobseekerData();
    }

    public Jobseeker getJobseekerData() {

        Jobseeker jobseeker = null;
        Cursor cursor = mDatabase.query(
                DatabaseHelper.TABLE_JOBSEEKER,         // The table to query
                mAllColumns,                            // The columns to return
                null,                                   // The columns for the WHERE clause
                null,                                   // The values for the WHERE clause
                null,                                   // don't group the rows
                null,                                   // don't filter by row groups
                null                                    // The sort order
        );

        if (cursor != null && cursor.moveToFirst()) {

            jobseeker = new Jobseeker();
            jobseeker.setId(cursor.getString(0));
            jobseeker.setName(cursor.getString(1));
            jobseeker.setGender(cursor.getString(2).charAt(0));
            jobseeker.setDob(new Date(cursor.getLong(3)));
            jobseeker.setIc(cursor.getString(4));
            jobseeker.setAddress(cursor.getString(5));
            jobseeker.setPhone_number(cursor.getString(6));
            jobseeker.setEmail(cursor.getString(7));
            jobseeker.setPreferred_job(cursor.getString(8));
            jobseeker.setPreferred_location(cursor.getString(9));
            jobseeker.setExperience(cursor.getString(10));
            jobseeker.setRating(Double.parseDouble(cursor.getString(11)));
            jobseeker.setImg(cursor.getString(12));
        }
        assert cursor != null;
        cursor.close();

        return jobseeker;
    }

    public void updateJobseekerData(Jobseeker jobseeker) {

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_JOBSEEKER_ID, jobseeker.getId());
        values.put(DatabaseHelper.COLUMN_JOBSEEKER_NAME, jobseeker.getName());
        values.put(DatabaseHelper.COLUMN_JOBSEEKER_GENDER, Character.toString(jobseeker.getGender()));
        values.put(DatabaseHelper.COLUMN_JOBSEEKER_DOB, jobseeker.getDob().getTime());
        values.put(DatabaseHelper.COLUMN_JOBSEEKER_IC, jobseeker.getIc());
        values.put(DatabaseHelper.COLUMN_JOBSEEKER_ADDRESS, jobseeker.getAddress());
        values.put(DatabaseHelper.COLUMN_JOBSEEKER_PHONE, jobseeker.getPhone_number());
        values.put(DatabaseHelper.COLUMN_JOBSEEKER_EMAIL, jobseeker.getEmail());
        values.put(DatabaseHelper.COLUMN_JOBSEEKER_PREFERRED_JOB, jobseeker.getPreferred_job());
        values.put(DatabaseHelper.COLUMN_JOBSEEKER_PREFERRED_LOC, jobseeker.getPreferred_location());
        values.put(DatabaseHelper.COLUMN_JOBSEEKER_EXPERIENCE, jobseeker.getExperience());
        values.put(DatabaseHelper.COLUMN_JOBSEEKER_RATING, jobseeker.getRating());
        values.put(DatabaseHelper.COLUMN_JOBSEEKER_IMG, jobseeker.getImg());

        mDatabase.update(DatabaseHelper.TABLE_JOBSEEKER, values,
                DatabaseHelper.COLUMN_JOBSEEKER_ID + " = ?", new String[]{userId});
    }

    public void close() {
        mDatabaseHelper.close();
    }
}
