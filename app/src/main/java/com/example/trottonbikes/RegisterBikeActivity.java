package com.example.trottonbikes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.trottonbikes.databinding.ActivityRegisterBikeBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterBikeActivity extends AppCompatActivity {

    ActivityRegisterBikeBinding binding;
    private DatabaseReference databaseReference;
    private long bikeCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBikeBinding.inflate(getLayoutInflater());
        View view =  binding.getRoot();
        setContentView(view);

        databaseReference = FirebaseDatabase.getInstance("https://learnfirebase-61b73-default-rtdb.asia-southeast1.firebasedatabase.app").
                getReference("bikes");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                bikeCount = snapshot.getChildrenCount();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        binding.uploadPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: Create a method to upload image
            }
        });

        binding.uploadBikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String desc = binding.descETV.getText().toString();
                if(desc.isEmpty()) {
                    binding.descETV.setError("Please enter a description");
                    binding.descETV.requestFocus();
                } else {
                    String id = "bike" + Long.toString(bikeCount);
                    String ownersName = "User X", ownerAddress ="User's City";
                    float rating = 0.0f;
                    String imgUrl = "@mipmap/bikestockimage.png";

                    Bike bike = new Bike(id, ownersName, ownerAddress, desc, rating, imgUrl);
                    databaseReference.child(id).setValue(bike);
                }
            }
        });
    }
}