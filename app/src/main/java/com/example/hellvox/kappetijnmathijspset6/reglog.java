package com.example.hellvox.kappetijnmathijspset6;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class reglog extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    Button register;
    Button login;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reglog);

        mAuth = FirebaseAuth.getInstance();

        mDatabase = FirebaseDatabase.getInstance().getReference();

        login = (Button) findViewById(R.id.Login);
        register = (Button) findViewById(R.id.Register);
        progressBar = findViewById(R.id.progressBar5);
        register.setOnClickListener(new registerListener());
        login.setOnClickListener(new loginListener());


    }

    private class registerListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            progressBar.setVisibility(View.VISIBLE);
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
                createUser(email, password, nickname);
            }
        }
    }

    public void createUser(String email, String password, final String nickname) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("sign", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            String userId = user.getUid();
                            mDatabase.child("users").child(userId).child("username").setValue(nickname);
                            mDatabase.child("users").child(userId).child("karma").setValue(0);
                            Toast.makeText(getApplicationContext(), "Succes!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(reglog.this, Logon.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("fail", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    }
                });

    }

    private class loginListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            login_dialog fragment = new login_dialog();
            fragment.show(ft, "dialog");
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
