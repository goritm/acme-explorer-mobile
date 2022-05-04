package com.gori.acmeexplorer.trips;

import static com.gori.acmeexplorer.utils.Utils.LOGGER_NAME;
import static com.gori.acmeexplorer.utils.Utils.formatDate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.snackbar.Snackbar;
import com.gori.acmeexplorer.BuildConfig;
import com.gori.acmeexplorer.R;
import com.gori.acmeexplorer.api.Geolocation;
import com.gori.acmeexplorer.api.GeolocationService;
import com.gori.acmeexplorer.api.RetrofitSingleton;
import com.gori.acmeexplorer.models.Trip;
import com.gori.acmeexplorer.utils.FirestoreService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class TripDetailActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final int LOCATION_REQUEST_CODE = 0x123;
    private LatLng endCityLocation, userLocation;
    private GoogleMap mMap;
    private Polyline polyline;

    private FirestoreService firestoreService;
    private Retrofit retrofit;

    private Trip trip;

    private ImageView ivImage, ivIcon;
    private TextView tvStartDate, tvEndDate, tvStartCity, tvEndCity, tvPrice;
    private Button buyButton;

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

        firestoreService = FirestoreService.getServiceInstance();
        retrofit = RetrofitSingleton.getRetrofitInstance();

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        requestLocationPermissions();
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
                    endCityLocation = new LatLng(firstItem.getLat(), firstItem.getLon());
                    googleMap.addMarker(new MarkerOptions().title(firstItem.getCountry()).position(endCityLocation));
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(endCityLocation, 4.0f));

                    ArrayList<LatLng> pathPoints = new ArrayList<>();
                    pathPoints.add(endCityLocation);
                    pathPoints.add(userLocation);

                    PolylineOptions polyLineOptions = new PolylineOptions();
                    polyLineOptions.color(Color.BLUE);
                    polyLineOptions.width(5f);
                    polyLineOptions.addAll(pathPoints);
                    polyLineOptions.geodesic(true);

                    polyline = googleMap.addPolyline(polyLineOptions);
                    polyline.setGeodesic(true);
                }
            }

            @Override
            public void onFailure(Call<List<Geolocation>> call, Throwable t) {
                Log.i(LOGGER_NAME, "onFailure: " + t.getMessage());
            }
        });
    }

    public void requestLocationPermissions() {
        String[] permissions = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        if (ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                Snackbar.make(ivImage, "Necesitamos tu localización para esta funcionalidad", Snackbar.LENGTH_SHORT).setAction("Aceptar", view -> {
                    ActivityCompat.requestPermissions(TripDetailActivity.this, permissions, LOCATION_REQUEST_CODE);
                }).show();
            } else {
                ActivityCompat.requestPermissions(TripDetailActivity.this, permissions, LOCATION_REQUEST_CODE);
            }
        } else {
            StartLocationService();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                StartLocationService();
            } else {
                Toast.makeText(this, "Necesitas darle permisos para mostrar la distancia hasta el destino", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void StartLocationService() {
        FusedLocationProviderClient locationServices = LocationServices.getFusedLocationProviderClient(this);
        locationServices.getLastLocation().addOnCompleteListener(task -> {
            if(task.isSuccessful() && task.getResult() != null){
                Location location = task.getResult();
                Toast.makeText(this,
                        "Current Location: " + location.getLatitude()
                                + ", " + location.getLongitude(),
                        Toast.LENGTH_SHORT)
                        .show();

                userLocation = new LatLng(location.getLatitude(), location.getLongitude());

                mMap.addCircle(new CircleOptions()
                        .center(userLocation)
                        .radius(1000)
                        .strokeColor(Color.RED)
                        .fillColor(Color.BLUE));
            }
        });

    }
}