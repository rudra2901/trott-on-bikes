package com.example.trottonbikes;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;

import com.example.trottonbikes.databinding.ActivityBikeBinding;
import com.example.trottonbikes.databinding.ActivityBikeBookedBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class BikeBookedActivity extends AppCompatActivity {

    ActivityBikeBookedBinding binding;
    Bike bike;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBikeBookedBinding.inflate(getLayoutInflater());
        View view =  binding.getRoot();
        setContentView(view);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.hide();

        bike = getIntent().getParcelableExtra("bike");

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference pathRef = storageReference.child("bikes").child(bike.getId());

        GlideApp.with(this).load(pathRef).centerCrop().into(binding.bikeImage);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        CollectionReference bookingReference = firestore.collection("booking");
        DocumentReference documentReference = bookingReference.document();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        String userId = user.getUid();


        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.bookTimeFL, new TimeRemainingFragment()).commit();
    }
}