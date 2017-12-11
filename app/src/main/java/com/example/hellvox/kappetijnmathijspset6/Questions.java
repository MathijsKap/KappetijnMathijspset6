package com.example.hellvox.kappetijnmathijspset6;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.ArrayList;

public class Questions extends AppCompatActivity {

    ArrayList<Trivia> Questions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        Questions = this.getIntent().getExtras().getParcelableArrayList("Questions");

        TextView textView = findViewById(R.id.question_Question);
        textView.setText(Questions.get(0).getQuestion());
    }
}
