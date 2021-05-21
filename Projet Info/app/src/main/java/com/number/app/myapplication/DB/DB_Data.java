package com.number.app.myapplication.DB;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class DB_Data {


    private static  DataHelper dataHelper = null;

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private DB_Data()
    {
    }

    private static DB_Data instance = null;


    public static DB_Data getInstance(Context context)
    {
        if (instance == null) {
            instance = new DB_Data();
            dataHelper = new DataHelper(context);
        }
        return instance;
    }
    public void addElementTodB(String id, String name)
    {
        SQLiteDatabase db = dataHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(FeedEntry.COLUMN_ID, id);
        values.put(FeedEntry.COLUMN_Name, name);
        long id_show = db.insert(FeedEntry.TABLE_NAME, null, values);
        Log.i("Add_ID", Long.toString(id_show));
    }
    public void fillInlist()
    {
    }
    public String getNote (String id)
    {
        SQLiteDatabase db = dataHelper.getWritableDatabase();

        Cursor cursor = db.rawQuery("select * from " + FeedEntry.TABLE_NAME + " where " + FeedEntry.COLUMN_ID + "=?", new String[]{id});

        while (cursor.moveToNext())
        {
            return cursor.getString(cursor.getColumnIndexOrThrow(FeedEntry.COLUMN_Name));
        }
        cursor.close();
        return "";
    }
    public void Deletecom (String Key)
    {
    }


    public void dropDB(){
        SQLiteDatabase db = dataHelper.getWritableDatabase();
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void delete(String key)
    {
        SQLiteDatabase db = dataHelper.getWritableDatabase();
        db.delete(FeedEntry.TABLE_NAME, FeedEntry.COLUMN_ID + "=?", new String[]{key});
    }

    /* Inner class that defines the table contents */
    public static class FeedEntry implements BaseColumns
    {
        static final String TABLE_NAME = "SimpleDatas";
        static final String COLUMN_ID = "ID";
        static final String COLUMN_Name = "Value";

    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedEntry.TABLE_NAME + " (" +
                    FeedEntry.COLUMN_ID + " TEXT PRIMARY KEY," +
                    FeedEntry.COLUMN_Name + TEXT_TYPE +" )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedEntry.TABLE_NAME;


    private static class DataHelper extends SQLiteOpenHelper
    {
        // If you change the database schema, you must increment the database version.
        static final int DATABASE_VERSION = 1;
        static final String DATABASE_NAME = "data.db";

        DataHelper(Context context) {
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