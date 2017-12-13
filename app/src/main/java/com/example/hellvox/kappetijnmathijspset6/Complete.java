package com.example.hellvox.kappetijnmathijspset6;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Complete extends AppCompatActivity {

    // Initialize variables
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    TextView scoreText;
    TextView scoreNumber;
    TextView karmaEarned;
    Button backbutton;
    int score;
    int amount;
    int correct;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete);
        context = getApplicationContext();

        // Get the variables needed from the previous activity.
        Intent intent = getIntent();
        score = intent.getIntExtra("score", 0);
        amount = intent.getIntExtra("amount", 0);
        correct = intent.getIntExtra("correct", 0);

        // Setup the user and database connection.
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Assign the views to the variables.
        setViews();

        // Calculate the user score for image and percentage.
        float score_image = (float)correct/amount;
        int score_percent = Math.round(score_image*100);

        // Set the content to the views.
        scoreText.setText(R.string.Complete_score);
        scoreNumber.setText(score_percent+"%");
        karmaEarned.setText("Karma earned: "+score);
        setImage(score_image);

        // Update user score in the database.
        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();
        readScore(userId);

        // Set listeners.
        backbutton.setOnClickListener(new backListener());
    }

    private void setViews() {
        scoreText = findViewById(R.id.Complete_correct);
        scoreNumber = findViewById(R.id.Complete_percent);
        karmaEarned = findViewById(R.id.Complete_earned);
        backbutton = findViewById(R.id.Complete_back);
    }

    // Function to set the image to the view, depending on the user score.
    private void setImage(double score_image) {
        ImageView image = findViewById(R.id.imageView2);
        if (score_image == 1) {
            image.setImageResource(R.drawable.perfect);
        }
        if (score_image >= 0.7 && score_image < 1 ) {
            image.setImageResource(R.drawable.welldone);
        }
        if (score_image > 0.4 && score_image < 0.7 ) {
            image.setImageResource(R.drawable.notbad);
        }
        if (score_image <= 0.4 ) {
            image.setImageResource(R.drawable.badscore);
        }
    }

    // Function to get the old user score.
    public void readScore(final String id) {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User aUser = dataSnapshot.child("users").child(id).getValue(User.class);
                score = score + aUser.karma;
                TextView totalKarma = findViewById(R.id.Complete_karma);
                totalKarma.setText("Total Karma: " + score);
                updateScore(id);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.addListenerForSingleValueEvent(postListener);
    }

    // Function replace the user score with the new one in the database.
    private void updateScore(String userId) {
        mDatabase.child("users").child(userId).child("karma").setValue(score);
    }

    // Listener for the button on the layout
    private class backListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            finish();
        }
    }

    // Create the menu.
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
    public boolean onOptionsItemSelected(MenuItem log) {
        // Handle item selection
        switch (log.getItemId()) {
            case R.id.Logout:
                Functions.Logout(context, mAuth);
                return true;
            case R.id.Login:
                FragmentTransaction fragt = getSupportFragmentManager().beginTransaction();
                login_dialog fragment = new login_dialog();
                fragment.show(fragt, "dialog");
                return true;
            default:
                return super.onOptionsItemSelected(log);
        }
    }
}
