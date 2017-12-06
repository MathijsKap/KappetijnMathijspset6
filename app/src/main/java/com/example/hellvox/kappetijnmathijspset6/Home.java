package com.example.hellvox.kappetijnmathijspset6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Home extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Button newUser;
    Button login;
    Button start;
    View view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        newUser = (Button) findViewById(R.id.New);
        login = (Button) findViewById(R.id.Login);
        start = (Button) findViewById(R.id.StartButton);

        newUser.setOnClickListener(new newUserListener());
        login.setOnClickListener(new loginListener());
        start.setOnClickListener(new startListener());


    }

    private class newUserListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Home.this, Register.class);
            startActivity(intent);
        }
    }

    private class loginListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Home.this, Login.class);
            startActivity(intent);
        }
    }

    private class startListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Home.this, SelectTrivia.class);
            startActivity(intent);
        }
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        if (userState()) {
            login.setVisibility(View.GONE);
            newUser.setVisibility(View.GONE);
            start.setVisibility(View.VISIBLE);
        }
    }
    public void Logout() {
        FirebaseAuth.getInstance().signOut();
        //Snackbar.make(this.findViewById(android.R.id.content), "Logout Successful", Snackbar.LENGTH_LONG).show();
        Toast.makeText(this, "Logout succesful",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this, Home.class);
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
                Intent intent = new Intent(Home.this, Login.class);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
