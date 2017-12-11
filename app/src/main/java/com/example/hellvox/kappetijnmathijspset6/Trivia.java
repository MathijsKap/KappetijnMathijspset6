package com.example.hellvox.kappetijnmathijspset6;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;

/**
 * Created by HellVox on 4-12-2017.
 */

public class Trivia implements Parcelable {

    // Initialize variables
    public String Question;
    public String correct_answer;
    public String Incorrect;

    // Construct the food object.
    public Trivia(String Question, String correct_answer, String Incorrect) {
        this.Question = Question;
        this.correct_answer = correct_answer;
        this.Incorrect = Incorrect;
    }

    protected Trivia(Parcel in) {
        Question = in.readString();
        correct_answer = in.readString();
        Incorrect = in.readString();
    }

    public static final Creator<Trivia> CREATOR = new Creator<Trivia>() {
        @Override
        public Trivia createFromParcel(Parcel in) {
            return new Trivia(in);
        }

        @Override
        public Trivia[] newArray(int size) {
            return new Trivia[size];
        }
    };

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

    public String getIncorrect() {
        return Incorrect;
    }

    public void setIncorrect(String Incorrect) {
        this.Incorrect = Incorrect;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Question);
        parcel.writeString(correct_answer);
        parcel.writeString(Incorrect);
    }
}
