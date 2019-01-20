package com.example.alert;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBhelperContact extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "articles.db";
    private static final String TAG = "dbhelper";

    public DBhelperContact(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String queryString = "CREATE TABLE " + ContractContact.CONTACTS.TABLE_NAME + " ("+
                ContractContact.CONTACTS._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                ContractContact.CONTACTS.COLUMN_NAME_NAME + " TEXT NOT NULL, " +
                ContractContact.CONTACTS.COLUMN_NAME_EMAIL + " TEXT NOT NULL, " +
                ContractContact.CONTACTS.COLUMN_NAME_PHONE + " TEXT" +
                "); ";

        Log.d(TAG, "Create table SQL: " + queryString);
        db.execSQL(queryString);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//        db.execSQL("drop table " + Contract.TABLE_ARTICLES.TABLE_NAME + " if exists;");
    }
}
