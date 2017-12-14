package com.example.hellvox.kappetijnmathijspset6;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Random;

public class reglog extends AppCompatActivity {

    // Initialize variables
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    Button register;
    Button login;
    ProgressBar progressBar;
    TextView guest;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reglog);
        context = getApplicationContext();

        // Setup the user and database connection.
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Assign the views to the variables.
        login = (Button) findViewById(R.id.Login);
        register = (Button) findViewById(R.id.Register);
        progressBar = findViewById(R.id.progressBar5);
        guest = findViewById(R.id.reglog_guest);
        guest.setText(Html.fromHtml("Play as <font color=#2196F3>guest</font>"));

        // Set listeners
        register.setOnClickListener(new registerListener());
        login.setOnClickListener(new loginListener());
        guest.setOnClickListener(new guestListener());
    }

    // Function to get all the variables and pass them to the register function.
    private class registerListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            EditText username = findViewById(R.id.Username);
            EditText pass = findViewById(R.id.Password);
            EditText name = findViewById(R.id.Name);
            String email = username.getText().toString();
            String password = pass.getText().toString();
            String nickname = name.getText().toString();
            if (password.length() < 6) {
                Toast.makeText(getApplicationContext(), "Password to short!", Toast.LENGTH_SHORT).show();
            } else if (nickname.length() < 1) {
                Toast.makeText(getApplicationContext(), "Please fill in your name", Toast.LENGTH_SHORT).show();
            }
            else if (email.length() < 1) {
                Toast.makeText(getApplicationContext(), "Please fill in your email", Toast.LENGTH_SHORT).show();
            }
            else {
                createUser(email, password, nickname, 0);
                progressBar.setVisibility(View.VISIBLE);
            }
        }
    }

    // Function to make a guest account
    private class guestListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            if (!Functions.isOnline(context)) {
                Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                String email = getSaltString(15) + "@gmail.com";
                String password = getSaltString(8);
                String nickname = "guest" + getSaltString(5);
                createUser(email, password, nickname, 1);
            }
        }
    }

    // Function to make a salt
    // source: https://stackoverflow.com/questions/45841500/generate-random-emails
    private String getSaltString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random random = new Random();
        while (salt.length() < length) {
            int index = (int) (random.nextFloat() * chars.length());
            salt.append(chars.charAt(index));
        }
        return salt.toString();

    }

    // Function provided by google to create an user.
    public void createUser(String email, String password, final String nickname, final int guest) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userId = user.getUid();
                            mDatabase.child("users").child(userId).child("username").setValue(nickname);
                            mDatabase.child("users").child(userId).child("karma").setValue(0);
                            mDatabase.child("users").child(userId).child("guest").setValue(guest);
                            Toast.makeText(getApplicationContext(), "Have fun!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(reglog.this, Logon.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            progressBar.setVisibility(View.INVISIBLE);
                            if (!Functions.isOnline(context)) {
                                Toast.makeText(context, "No internet connection", Toast.LENGTH_SHORT).show();
                            } else Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    // Function to pop up the login dialog.
    private class loginListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            login_dialog fragment = new login_dialog();
            fragment.show(ft, "dialog");
        }
    }

    // Create the menu
    @Override
    public boolean onCreateOptionsMenu(Menu unit) {
        MenuInflater inflater = getMenuInflater();
        if (Functions.userState(mAuth)) {
            inflater.inflate(R.menu.menu_logout, unit);
        } else {
            inflater.inflate(R.menu.menu_login, unit);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem unit) {
        // Handle item selection
        switch (unit.getItemId()) {
            case R.id.Logout:
                Functions.Logout(context, mAuth);
                finish();
                return true;
            case R.id.Login:
                FragmentTransaction fragmentTransactiont = getSupportFragmentManager().beginTransaction();
                login_dialog fragment = new login_dialog();
                fragment.show(fragmentTransactiont, "dialog");
                return true;
            case R.id.Rules:
                FragmentTransaction fragtransRules = getSupportFragmentManager().beginTransaction();
                info_dialog infofragment = new info_dialog();
                infofragment.show(fragtransRules, "info");
                return true;
            default:
                return super.onOptionsItemSelected(unit);
        }
    }
}
