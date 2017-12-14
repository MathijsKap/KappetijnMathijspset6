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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SelectTrivia extends AppCompatActivity {

    // Initialize variables
    private FirebaseAuth mAuth;
    private JSONObject mObjectArray;
    private ArrayList<Trivia> Questions = new ArrayList<>();
    private Button mNext;
    private Spinner mCategorySpinner;
    private Spinner mDifficultySpinner;
    private EditText mEditTextAmount;
    private ProgressBar mProgressBar;
    private ConstraintLayout mConstraintLayout;
    private int mCategory;
    private String mDifficulty;
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_trivia);
        mContext = getApplicationContext();

        // Setup the user and database connection.
        mAuth = FirebaseAuth.getInstance();

        // Assign the views to the variables.
        assignViews();
        addItemsOnSpinner();

        // Set listeners.
        mCategorySpinner.setOnItemSelectedListener(new categorySpinnerList());
        mDifficultySpinner.setOnItemSelectedListener(new difficultySpinnerList());
        mNext.setOnClickListener(new nextListener());
    }

    private void assignViews() {
        mCategorySpinner = findViewById(R.id.Category);
        mDifficultySpinner = findViewById(R.id.Difficulty);
        mEditTextAmount = findViewById(R.id.NumberAmount);
        mProgressBar = findViewById(R.id.progressBar4);
        mConstraintLayout = findViewById(R.id.select_layout);
        mNext = findViewById(R.id.next);
    }

    // Function to add items on the Spinner objects.
    private void addItemsOnSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mCategorySpinner.setAdapter(adapter);

        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.difficulty_spinner, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDifficultySpinner.setAdapter(adapter2);
    }

    // Listener to go to the questions and get all the content needed.
    private class nextListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String value = mEditTextAmount.getText().toString();
            final int mAmount;
            if (!value.equals("")) {
                mAmount = Integer.parseInt(value);
            } else mAmount = 5;
            mConstraintLayout.setBackgroundColor(Color.parseColor("#CFD8DC"));
            mProgressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            final String url = "https://opentdb.com/api.php?amount="+mAmount+"&category="+mCategory+
                    "&difficulty="+mDifficulty+"&type=multiple";
            getTrivia(url, mAmount);
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
                            mObjectArray = array.optJSONObject(i);
                            Questions.add(new Trivia(mObjectArray.optString("question"),
                                    mObjectArray.optString("correct_answer"),
                                    mObjectArray.optJSONArray("incorrect_answers").toString()));
                        }
                        goToQuestions(amount);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {onError();}
                });
        MySingleton.getInstance(mContext).addToRequestQueue(jsObjRequest);
    }

    // Function to go to the questions activity.
    private void goToQuestions(int amount) {
        Intent intent = new Intent(SelectTrivia.this, Questions.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("Questions", Questions);
        intent.putExtras(bundle);
        intent.putExtra("amount", amount);
        intent.putExtra("difficulty", mDifficulty);
        startActivity(intent);
        finish();
    }

    private void onError() {
        if (!Functions.isOnline(mContext)) {
            Snackbar.make(findViewById(android.R.id.content), "No internet connection", Snackbar.LENGTH_LONG).show();
        } else {
            Toast.makeText(mContext, "Something went wrong, try again", Toast.LENGTH_SHORT).show();
        }
        mProgressBar.setVisibility(View.INVISIBLE);
        mConstraintLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
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
            Toast.makeText(mContext, "Offline, karma will not be saved",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void saveToSharedPrefs(JSONArray ObjectArray) {
        SharedPreferences prefs = this.getSharedPreferences("old_questions", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("array", ObjectArray.toString());
        editor.apply();
    }

    private void loadFromSharedPrefs() {
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
        goToOfflineQ();
    }

    // Function to go to the questions activity.
    private void goToOfflineQ() {
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
                    mDifficulty = "easy";
                    break;
                case 1:
                    mDifficulty = "medium";
                    break;
                case 2:
                    mDifficulty = "hard";
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
                    mCategory = 9;
                    break;
                case 1:
                    mCategory = 11;
                    break;
                case 2:
                    mCategory = 12;
                    break;
                case 3:
                    mCategory = 15;
                    break;
                case 4:
                    mCategory = 21;
                    break;
                case 5:
                    mCategory = 27;
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
                Functions.Logout(mContext, mAuth);
                return true;
            case R.id.Login:
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                login_dialog fragment = new login_dialog();
                fragment.show(ft, "dialog");
                return true;
            case R.id.Rules:
                FragmentTransaction fragtrans2 = getSupportFragmentManager().beginTransaction();
                info_dialog infofragment = new info_dialog();
                infofragment.show(fragtrans2, "info");
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
