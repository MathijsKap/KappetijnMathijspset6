package com.example.hellvox.kappetijnmathijspset6;

import org.json.JSONArray;

/**
 * Created by HellVox on 4-12-2017.
 */

public class Trivia {

    // Initialize variables
    public String Question;
    public String correct_answer;
    public JSONArray Incorrect;

    // Construct the food object.
    public Trivia(String Question, String correct_answer, JSONArray Incorrect) {
        this.Question = Question;
        this.correct_answer = correct_answer;
        this.Incorrect = Incorrect;
    }

    public String getQuestion() {
        return Question;
    }

    public void setQuestion(String Question) {
        this.Question = Question;
    }

    public String getcorrect_answer() {
        return correct_answer;
    }

    public void setcorrect_answer(String correct_answer) {
        this.correct_answer = correct_answer;
    }

    public JSONArray getIncorrect() {
        return Incorrect;
    }

    public void setIncorrect(JSONArray Incorrect) {
        this.Incorrect = Incorrect;
    }
}
