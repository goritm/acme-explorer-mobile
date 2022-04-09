package com.gori.acmeexplorer;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.widget.Switch;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.gori.acmeexplorer.adapters.TripsAdapter;
import com.gori.acmeexplorer.models.Trip;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.util.ArrayList;

public class TripListActivity extends AppCompatActivity implements TripsAdapter.OnTripListener {
    private ArrayList<Trip> trips;
    public ArrayList<Trip> selectedTrips;

    private Switch switchColumns;
    private Button filterButton;
    private TripsAdapter tripsAdapter;
    private RecyclerView rvTripList;

    SharedPreferences sharedPreferences;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);

        rvTripList = findViewById(R.id.rvTripList);
        switchColumns = findViewById(R.id.switchCols);
        filterButton = findViewById(R.id.filterButton);

        try {
            sharedPreferences = getSharedPreferences("com.gori.acmeexplorer", MODE_PRIVATE);
            String trips_json = sharedPreferences.getString("trip-data", "{}");
            String selected_trips_json = sharedPreferences.getString("selected-trip-data","{}");

            gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(LocalDate.class, new Utils.LocalDateConverter())
                    .create();

            Type type = new TypeToken<ArrayList<Trip>>(){}.getType();

            if(trips_json == "{}"){
                trips = Trip.createTripsList();
                sharedPreferences.edit().putString("trip-data", gson.toJson(trips)).apply();
            } else {
                trips = gson.fromJson(trips_json, type);
            }

            if(selected_trips_json == "{}"){
                selectedTrips = new ArrayList<>();
            } else {
                selectedTrips = gson.fromJson(selected_trips_json, type);
            }
        } catch (Exception e) {

        }


        tripsAdapter = new TripsAdapter(trips, this);
        rvTripList.setAdapter(tripsAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        rvTripList.setLayoutManager(gridLayoutManager);

        switchColumns.setOnCheckedChangeListener((compoundButton, b) -> {
            if (compoundButton.isChecked()) {
                gridLayoutManager.setSpanCount(2);
            } else {
                gridLayoutManager.setSpanCount(1);
            }
        });

        filterButton.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), FilterActivity.class);
            activityResultLauncher.launch(intent);
        });
    }

    ActivityResultLauncher<Intent> activityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == 1) {
                    trips.remove(0);
                    tripsAdapter.notifyItemRemoved(0);
                    Intent data = result.getData();
                } else if (result.getResultCode() == 2) {
                    Intent data = result.getData();
                }
            });

    @Override
    public void onTripClick(int position) {
        Intent intent = new Intent(this, TripDetailActivity.class);
        intent.putExtra("trip", trips.get(position));
        startActivity(intent);
    }

    @Override
    public void onSelectTrip(int position) {
        Trip trip = trips.get(position);
        trip.setSelected(!trip.getSelected());

        if (trip.getSelected()) {
            selectedTrips.add(trip);
        } else {
            selectedTrips.remove(trip);
        }

        sharedPreferences.edit().putString("trip-data", gson.toJson(trips)).apply();
        sharedPreferences.edit().putString("selected-trip-data", gson.toJson(selectedTrips)).apply();
        tripsAdapter.notifyDataSetChanged();
    }
}