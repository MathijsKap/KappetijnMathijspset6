package com.example.hellvox.kappetijnmathijspset6;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    ProgressBar progressBar;
    ListView topScores;
    TextView topUs;
    TextView guestText;
    Button startTrivia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logon);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        startTrivia = findViewById(R.id.Start);
        progressBar = findViewById(R.id.progressBar2);
        topScores = findViewById(R.id.Logon_top);
        topUs = findViewById(R.id.logon_top);
        guestText = findViewById(R.id.textView5);

        FirebaseUser user = mAuth.getCurrentUser();
        if (!isOnline(getApplicationContext())) {
            Snackbar.make(findViewById(android.R.id.content), "No internet connection", Snackbar.LENGTH_LONG).show();
        }
        if (user != null) {
            String userId = user.getUid();
            readNameFromDB(userId);
            getTopScores();
            getGuest(userId);
            progressBar.setVisibility(View.VISIBLE);
        } else goToHome();

        startTrivia.setOnClickListener(new startListener());
    }

    private void userCheck() {
        if (!userState()) {
            goToHome();
        }
    }

    public void readNameFromDB(final String id) {
        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User aUser = dataSnapshot.child("users").child(id).getValue(User.class);

                TextView tv = findViewById(R.id.Logon_begin);
                TextView karma = findViewById(R.id.Logon_karma);
                tv.setText(getString(R.string.hello_message)+aUser.username + getString(R.string.Ex));
                karma.setText(getString(R.string.your_karma)+aUser.karma);
                topUs.setText(getString(R.string.top_users));
                progressBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mDatabase.addValueEventListener(postListener);
    }

    public void getGuest(final String id) {
        DatabaseReference ref = mDatabase.child("users").child(id);
        ref.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        User aUser = dataSnapshot.getValue(User.class);
                        if (aUser.guest == 1) {
                            guestText.setText(Html.fromHtml("You are playing as guest, karma will not be saved! <font color=#2196F3>Register now!</font>"));
                            guestText.setVisibility(View.VISIBLE);
                            guestText.setOnClickListener(new registerListener());
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        //handle databaseError
                    }
                }
        );
    }

    public void getTopScores() {
        DatabaseReference ref = mDatabase.child("users");
        ref.addListenerForSingleValueEvent(
            new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    //Get map of users in datasnapshot
                    collectTopScores((Map<String,Object>) dataSnapshot.getValue());
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    //handle databaseError
                }
            }
        );
    }

    private void collectTopScores(Map<String,Object> users) {

        ArrayList<UserTop> KarmaList = new ArrayList<>();
        int totalKarmaAll = 0;

        //iterate through each user, ignoring their UID
        for (Map.Entry<String, Object> entry : users.entrySet()){
            //Get user map
            Map singleUser = (Map) entry.getValue();
            //Get phone field and append to list
            if (((Long) singleUser.get("guest")).intValue() == 0) {
                KarmaList.add(new UserTop((String) singleUser.get("username"), (Long) singleUser.get("karma")));
                totalKarmaAll = totalKarmaAll +  ((Long) singleUser.get("karma")).intValue();
            }
        }
        TextView textView = findViewById(R.id.logon_total);
        textView.setText("Total karma earned by all users: "+totalKarmaAll);


        Collections.sort(KarmaList, new Comparator<UserTop>() {
            @Override
            public int compare(UserTop userTop, UserTop t1) {
                return t1.getKarma().compareTo(userTop.getKarma());
            }
        });
        ArrayAdapter<UserTop> adapter = new UserTopAdapter(getApplicationContext(), R.layout.row_user, KarmaList);
        topScores.setAdapter(adapter);
    }

    private class registerListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            FirebaseAuth.getInstance().signOut();
            goToHome();
        }
    }


    private class startListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(Logon.this, SelectTrivia.class);
            startActivity(intent);
        }
    }

    private void goToHome() {
        startActivity(new Intent(Logon.this, reglog.class));
        finish();
    }

    public void onResume() {
        super.onResume();
        getTopScores();
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            goToHome();
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

    public static boolean isOnline(Context c) {
        ConnectivityManager cm =
                (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
