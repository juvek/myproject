package com.example.juvek.myproject;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

public class AddContact extends AppCompatActivity {
 LoginDataBaseAdapter dbh;
    EditText name,phone;
    Button submit;
    AwesomeValidation awesomeValidation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);
        dbh=new LoginDataBaseAdapter(this);
        awesomeValidation=new AwesomeValidation(ValidationStyle.BASIC);
        name=(EditText)findViewById(R.id.name);
        phone=(EditText)findViewById(R.id.phoneno);
        submit=(Button)findViewById(R.id.save);
        awesomeValidation.addValidation(this, R.id.name, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.phoneno, "^[2-9]{2}[0-9]{8}$", R.string.mobileerror);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(awesomeValidation.validate()){

                 dbh.addContact(new Contact(name.getText().toString(),phone.getText().toString()));
                    Toast.makeText(getApplicationContext(),"Contact added successfully",Toast.LENGTH_SHORT).show();
                    name.setText("");
                    phone.setText("");
                }
            }
        });

    }
}
