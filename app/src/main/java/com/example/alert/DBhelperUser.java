package com.example.alert;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by GloTech on 23-07-2017.
 */

public class DBhelperUser extends SQLiteOpenHelper{
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "userdetails.db";
    private static final String TAG = "dbhelper";

    public DBhelperUser(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryString = "CREATE TABLE " + ContractUser.USER.TABLE_NAME + " ("+
                ContractUser.USER._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractUser.USER.COLUMN_NAME_NAME + " TEXT NOT NULL, " +
                ContractUser.USER.COLUMN_NAME_EMAIL + " TEXT NOT NULL, " +
                ContractUser.USER.COLUMN_NAME_ADDRESS + " TEXT, " +
                ContractUser.USER.COLUMN_NAME_PASSWORD + " TEXT NOT NULL" +
                "); ";

        Log.d(TAG, "Create table SQL: " + queryString);
        db.execSQL(queryString);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("drop table " + Contract.TABLE_ARTICLES.TABLE_NAME + " if exists;");
    }
}
