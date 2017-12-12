package com.example.hellvox.kappetijnmathijspset6;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;

public class Questions extends AppCompatActivity {

    ArrayList<Trivia> Questions = new ArrayList<>();
    JSONArray array;
    ArrayList<String> answers = new ArrayList<>();
    TriviaListAdapater adapater;
    int number;
    int score;
    int amount;
    int correct;
    String difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        Intent intent = getIntent();


        Questions = intent.getExtras().getParcelableArrayList("Questions");
        number = intent.getIntExtra("number", 0);
        score = intent.getIntExtra("score", 0);
        amount = intent.getIntExtra("max", 5);
        correct = intent.getIntExtra("correct", 0);
        difficulty = intent.getStringExtra("difficulty");

        TextView textView = findViewById(R.id.question_Question);
        ListView answerss = findViewById(R.id.question_answers);


        textView.setText(Html.fromHtml(Questions.get(number).getQuestion()).toString());
        try {
            array = new JSONArray(Html.fromHtml(Questions.get(number).getIncorrect()).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0;i<array.length();i++) {
            answers.add(array.optString(i));
        }
        answers.add(Html.fromHtml(Questions.get(number).getcorrect_answer()).toString());
        Collections.shuffle(answers);

        adapater = new TriviaListAdapater(getApplicationContext(),R.layout.row_todo,answers);
        answerss.setAdapter(adapater);

        answerss.setOnItemClickListener(new GoButtonClickListener());
    }


    private class GoButtonClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Object object  = parent.getItemAtPosition(position);
            String Useranswer = object.toString();
            if (Useranswer.equals(Html.fromHtml(Questions.get(number).getcorrect_answer()).toString())) {
                switch (difficulty) {
                    case "easy":
                        correct++;
                        score++;
                        break;
                    case "medium":
                        correct++;
                        score += 2;
                        break;
                    case "hard":
                        correct++;
                        score += 3;
                        break;
                }
            }
            number++;
            if (number < amount) {
                Intent intentNext = new Intent(Questions.this, Questions.class);
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("Questions", Questions);
                intentNext.putExtras(bundle);
                intentNext.putExtra("number", number);
                intentNext.putExtra("score", score);
                intentNext.putExtra("difficulty", difficulty);
                intentNext.putExtra("correct", correct);
                startActivity(intentNext);
                finish();
            } else {
                Intent intentNext = new Intent(Questions.this, Complete.class);
                intentNext.putExtra("score", score);
                intentNext.putExtra("amount", amount);
                intentNext.putExtra("correct", correct);
                startActivity(intentNext);
                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to stop?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Questions.this.finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
