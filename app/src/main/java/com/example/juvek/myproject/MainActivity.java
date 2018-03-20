package com.example.juvek.myproject;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.ufobeaconsdk.callback.OnConnectSuccessListener;
import com.ufobeaconsdk.callback.OnFailureListener;
import com.ufobeaconsdk.callback.OnRangingListener;
import com.ufobeaconsdk.callback.OnScanSuccessListener;
import com.ufobeaconsdk.callback.OnSuccessListener;
import com.ufobeaconsdk.main.RangeType;
import com.ufobeaconsdk.main.UFOBeaconManager;
import com.ufobeaconsdk.main.UFODevice;
import com.ufobeaconsdk.main.UFODeviceType;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {
    UFOBeaconManager ufoBeaconManager;
    private String TAG = "Main";
    Timer t;
    boolean enabled = false;
    boolean isConnected = false;
    private Ringtone r;
    Button b1, b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ufoBeaconManager = new UFOBeaconManager(getApplicationContext());
        b1 = (Button) findViewById(R.id.button);
        b2 = (Button) findViewById(R.id.button2);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timerFunc();

            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                t.cancel();

                stopScan();
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


0                    }
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


}
