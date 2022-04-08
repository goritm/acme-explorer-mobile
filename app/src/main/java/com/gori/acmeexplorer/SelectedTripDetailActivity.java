package com.gori.acmeexplorer;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.gori.acmeexplorer.models.Trip;
import com.squareup.picasso.Picasso;

import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class SelectedTripDetailActivity extends AppCompatActivity {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);

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
        tvStartDate.setText("Fecha de Ida: " + trip.getStartDate().format(formatter));
        tvEndDate.setText("Fecha de Vuelta: " + trip.getEndDate().format(formatter));
        tvPrice.setText(trip.getPrice() + "€");

        if(trip.getSelected()) {
            ivIcon.setImageResource(R.drawable.ic_selected);
        } else {
            ivIcon.setImageResource(R.drawable.ic_not_selected);
        }

        ivIcon.setOnClickListener(view -> {
            trip.setSelected(!trip.getSelected());

            if(trip.getSelected()) {
                ivIcon.setImageResource(R.drawable.ic_selected);
            } else {
                ivIcon.setImageResource(R.drawable.ic_not_selected);
            }
        });
    }

    public void buyTrip(View view) {
        Snackbar.make(view, "Buy logic goes here", Snackbar.LENGTH_SHORT).show();
    }
}