package com.example.trottonbikes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.trottonbikes.databinding.ActivityBikeListBinding;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BikeListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<Bike> bikeArrayList;
    ActivityBikeListBinding binding;

    ActionBar actionBar;
    ActionBarDrawerToggle toggle;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBikeListBinding.inflate(getLayoutInflater());
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
        bikeArrayList = new ArrayList<Bike>();

        initialiseBikeList();

        //TODO: Change progress bar to shimmer view
    }

    private void initialiseBikeList() {

        BikeViewAdapter adapter = new BikeViewAdapter(this, bikeArrayList);

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                bikeArrayList.add(snapshot.getValue(Bike.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull @NotNull DataSnapshot snapshot) {
                bikeArrayList.remove(snapshot.getValue(Bike.class));
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull @NotNull DataSnapshot snapshot, @Nullable @org.jetbrains.annotations.Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {

            }
        });

        binding.bikeListView.setAdapter(adapter);

        binding.loadingSpinner.setVisibility(View.GONE);
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
            startActivity(new Intent(BikeListActivity.this,SignInActivity.class));

        } else if(item.getItemId() == R.id.nav_item_profile) {
            startActivity(new Intent(BikeListActivity.this, ProfilePageActivity.class));

        } else if(item.getItemId() == R.id.nav_item_settings) {
            //TODO: make a settings page
            Toast.makeText(BikeListActivity.this, "Opening Settings Page", Toast.LENGTH_SHORT).show();

        } else if(item.getItemId() == R.id.nav_item_register_bike) {
            startActivity(new Intent(BikeListActivity.this,RegisterBikeActivity.class));

        }
        return false;
    }
}