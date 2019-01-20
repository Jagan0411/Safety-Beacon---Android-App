package com.example.alert;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.squareup.seismic.ShakeDetector;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.Camera;
import android.location.Location;
import android.net.Uri;
import android.os.BatteryManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface;
import android.hardware.camera2.CameraManager;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ToggleButton;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity implements ShakeDetector.Listener,ConnectionCallbacks,
        OnConnectionFailedListener,
        LocationListener {

    PendingIntent pendingIntent;
    SensorManager sm;
    AlarmManager alarmManager;
    Context context;
    Button callbtn;
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;
    String locationAddress;
    MediaPlayer mp;
    boolean siren_button;
    boolean sos_button;
    MediaPlayer mp_msg_sent;
    ImageButton strobe;
    boolean isFlash;
    boolean isOn;
    int batLevel;
    String fromemail;
    String password;
    DBhelperUser helperUser;
    Cursor cursorUser;
    SQLiteDatabase dbUser;
    DBhelperContact helperContacts;
    Cursor cursorContacts;
    SQLiteDatabase dbContacts;

    ArrayList<ContactInfo> carray = new ArrayList<ContactInfo>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        callbtn = (Button) findViewById(R.id.call);
        this.context = this;
        alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        final Intent intent  = new Intent(this.context, AlarmReceiver.class);
        ImageButton btnStart = (ImageButton) findViewById(R.id.start);
        ImageButton btnStop = (ImageButton) findViewById(R.id.stop);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        sm = (SensorManager) getSystemService(SENSOR_SERVICE);
        BatteryManager bm = (BatteryManager)getSystemService(BATTERY_SERVICE);
        batLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        ShakeDetector sD = new ShakeDetector(this);
        sD.start(sm);
        mp_msg_sent = MediaPlayer.create(MainActivity.this,R.raw.message_sent);
        mp = MediaPlayer.create(MainActivity.this,R.raw.car_alarm);
        strobe = (ImageButton) findViewById(R.id.strobe_light_button);


        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); // 1 second, in milliseconds

        btnStart.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                intent.putExtra("address", locationAddress);
                intent.putExtra("location", buildMapURL(currentLatitude,currentLongitude));
                Log.e("batLevel",batLevel+"");
                carray.clear();
                executeContacts(cursorContacts);
                ArrayList<String> sarray = new ArrayList<String>();
                for(ContactInfo c:carray){
                    sarray.add(c.getPhone());
                }
                Log.e("phones",sarray.toString());
                intent.putStringArrayListExtra("phone",sarray);
                pendingIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                sendBroadcast(intent);
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(), 3*60*1000, pendingIntent);
                Toast.makeText(getApplicationContext(),"Message Sending",Toast.LENGTH_SHORT).show();
                mp_msg_sent.start();
            }
        });
        btnStop.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
               // Intent intent  = new Intent(getBaseContext(), AlarmReceiver.class);
                 pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0 , intent, PendingIntent.FLAG_NO_CREATE);
                //AlarmManager alarmManager = (AlarmManager)getSystemService(getApplicationContext().ALARM_SERVICE);

                alarmManager.cancel(pendingIntent);
                Toast.makeText(getApplicationContext(),"Message Sending Stopped",Toast.LENGTH_SHORT).show();
                mp_msg_sent.stop();
                mp_msg_sent = MediaPlayer.create(MainActivity.this,R.raw.message_sent);
             }
        });

        callbtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:6264384838"));
                if (ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{Manifest.permission.CALL_PHONE},
                            10);
                    return;
                }else {
                    try{
                        startActivity(callIntent);
                    }
                    catch (android.content.ActivityNotFoundException ex){
                        Toast.makeText(getApplicationContext(),"Call cannot be placed",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });




        strobe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                /*String s ="111000111000111000111000";
                for(int i=0;i<s.length();i++) {*/
                    if (Flash.getTorch()) {
                        strobe.setBackgroundResource(R.drawable.lighton);
                    } else {
                        strobe.setBackgroundResource(R.drawable.lightoff);
                    }
                //}


            }
        });


    }


    public void toggleSiren(View v)
    {
        siren_button = ((ToggleButton) v).isChecked();

        if(siren_button)
        {
            mp.start();
        }

        else
        {
            mp.stop();
            mp = MediaPlayer.create(MainActivity.this,R.raw.car_alarm);
        }
    }

    @Override
    public void hearShake() {
        Toast.makeText(getApplication(), "Alert Sent", Toast.LENGTH_SHORT).show();
        carray.clear();
        executeContacts(cursorContacts);
        Log.e("contact arr",carray.toString());
        for(ContactInfo c : carray){
            Log.e("errrrrrr",c.getPhone());
            sendSMS(c.getPhone(),"HELP ME! " +"\n"+ "Battery percentage " +batLevel+"%",locationAddress,currentLatitude,currentLongitude);
        }
        for(ContactInfo c : carray){
            Log.e("errrrrrr",c.getEmail());
            SendMail sm = new SendMail(this, c.getEmail(), "Safety Beacon", "HELP ME! \n\n"+"Battery percentage " +batLevel+"%\n\n"+locationAddress+"  \n\n"+buildMapURL(currentLatitude,currentLongitude),fromemail,password);
            sm.execute();
        }

        //sendSMS("6264384838","HELP ME " + "Battery percentage " +batLevel,locationAddress,currentLatitude,currentLongitude);
    }
    //------------------------------------------------------------------------------------------------------
    //Inflating menus
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_items, menu);

        return true;
    }

    //onClick event for menu items
    public void viewContact(MenuItem item) {
        // Toast.makeText(getApplicationContext(), "To View Contacts", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(MainActivity.this,ViewContact.class);
        startActivity(i);
        //finish();

    }
    //onClick event for menu items
    public void viewProfile(MenuItem item) {
        // Toast.makeText(getApplicationContext(), "To View Contacts", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(MainActivity.this,ProfileInfo.class);
        startActivity(i);
        //finish();

    }

    public void appInfo(MenuItem item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle("How to use SOS App");
        builder.setMessage("Shake the phone to send an Immediate Alert\n\n" +
                "Press Green SOS logo to send periodic SMS alert for every 15 minutes\n\n" +
                "Press Red SOS logo to stop sending periodic SMS alert \n\n" +
                "Press 911 emergency call button to call 911\n\n" +
                "Press Siren to make loud noise\n\n" +
                "Press Flash light for camera flash ");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog ad = builder.create();
        ad.show();
    }
    @Override
    protected void onResume() {
        super.onResume();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.v(this.getClass().getSimpleName(), "onPause()");

        //Disconnect from API onPause()
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }


    }

    /**
     * If connected get lat and long
     *
     */
    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);

        } else {
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
            LocationAddress locationAddress = new LocationAddress();
            locationAddress.getAddressFromLocation(currentLatitude, currentLongitude,
                    getApplicationContext(), new GeocoderHandler());

        }
    }
    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {

            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
            /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }

    /**
     * If locationChanges change lat and long
     *
     *
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
        LocationAddress locationAddress = new LocationAddress();
        locationAddress.getAddressFromLocation(currentLatitude, currentLongitude,
                getApplicationContext(), new GeocoderHandler());
        //Toast.makeText(this, currentLatitude + " WORKS " + currentLongitude + "", Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onStop() {
        super.onStop();

        if (dbUser != null) dbUser.close();
        if (cursorUser != null) cursorUser.close();
        if (dbContacts != null) dbContacts.close();
        if (cursorContacts != null) cursorContacts.close();
    }

    @Override
    protected void onStart() {
        super.onStart();
        helperUser = new DBhelperUser(this);
        dbUser = helperUser.getReadableDatabase();
        cursorUser = getUserContact(dbUser);
        executeUser(cursorUser);

        helperContacts = new DBhelperContact(this);
        dbContacts = helperContacts.getReadableDatabase();
        cursorContacts = getAllContact(dbContacts);
        executeContacts(cursorContacts);

    }
    public static Cursor getAllContact(SQLiteDatabase db) {
        Cursor c = db.rawQuery("SELECT * FROM "+ContractContact.CONTACTS.TABLE_NAME+"",null);
        return c;
    }
    public static Cursor getUserContact(SQLiteDatabase db) {
        Cursor c = db.rawQuery("SELECT * FROM "+ContractUser.USER.TABLE_NAME+"",null);
        return c;
    }
    protected void executeUser(Cursor cursor){
        if (!cursor.moveToFirst())
            cursor.moveToFirst();
        String user_password,user_email;
        user_password = cursor.getString(cursor.getColumnIndex(ContractUser.USER.COLUMN_NAME_PASSWORD));
        user_email = cursor.getString(cursor.getColumnIndex(ContractUser.USER.COLUMN_NAME_EMAIL));


        fromemail = user_email;
        password = user_password;

    }
    protected void executeContacts(Cursor cursor){
        if (!cursor.moveToFirst())
            cursor.moveToFirst();
        if(cursor.getCount() >= 1)
        do
        {
            carray.add(new ContactInfo(cursor.getString(cursor.getColumnIndex(ContractContact.CONTACTS.COLUMN_NAME_NAME)),cursor.getString(cursor.getColumnIndex(ContractContact.CONTACTS.COLUMN_NAME_PHONE)),cursor.getString(cursor.getColumnIndex(ContractContact.CONTACTS.COLUMN_NAME_EMAIL))));
        }while(cursor.moveToNext());


    }
    private void sendSMS(String phoneNumber, String message, String address, double latitude , double longitude )
    {
        try {
            SmsManager sms = SmsManager.getDefault();
            sms.sendTextMessage(phoneNumber, null,message + address + "\n Map\n"+buildMapURL(latitude, longitude)  , null, null);
            //Toast.makeText(getApplicationContext(), "Text Message Sent",Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            //Toast.makeText(getApplicationContext(), "Text Message gone bad", Toast.LENGTH_SHORT).show();
        }
    }
 private String buildMapURL(double latitude , double longitude){
     StringBuffer body = new StringBuffer();
     body.append("http://maps.google.com?q=");
     body.append(latitude);
     body.append(",");
     body.append(longitude);
     return body.toString();
 }
}
