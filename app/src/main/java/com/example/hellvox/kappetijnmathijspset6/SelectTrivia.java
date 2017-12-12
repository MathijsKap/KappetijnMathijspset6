package com.example.hellvox.kappetijnmathijspset6;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class SelectTrivia extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Spinner categorySpinner;
    Spinner difficultySpinner;
    EditText editTextAmount;
    int category;
    String difficulty;
    JSONObject ObjectArray;
    ArrayList<Trivia> Questions = new ArrayList<>();
    ProgressBar progressBar;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_trivia);

        mAuth = FirebaseAuth.getInstance();

        categorySpinner = findViewById(R.id.Category);
        difficultySpinner = findViewById(R.id.Difficulty);
        editTextAmount = findViewById(R.id.NumberAmount);
        progressBar = findViewById(R.id.progressBar4);
        constraintLayout = findViewById(R.id.select_layout);

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
            final int amount;
            if (!value.equals("")) {
                amount = Integer.parseInt(value);
            } else amount = 5;
            constraintLayout.setBackgroundColor(Color.parseColor("#CFD8DC"));
            progressBar.setVisibility(View.VISIBLE);
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
            final String url = "https://opentdb.com/api.php?amount="+amount+"&category="+category+"&difficulty="+difficulty+"&type=multiple";
            JsonObjectRequest jsObjRequest = new JsonObjectRequest
                    (Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            JSONArray array = response.optJSONArray("results");
                            for(int i=0; i<array.length(); i++) {
                                ObjectArray = array.optJSONObject(i);
                                Questions.add(new Trivia(ObjectArray.optString("question"), ObjectArray.optString("correct_answer"), ObjectArray.optJSONArray("incorrect_answers").toString()));
                            }
                            Intent intent = new Intent(SelectTrivia.this, Questions.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelableArrayList("Questions", Questions);
                            intent.putExtras(bundle);
                            intent.putExtra("max", amount);
                            startActivity(intent);
                            finish();
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (!Logon.isOnline(getApplicationContext())) {
                                Snackbar.make(findViewById(android.R.id.content), "No internet connection", Snackbar.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Something went wrong, try again", Toast.LENGTH_SHORT).show();
                            }
                            progressBar.setVisibility(View.INVISIBLE);
                            constraintLayout.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                        }
                    });
            // Access the RequestQueue through your singleton class.
            MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsObjRequest);
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

    public void Logout() {
        FirebaseAuth.getInstance().signOut();
        //Snackbar.make(this.findViewById(android.R.id.content), "Logout Successful", Snackbar.LENGTH_LONG).show();
        Toast.makeText(this, "Logout succesful",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, reglog.class);
        startActivity(intent);
        finish();
    }

    public boolean userState() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser != null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        if (userState()) {
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
                Logout();
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
