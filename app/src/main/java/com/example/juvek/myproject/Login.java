package com.example.juvek.myproject;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    LoginDataBaseAdapter loginDataBaseAdapter;
    EditText user, pass, phone;
    Button b1, b2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
      /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getApplicationContext().getResources().getColor(R.color.limegreen));
            // window.setStatusBarColor(R.color.myPrimaryDarkColor);

        }*/
        loginDataBaseAdapter = new LoginDataBaseAdapter(this);
        loginDataBaseAdapter = loginDataBaseAdapter.open();
        b1 = (Button) findViewById(R.id.login);
        user = (EditText) findViewById(R.id.uname);
        pass = (EditText) findViewById(R.id.pass);
        phone = (EditText) findViewById(R.id.phone);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
            }
        });
        b2 = (Button) findViewById(R.id.btn);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!TextUtils.isEmpty(phone.getText().toString()) && !TextUtils.isEmpty(pass.getText().toString())) {



                    String log = loginDataBaseAdapter.getSinlgeEntry(phone.getText().toString());
                    Log.i("Tag", "tag" + log);
                    if (log.equalsIgnoreCase("Exist")) {

                        SharedPreferences sharedPreferences=getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.putString("phone",phone.getText().toString());
                        editor.apply();
                        editor.commit();
                        startActivity(new Intent(getApplicationContext(),MainActivity.class));
                    } else {
                        Toast.makeText(getApplicationContext(), "Invalid login!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

    }


}
