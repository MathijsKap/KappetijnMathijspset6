package com.example.hellvox.kappetijnmathijspset6;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;


public class login_dialog extends DialogFragment {

    private FirebaseAuth mAuth;
    EditText username;
    EditText pass;
    Button login;
    ProgressBar progressBar;
    View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_login_dialog, container, false);
        username = view.findViewById(R.id.Username2);
        pass = view.findViewById(R.id.Password2);
        login = view.findViewById(R.id.Login);
        progressBar = view.findViewById(R.id.progressBar3);
        mAuth = FirebaseAuth.getInstance();
        return view;
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        login.setOnClickListener(new LoginListener());
    }


    private class LoginListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            String email = username.getText().toString();
            String password = pass.getText().toString();
            if (password.length() < 6) {
                Toast.makeText(getContext(), "Password to short!", Toast.LENGTH_SHORT).show();
            } else {
                progressBar.setVisibility(View.VISIBLE);
                login(email, password);
            }
        }
    }

    public void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("sign", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            progressBar.setVisibility(View.INVISIBLE);
                            //Toast.makeText(getContext(), "Succes!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getActivity(), Logon.class);
                            startActivityForResult(intent, 0);
                            getActivity().finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("fail", "signInWithEmail:failure", task.getException());
                            //Toast.makeText(getContext(), "Wrong email/password combo.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

}
