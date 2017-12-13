package com.example.hellvox.kappetijnmathijspset6;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Functions {

    public static void Logout(Context context, FirebaseAuth mAuth) {
        mAuth.signOut();
        Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(context, reglog.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }

    public static boolean userState(FirebaseAuth mAuth) {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        return currentUser != null;
    }
}
