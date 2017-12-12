package com.example.hellvox.kappetijnmathijspset6;

import android.content.Intent;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Complete extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    int score;
    int amount;
    int correct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);
        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);
        amount = intent.getIntExtra("amount", 0);
        correct = intent.getIntExtra("correct", 0);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        TextView scoreText = findViewById(R.id.Complete_correct);
        Button backbutton = findViewById(R.id.Complete_back);

        float temp = (float)correct/amount;
        scoreText.setText("Your score this game: "+correct + "/" + amount);


        setImage(temp);

        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();
        readScore(userId);

        backbutton.setOnClickListener(new backListener());

    }

    private void setImage(double temp) {
        ImageView image = findViewById(R.id.imageView2);
        if (temp == 1) {
            image.setImageResource(R.drawable.perfect);
        }
        if (temp >= 0.7 && temp < 1 ) {
            image.setImageResource(R.drawable.welldone);
        }
        if (temp > 0.4 && temp < 0.7 ) {
            image.setImageResource(R.drawable.notbad);
        }
        if (temp <= 0.4 ) {
            image.setImageResource(R.drawable.badscore);
        }
    }

    private void updateScore(String userId) {
        mDatabase.child("users").child(userId).child("karma").setValue(score);
    }

    public void readScore(final String id) {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User aUser = dataSnapshot.child("users").child(id).getValue(User.class);
                score = score + aUser.karma;
                TextView totalKarma = findViewById(R.id.Complete_karma);
                totalKarma.setText("Total Karma:" + score);
                updateScore(id);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.addListenerForSingleValueEvent(postListener);
    }

    private class backListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            finish();
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
