package com.example.trottonbikes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.trottonbikes.databinding.ActivityBikeListBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class BikeListActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ArrayList<Bike> bikeArrayList;
    ActivityBikeListBinding binding;

    ActionBar actionBar;
    ActionBarDrawerToggle toggle;
    private DatabaseReference databaseReference;
    FirebaseFirestore db;
    LinearLayoutManager manager;
    DocumentSnapshot lastVisible;
    int x = 0;

    int current, total, scrolled;
    boolean isScrolling = false;
    boolean isLastItemReached = false;
    BikeAdapter bikeAdapter;

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
        db = FirebaseFirestore.getInstance();

        bikeArrayList = new ArrayList<Bike>();

        bikeAdapter = new BikeAdapter(this, bikeArrayList);
        manager = new LinearLayoutManager(this);
        binding.bikeListView.setLayoutManager(manager);
        binding.bikeListView.setAdapter(bikeAdapter);

        Query query = db.collection("available").orderBy("id").limit(20);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()) {
                    for (DocumentSnapshot snapshot : task.getResult()) {
                        Bike item = snapshot.toObject(Bike.class);
                        bikeArrayList.add(item);
                    }
                    bikeAdapter.notifyDataSetChanged();

                    lastVisible = task.getResult().getDocuments().get(task.getResult().size() - 1);
                    if (task.getResult().size() < 20) {
                        isLastItemReached = true;
                    }
                    binding.progressBarList.setVisibility(View.GONE);

                    binding.bikeListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                            if(newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
                                isScrolling = true;
                        }

                        @Override
                        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                            super.onScrolled(recyclerView, dx, dy);

                            current = manager.getChildCount();
                            total =  manager.getItemCount();
                            scrolled = manager.findFirstCompletelyVisibleItemPosition();

                            if(isScrolling && (current+scrolled == total) && !isLastItemReached) {
                                binding.progressBarList.setVisibility(View.VISIBLE);
                                Query nextQuery = db.collection("available").orderBy("id", Query.Direction.ASCENDING).startAfter(lastVisible).limit(20);
                                nextQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> t) {
                                        if (t.isSuccessful()) {
                                            for (DocumentSnapshot d : t.getResult()) {
                                                Bike item = d.toObject(Bike.class);
                                                bikeArrayList.add(item);
                                            }
                                            bikeAdapter.notifyDataSetChanged();
                                            lastVisible = t.getResult().getDocuments().get(t.getResult().size() - 1);

                                            if (t.getResult().size() < 20) {
                                                isLastItemReached = true;
                                            }
                                        }
                                    }
                                });
                            }
                        }
                    });
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
            startActivity(new Intent(BikeListActivity.this,SignInActivity.class));

        } else if(item.getItemId() == R.id.nav_item_profile) {
            startActivity(new Intent(BikeListActivity.this, ProfilePageActivity.class));

        } else if(item.getItemId() == R.id.nav_item_settings) {
            //TODO: make a settings page
            Toast.makeText(BikeListActivity.this, "Opening Settings Page", Toast.LENGTH_SHORT).show();

        } else if(item.getItemId() == R.id.nav_item_register_bike) {
            startActivity(new Intent(BikeListActivity.this,RegisterBikeActivity.class));

        } else if(item.getItemId() == R.id.nav_item_home) {
            Toast.makeText(BikeListActivity.this, "Already in Home Page", Toast.LENGTH_SHORT).show();

        }
        return false;
    }
}