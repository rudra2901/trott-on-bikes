package com.example.trottonbikes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.trottonbikes.databinding.ActivityRegisterBikeBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class RegisterBikeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityRegisterBikeBinding binding;
    private DatabaseReference databaseReference;
    private long bikeCount = 0;

    ActionBar actionBar;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBikeBinding.inflate(getLayoutInflater());
        View view =  binding.getRoot();
        setContentView(view);

        actionBar = getSupportActionBar();
        toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.navView.setNavigationItemSelectedListener(this);
        actionBar.setDisplayHomeAsUpEnabled(true);

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
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        if(item.getItemId() == R.id.nav_item_log_out) {
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(RegisterBikeActivity.this,SignInActivity.class));
        } else if(item.getItemId() == R.id.nav_item_profile) {
            //TODO: make a profile page
            Toast.makeText(RegisterBikeActivity.this, "Opening Profile Page", Toast.LENGTH_SHORT).show();
            //startActivity(new Intent(AvailableSlotActivity.this,ChangePinPopupActivity.class));
        } else if(item.getItemId() == R.id.nav_item_settings) {
            //TODO: make a settings page
            Toast.makeText(RegisterBikeActivity.this, "Opening Settings Page", Toast.LENGTH_SHORT).show();
            //startActivity(new Intent(AvailableSlotActivity.this,ChangePinPopupActivity.class));
        } else if(item.getItemId() == R.id.nav_item_register_bike) {
            startActivity(new Intent(RegisterBikeActivity.this,RegisterBikeActivity.class));
        }
        return false;
    }
}