package com.example.hellvox.kappetijnmathijspset6;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;



import com.google.firebase.database.FirebaseDatabase;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        Button newUser = (Button) findViewById(R.id.New);
        Button login = (Button) findViewById(R.id.Login);

        newUser.setOnClickListener(new newUserListener());
        login.setOnClickListener(new loginListener());


    }

    private class newUserListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Home.this, SelectTrivia.class);
            startActivity(intent);
        }
    }

    private class loginListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }
}
