package com.example.trottonbikes;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.Bundle;

import com.example.trottonbikes.databinding.ActivityPaymentDetailsBinding;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class PaymentDetailsActivity extends AppCompatActivity {

    ActivityPaymentDetailsBinding binding;

    //TODO: fix ui
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPaymentDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = getIntent().getExtras();
        long bookVal = bundle.getLong("bookTime");
        long rideVal = bundle.getLong("rideTime");

        NumberFormat f = new DecimalFormat("00");
        long hour = (bookVal / 3600000) % 24;
        long min = (bookVal / 60000) % 60;
        long sec = (bookVal / 1000) % 60;
        binding.bookedTimeTV.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));

        hour = (rideVal / 3600000) % 24;
        min = (rideVal / 60000) % 60;
        sec = (rideVal / 1000) % 60;
        binding.rideTimeTV.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));

        float total = (bookVal+rideVal)/1000.0f;
    }
}