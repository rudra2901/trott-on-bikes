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
        String id = bundle.getString("id");
        firestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firestore.collection("available").document(id);

        binding.fragmentButtonBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()) {
                            FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                            fragmentTransaction.replace(R.id.btnFL, new BikeBookFragment()).commit();
                        }
                        else {
                            Toast.makeText(getActivity(), "Sorry! The bike has been already booked!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getContext(), BikeListActivity.class));
                        }
                    }
                });
            }
        });
        binding.fragmentButtonRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if(documentSnapshot.exists()) {
                            Bike currentBike = documentSnapshot.toObject(Bike.class);
                            Intent intent = new Intent(getContext(), RideMapsActivity.class);
                            intent.putExtra("bike", currentBike);
                            startActivity(intent);
                        }
                        else {
                            Toast.makeText(getActivity(), "Sorry! The bike has been already booked!", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getContext(), BikeListActivity.class));
                        }
                    }
                });
            }
        });

        return binding.getRoot();
    }
  }