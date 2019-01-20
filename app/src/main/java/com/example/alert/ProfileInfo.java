package com.example.alert;



import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.EditText;
import android.widget.TextView;


public class ProfileInfo extends AppCompatActivity  {

    TextView edit_name;
    EditText edit_password;
    TextView edit_email;
    TextView edit_address;
    DBhelperUser helper;
    Cursor cursor;
    SQLiteDatabase db;

    String user_name,user_password,user_email,user_address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_info);




        edit_name = (TextView) findViewById(R.id.edit_name);
        //edit_password = (EditText) findViewById(R.id.edit_password);
        edit_email = (TextView) findViewById(R.id.edit_email);
        edit_address = (TextView) findViewById(R.id.edit_address);





    }




    @Override
    protected void onStop() {
        super.onStop();
        if (db != null) db.close();
        if (cursor != null) cursor.close();
    }

    @Override
    protected void onStart() {
        super.onStart();
        helper = new DBhelperUser(this);
        db = helper.getReadableDatabase();
        cursor = getUserContact(db);
        execute(cursor);

    }

    public static Cursor getUserContact(SQLiteDatabase db) {
        Cursor c = db.rawQuery("SELECT * FROM "+ContractUser.USER.TABLE_NAME+"",null);
        return c;
    }
    protected void execute(Cursor cursor){
        if (!cursor.moveToFirst())
            cursor.moveToFirst();
        user_name = cursor.getString(cursor.getColumnIndex(ContractUser.USER.COLUMN_NAME_NAME));
        user_password = cursor.getString(cursor.getColumnIndex(ContractUser.USER.COLUMN_NAME_PASSWORD));
        user_email = cursor.getString(cursor.getColumnIndex(ContractUser.USER.COLUMN_NAME_EMAIL));
        user_address = cursor.getString(cursor.getColumnIndex(ContractUser.USER.COLUMN_NAME_ADDRESS));
        edit_name.setText(user_name);
        edit_email.setText(user_email);
        edit_address.setText(user_address);

    }
}
