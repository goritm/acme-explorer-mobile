package com.gori.acmeexplorer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.gori.acmeexplorer.models.Trip;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

public class TripDetailActivity extends AppCompatActivity {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM);

    private ImageView ivImage, ivIcon;
    private TextView tvStartDate, tvEndDate, tvStartCity, tvEndCity, tvPrice;

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
        ivIcon = findViewById(R.id.ivIcon);

        Trip trip = (Trip) getIntent().getSerializableExtra("trip");

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
        });
    }
}