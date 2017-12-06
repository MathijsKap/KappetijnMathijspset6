package com.example.hellvox.kappetijnmathijspset6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Logon extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logon);

        mAuth = FirebaseAuth.getInstance();
        Toast.makeText(getApplicationContext(), "1", Toast.LENGTH_SHORT).show();

        TextView tv = findViewById(R.id.textView5);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            
        }

    }


    public void addToDB(View view) {

    }

    private void gotToCategory() {
        startActivity(new Intent(Logon.this, SelectTrivia.class));
    }

    private void goToHome() {
        Toast.makeText(getApplicationContext(), "Not logged in", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(Logon.this, Home.class));
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }
}
