package com.gori.acmeexplorer.trips.selected;

import static com.gori.acmeexplorer.utils.Utils.SHARED_DATA_SELECTED_TRIPS;
import static com.gori.acmeexplorer.utils.Utils.SHARED_DATA_TRIPS;
import static com.gori.acmeexplorer.utils.Utils.SHARED_DATA_UNIQUE_NAME;
import static com.gori.acmeexplorer.utils.Utils.gson;
import static com.gori.acmeexplorer.utils.Utils.tripArrayType;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gori.acmeexplorer.R;
import com.gori.acmeexplorer.adapters.TripsAdapter;
import com.gori.acmeexplorer.models.Trip;

import java.util.ArrayList;

public class SelectedTripListActivity extends AppCompatActivity implements TripsAdapter.OnTripListener {
    private ArrayList<Trip> trips;
    private ArrayList<Trip> selectedTrips;
    private TripsAdapter selectedTripsAdapter;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_trip_list);

        RecyclerView rvSelectedTripList = findViewById(R.id.rvSelectedTripList);

        try {
            sharedPreferences = getSharedPreferences(SHARED_DATA_UNIQUE_NAME, MODE_PRIVATE);
            String json = sharedPreferences.getString(SHARED_DATA_SELECTED_TRIPS, "{}");
            String trips_json = sharedPreferences.getString(SHARED_DATA_TRIPS, "{}");

            selectedTrips = json == "{}" ? new ArrayList<>() : gson.fromJson(json, tripArrayType);

            if (trips_json != "{}") {
                trips = gson.fromJson(trips_json, tripArrayType);
            }
        } catch (Exception e) {

        }

        selectedTripsAdapter = new TripsAdapter(selectedTrips, this);
        rvSelectedTripList.setAdapter(selectedTripsAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        rvSelectedTripList.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void onTripClick(int position) {
        Intent intent = new Intent(this, SelectedTripDetailActivity.class);
        intent.putExtra("selected_trip", selectedTrips.get(position));
        startActivity(intent);
    }

    @Override
    public void onSelectTrip(int position) {
        Trip selectedTrip = selectedTrips.get(position);

        selectedTrip.setSelected(false);
        selectedTrips.remove(selectedTrip);

        selectedTripsAdapter.notifyItemRemoved(position);
        selectedTripsAdapter.notifyItemRangeChanged(position, selectedTripsAdapter.getItemCount());

        // Delete from main trips page
        for(int i = 0; i < trips.size(); i++) {
            if(trips.get(i).equals(selectedTrip)){
                trips.get(i).setSelected(false);
            }
        }

        // Update local db
        sharedPreferences.edit().putString(SHARED_DATA_SELECTED_TRIPS, gson.toJson(selectedTrips)).apply();
        sharedPreferences.edit().putString(SHARED_DATA_TRIPS, gson.toJson(trips)).apply();
    }
}