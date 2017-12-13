package com.example.hellvox.kappetijnmathijspset6;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;


public class Logon extends AppCompatActivity {

    // Initialize variables
    FirebaseAuth mAuth;
    FirebaseUser user;
    DatabaseReference mDatabase;
    ProgressBar progressBar;
    ListView topScores;
    TextView topUs;
    TextView guestText;
    Button startTrivia;
    LinearLayout header;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logon);
        context=getApplicationContext();

        // Setup the user and database connection.
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = mAuth.getCurrentUser();

        // Assign the views to the variables.
        startTrivia = findViewById(R.id.Start);
        progressBar = findViewById(R.id.progressBar2);
        topScores = findViewById(R.id.Logon_top);
        topUs = findViewById(R.id.logon_top);
        guestText = findViewById(R.id.textView5);
        header = findViewById(R.id.list_header);

        // Check for internet to inform user.
        if (!Functions.isOnline(context)) {
            Snackbar.make(findViewById(android.R.id.content), "No internet connection",
                    Snackbar.LENGTH_LONG).show();
        }

        // If user is logged in, setup the activity, else logout and go to login screen.
        if (user != null) {
            progressBar.setVisibility(View.VISIBLE);
            String userId = user.getUid();
            readUserFromDB(userId);
            getTopScores();
            getGuest(userId);
        } else goToHome();

        // Set listeners.
        startTrivia.setOnClickListener(new startListener());
    }

    // Function to get the user info from the database and set views accordingly.
    public void readUserFromDB(final String id) {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User aUser = dataSnapshot.child("users").child(id).getValue(User.class);

                TextView tv = findViewById(R.id.Logon_begin);
                TextView karma = findViewById(R.id.Logon_karma);

                tv.setText(getString(R.string.hello_message)+aUser.username + getString(R.string.Ex));
                karma.setText(getString(R.string.your_karma)+aUser.karma);
                topUs.setText(getString(R.string.top_users));
                header.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.INVISIBLE);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.addValueEventListener(postListener);
    }

    // Function to check if the user is a guest and set views accordingly.
    public void getGuest(final String id) {
        DatabaseReference reference = mDatabase.child("users").child(id);
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User aUser = dataSnapshot.getValue(User.class);
                if (aUser.guest == 1) {
                    guestText.setText(Html.fromHtml("You are playing as guest, " +
                            "karma will not be saved! " +
                            "<font color=#2196F3>Register now!</font>"));
                    guestText.setVisibility(View.VISIBLE);
                    guestText.setOnClickListener(new registerListener());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        reference.addListenerForSingleValueEvent(postListener);
    }

    // Function to get topScores from all users via a user object.
    public void getTopScores() {
        DatabaseReference reference = mDatabase.child("users");
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                collectTopScores((Map<String,Object>) dataSnapshot.getValue());
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        reference.addListenerForSingleValueEvent(postListener);
    }

    // Function to collect all the top scores in a loop from the object of the previous function.
    private void collectTopScores(Map<String,Object> users) {
        ArrayList<UserTop> KarmaList = new ArrayList<>();
        int totalKarmaAll = 0;
        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){
            //Get user map
            Map singleUser = (Map) entry.getValue();
            if (((Long) singleUser.get("guest")).intValue() == 0) {
                KarmaList.add(new UserTop((String) singleUser.get("username"), (Long) singleUser.get("karma")));
                totalKarmaAll = totalKarmaAll +  ((Long) singleUser.get("karma")).intValue();
            }
        }
        TextView textView = findViewById(R.id.logon_total);
        textView.setText(getString(R.string.AllUserKarma)+totalKarmaAll);
        // Sort the list from high to low.
        Collections.sort(KarmaList, new Comparator<UserTop>() {
            @Override
            public int compare(UserTop userTop, UserTop t1) {
                return t1.getKarma().compareTo(userTop.getKarma());
            }
        });
        ArrayAdapter<UserTop> adapter = new UserTopAdapter(getApplicationContext(), R.layout.row_user, KarmaList);
        topScores.setAdapter(adapter);
    }

    // Function to got to the register page.
    private void goToHome() {
        startActivity(new Intent(Logon.this, reglog.class));
        finish();
    }

    // Listener if the user is a guest.
    private class registerListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            FirebaseAuth.getInstance().signOut();
            goToHome();
        }
    }

    // Listener to start a trivia game.
    private class startListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Logon.this, SelectTrivia.class);
            startActivity(intent);
        }
    }

    // Function to refresh the topscores after a user finished a trivia.
    public void onResume() {
        super.onResume();
        getTopScores();
    }

    // Function to check if the user is really logged in.
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            goToHome();
        }
    }

    // Functions to create the menu
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
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.Logout:
                Functions.Logout(context, mAuth);
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
