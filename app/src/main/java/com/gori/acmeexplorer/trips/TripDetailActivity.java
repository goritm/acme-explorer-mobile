package com.gori.acmeexplorer.trips;

import static com.gori.acmeexplorer.utils.Utils.LOGGER_NAME;
import static com.gori.acmeexplorer.utils.Utils.formatDate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.gori.acmeexplorer.BuildConfig;
import com.gori.acmeexplorer.R;
import com.gori.acmeexplorer.api.Geolocation;
import com.gori.acmeexplorer.api.GeolocationService;
import com.gori.acmeexplorer.api.RetrofitSingleton;
import com.gori.acmeexplorer.models.Trip;
import com.gori.acmeexplorer.utils.FirestoreService;
import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TripDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    private FirestoreService firestoreService;
    private Retrofit retrofit;
    private GoogleMap mMap;

    private Trip trip;

    private ImageView ivImage, ivIcon;
    private TextView tvStartDate, tvEndDate, tvStartCity, tvEndCity, tvPrice;
    private Button buyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_detail);

        firestoreService = FirestoreService.getServiceInstance();
        retrofit = RetrofitSingleton.getRetrofitInstance();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        ivImage = findViewById(R.id.ivTrip);
        tvStartCity = findViewById(R.id.tvStartCity);
        tvEndCity = findViewById(R.id.tvEndCity);
        tvPrice = findViewById(R.id.tvPrice);
        tvStartDate = findViewById(R.id.tvStartDate);
        tvEndDate = findViewById(R.id.tvEndDate);
        ivIcon = findViewById(R.id.ivIcon);
        buyButton = findViewById(R.id.buy_trip_button);

        trip = (Trip) getIntent().getSerializableExtra("trip");

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

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.mMap = googleMap;

        GeolocationService service = retrofit.create(GeolocationService.class);
        Call<List<Geolocation>> response = service.getGeolocation(trip.getEndCity(), BuildConfig.OPENWEATHER_API_KEY);

        response.enqueue(new Callback<List<Geolocation>>() {
            @Override
            public void onResponse(Call<List<Geolocation>> call, Response<List<Geolocation>> response) {
                if(response.isSuccessful() && response.body() != null){
                    List<Geolocation> geolocationList = response.body();
                    Geolocation firstItem = geolocationList.get(0);
                    LatLng location = new LatLng(firstItem.getLat(), firstItem.getLon());
                    googleMap.addMarker(new MarkerOptions().title(firstItem.getCountry()).position(location));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(location, 4.0f));
                }
            }

            @Override
            public void onFailure(Call<List<Geolocation>> call, Throwable t) {
                Log.i(LOGGER_NAME, "onFailure: " + t.getMessage());
            }
        });
    }
}