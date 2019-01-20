package com.example.alert;

/**
 * Created by GloTech on 30-07-2017.
 */
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class Home extends AppCompatActivity {

    /*ImageView home_icon;*/

    TextView tagline1;
    TextView tagline2;
    private static int TIME_OUT = 3000;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        tagline1 = (TextView) findViewById(R.id.welcome_note);
        tagline2 = (TextView) findViewById(R.id.welcome_note2);
        Typeface custom_Fonts = Typeface.createFromAsset(getAssets(),"font/Raleway-SemiBold.ttf");
        tagline1.setTypeface(custom_Fonts);
        Typeface custom_Fonts2 = Typeface.createFromAsset(getAssets(),"font/Raleway-SemiBoldItalic.ttf");
        tagline2.setTypeface(custom_Fonts2);

        sharedPreferences = getSharedPreferences("SharedPreferences", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        boolean  isFirstTime = sharedPreferences.getBoolean("isFirst", true);


        if(isFirstTime) {
            editor.putBoolean("isFirst",false);
            editor.commit();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent i = new Intent(Home.this,UserSignup.class);
                    Home.this.startActivity(i);
                    //overridePendingTransition(R.anim.slide_in_up,R.anim.fade_out);
                    finish();
                }
            },TIME_OUT);
        }
        else
        {
            Intent intent = new Intent(Home.this,MainActivity.class);
            startActivity(intent);
            finish();
        }

    }
}
