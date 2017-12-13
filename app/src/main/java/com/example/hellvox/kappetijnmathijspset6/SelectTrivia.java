package com.example.hellvox.kappetijnmathijspset6;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SelectTrivia extends AppCompatActivity {

    // Initialize variables
    FirebaseAuth mAuth;
    JSONObject ObjectArray;
    ArrayList<Trivia> Questions = new ArrayList<>();
    Button next;
    Spinner categorySpinner;
    Spinner difficultySpinner;
    EditText editTextAmount;
    ProgressBar progressBar;
    ConstraintLayout constraintLayout;
    int category;
    int counter;
    String difficulty;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_trivia);
        context = getApplicationContext();

        // Setup the user and database connection.
        mAuth = FirebaseAuth.getInstance();

        // Assign the views to the variables.
        categorySpinner = findViewById(R.id.Category);
        difficultySpinner = findViewById(R.id.Difficulty);
        editTextAmount = findViewById(R.id.NumberAmount);
        progressBar = findViewById(R.id.progressBar4);
        constraintLayout = findViewById(R.id.select_layout);
        next = findViewById(R.id.next);
        addItemsOnSpinner();

        // Set listeners.
        categorySpinner.setOnItemSelectedListener(new categorySpinnerList());
        difficultySpinner.setOnItemSelectedListener(new difficultySpinnerList());
        next.setOnClickListener(new nextListener());
    }

    // Function to add items on the Spinner objects.
    public void addItemsOnSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.difficulty_spinner, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        difficultySpinner.setAdapter(adapter2);
    }

    // Listener to go to the questions and get all the content needed.
    private class nextListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String value = editTextAmount.getText().toString();
            final int amount;
            if (!value.equals("")) {
                amount = Integer.parseInt(value);
            } else amount = 5;
            constraintLayout.setBackgroundColor(Color.parseColor("#CFD8DC"));
            progressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            final String url = "https://opentdb.com/api.php?amount="+amount+"&category="+category+
                    "&difficulty="+difficulty+"&type=multiple";
            getTrivia(url, amount);
        }
    }

    private void getTrivia(String url, final int amount) {
        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        JSONArray array = response.optJSONArray("results");
                        saveToSharedPrefs(array);
                        for(int i=0; i<array.length(); i++) {
                            ObjectArray = array.optJSONObject(i);
                            Trivia trivia = new Trivia(ObjectArray.optString("question"),
                                    ObjectArray.optString("correct_answer"),
                                    ObjectArray.optJSONArray("incorrect_answers").toString());
                            Questions.add(trivia);
                        }
                        Intent intent = new Intent(SelectTrivia.this, Questions.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("Questions", Questions);
                        intent.putExtras(bundle);
                        intent.putExtra("amount", amount);
                        intent.putExtra("difficulty", difficulty);
                        startActivity(intent);
                        finish();
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {onError();}
                });
        MySingleton.getInstance(context).addToRequestQueue(jsObjRequest);
    }

    private void onError() {
        if (!Functions.isOnline(context)) {
            Snackbar.make(findViewById(android.R.id.content), "No internet connection", Snackbar.LENGTH_LONG).show();
        } else {
            Toast.makeText(context, "Something went wrong, try again", Toast.LENGTH_SHORT).show();
        }
        progressBar.setVisibility(View.INVISIBLE);
        constraintLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        TextView old_questions = findViewById(R.id.Select_old);
        old_questions.setVisibility(View.VISIBLE);
        old_questions.setOnClickListener(new oldquestionListener());

    }

    // Listener to go to the questions and get all the content needed.
    private class oldquestionListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            loadFromSharedPrefs();
            Toast.makeText(context, "Offline, karma will not be saved",
                    Toast.LENGTH_SHORT).show();
        }
    }

    public void saveToSharedPrefs(JSONArray ObjectArray) {
        SharedPreferences prefs = this.getSharedPreferences("old_questions", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("array", ObjectArray.toString());
        editor.apply();
    }

    public void loadFromSharedPrefs() {
        SharedPreferences prefs = this.getSharedPreferences("old_questions", MODE_PRIVATE);
        String s = prefs.getString("array",  null);
        JSONObject Array;
        JSONArray data = null;
        try {
            data = new JSONArray(s);
        } catch (JSONException e) {}
        for(int i=0; i<data.length(); i++) {
            Array = data.optJSONObject(i);
            Trivia trivia = new Trivia(Array.optString("question"),
                    Array.optString("correct_answer"),
                    Array.optJSONArray("incorrect_answers")
                            .toString());
            Questions.add(trivia);
        }
        Intent intent = new Intent(SelectTrivia.this, Questions.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("Questions", Questions);
        intent.putExtras(bundle);
        intent.putExtra("amount", Questions.size());
        startActivity(intent);
        finish();
    }

    // Variables for the spinner classes.
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

    // Function to create the menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (Functions.userState(mAuth)) {
            inflater.inflate(R.menu.menu_logout, menu);
        } else {
            inflater.inflate(R.menu.menu_login, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.Logout:
                Functions.Logout(context, mAuth);
                return true;
            case R.id.Login:
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                login_dialog fragment = new login_dialog();
                fragment.show(ft, "dialog");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
