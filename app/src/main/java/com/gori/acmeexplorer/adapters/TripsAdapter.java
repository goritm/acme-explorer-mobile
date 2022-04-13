package com.gori.acmeexplorer.adapters;

import static com.gori.acmeexplorer.utils.Utils.dateFormatter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.gori.acmeexplorer.R;
import com.gori.acmeexplorer.models.Trip;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TripsAdapter extends RecyclerView.Adapter<TripsAdapter.ViewHolder> {
    private ArrayList<Trip> localDataSet;
    private OnTripListener mOnTripListener;

    public TripsAdapter(ArrayList<Trip> dataSet, OnTripListener onTripListener) {
        this.localDataSet = dataSet;
        this.mOnTripListener = onTripListener;
    }

    public interface OnTripListener {
        void onTripClick(int position);

        void onSelectTrip(int position);
    }

    @Override
    public int getItemCount() {
        return localDataSet.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.trip_item, viewGroup, false);

        return new ViewHolder(view, mOnTripListener);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Trip trip = localDataSet.get(position);

        viewHolder.bindView(position, mOnTripListener);

        Picasso.with(viewHolder.itemView.getContext()).load(trip.getImageUrl()).placeholder(R.drawable.ic_placeholder).error(R.drawable.ic_error).into(viewHolder.getImageView());
        viewHolder.getTextViewCities().setText(trip.getStartCity() + " - " + trip.getEndCity());
        viewHolder.getTextViewPrice().setText(trip.getPrice().toString() + "€");
        viewHolder.getTextViewDates().setText(trip.getStartDate().format(dateFormatter) + " - " + trip.getEndDate().format(dateFormatter));
        viewHolder.getSelectedIcon().setImageResource(trip.getSelected() ? R.drawable.ic_selected : R.drawable.ic_not_selected);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private final ImageView imageView, selectedIcon;
        private final TextView textViewCities, textViewPrice, textViewDates;

        OnTripListener onTripListener;

        public ViewHolder(View view, OnTripListener onTripListener) {
            super(view);

            this.onTripListener = onTripListener;

            imageView = view.findViewById(R.id.trip_image);
            selectedIcon = view.findViewById(R.id.selectedIcon);
            textViewCities = view.findViewById(R.id.trip_cities);
            textViewPrice = view.findViewById(R.id.trip_price);
            textViewDates = view.findViewById(R.id.trip_dates);

            view.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onTripListener.onTripClick(getAdapterPosition());
        }

        public void bindView(int position, OnTripListener onTripListener) {
            selectedIcon.setOnClickListener(v -> onTripListener.onSelectTrip(getAdapterPosition()));
        }

        private ImageView getImageView() {
            return imageView;
        }

        public ImageView getSelectedIcon() {
            return selectedIcon;
        }

        public TextView getTextViewCities() {
            return textViewCities;
        }

        public TextView getTextViewPrice() {
            return textViewPrice;
        }

        public TextView getTextViewDates() {
            return textViewDates;
        }
    }
}
