package com.gori.acmeexplorer;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.widget.Button;
import android.widget.Switch;

import com.gori.acmeexplorer.adapters.TripsAdapter;
import com.gori.acmeexplorer.models.Trip;

import java.util.ArrayList;

public class TripListActivity extends AppCompatActivity implements TripsAdapter.OnTripListener {
    private ArrayList<Trip> trips;
    private Switch switchColumns;
    private Button filterButton;
    private TripsAdapter tripsAdapter;
    private RecyclerView rvTripList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);

        rvTripList = findViewById(R.id.rvTripList);
        switchColumns = findViewById(R.id.switchCols);
        filterButton = findViewById(R.id.filterButton);

        trips = Trip.createTripsList();
        tripsAdapter = new TripsAdapter(trips, this);
        rvTripList.setAdapter(tripsAdapter);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 1);
        rvTripList.setLayoutManager(gridLayoutManager);

        switchColumns.setOnCheckedChangeListener((compoundButton, b) -> {
            if(compoundButton.isChecked()){
                gridLayoutManager.setSpanCount(2);
            }
            else {
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
}