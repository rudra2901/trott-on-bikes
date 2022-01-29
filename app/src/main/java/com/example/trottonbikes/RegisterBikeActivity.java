package com.example.trottonbikes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.trottonbikes.databinding.ActivityRegisterBikeBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.UUID;

public class RegisterBikeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    ActivityRegisterBikeBinding binding;
    private DatabaseReference databaseReference;
    FirebaseStorage storage;
    StorageReference storageRef;
    FirebaseFirestore firestore;

    private long bikeCount = 0;
    private final int PICK_IMAGE_REQUEST = 22;
    private Uri filePath;

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
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference().child("bikes");
        firestore = FirebaseFirestore.getInstance();

        binding.uploadPic.setOnClickListener(v -> selectImage());

        binding.uploadBikeButton.setOnClickListener(v -> {
            String desc = binding.descETV.getText().toString();
            if(desc.isEmpty()) {
                binding.descETV.setError("Please enter a description");
                binding.descETV.requestFocus();

            } else if(filePath == null) {
                Toast.makeText(RegisterBikeActivity.this, "Please upload a picture", Toast.LENGTH_LONG).show();

            } else {
                CollectionReference availableReference = firestore.collection("available");
                DocumentReference documentReference = availableReference.document();

                String id = documentReference.getId();
                String imgUrl = uploadImage(id);

                FirebaseAuth auth = FirebaseAuth.getInstance();
                FirebaseUser user = auth.getCurrentUser();

                String ownersName, ownerAddress ="User's City";

                assert user != null;
                ownersName = user.getDisplayName();

                float rating = 0.0f;

                Bike bike = new Bike(id, ownersName, ownerAddress, desc, rating, imgUrl);
                databaseReference.child(id).setValue(bike);

                documentReference.set(bike).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(RegisterBikeActivity.this, "Bike Uploaded", Toast.LENGTH_LONG).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(RegisterBikeActivity.this, "Error uploading bike! Try again later", Toast.LENGTH_LONG).show();
                    }
                });

                startActivity(new Intent(RegisterBikeActivity.this, BikeListActivity.class));
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
            startActivity(new Intent(RegisterBikeActivity.this,ProfilePageActivity.class));

        } else if(item.getItemId() == R.id.nav_item_settings) {
            //TODO: make a settings page
            Toast.makeText(RegisterBikeActivity.this, "Opening Settings Page", Toast.LENGTH_SHORT).show();

        } else if(item.getItemId() == R.id.nav_item_register_bike) {
            Toast.makeText(RegisterBikeActivity.this, "Register Bike Page already open", Toast.LENGTH_SHORT).show();

        } else if(item.getItemId() == R.id.nav_item_home) {
            startActivity(new Intent(RegisterBikeActivity.this,BikeListActivity.class));

        }
        return false;
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*").setAction(Intent.ACTION_GET_CONTENT);

        startActivityForResult(Intent.createChooser(intent, "Select image..."), PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            // Get the Uri of data
            filePath = data.getData();
            try {
                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                binding.uploadPic.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    // UploadImage method
    private String uploadImage(String bikeid) {
        String url = "@mipmap/bikestockimage.png";

        if (filePath != null) {
            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref = storageRef.child(bikeid);

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            // Image uploaded successfully
                            // Dismiss dialog
                            progressDialog.dismiss();
                            Toast.makeText(RegisterBikeActivity.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(RegisterBikeActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        // Progress Listener for loading
                        // percentage on the dialog box
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int)progress + "%");
                        }
                    });
            return ref.getDownloadUrl().toString();
        }
        return url;
    }
}