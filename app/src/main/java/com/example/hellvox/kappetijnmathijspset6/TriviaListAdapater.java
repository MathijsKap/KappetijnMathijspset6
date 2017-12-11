package com.example.hellvox.kappetijnmathijspset6;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.ArrayList;

public class TriviaListAdapater extends ArrayAdapter<String> {

    // Initialize variables
    private Context mContext;
    int mResource;

    public TriviaListAdapater(Context context, int resource, ArrayList<String> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    // Function to set a food object into a item from a list.
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String answer = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView answers = convertView.findViewById(R.id.logon_top);
        TextView number = convertView.findViewById(R.id.row_Number);

        answers.setText(answer);
        number.setText(toAlphabetic(position));
        return convertView;
    }

    private String toAlphabetic(int i) {
        if( i<0 ) {
            return "-"+toAlphabetic(-i-1);
        }

        int quot = i/26;
        int rem = i%26;
        char letter = (char)((int)'A' + rem);
        if( quot == 0 ) {
            return ""+letter;
        } else {
            return toAlphabetic(quot-1) + letter;
        }
    }
}
