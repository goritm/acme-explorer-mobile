package com.gori.acmeexplorer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.gori.acmeexplorer.adapters.TripsAdapter;
import com.gori.acmeexplorer.models.Trip;

import java.util.ArrayList;

public class SelectedTripListActivity extends AppCompatActivity implements TripsAdapter.OnTripListener {
    public ArrayList<Trip> selectedTrips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_trip_list);

        RecyclerView rvSelectedTripList = findViewById(R.id.rvSelectedTripList);

        TripsAdapter tripsAdapter = new TripsAdapter(selectedTrips, this);
        rvSelectedTripList.setAdapter(tripsAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        rvSelectedTripList.setLayoutManager(gridLayoutManager);
    }

    @Override
    public void onTripClick(int position) {
        Intent intent = new Intent(this, SelectedTripListActivity.class);
        intent.putExtra("trip", selectedTrips.get(position));
        startActivity(intent);
    }
}