package com.example.hellvox.kappetijnmathijspset6;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by HellVox on 11-12-2017.
 */

public class UserTopAdapter extends ArrayAdapter<UserTop> {

    // Initialize variables
    private Context mContext;
    int mResource;

    public UserTopAdapter(Context context, int resource, ArrayList<UserTop> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    // Function to set a User object into a item from a list.
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String username = getItem(position).getUsername();
        Long karma = getItem(position).getKarma();

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView karmaa = convertView.findViewById(R.id.row_Karma);
        TextView usernamee = convertView.findViewById(R.id.row_Username);
        TextView rank = convertView.findViewById(R.id.row_Rank);

        karmaa.setText(""+karma);
        usernamee.setText(username);
        int ranking = position +1;
        rank.setText(""+ranking);
        return convertView;
    }
}