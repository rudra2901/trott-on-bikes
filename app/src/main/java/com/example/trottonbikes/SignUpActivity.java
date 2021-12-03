package com.example.trottonbikes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    EditText emailView, passwordView, repeatPasswordView;
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
        repeatPasswordView = findViewById(R.id.repeatPasswordText);
        signUp = findViewById(R.id.signupButton);
        googleSignIn = findViewById(R.id.googleSignIn);

        mFirebaseAuth = FirebaseAuth.getInstance();
    }
}