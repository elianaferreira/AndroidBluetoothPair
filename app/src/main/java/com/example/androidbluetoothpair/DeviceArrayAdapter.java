package com.example.androidbluetoothpair;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DeviceArrayAdapter extends ArrayAdapter<String> {

    public DeviceArrayAdapter(Context context) {
        super(context, android.R.layout.simple_list_item_1);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) getContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View listItemView = convertView;

        if (null == convertView) {
            listItemView = inflater.inflate(
                    android.R.layout.simple_list_item_1,
                    parent,
                    false);
        }

        TextView name = listItemView.findViewById(android.R.id.text1);

        String itemName = getItem(position);
        name.setText(itemName);

        return listItemView;
    }
}