package com.gori.acmeexplorer.trips.selected;

import static com.gori.acmeexplorer.utils.Utils.dateFormatter;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.gori.acmeexplorer.R;
import com.gori.acmeexplorer.models.Trip;
import com.squareup.picasso.Picasso;

public class SelectedTripDetailActivity extends AppCompatActivity {
    private ImageView ivImage, ivIcon;
    private TextView tvStartDate, tvEndDate, tvStartCity, tvEndCity, tvPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_trip_detail);

        ivImage = findViewById(R.id.ivEndCityImage);
        tvStartCity = findViewById(R.id.tvSelectedStartCity);
        tvEndCity = findViewById(R.id.tvSelectedEndCity);
        tvPrice = findViewById(R.id.tvSelectedPrice);
        tvStartDate = findViewById(R.id.tvSelectedStartDate);
        tvEndDate = findViewById(R.id.tvSelectedEndDate);
        ivIcon = findViewById(R.id.ivSelectedIcon);

        Trip trip = (Trip) getIntent().getSerializableExtra("selected_trip");

        Picasso.with(this).load(trip.getImageUrl()).into(ivImage);
        tvStartCity.setText("Sale desde: " + trip.getStartCity());
        tvEndCity.setText(trip.getEndCity());
        tvStartDate.setText("Fecha de Ida: " + trip.getStartDate().format(dateFormatter));
        tvEndDate.setText("Fecha de Vuelta: " + trip.getEndDate().format(dateFormatter));
        tvPrice.setText(trip.getPrice() + "€");

        if(trip.getSelected()) {
            ivIcon.setImageResource(R.drawable.ic_selected);
        } else {
            ivIcon.setImageResource(R.drawable.ic_not_selected);
        }
    }

    public void buyTrip(View view) {
        Snackbar.make(view, "Buy logic goes here", Snackbar.LENGTH_SHORT).show();
    }
}