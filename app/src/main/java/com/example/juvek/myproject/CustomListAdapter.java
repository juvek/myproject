package com.example.juvek.myproject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;



import java.util.ArrayList;
import java.util.List;

public class CustomListAdapter extends BaseAdapter {
    private LayoutInflater inflater = null;
    public Resources res;
    Contact tempValues = null;

    android.support.v7.app.AlertDialog.Builder alertDialog;
    Object p;
    View view;
    private Activity activity;
    private List<Contact> data;
    private Context context;
    private Context clickcontext;


    public CustomListAdapter(Activity a, List<Contact> d) {
        //Take passed values The phone having
        activity = a;
        data = d;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    //return arraylist size
    public int getCount() {

        if (data.size() <= 0)
            return 1;
        return data.size();
    }

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    //inflate ach list row
    public View getView(final int position, View convertView, ViewGroup parent) {
        View vi = convertView;
        final ViewHolder holder;
        if (convertView == null && data.size() > 0) {
            vi = inflater.inflate(R.layout.listrow, parent, false);
            holder = new ViewHolder();
            holder.gpname = (TextView) vi.findViewById(R.id.gpname);
            holder.status = (TextView) vi.findViewById(R.id.status);
            vi.setTag(holder);
            tempValues = null;
            tempValues = (Contact) data.get(position);
            p = data.get(position);
            holder.gpname.setText(((Contact) p).getName());
            holder.status.setText(((Contact) p).getPhoneNumber());
            vi.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(view.getContext(),ContactsEdit.class);
                    Bundle b=new Bundle();
                    b.putInt("ID",tempValues.getID());
                    intent.putExtras(b);
                    view.getContext().startActivity(intent);
                    activity.finish();

                }
            });
        }
        return vi;
    }

    //view holder for layout row
    public static class ViewHolder {
        public TextView gpname, status;
    }


}