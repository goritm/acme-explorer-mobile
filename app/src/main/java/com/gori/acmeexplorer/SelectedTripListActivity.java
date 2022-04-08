package com.gori.acmeexplorer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gori.acmeexplorer.adapters.TripsAdapter;
import com.gori.acmeexplorer.models.Trip;

import java.util.ArrayList;

public class SelectedTripListActivity extends AppCompatActivity implements TripsAdapter.OnTripListener {
    public ArrayList<Trip> selectedTrips;
    private TripsAdapter tripsAdapter;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_trip_list);

        RecyclerView rvSelectedTripList = findViewById(R.id.rvSelectedTripList);

        try {
            sharedPreferences  = getSharedPreferences("trip_data", MODE_PRIVATE);
            String json = sharedPreferences.getString("my_object","{}");
            selectedTrips = new Gson().fromJson(json, new TypeToken<ArrayList<Trip>>(){}.getType());
        } catch (Exception e){

        }

        tripsAdapter = new TripsAdapter(selectedTrips, this);
        rvSelectedTripList.setAdapter(tripsAdapter);

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
        Trip trip = selectedTrips.get(position);
        trip.setSelected(!trip.getSelected());
        tripsAdapter.notifyDataSetChanged();
    }
}