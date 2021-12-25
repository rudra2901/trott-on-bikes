  package com.example.trottonbikes;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.trottonbikes.databinding.FragmentBikeOptionBinding;

import org.jetbrains.annotations.NotNull;

  /**
 * A simple {@link Fragment} subclass.
 */
public class BikeOptionFragment extends Fragment {

    Button buttonBook, buttonRide;
    FragmentBikeOptionBinding binding;

    public BikeOptionFragment() {
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
        binding = FragmentBikeOptionBinding.inflate(inflater, container, false);
        // View rootView = inflater.inflate(R.layout.fragment_bike_option, container, false);

        binding.fragmentButtonBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.btnFL, new BikeBookFragment()).commit();
            }
        });

        return binding.getRoot();
    }
  }