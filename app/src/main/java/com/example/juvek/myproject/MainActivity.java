package com.example.juvek.myproject;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.ufobeaconsdk.callback.OnConnectSuccessListener;
import com.ufobeaconsdk.callback.OnFailureListener;
import com.ufobeaconsdk.callback.OnRangingListener;
import com.ufobeaconsdk.callback.OnScanSuccessListener;
import com.ufobeaconsdk.callback.OnSuccessListener;
import com.ufobeaconsdk.main.RangeType;
import com.ufobeaconsdk.main.UFOBeaconManager;
import com.ufobeaconsdk.main.UFODevice;
import com.ufobeaconsdk.main.UFODeviceType;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    UFOBeaconManager ufoBeaconManager;
    private String TAG = "Main";
    Spinner e;
    Timer t;
    boolean enabled = false;
    boolean isConnected = false;
    private Ringtone r;
    Button b1, b2, addButton;
    private PendingIntent pendingIntent;
    SharedPreferences s;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ufoBeaconManager = new UFOBeaconManager(getApplicationContext());
        b1 = (Button) findViewById(R.id.button);
        e = (Spinner) findViewById(R.id.name);
        b2 = (Button) findViewById(R.id.button2);
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
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
        Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = location.getLongitude();
        double latitude = location.getLatitude();
        Toast.makeText(this, "l:"+latitude+"lo:"+longitude, Toast.LENGTH_LONG).show();
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            Toast.makeText(this, "l:"+address+"lo:"+city, Toast.LENGTH_LONG).show();
        } catch (IOException e1) {
            e1.printStackTrace();
            Toast.makeText(this, "l:"+e1, Toast.LENGTH_LONG).show();
        }

         // Only if available else return NULL
        addButton= (Button) findViewById(R.id.addcontacts);



        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startBeaconScan();

            }
        });
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),ViewContact.class));

            }
        });




        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // t.cancel();

                stopscan();
            }

        });


    }

    private void timerFunc() {

        t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {
            public void run() {

                try {

                    Log.i("qw", "a");
                    if (ufoBeaconManager.isScanRunning()) {
                        r.stop();
                        stopScan();
                    }
                    if (!enabled) {

                        Log.i("v", "" + enabled);
                        startScan();


                    }
                    /*else{
                        Log.i("v1",""+enabled);
                        stopScan();


                    }*/

                } catch (Exception e) {
                    // TODO: handle exception
                }

            }
        }, 0, 9000);
        /*tt = new TimerTask() {

            @Override
            public void run() {
                Log.i("qw","a");
                if (!enabled){
                    Log.i("v",""+enabled);
                    startScan();


                }else{
                    Log.i("v1",""+enabled);
                    stopScan();


                }


            }


        };
        t.schedule(tt, 2*1000);

 */
    }


    private void startScan() {


        ufoBeaconManager.startScan(new OnScanSuccessListener() {
                                       @Override
                                       public void onSuccess(final UFODevice ufodevice) {
                                           runOnUiThread(new Runnable() {
                                               @Override
                                               public void run() {


                                                   ufodevice.connect(new OnConnectSuccessListener() {
                                                       @Override
                                                       public void onSuccess(UFODevice ufoDevice) {


                                                           if (ufodevice != null && ufodevice.getDeviceType() == UFODeviceType.IBEACON) {

                                                               Log.i(TAG, "ibeacon");
                                                           } else if (ufodevice != null && ufodevice.getDeviceType() == UFODeviceType.EDDYSTONE) {
                                                               Log.i(TAG, "eddy");
                                                               isConnected = true;
                                                               enabled = true;

                                                           }

                                                       }
                                                   }, new OnFailureListener() {
                                                       @Override
                                                       public void onFailure(int i, String s) {
                                                           Log.i(TAG, "error " + s);
                                                           isConnected = false;
                                                           setAlarm();


                                                       }
                                                   });


                                                   //
                                               }
                                           });
                                       }
                                   },
                new OnFailureListener() {
                    @Override
                    public void onFailure(final int code, final String message) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Log.i(TAG, "error connect " + message);
                            }
                        });
                    }
                });

    }

    private void setAlarm() {

        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        if (!isConnected) {
            r.play();
        }
    }

    private void stopScan() {


        ufoBeaconManager.stopScan(new OnSuccessListener() {

            @Override
            public void onSuccess(boolean isStop) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        enabled = false;
                    }
                });
            }
        }, new OnFailureListener() {
            @Override
            public void onFailure(final int code, final String message) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // update UI

                    }
                });
            }
        });

    }

    private void stopscan() {
        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        alarmManager.cancel(pendingIntent);
    }

    private void startBeaconScan() {

        SharedPreferences sharedPreferences=getSharedPreferences("MyPrefs",Context.MODE_PRIVATE);
        Intent myIntent = new Intent(MainActivity.this, MyService.class);

        pendingIntent = PendingIntent.getService(MainActivity.this, 0, myIntent, 0);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putString("Loc",e.getSelectedItem().toString());
        editor.apply();
        editor.commit();



        AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);



        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(System.currentTimeMillis());

        calendar.add(Calendar.SECOND, 20);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 5000,pendingIntent);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopscan();
    }





}
