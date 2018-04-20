package com.example.juvek.myproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;

public class ContactsEdit extends AppCompatActivity {
 LoginDataBaseAdapter dbh;
    EditText name,phone;
    Button submit,delete;
    AwesomeValidation awesomeValidation;
    Bundle b;
    int id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        dbh=new LoginDataBaseAdapter(this);
        awesomeValidation=new AwesomeValidation(ValidationStyle.BASIC);
        name=(EditText)findViewById(R.id.name);
        phone=(EditText)findViewById(R.id.phoneno);
        submit=(Button)findViewById(R.id.save);
        delete=(Button)findViewById(R.id.delete);
        if(getIntent().getExtras()!=null){
            b=getIntent().getExtras();
            id=b.getInt("ID");
        }
         final Contact c=dbh.getContact(id);
        name.setText(c.getName());
        phone.setText(c.getPhoneNumber());


        awesomeValidation.addValidation(this, R.id.name, "^[A-Za-z\\s]{1,}[\\.]{0,1}[A-Za-z\\s]{0,}$", R.string.nameerror);
        awesomeValidation.addValidation(this, R.id.phoneno, "^[2-9]{2}[0-9]{8}$", R.string.mobileerror);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(awesomeValidation.validate()){

                 dbh.updateContact(new Contact(name.getText().toString(),phone.getText().toString()),id);
                    Toast.makeText(getApplicationContext(),"Contact added successfully",Toast.LENGTH_SHORT).show();
                    name.setText("");
                    phone.setText("");
                }
            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbh.deleteContact(c);
                Toast.makeText(getApplicationContext(),"Deleted successfully!",Toast.LENGTH_SHORT).show();
                Intent intent=new Intent(getApplicationContext(),ViewContact.class);
                startActivity(intent);
                finish();



            }
        });

    }
}
