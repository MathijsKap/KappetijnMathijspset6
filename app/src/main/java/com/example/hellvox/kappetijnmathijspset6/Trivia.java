package com.example.hellvox.kappetijnmathijspset6;

import android.os.Parcel;
import android.os.Parcelable;

public class Trivia implements Parcelable {

    // Initialize variables
    public String question;
    public String correctAnswer;
    public String incorrect;

    // Construct the food object.
    public Trivia(String Question, String correct_answer, String Incorrect) {
        this.question = Question;
        this.correctAnswer = correct_answer;
        this.incorrect = Incorrect;
    }

    protected Trivia(Parcel in) {
        question = in.readString();
        correctAnswer = in.readString();
        incorrect = in.readString();
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
        return question;
    }

    public void setQuestion(String Question) {
        this.question = Question;
    }

    public String getcorrect_answer() {
        return correctAnswer;
    }

    public void setcorrect_answer(String correct_answer) {
        this.correctAnswer = correct_answer;
    }

    public String getIncorrect() {
        return incorrect;
    }

    public void setIncorrect(String Incorrect) {
        this.incorrect = Incorrect;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(question);
        parcel.writeString(correctAnswer);
        parcel.writeString(incorrect);
    }
}
