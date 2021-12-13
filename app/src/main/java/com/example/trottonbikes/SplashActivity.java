package com.example.trottonbikes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.window.SplashScreen;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);

        int SPLASH_SCREEN_TIME_OUT = 1000;
        new Handler().postDelayed(() -> {
            Intent i;
            i = new Intent(SplashActivity.this, SignInActivity.class);
            startActivity(i);

            //invoke the SecondActivity.
            finish();
        }, SPLASH_SCREEN_TIME_OUT);
    }
}