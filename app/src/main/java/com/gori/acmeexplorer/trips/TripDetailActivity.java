package com.gori.acmeexplorer.trips;

import static com.gori.acmeexplorer.utils.Utils.formatDate;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.gori.acmeexplorer.R;
import com.gori.acmeexplorer.maps.MapsActivity;
import com.gori.acmeexplorer.models.Trip;
import com.gori.acmeexplorer.utils.FirestoreService;
import com.squareup.picasso.Picasso;

public class TripDetailActivity extends AppCompatActivity {
    private FirestoreService firestoreService;

    private ImageView ivImage, ivIcon;
    private TextView tvStartDate, tvEndDate, tvStartCity, tvEndCity, tvPrice;
    private Button buyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);

        firestoreService = FirestoreService.getServiceInstance();

        ivImage = findViewById(R.id.ivTrip);
        tvStartCity = findViewById(R.id.tvStartCity);
        tvEndCity = findViewById(R.id.tvEndCity);
        tvPrice = findViewById(R.id.tvPrice);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        ivIcon = findViewById(R.id.ivIcon);
        buyButton = findViewById(R.id.buy_trip_button);

        Trip trip = (Trip) getIntent().getSerializableExtra("trip");

        Picasso.with(this).load(trip.getImageUrl()).into(ivImage);
        tvStartCity.setText("Sale desde: " + trip.getStartCity());
        tvEndCity.setText(trip.getEndCity());
        tvStartDate.setText("Fecha de Ida: " + formatDate(trip.getStartDate()));
        tvEndDate.setText("Fecha de Vuelta: " + formatDate(trip.getEndDate()));
        tvPrice.setText(trip.getPrice() + "€");

        ivIcon.setImageResource(trip.getIsSelected() ? R.drawable.ic_selected : R.drawable.ic_not_selected);
        buyButton.setVisibility(trip.getIsSelected() ? View.VISIBLE : View.INVISIBLE);

        ivIcon.setOnClickListener(view -> {
            trip.setIsSelected(!trip.getIsSelected());
            firestoreService.selectTrip(trip.getId(), trip.getIsSelected()).addOnSuccessListener(queryDocumentSnapshots -> {
                ivIcon.setImageResource(trip.getIsSelected() ? R.drawable.ic_selected : R.drawable.ic_not_selected);
                buyButton.setVisibility(trip.getIsSelected() ? View.VISIBLE : View.INVISIBLE);
            }).addOnFailureListener(e -> {
                Snackbar.make(view, "Error: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
            });
        });
    }

    public void buyTrip(View view) {
        Toast.makeText(this, "Buy Logic goes here", Toast.LENGTH_SHORT).show();
    }

    public void openMap(View view) {
        startActivity(new Intent(this, MapsActivity.class));
    }
}