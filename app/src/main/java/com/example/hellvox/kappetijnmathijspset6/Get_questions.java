package com.example.hellvox.kappetijnmathijspset6;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;

public class Get_questions extends AppCompatActivity {

    JSONObject ObjectArray;
    private FirebaseAuth mAuth;
    ProgressBar progressBar;
    ArrayList<Trivia> Questions = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_questions);
        Intent intent = getIntent();
        int amount = intent.getIntExtra("amount", 5);
        int category = intent.getIntExtra("category", 0);
        String difficulty = intent.getStringExtra("difficulty");
        mAuth = FirebaseAuth.getInstance();

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
                        Intent intent = new Intent(Get_questions.this, Questions.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList("Questions", Questions);
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Something went wrong, try again", Toast.LENGTH_SHORT).show();
                    }
                });
        // Access the RequestQueue through your singleton class.
        MySingleton.getInstance(this).addToRequestQueue(jsObjRequest);
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
