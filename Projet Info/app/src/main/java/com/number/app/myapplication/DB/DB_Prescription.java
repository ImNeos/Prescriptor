package com.number.app.myapplication.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DB_Prescription {


    private static PrescriHelper mDbHelper = null;

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DB_Prescription()
    {
    }

    private static DB_Prescription instance = null;


    public static DB_Prescription getInstance(Context context)
    {
        if (instance == null) {
            instance = new DB_Prescription();
            mDbHelper = new PrescriHelper(context);
        }
        return instance;
    }
    public void addElementTodB()
    {
    }
    public void fillInlist()
    {
    }
    public void Deletecom (String Key)
    {
    }


    public void dropDB(){
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void delete(String key)
    {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        db.delete(FeedEntry.TABLE_NAME, FeedEntry.COLUMN_KEY + "=?", new String[]{key});
    }

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns
    {
        static final String TABLE_NAME = "Prescription";

        static final String COLUMN_KEY = "RandomID";
        static final String COLUMN_Patient = "PatientRegNat";
        static final String COLUMN_Doctor = "DoctorRegNat";
        static final String COLUMN_MedocID = "MedocID";
        static final String COLUMN_Note = "Note docteur";
        static final String COLUMN_TIMESTAMP = "Date";



    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry.COLUMN_KEY + " TEXT PRIMARY KEY," +
                    FeedEntry.COLUMN_Patient + TEXT_TYPE + "," +
                    FeedEntry.COLUMN_Doctor + TEXT_TYPE + "," +
                    FeedEntry.COLUMN_MedocID + TEXT_TYPE + "," +
                    FeedEntry.COLUMN_Note + TEXT_TYPE + "," +
                    FeedEntry.COLUMN_TIMESTAMP + TEXT_TYPE +" )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;


    private static class PrescriHelper extends SQLiteOpenHelper
    {
        // If you change the database schema, you must increment the database version.
        static final int DATABASE_VERSION = 1;
        static final String DATABASE_NAME = "prescription.db";

        PrescriHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }

        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // This database is only a cache for online data, so its upgrade policy is
            // to simply to discard the data and start over
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion)
        {
            onUpgrade(db, oldVersion, newVersion);
        }
    }
}