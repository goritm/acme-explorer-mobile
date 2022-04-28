package com.gori.acmeexplorer.trips;

import static com.gori.acmeexplorer.utils.Utils.SHARED_DATA_SELECTED_TRIPS;
import static com.gori.acmeexplorer.utils.Utils.SHARED_DATA_TRIPS;
import static com.gori.acmeexplorer.utils.Utils.SHARED_DATA_UNIQUE_NAME;
import static com.gori.acmeexplorer.utils.Utils.gson;
import static com.gori.acmeexplorer.utils.Utils.parseDate;
import static com.gori.acmeexplorer.utils.Utils.tripArrayType;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.widget.Button;
import android.widget.Switch;

import com.google.android.material.snackbar.Snackbar;
import com.gori.acmeexplorer.R;
import com.gori.acmeexplorer.adapters.TripsAdapter;
import com.gori.acmeexplorer.models.Trip;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class TripListActivity extends AppCompatActivity implements TripsAdapter.OnTripListener {
    private ArrayList<Trip> trips;
    private ArrayList<Trip> selectedTrips;
    private ArrayList<Trip> filteredTrips = new ArrayList<>();

    private Switch switchColumns;
    private Button filterButton;
    private TripsAdapter tripsAdapter;
    private RecyclerView rvTripList;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);

        rvTripList = findViewById(R.id.rvTripList);
        switchColumns = findViewById(R.id.switchCols);
        filterButton = findViewById(R.id.filterButton);

        try {
            sharedPreferences = getSharedPreferences(SHARED_DATA_UNIQUE_NAME, MODE_PRIVATE);
            String trips_json = sharedPreferences.getString(SHARED_DATA_TRIPS, "{}");
            String selected_trips_json = sharedPreferences.getString(SHARED_DATA_SELECTED_TRIPS, "{}");

            if (trips_json == "{}") {
                trips = Trip.createTripsList();
                sharedPreferences.edit().putString("trip-data", gson.toJson(trips)).apply();
            } else {
                trips = gson.fromJson(trips_json, tripArrayType);
            }

            selectedTrips = selected_trips_json == "{}" ? new ArrayList<>() : gson.fromJson(selected_trips_json, tripArrayType);
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
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();

                    Double filterMinPrice = Double.parseDouble(data.getStringExtra("filterMinPrice"));
                    Double filterMaxPrice = Double.parseDouble(data.getStringExtra("filterMaxPrice"));
                    Date filterMinDate = parseDate(data.getStringExtra("filterMinDate"));
                    Date filterMaxDate = parseDate(data.getStringExtra("filterMaxDate"));

                    for (int i = 0; i < trips.size(); i++) {
                        Trip trip = trips.get(i);

                        Boolean validMinPrice = trip.getPrice() >= filterMinPrice;
                        Boolean validMaxPrice = trip.getPrice() <= filterMaxPrice;

                        Boolean validStartDate = trip.getStartDate().compareTo(filterMinDate) > 0 || trip.getStartDate().compareTo(filterMinDate) == 0;
                        Boolean validEndDate = trip.getStartDate().compareTo(filterMaxDate) < 0 || trip.getStartDate().compareTo(filterMaxDate) == 0;

                        if ((validMinPrice && validMaxPrice) || (validStartDate && validEndDate)) {
                            filteredTrips.add(trip);
                        }
                    }

                    if (filteredTrips.size() > 0) {
                        trips.clear();
                        trips.addAll(filteredTrips);
                        tripsAdapter.notifyDataSetChanged();
                    } else {
                        Snackbar.make(rvTripList, "No se encontraron viajes con los filtros seleccionados", Snackbar.LENGTH_SHORT).show();
                    }
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
            for (int i = 0; i < selectedTrips.size(); i++) {
                if (selectedTrips.get(i).equals(trip)) {
                    selectedTrips.remove(i);
                }
            }
        }

        sharedPreferences.edit().putString(SHARED_DATA_TRIPS, gson.toJson(trips)).apply();
        sharedPreferences.edit().putString(SHARED_DATA_SELECTED_TRIPS, gson.toJson(selectedTrips)).apply();
        tripsAdapter.notifyDataSetChanged();
    }
}