package com.example.juvek.myproject;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.ufobeaconsdk.callback.OnConnectSuccessListener;
import com.ufobeaconsdk.callback.OnFailureListener;
import com.ufobeaconsdk.callback.OnScanSuccessListener;
import com.ufobeaconsdk.callback.OnSuccessListener;
import com.ufobeaconsdk.main.UFOBeaconManager;
import com.ufobeaconsdk.main.UFODevice;
import com.ufobeaconsdk.main.UFODeviceType;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MyService extends Service {
    private static final int TODO =1 ;
    private UFOBeaconManager ufoBeaconManager;
    public static String TAG = "Beacon";

    private SharedPreferences sharedpreferences;


    public MyService() {
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        Log.i(TAG, "distibind");

        Toast.makeText(this, "MyAlarmService.onBind()", Toast.LENGTH_LONG).show();

        return null;
    }

    @SuppressLint("WrongConstant")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        ufoBeaconManager = new UFOBeaconManager(MyService.this);

        // sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return TODO;
        }
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        Toast.makeText(this, "l:"+latitude+"lo:"+longitude, Toast.LENGTH_LONG).show();
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        final String city;

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            final String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            Toast.makeText(this, "l:"+address+"lo:"+city, Toast.LENGTH_LONG).show();
            ufoBeaconManager.startScan(new OnScanSuccessListener() {
                                           @Override
                                           public void onSuccess(final UFODevice ufodevice) {
                                               Log.i(TAG,"dist" +ufodevice.getDistance()+"#"+ufodevice.getBtdevice()) ;
                                               //if(ufodevice.getBtdevice().toString().equalsIgnoreCase("55:46:4F:B2:DB:D3")){

                                               LoginDataBaseAdapter dbh=new LoginDataBaseAdapter(getApplicationContext());
                                               final SharedPreferences sharedPreferences=getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);


                                               String loc=sharedPreferences.getString("Loc","");
                                               String phone=sharedPreferences.getString("phone","");



                                               ufodevice.connect(new OnConnectSuccessListener() {
                                                   @Override
                                                   public void onSuccess(UFODevice ufoDevice) {

                                                       if(!sharedPreferences.getString("isAdded","").equalsIgnoreCase("True")){
                                                           SharedPreferences.Editor editor=sharedPreferences.edit();
                                                           editor.putString("isAdded","True");
                                                           editor.putString("id","55:46:4F:E4:82:CB");
                                                           editor.apply();
                                                           editor.commit();}

                                                   }
                                               }, new OnFailureListener() {
                                                   @Override
                                                   public void onFailure(int i, String s) {
                                                       Log.i(TAG, "error " + s);
                                                       if (sharedPreferences.getString("id","").equalsIgnoreCase("55:46:4F:E4:82:CB")){

                                                           LoginDataBaseAdapter dbh=new LoginDataBaseAdapter(getApplicationContext());
                                                           SharedPreferences sharedPreferences=getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);


                                                           String loc=sharedPreferences.getString("Loc","");
                                                           String phone=sharedPreferences.getString("phone","");


                                                           if(!loc.equalsIgnoreCase("HOME")||!loc.equalsIgnoreCase("OFFICE")) {
                                                               createNotification();
                                                               for (Contact c : dbh.getAllContacts()) {

                                                                   SmsManager sms = SmsManager.getDefault();
                                                                   sms.sendTextMessage(c.getPhoneNumber(), null, "The phone having this number "+phone +"is lost!"+"in"+address, null, null);
                                                               }}




                                                       }


                                                   }
                                               });

                                           }
                                       },
                    new OnFailureListener() {
                        @Override
                        public void onFailure(final int code, final String message) {
                            Log.i(TAG,"errorfailed "+message);
                            LoginDataBaseAdapter dbh=new LoginDataBaseAdapter(getApplicationContext());
                            SharedPreferences sharedPreferences=getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);


                            String loc=sharedPreferences.getString("Loc","");
                            String phone=sharedPreferences.getString("phone","");


                            if(!loc.equalsIgnoreCase("HOME")||!loc.equalsIgnoreCase("OFFICE")) {
                                for (Contact c : dbh.getAllContacts()) {

                                    SmsManager sms = SmsManager.getDefault();
                                    sms.sendTextMessage(c.getPhoneNumber(), null, "The phone having this number "+phone +"is lost!"+city, null, null);
                                }}

                        }
                    });
        } catch (IOException e1) {
            e1.printStackTrace();
            Toast.makeText(this, "l:"+e1, Toast.LENGTH_LONG).show();
        }

        //Beacon sccanning

        Toast.makeText(this, "Service Started", Toast.LENGTH_LONG).show();
        return START_STICKY;
    }


    public void createNotification() {
        // Prepare intent which is triggered if the
        // notification is selected
        Intent intent = new Intent(this,MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        // Build notification
        // Actions are just fake

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(), alarmUri);
        ringtone.play();
        Notification noti = new Notification.Builder(this)
                .setContentTitle("Alarm  Notification")
                .setContentText("English").setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(pIntent)
                .addAction(R.mipmap.ic_launcher, "Call", pIntent).build();
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // hide the notification after its selected
        noti.flags |= Notification.FLAG_AUTO_CANCEL;

        notificationManager.notify(0, noti);

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
    }
    @Override

    public boolean onUnbind(Intent intent) {

// TODO Auto-generated method stub

        Toast.makeText(this, "MyAlarmService.onUnbind()", Toast.LENGTH_LONG).show();

        return super.onUnbind(intent);
        //55:46:4F:B2:DB:D3
        //55:46:4F:E4:82:CB    2030284501
        //55:46:4F:E4:82:CB
    }

}
