package com.TimeToWork.TimeToWork.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.TimeToWork.TimeToWork.Database.Entity.Company;

import static com.TimeToWork.TimeToWork.MainApplication.userId;

public class CompanyDA {

    private SQLiteDatabase mDatabase;
    private DatabaseHelper mDatabaseHelper;
    private String[] mAllColumns = {
            DatabaseHelper.COLUMN_COMPANY_ID,
            DatabaseHelper.COLUMN_COMPANY_NAME,
            DatabaseHelper.COLUMN_COMPANY_ADDRESS,
            DatabaseHelper.COLUMN_COMPANY_PHONE,
            DatabaseHelper.COLUMN_COMPANY_EMAIL,
            DatabaseHelper.COLUMN_COMPANY_RATING,
            DatabaseHelper.COLUMN_COMPANY_IMG
    };

    public CompanyDA(Context mContext) {

        mDatabaseHelper = new DatabaseHelper(mContext);
        //Open database
        try {
            mDatabase = mDatabaseHelper.getWritableDatabase();
        } catch (SQLException e) {
            Log.e(e.getClass().getName(), e.getMessage());
        }
    }

    public Company insertCompany(Company company) {

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_COMPANY_ID, company.getId());
        values.put(DatabaseHelper.COLUMN_COMPANY_NAME, company.getName());
        values.put(DatabaseHelper.COLUMN_COMPANY_ADDRESS, company.getAddress());
        values.put(DatabaseHelper.COLUMN_COMPANY_PHONE, company.getPhone_number());
        values.put(DatabaseHelper.COLUMN_COMPANY_EMAIL, company.getEmail());
        values.put(DatabaseHelper.COLUMN_COMPANY_RATING, company.getRating());
        values.put(DatabaseHelper.COLUMN_COMPANY_IMG, company.getImg());

        mDatabase.insert(DatabaseHelper.TABLE_COMPANY, null, values);

        return getCompanyData();
    }

    public Company getCompanyData() {

        Company company = null;
        Cursor cursor = mDatabase.query(
                DatabaseHelper.TABLE_COMPANY,           // The table to query
                mAllColumns,                            // The columns to return
                null,                                   // The columns for the WHERE clause
                null,                                   // The values for the WHERE clause
                null,                                   // don't group the rows
                null,                                   // don't filter by row groups
                null                                    // The sort order
        );

        if (cursor != null && cursor.moveToFirst()) {

            company = new Company();
            company.setId(cursor.getString(0));
            company.setName(cursor.getString(1));
            company.setAddress(cursor.getString(2));
            company.setPhone_number(cursor.getString(3));
            company.setEmail(cursor.getString(4));
            company.setRating(Double.parseDouble(cursor.getString(5)));
            company.setImg(cursor.getString(6));
        }
        assert cursor != null;
        cursor.close();

        return company;
    }

    public void updateCompanyData(Company company) {

        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_COMPANY_ID, company.getId());
        values.put(DatabaseHelper.COLUMN_COMPANY_NAME, company.getName());
        values.put(DatabaseHelper.COLUMN_COMPANY_ADDRESS, company.getAddress());
        values.put(DatabaseHelper.COLUMN_COMPANY_PHONE, company.getPhone_number());
        values.put(DatabaseHelper.COLUMN_COMPANY_EMAIL, company.getEmail());
        values.put(DatabaseHelper.COLUMN_COMPANY_RATING, company.getRating());
        values.put(DatabaseHelper.COLUMN_COMPANY_IMG, company.getImg());

        mDatabase.update(DatabaseHelper.TABLE_COMPANY, values,
                DatabaseHelper.COLUMN_COMPANY_ID + " = ?", new String[]{userId});
    }


    public void close() {
        mDatabaseHelper.close();
    }
}
