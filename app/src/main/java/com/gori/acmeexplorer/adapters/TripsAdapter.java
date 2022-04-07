package com.gori.acmeexplorer.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.gori.acmeexplorer.R;
import com.gori.acmeexplorer.models.Trip;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.ViewHolder> {
    private ArrayList<Trip> localDataSet;

    public TripsAdapter(ArrayList<Trip> dataSet) {
        localDataSet = dataSet;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView imageView;
        private final TextView textViewCities, textViewPrice, textViewDates;
        private final Button button;

        public ViewHolder(View view){
            super(view);

            view.setOnClickListener(this);

            imageView = view.findViewById(R.id.trip_image);
            textViewCities = view.findViewById(R.id.trip_cities);
            textViewPrice = view.findViewById(R.id.trip_price);
            textViewDates = view.findViewById(R.id.trip_dates);
            button = view.findViewById(R.id.select_button);
        }

        @Override
        public void onClick(View view) {
            for(int i = 0; i < localDataSet.size(); i++){
                Snackbar.make(view, "position " + getLayoutPosition(), Snackbar.LENGTH_SHORT).show();
            }
        }

        private ImageView getImageView() {
            return imageView;
        };

        public TextView getTextViewCities() {
            return textViewCities;
        }

        public TextView getTextViewPrice() {
            return textViewPrice;
        }

        public TextView getTextViewDates() {
            return textViewDates;
        }

        public Button getButton() {
            return button;
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trip_item, viewGroup, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Trip trip = localDataSet.get(position);

        Picasso.with(viewHolder.itemView.getContext()).load(trip.getImageUrl()).into(viewHolder.getImageView());
        viewHolder.getTextViewCities().setText(trip.getStartCity() + " - " + trip.getEndCity());
        viewHolder.getTextViewPrice().setText(trip.getPrice().toString() + "€");
        viewHolder.getTextViewDates().setText(trip.getStartDate()  + " - " + trip.getEndDate());
        viewHolder.getButton().setOnClickListener(view -> {
            Snackbar.make(view, "sex " + position, Snackbar.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

}
