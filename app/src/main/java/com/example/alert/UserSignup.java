package com.example.alert;

/**
 * Created by GloTech on 30-07-2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;



public class UserSignup extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private Vibrator vib;
    Animation animShake;
    private EditText signupInputName, signupInputEmail, signupInputPassword,signupInputAddress;
    private TextInputLayout signupInputLayoutName, signupInputLayoutEmail, signupInputLayoutPassword, signupInputLayoutAddress;
    private Button btnSignUp;
    DBhelperUser helper;
    Cursor cursor;
    SQLiteDatabase db;
    String user_name,user_password,user_email,user_address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_details);

        signupInputLayoutName = (TextInputLayout) findViewById(R.id.signup_input_layout_name);
        signupInputLayoutEmail = (TextInputLayout) findViewById(R.id.signup_input_layout_email);
        signupInputLayoutPassword = (TextInputLayout) findViewById(R.id.signup_input_layout_password);
        signupInputLayoutAddress = (TextInputLayout) findViewById(R.id.signup_input_layout_address);


        signupInputName = (EditText) findViewById(R.id.signup_input_name);
        signupInputEmail = (EditText) findViewById(R.id.signup_input_email);
        signupInputPassword = (EditText) findViewById(R.id.signup_input_password);
        signupInputAddress = (EditText) findViewById(R.id.signup_input_address);


        btnSignUp = (Button) findViewById(R.id.btn_signup);

        animShake = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.shake);
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
            }
        });

    }
    @Override
    protected void onStart() {
        super.onStart();
        helper = new DBhelperUser(this);
        db = helper.getWritableDatabase();
        cursor = getUserContact(db);
        //execute(cursor);

    }
    public static Cursor getUserContact(SQLiteDatabase db) {
        Cursor c = db.rawQuery("SELECT * FROM "+ContractUser.USER.TABLE_NAME+"",null);
        return c;
    }
    @Override
    protected void onStop() {
        super.onStop();
        if (db != null) db.close();
        if (cursor != null) cursor.close();
    }
    private void submitForm() {

        if (!checkName()) {
            signupInputName.setAnimation(animShake);
            signupInputName.startAnimation(animShake);
            vib.vibrate(120);
            return;
        }
        if (!checkEmail()) {
            signupInputEmail.setAnimation(animShake);
            signupInputEmail.startAnimation(animShake);
            vib.vibrate(120);
            return;
        }
        if (!checkPassword()) {
            signupInputPassword.setAnimation(animShake);
            signupInputPassword.startAnimation(animShake);
            vib.vibrate(120);
            return;
        }

        signupInputLayoutName.setErrorEnabled(false);
        signupInputLayoutEmail.setErrorEnabled(false);
        signupInputLayoutPassword.setErrorEnabled(false);


        //Toast.makeText(getApplicationContext(), "You are successfully Registered !!", Toast.LENGTH_SHORT).show();
        user_name = signupInputName.getText().toString();
        user_password = signupInputPassword.getText().toString();
        user_email = signupInputEmail.getText().toString();
        user_address = signupInputAddress.getText().toString();


        addUser(db, user_name, user_password,user_email,user_address);

       Intent i = new Intent(UserSignup.this,MainActivity.class);
        i.putExtra("user_name",user_name);
        i.putExtra("user_password",user_password);
        i.putExtra("user_email",user_email);
        i.putExtra("user_address",user_address);
        UserSignup.this.startActivity(i);
        finish();
    }
    public void swapCursor(Cursor newCursor){
        if (cursor != null) cursor.close();
        cursor = newCursor;
    }
    private long addUser(SQLiteDatabase db, String name, String password, String email,String address) {
        ContentValues cv = new ContentValues();
        cv.put(ContractUser.USER.COLUMN_NAME_NAME, name);
        cv.put(ContractUser.USER.COLUMN_NAME_EMAIL, email);
        cv.put(ContractUser.USER.COLUMN_NAME_PASSWORD, password);
        cv.put(ContractUser.USER.COLUMN_NAME_ADDRESS,address);
        return db.insert(ContractUser.USER.TABLE_NAME, null, cv);
    }
    private boolean checkName() {
        if (signupInputName.getText().toString().trim().isEmpty()) {

            signupInputLayoutName.setErrorEnabled(true);
            signupInputLayoutName.setError(getString(R.string.err_msg_name));
            signupInputName.setError(getString(R.string.err_msg_required));
            return false;
        }
        signupInputLayoutName.setErrorEnabled(false);
        return true;
    }

    private boolean checkEmail() {
        String email = signupInputEmail.getText().toString().trim();
        if (email.isEmpty() || !isValidEmail(email)) {

            signupInputLayoutEmail.setErrorEnabled(true);
            signupInputLayoutEmail.setError(getString(R.string.err_msg_email));
            signupInputEmail.setError(getString(R.string.err_msg_required));
            requestFocus(signupInputEmail);
            return false;
        }
        signupInputLayoutEmail.setErrorEnabled(false);
        return true;
    }

    private boolean checkPassword() {
        if (signupInputPassword.getText().toString().trim().isEmpty()) {

            signupInputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(signupInputPassword);
            return false;
        }
        signupInputLayoutPassword.setErrorEnabled(false);
        return true;
    }


    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

}
