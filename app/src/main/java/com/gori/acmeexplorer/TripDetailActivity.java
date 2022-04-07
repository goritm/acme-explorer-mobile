package com.gori.acmeexplorer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.gori.acmeexplorer.models.Trip;
import com.squareup.picasso.Picasso;

public class TripDetailActivity extends AppCompatActivity {
    private ImageView ivImage;
    private TextView tvStartDate, tvEndDate, tvStartCity, tvEndCity, tvPrice, tvSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);

        ivImage = findViewById(R.id.ivTrip);
        tvStartCity = findViewById(R.id.tvStartCity);
        tvEndCity = findViewById(R.id.tvEndCity);
        tvPrice = findViewById(R.id.tvPrice);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        tvSelected = findViewById(R.id.tvSelected);

        Trip trip = (Trip) getIntent().getSerializableExtra("trip");

        Picasso.with(this).load(trip.getImageUrl()).into(ivImage);
        tvStartCity.setText("Start City: " + trip.getStartCity());
        tvEndCity.setText("End City: " + trip.getEndCity());
        tvStartDate.setText("Start Date: " + trip.getStartDate());
        tvEndDate.setText("End Date: " + trip.getEndDate());
        tvPrice.setText("Price: " + trip.getPrice());
        tvSelected.setText("This is selected: " + trip.getSelected());
    }
}