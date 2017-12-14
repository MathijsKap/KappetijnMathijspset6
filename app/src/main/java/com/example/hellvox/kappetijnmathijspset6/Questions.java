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

    // Initialize variables
    private ArrayList<Trivia> Questions = new ArrayList<>();
    private ArrayList<String> answers = new ArrayList<>();
    private JSONArray mArray;
    private TextView mQuestionField;
    private int mNumber;
    private int mScore;
    private int mAmount;
    private int mCorrect;
    private String mDifficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);

        // Assign all the variables from the previous activity.
        Intent intent = getIntent();
        Questions = intent.getExtras().getParcelableArrayList("Questions");
        mNumber = intent.getIntExtra("number", 0);
        mScore = intent.getIntExtra("score", 0);
        mAmount = intent.getIntExtra("amount", 5);
        mCorrect = intent.getIntExtra("correct", 0);
        mDifficulty = intent.getStringExtra("difficulty");
        if (mDifficulty == null) mDifficulty = "easy";

        // Assign the views to the variables.
        mQuestionField = findViewById(R.id.question_Question);
        ListView answersList = findViewById(R.id.question_answers);

        // Set the questions and answers to the views.
        setQandA();
        TriviaListAdapater adapater = new TriviaListAdapater(getApplicationContext(),
                R.layout.row_trivia, answers);
        answersList.setAdapter(adapater);

        // Set listener
        answersList.setOnItemClickListener(new AnswerClickListener());
    }

    // Function to set the question and collect all the possible answers.
    private void setQandA() {
        mQuestionField.setText(Html.fromHtml(Questions.get(mNumber).getQuestion()).toString());
        try {
            mArray = new JSONArray(Html.fromHtml(Questions.get(mNumber).getIncorrect()).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        for (int i = 0; i< mArray.length(); i++) {
            answers.add(mArray.optString(i));
        }
        answers.add(Html.fromHtml(Questions.get(mNumber).getcorrect_answer()).toString());
        Collections.shuffle(answers);
    }

    // Listener to get the users Answer and get all the variables for the next question.
    private class AnswerClickListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Object object  = parent.getItemAtPosition(position);
            String Useranswer = object.toString();
            if (Useranswer.equals(Html.fromHtml(Questions.get(mNumber).getcorrect_answer())
                    .toString())) {
               counter();
            }
            mNumber++;
            // Only start the next question if there is one
            if (mNumber < mAmount) {
                startNextQuestion();
            } else {
                startCompelte();
            }
        }
    }

    // Function to start the next question.
    private void startNextQuestion() {
        Intent intentNext = new Intent(Questions.this, Questions.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("Questions", Questions);
        intentNext.putExtras(bundle);
        intentNext.putExtra("number", mNumber);
        intentNext.putExtra("score", mScore);
        intentNext.putExtra("difficulty", mDifficulty);
        intentNext.putExtra("correct", mCorrect);
        intentNext.putExtra("amount", mAmount);
        startActivity(intentNext);
        finish();
    }

    // Function to start the last activity.
    private void startCompelte() {
        Intent intentNext = new Intent(Questions.this, Complete.class);
        intentNext.putExtra("score", mScore);
        intentNext.putExtra("amount", mAmount);
        intentNext.putExtra("correct", mCorrect);
        startActivity(intentNext);
        finish();
    }

    // Function to count the score and current question.
    private void counter() {
        switch (mDifficulty) {
            case "easy":
                mCorrect++;
                mScore++;
                break;
            case "medium":
                mCorrect++;
                mScore += 2;
                break;
            case "hard":
                mCorrect++;
                mScore += 3;
                break;
        }
    }

    // Function to alert the user if they want to stop in the middle of a trivia.
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
