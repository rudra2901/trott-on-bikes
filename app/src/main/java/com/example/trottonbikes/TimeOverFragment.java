package com.example.trottonbikes;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trottonbikes.databinding.FragmentTimeOverBinding;
import com.example.trottonbikes.databinding.FragmentTimeRemainingBinding;

import androidx.fragment.app.Fragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimeOverFragment extends Fragment {

    FragmentTimeOverBinding binding;

    public TimeOverFragment() {
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
        binding = FragmentTimeOverBinding.inflate(inflater, container, false);

        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("booking", Context.MODE_PRIVATE);
        long booktime = 0;
        sharedPreferences.getLong("timeBooked", booktime);

        binding.payButton.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), PaymentDetailsActivity.class);
            intent.putExtra("rideTime", 0);
            intent.putExtra("bookTime", booktime);
            startActivity(intent);
        });

        //return inflater.inflate(R.layout.fragment_time_over, container, false);
        return binding.getRoot();
    }
}