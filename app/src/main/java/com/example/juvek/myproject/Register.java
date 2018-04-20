package com.example.juvek.myproject;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

public class Register extends AppCompatActivity {


    LoginDataBaseAdapter loginDataBaseAdapter;
    EditText user,pass,phone;
    Button b1;
    private EditText email;
    private AwesomeValidation awesomeValidation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        awesomeValidation=new AwesomeValidation(ValidationStyle.BASIC);
        loginDataBaseAdapter=new LoginDataBaseAdapter(this);
        loginDataBaseAdapter=loginDataBaseAdapter.open();
        b1=(Button)findViewById(R.id.btn);

        email=(EditText)findViewById(R.id.email);
        user=(EditText)findViewById(R.id.uname);
        pass=(EditText)findViewById(R.id.pass);
        phone=(EditText)findViewById(R.id.phone);
        awesomeValidation.addValidation(this, R.id.uname, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.email, "^[A-Za-z0-9._-]+@[a-z]+\\.[a-z]+$", R.string.emailerror);
        awesomeValidation.addValidation(this, R.id.phone, "^[2-9]{2}[0-9]{8}$", R.string.mobileerror);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(awesomeValidation.validate()){
                loginDataBaseAdapter.insertEntry(user.getText().toString(),pass.getText().toString(),email.getText().toString(),phone.getText().toString());
                Intent intent=new Intent(getApplicationContext(), Login.class);
                startActivity(intent);}
            }
        });



    }

}
