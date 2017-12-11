package com.example.hellvox.kappetijnmathijspset6;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;

public class TriviaListAdapater extends ArrayAdapter<Trivia> {

    // Initialize variables
    private Context mContext;
    int mResource;

    public TriviaListAdapater(Context context, int resource, ArrayList<Trivia> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    // Function to set a food object into a item from a list.
    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        String Question = getItem(position).getQuestion();
        String correct_answer = getItem(position).getcorrect_answer();
        String Incorrect = getItem(position).getIncorrect();

        Trivia trivia = new Trivia(Question,correct_answer, Incorrect);

        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView answers = convertView.findViewById(R.id.row_Answer);
        CheckBox box = convertView.findViewById(R.id.check);

        answers.setText(correct_answer);

        return convertView;
    }
}
