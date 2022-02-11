package com.example.trottonbikes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.trottonbikes.databinding.ActivitySignUpBinding;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    private static final int RC_SIGN_IN = 1234;

    FirebaseAuth mFirebaseAuth;
    GoogleSignInOptions googleSignInOptions;
    GoogleSignInClient mGoogleSignInClient;

    private ActivitySignUpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.grey_signup)));

        View view = binding.getRoot();
        setContentView(view);

        mFirebaseAuth = FirebaseAuth.getInstance();

        // Configure sign-in to request the user's ID, email address, and basic profile.
        // ID and basic profile are included in DEFAULT_SIGN_IN.
        googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions);

        binding.signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.emailText.getText().toString();
                String pwd = binding.passwordText.getEditText().getText().toString();
                String rPwd = binding.repeatPasswordText.getEditText().getText().toString();
                if (email.isEmpty()) {
                    binding.emailText.setError("Please provide your email");
                    binding.emailText.requestFocus();

                } else if (pwd.isEmpty()) {
                    binding.passwordText.setError("Please enter your password");
                    binding.passwordText.requestFocus();

                } else if(rPwd.isEmpty()){
                    binding.repeatPasswordText.setError("Please confirm password");
                    binding.repeatPasswordText.requestFocus();

                } else if(!pwd.equals(rPwd)) {
                    Toast.makeText(SignUpActivity.this, "Passwords don't match! Please try again", Toast.LENGTH_SHORT).show();
                    binding.passwordText.requestFocus();

                } else {
                    //TODO: Add phone number with password login system
                    if (!isValidEmailId(email)) {
                        binding.emailText.setError("Please provide correct email");
                        binding.emailText.requestFocus();
                    } else {
                        mFirebaseAuth.createUserWithEmailAndPassword(email, pwd).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignUpActivity.this, "Oops! Sign up unsuccessful, please try again. ", Toast.LENGTH_SHORT).show();

                                } else {
                                    startActivity(new Intent(SignUpActivity.this, ProfilePageActivity.class));
                                }
                            }
                        });
                    }
                }
            }
        });

        binding.googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signInWithGoogle();
            }
        });

        binding.signInText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SignUpActivity.this ,SignInActivity.class));
            }
        });
    }
    private void signInWithGoogle() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode != RESULT_CANCELED) {
            if (requestCode == RC_SIGN_IN) {
                if (data != null) {
                    Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                    try {
                        GoogleSignInAccount account = task.getResult(ApiException.class);
                        firebaseAuthWithGoogle(account);
                    } catch (ApiException e) {
                        Log.w("SignUpActivity", "Google sign in failed", e);
                    }
                }
            }
        }
    }

    private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnSuccessListener(this, authResult -> {
                    startActivity(new Intent(SignUpActivity.this, BikeListActivity.class));
                    finish();
                })
                .addOnFailureListener(this, e -> Toast.makeText(SignUpActivity.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show());
    }

    private static boolean isValidPhoneNumber(String mobile) {
        String regEx = "^[0-9]{11,12}$";
        return mobile.matches(regEx);
    }

    private boolean isValidEmailId(String email){
        String emailRegEx = "^[A-Za-z0-9._%+\\-]+@[A-Za-z0-9.\\-]+\\.[A-Za-z]{2,4}$";

        return Pattern.compile(emailRegEx).matcher(email).matches();
    }
}