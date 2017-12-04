package com.example.hellvox.kappetijnmathijspset6;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class logon extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logon);

        mAuth = FirebaseAuth.getInstance();
        setListener();

        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    private void setListener() {

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser currentUser = mAuth.getCurrentUser();

                if (currentUser != null) {
                    gotToCategory();
                } else {
                    goToHome();
                }
            }
        };
    }

    private void gotToCategory() {
        startActivity(new Intent(logon.this, Select.class));
    }

    private void goToHome() {
        Toast.makeText(getApplicationContext(), "Not logged in", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(logon.this, Home.class));
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
}
