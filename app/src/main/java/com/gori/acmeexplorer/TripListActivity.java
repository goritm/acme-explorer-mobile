package com.gori.acmeexplorer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import android.widget.LinearLayout;
import android.widget.Switch;

import com.gori.acmeexplorer.adapters.TripsAdapter;
import com.gori.acmeexplorer.models.Trip;

import java.util.ArrayList;

public class TripListActivity extends AppCompatActivity {
    private ArrayList<Trip> trips;
    private Switch switchColumns;
    private GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);

        switchColumns = findViewById(R.id.switchCols);
        RecyclerView rvTripList = findViewById(R.id.rvTripList);

        trips = Trip.createTripsList();
        TripsAdapter adapter = new TripsAdapter(trips);

        rvTripList.setAdapter(adapter);

        gridLayoutManager = new GridLayoutManager(this, 1);

        rvTripList.setLayoutManager(gridLayoutManager);

        switchColumns.setOnCheckedChangeListener((compoundButton, b) -> {
            if(compoundButton.isChecked()){
                gridLayoutManager.setSpanCount(2);
            }
            else {
                gridLayoutManager.setSpanCount(1);
            }
        });
    }
}