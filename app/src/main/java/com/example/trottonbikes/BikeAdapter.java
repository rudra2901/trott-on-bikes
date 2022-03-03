package com.example.trottonbikes;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.trottonbikes.databinding.BikeListItemBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

public class BikeAdapter extends RecyclerView.Adapter<BikeAdapter.ViewHolder> {
    Context context;
    ArrayList<Bike> bikeArrayList;

    public BikeAdapter(Context context, ArrayList<Bike> bikes) {
        this.context = context;
        this.bikeArrayList = bikes;
    }

    @NonNull
    @Override
    public BikeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BikeListItemBinding binding = BikeListItemBinding.inflate(LayoutInflater.from(context), parent, false);
        // passing our layout file for displaying our card item
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull BikeAdapter.ViewHolder holder, int position) {
        // setting data to our views in Recycler view items.
        Bike currentBike = bikeArrayList.get(position);
        holder.binding.ownernameTV.setText(currentBike.getOwnersName());
        holder.binding.ownerAddTV.setText(currentBike.getOwnerAddress());
        holder.binding.ratingBar.setRating(currentBike.getRating());

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firestore.collection("available").document(currentBike.getId());

        holder.binding.viewBikeBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, BikeActivity.class);
            intent.putExtra("bike", currentBike);
            context.startActivity(intent);
        });
        holder.binding.rideBikeBtn.setOnClickListener(v -> documentReference.get().addOnSuccessListener(documentSnapshot -> {
            if(documentSnapshot.exists() && ((Long)documentSnapshot.get("booked") == 0)) {
                documentReference.update("booked", 1);
                Intent intent = new Intent(context, RideMapsActivity.class);
                intent.putExtra("bike", currentBike);
                context.startActivity(intent);
            }
            else {
                Toast.makeText(context, "Sorry! The bike has been already booked!", Toast.LENGTH_LONG).show();
                //context.startActivity(new Intent(context, BikeListActivity.class));
            }
        }));
        holder.binding.bookBikeBtn.setOnClickListener(v -> documentReference.get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists() && ((Long) documentSnapshot.get("booked") == 0)) {
                documentReference.update("booked", 1);
                Intent intent = new Intent(context, BikeBookedActivity.class);
                intent.putExtra("bike", currentBike);
                context.startActivity(intent);
            } else {
                Toast.makeText(context, "Sorry! The bike has been already booked!", Toast.LENGTH_LONG).show();
                //context.startActivity(new Intent(context, BikeListActivity.class));
            }
        }));

        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference pathRef = storageReference.child("bikes").child(currentBike.getId());
        GlideApp.with(context).load(pathRef).centerCrop().into(holder.binding.bikeListImage);
    }

    @Override
    public int getItemCount() {
        // returning the size of array list.
        return bikeArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        BikeListItemBinding binding;

        public ViewHolder(BikeListItemBinding b) {
            super(b.getRoot());
            // initializing the views of recycler views.
            binding = b;

            b.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bike currentBike = bikeArrayList.get(getAbsoluteAdapterPosition());

                    Intent intent = new Intent(context, BikeActivity.class);
                    intent.putExtra("bike", currentBike);
                    context.startActivity(intent);
                }
            });
        }
    }
}
