package com.example.trottonbikes;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trottonbikes.databinding.BikeListItemBinding;

import java.util.ArrayList;

import androidx.annotation.NonNull;
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
        holder.binding.bikeListImage.setImageResource(R.mipmap.bikeimage);

        holder.binding.viewBikeBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, BikeActivity.class);
            intent.putExtra("bike", currentBike);
            context.startActivity(intent);
        });
        holder.binding.rideBikeBtn.setOnClickListener(v -> {
            Intent intent = new Intent(context, RideMapsActivity.class);
            intent.putExtra("bike", currentBike);
            context.startActivity(intent);
        });

        // TODO: we are using Picasso/glide to load images
        // from URL inside our image view.
        //Picasso.get().load(modal.getImgUrl()).into(holder.courseIV);
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
