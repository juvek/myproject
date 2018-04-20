package com.example.juvek.myproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class ViewContact extends AppCompatActivity {
    ListView listView;
    LoginDataBaseAdapter dbh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);
        Button addButton = (Button) findViewById(R.id.addcontacts);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(getApplicationContext(),AddContact.class));
                finish();

            }
        });
        listView=(ListView)findViewById(R.id.list);
        dbh=new LoginDataBaseAdapter(this);
        if(dbh.getContactsCount()>0){

            CustomListAdapter listAdapter=new CustomListAdapter(ViewContact.this,dbh.getAllContacts());
            listView.setAdapter(listAdapter);
        }


    }
}
