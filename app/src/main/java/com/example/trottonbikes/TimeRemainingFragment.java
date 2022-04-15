package com.example.trottonbikes;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.trottonbikes.databinding.FragmentTimeRemainingBinding;

import org.json.JSONException;

import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimeRemainingFragment extends Fragment {

    FragmentTimeRemainingBinding binding;
    long currTime;

    public TimeRemainingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //View rootView = inflater.inflate(R.layout.fragment_time_remaining, container, false);
        binding = FragmentTimeRemainingBinding.inflate(inflater, container, false);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("booking", Context.MODE_PRIVATE);

        long timeBooked = 0, timeSelected = 0;
        sharedPreferences.getLong("timeBooked", timeBooked);
        sharedPreferences.getLong("timeCode", timeSelected);

        currTime = getCurrentTime();

        long timeLeft = (timeBooked + timeSelected) - currTime;
        // Time is in millisecond so 50sec = 50000 I have used
        // countdown Interval is 1sec = 1000 I have used
        new CountDownTimer(timeLeft, 1000) {
            public void onTick(long millisUntilFinished) {
                // Used for formatting digit to be in 2 digits only
                NumberFormat f = new DecimalFormat("00");
                long hour = (millisUntilFinished / 3600000) % 24;
                long min = (millisUntilFinished / 60000) % 60;
                long sec = (millisUntilFinished / 1000) % 60;
                binding.timeShowText.setText(f.format(hour) + ":" + f.format(min) + ":" + f.format(sec));
            }
            // When the task is over it will print 00:00:00 there
            public void onFinish() {
                binding.timeShowText.setText("00:00:00");
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.bookTimeFL, new TimeOverFragment()).commit();
            }
        }.start();

        return binding.getRoot();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //TODO: Destroy timer to stop memory leak
    }

    public long getCurrentTime() {
        String url = "http://worldtimeapi.org/api/timezone/Asia/Kolkata";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, response -> {
            try {
                currTime = response.getLong("timeunix");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(getActivity(), "Error Getting Time!", Toast.LENGTH_SHORT).show());

        return currTime;
    }
}