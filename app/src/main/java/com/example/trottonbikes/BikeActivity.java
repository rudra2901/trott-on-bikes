package com.example.trottonbikes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.trottonbikes.databinding.ActivityBikeBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class BikeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityBikeBinding binding;
    DatabaseReference databaseReference;
    String bikeID;
    //Bike bike;
    ActionBar actionBar;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBikeBinding.inflate(getLayoutInflater());
        View view =  binding.getRoot();
        setContentView(view);

        actionBar = getSupportActionBar();
        toggle = new ActionBarDrawerToggle(this, binding.drawerLayout, R.string.open, R.string.close);
        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        binding.navView.setNavigationItemSelectedListener(this);
        actionBar.setDisplayHomeAsUpEnabled(true);

        Bundle extras = getIntent().getExtras();
        bikeID = extras.getString("bikeID");

        databaseReference = FirebaseDatabase.getInstance("https://learnfirebase-61b73-default-rtdb.asia-southeast1.firebasedatabase.app").getReference("bikes");
        databaseReference.child(bikeID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                binding.descTV.setText(snapshot.getValue(Bike.class).getDesc());
                actionBar.setTitle(snapshot.getValue(Bike.class).getOwnersName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.btnFL, new BikeOptionFragment()).commit();
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
            startActivity(new Intent(BikeActivity.this,SignInActivity.class));

        } else if(item.getItemId() == R.id.nav_item_profile) {
            startActivity(new Intent(BikeActivity.this,ProfilePageActivity.class));

        } else if(item.getItemId() == R.id.nav_item_settings) {
            //TODO: make a settings page
            Toast.makeText(BikeActivity.this, "Opening Settings Page", Toast.LENGTH_SHORT).show();

        } else if(item.getItemId() == R.id.nav_item_register_bike) {
            startActivity(new Intent(BikeActivity.this,RegisterBikeActivity.class));

        }
        return false;
    }
}