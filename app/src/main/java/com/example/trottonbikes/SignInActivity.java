package com.example.trottonbikes;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

public class SignInActivity extends AppCompatActivity {

    EditText emailView, passwordView;
    Button signUp, googleSignIn;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.grey_signup)));

        setContentView(R.layout.activity_sign_up);

        emailView = findViewById(R.id.emailText);
        passwordView = findViewById(R.id.passwordText);
        signUp = findViewById(R.id.signupButton);
        googleSignIn = findViewById(R.id.googleSignIn);

        mFirebaseAuth = FirebaseAuth.getInstance();

        //TODO: Set Sign In method
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailView.getText().toString();
                String pwd = passwordView.getText().toString();
                if (email.isEmpty()) {
                    emailView.setError("Please provide your email");
                    emailView.requestFocus();

                } else if (pwd.isEmpty()) {
                    passwordView.setError("Please enter your password");
                    passwordView.requestFocus();
                } else {
                    mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(SignInActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (!task.isSuccessful()) {
                                Toast.makeText(SignInActivity.this, "Oops! Sign up unsuccessful, please try again. ", Toast.LENGTH_SHORT).show();

                            } else {
                                startActivity(new Intent(SignInActivity.this, BikeActivity.class));
                            }
                        }


                    });
                }
            }
        });
    }
}