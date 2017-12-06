package com.example.hellvox.kappetijnmathijspset6;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button login = findViewById(R.id.Login);

        mAuth = FirebaseAuth.getInstance();

        login.setOnClickListener(new LoginListener());
    }

    private class LoginListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            EditText username = findViewById(R.id.Username2);
            EditText pass = findViewById(R.id.Password2);
            String email = username.getText().toString();
            String password = pass.getText().toString();
            if (password.length() < 6) {
                Toast.makeText(Login.this, "Password to short!", Toast.LENGTH_SHORT).show();
            } else {
                login(email, password);
            }
        }
    }

    public void login(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("sign", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(Login.this, "Succes!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(Login.this, Logon.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("fail", "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Wrong email/password combo.",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
