package com.example.trottonbikes;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.trottonbikes.databinding.BikeListItemBinding;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BikeViewAdapter extends ArrayAdapter<Bike> {

    public BikeViewAdapter(@NonNull Context context, ArrayList<Bike> bikes) {
        super(context, 0, bikes);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View currentView = convertView;
        if(currentView == null)
            currentView = LayoutInflater.from(getContext()).inflate(R.layout.bike_list_item, parent, false);

        Bike currentPosition = getItem(position);

        ///ImageView bikeImage = currentView.findViewById(R.id.bikeListImage);
        assert currentPosition != null;
        //bikeImage.setImageResource(R.mipmap.bikeimage);

        TextView name = currentView.findViewById(R.id.ownernameTV);
        TextView address = currentView.findViewById(R.id.ownerAddTV);
        RatingBar ratingBar = currentView.findViewById(R.id.ratingBar);
        Button viewBike = currentView.findViewById(R.id.viewBikeBtn);
        Button rideBike = currentView.findViewById(R.id.rideBikeBtn);
        Button bookBike = currentView.findViewById(R.id.bookBikeBtn);

        name.setText(currentPosition.getOwnersName());
        address.setText(currentPosition.getOwnerAddress());
        ratingBar.setRating(currentPosition.getRating());

        viewBike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), BikeActivity.class);
                intent.putExtra("bikeID", currentPosition.getId());
                getContext().startActivity(intent);
            }
        });

        return currentView;
    }
}
