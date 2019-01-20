package com.example.alert;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class AlarmReceiver extends BroadcastReceiver {
    private static final String TAG = AlarmReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");

        String address =   intent.getStringExtra("address");
        String location =   intent.getStringExtra("location");
        ArrayList<String> sarray = intent.getStringArrayListExtra("phone");
        for(String phone:sarray){
            SMS(address,phone,location,context);
        }
    }

    private void SMS(String address,String phone,String location,Context context){
        try {

            SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phone, null,"SAFE  "+"\n"+address+"\n"+location, null, null);
        //Toast.makeText(context, "Text Message Sent", Toast.LENGTH_SHORT).show();
    }catch (Exception e){
        Log.e("err",e.getMessage());
        //Toast.makeText(context, "Text Message gone bad", Toast.LENGTH_SHORT).show();
    }
    }
}