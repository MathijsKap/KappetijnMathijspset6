package com.example.hellvox.kappetijnmathijspset6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

public class SelectTrivia extends AppCompatActivity {

    Spinner categorySpinner;
    Spinner difficultySpinner;
    EditText editTextAmount;
    int category;
    String difficulty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_trivia);


        categorySpinner = findViewById(R.id.Category);
        difficultySpinner = findViewById(R.id.Difficulty);
        editTextAmount = findViewById(R.id.NumberAmount);

        additemsonspinner();
        categorySpinner.setOnItemSelectedListener(new categorySpinnerList());
        difficultySpinner.setOnItemSelectedListener(new difficultySpinnerList());

        Button next = (Button) findViewById(R.id.next);
        next.setOnClickListener(new nextListener());
    }

    public void additemsonspinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.difficulty_spinner, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(adapter2);
    }

    private class nextListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String value = editTextAmount.getText().toString();
            int amount;
            if (!value.equals("")) {
                amount = Integer.parseInt(value);
            } else amount = 5;
            Intent intent = new Intent(SelectTrivia.this, Questions.class);
            intent.putExtra("category", category);
            intent.putExtra("difficulty", difficulty);
            intent.putExtra("amount", amount);
            startActivity(intent);
        }
    }

    private class difficultySpinnerList implements AdapterView.OnItemSelectedListener {
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            switch (position) {
                case 0:
                    difficulty = "easy";
                    break;
                case 1:
                    difficulty = "medium";
                    break;
                case 2:
                    difficulty = "hard";
                    break;
            }
        }
    }

    private class categorySpinnerList implements AdapterView.OnItemSelectedListener {
        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }

        public void onItemSelected(AdapterView<?> parent, View v, int position, long id) {
            switch (position) {
                case 0:
                    category = 9;
                    break;
                case 1:
                    category = 11;
                    break;
                case 2:
                    category = 12;
                    break;
                case 3:
                    category = 15;
                    break;
                case 4:
                    category = 21;
                    break;
                case 5:
                    category = 27;
                    break;
            }
        }
    }
}
