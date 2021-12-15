package com.example.trottonbikes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

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

        ImageView bikeImage = currentView.findViewById(R.id.bikeListImage);
        assert currentPosition != null;
        bikeImage.setImageResource(R.mipmap.bikestockimage);

        return super.getView(position, convertView, parent);
    }
}
