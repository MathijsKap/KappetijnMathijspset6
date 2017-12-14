package com.example.hellvox.kappetijnmathijspset6;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Functions {

    // Function to log out the user
    public static void Logout(Context context, FirebaseAuth mAuth) {
        mAuth.signOut();
        Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, RegLog.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    // Function to get the userstate
    public static boolean userState(FirebaseAuth mAuth) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser != null;
    }

    // Function to check if there is internet connectivity.
    public static boolean isOnline(Context c) {
        ConnectivityManager cm =
                (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }
}
