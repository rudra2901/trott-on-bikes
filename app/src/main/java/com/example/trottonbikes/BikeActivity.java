package com.example.trottonbikes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.trottonbikes.databinding.ActivityBikeBinding;
import com.example.trottonbikes.databinding.ActivityBikeListBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class BikeActivity extends AppCompatActivity {

    ActivityBikeBinding binding;
    DatabaseReference databaseReference;
    String bikeID;
    Bike bike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBikeBinding.inflate(getLayoutInflater());
        View view =  binding.getRoot();
        setContentView(view);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.btnFL, new BikeOptionFragment()).commit();

        Bundle extras = getIntent().getExtras();
        bikeID = extras.getString("bikeID");

        databaseReference = FirebaseDatabase.getInstance("https://learnfirebase-61b73-default-rtdb.asia-southeast1.firebasedatabase.app").getReference();
        databaseReference.child(bikeID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bike = snapshot.getValue(Bike.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.descTV.setText(bike.getDesc());
    }
}