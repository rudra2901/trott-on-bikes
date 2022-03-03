  package com.example.trottonbikes;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.trottonbikes.databinding.FragmentBikeOptionBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

  /**
 * A simple {@link Fragment} subclass.
 */
public class BikeOptionFragment extends Fragment {

    FragmentBikeOptionBinding binding;
    FirebaseFirestore firestore;

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

        Bundle bundle = this.getArguments();
        Bike bike = bundle.getParcelable("bike");
        firestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firestore.collection("available").document(bike.getId());

        binding.fragmentButtonBook.setOnClickListener(v -> documentReference.get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists() && ((Long)documentSnapshot.get("booked") == 0)) {

                documentReference.update("booked", 1);
                BikeBookFragment bikeBookFragment = new BikeBookFragment();
                Bundle nextBundle = new Bundle();
                nextBundle.putParcelable("bike", bike);
                bikeBookFragment.setArguments(nextBundle);
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.btnFL, bikeBookFragment).commit();
            }
            else {
                Toast.makeText(getActivity(), "Sorry! The bike has been already booked!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getContext(), BikeListActivity.class));
            }
        }));
        binding.fragmentButtonRide.setOnClickListener(v -> documentReference.get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists()) {
                documentReference.update("booked", 1);
                Intent intent = new Intent(getContext(), RideMapsActivity.class);
                intent.putExtra("bike", bike);
                startActivity(intent);
            }
            else {
                Toast.makeText(getActivity(), "Sorry! The bike has been already booked!", Toast.LENGTH_LONG).show();
                startActivity(new Intent(getContext(), BikeListActivity.class));
            }
        }));

        return binding.getRoot();
    }
  }