package com.gori.acmeexplorer.trips;

import static com.gori.acmeexplorer.utils.Utils.LOGGER_NAME;
import static com.gori.acmeexplorer.utils.Utils.parseDate;

import androidx.activity.result.ActivityResult;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.gori.acmeexplorer.R;
import com.gori.acmeexplorer.adapters.TripsAdapter;
import com.gori.acmeexplorer.models.Trip;
import com.gori.acmeexplorer.utils.BetterActivityResult;
import com.gori.acmeexplorer.utils.FirestoreService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TripListActivity extends AppCompatActivity implements TripsAdapter.OnTripListener {

    protected final BetterActivityResult<Intent, ActivityResult> activityLauncher = BetterActivityResult.registerActivityForResult(this);

    private FirestoreService firestoreService;
    private GridLayoutManager gridLayoutManager;

    private ArrayList<Trip> trips = new ArrayList<>();

    private Switch switchColumns;
    private Button filterButton, btn_resetFilters;
    private FloatingActionButton addTripButton;
    private ProgressBar loadingPB;

    private TripsAdapter tripsAdapter;
    private RecyclerView rvTripList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_list);

        firestoreService = FirestoreService.getServiceInstance();

        rvTripList = findViewById(R.id.rvTripList);
        switchColumns = findViewById(R.id.switchCols);
        filterButton = findViewById(R.id.filterButton);
        btn_resetFilters = findViewById(R.id.btn_resetFilters);
        addTripButton = findViewById(R.id.floating_action_button);
        loadingPB = findViewById(R.id.loadingPB);

        tripsAdapter = new TripsAdapter(trips, this);
        rvTripList.setAdapter(tripsAdapter);
        gridLayoutManager = new GridLayoutManager(this, 1);
        rvTripList.setLayoutManager(gridLayoutManager);

        switchColumns.setOnCheckedChangeListener((compoundButton, b) -> {
            if (compoundButton.isChecked()) {
                gridLayoutManager.setSpanCount(2);
            } else {
                gridLayoutManager.setSpanCount(1);
            }
        });

        filterButton.setOnClickListener(view -> filterTrips());
        addTripButton.setOnClickListener(view -> addTrip());
        btn_resetFilters.setOnClickListener(view -> {
            loadTrips();
            btn_resetFilters.setVisibility(View.GONE);
        });

        loadTrips();
    }

    @Override
    public void onTripClick(int position) {
        Intent intent = new Intent(this, TripDetailActivity.class);
        intent.putExtra("trip", trips.get(position));
        startActivity(intent);
    }

    @Override
    public void onSelectTrip(int position) {
        Trip trip = trips.get(position);

        trip.setIsSelected(!trip.getIsSelected());
        firestoreService.selectTrip(trip.getId(), trip.getIsSelected()).addOnSuccessListener(queryDocumentSnapshots -> {
            Snackbar.make(rvTripList, trip.getIsSelected() ? "Added trip to selected list" : "Removed trip from list", Snackbar.LENGTH_SHORT).show();
            tripsAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            Snackbar.make(rvTripList, "Error: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
        });
    }

    public void loadTrips() {
        trips.clear();

        firestoreService.getTrips().addOnSuccessListener(queryDocumentSnapshots -> {
            // trips = (ArrayList<Trip>) queryDocumentSnapshots.toObjects(Trip.class);
            // We manually add the id to allow trip modification
            List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
            for (DocumentSnapshot snapshot : documentSnapshotList) {
                Trip trip = snapshot.toObject(Trip.class);
                trip.setId(snapshot.getId());
                trips.add(trip);
            }
            loadingPB.setVisibility(View.GONE);
            tripsAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            Snackbar.make(rvTripList, "Error: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
        });
    }

    public void addTrip() {
        Intent intent = new Intent(this, AddTripActivity.class);
        activityLauncher.launch(intent, result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                String documentId = result.getData().getStringExtra("documentReference");
                Snackbar.make(rvTripList, "Trip " + documentId + " added successfully!", Snackbar.LENGTH_SHORT).show();
                loadTrips();
            }
        });
    }

    public void filterTrips() {
        Intent intent = new Intent(this, FilterActivity.class);
        activityLauncher.launch(intent, result -> {
            if (result.getResultCode() == Activity.RESULT_OK) {
                Intent data = result.getData();

                Double filterMinPrice =
                        data.getStringExtra("filterMinPrice").length() == 0
                        ? -1
                        : Double.valueOf(data.getStringExtra("filterMinPrice"));
                Double filterMaxPrice = data.getStringExtra("filterMaxPrice").length() == 0
                        ? -1
                        : Double.valueOf(data.getStringExtra("filterMaxPrice"));
                Date filterMinDate = parseDate(data.getStringExtra("filterMinDate"));
                Date filterMaxDate = parseDate(data.getStringExtra("filterMaxDate"));

                loadingPB.setVisibility(View.VISIBLE);

                firestoreService.getFilteredTrips(filterMinPrice, filterMaxPrice, filterMinDate, filterMaxDate)
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            trips.clear();
                            List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
                            for (DocumentSnapshot snapshot : documentSnapshotList) {
                                Trip trip = snapshot.toObject(Trip.class);
                                trip.setId(snapshot.getId());
                                trips.add(trip);
                            }
                            loadingPB.setVisibility(View.GONE);
                            btn_resetFilters.setVisibility(View.VISIBLE);
                            tripsAdapter.notifyDataSetChanged();
                        })
                        .addOnFailureListener(e -> {
                            Snackbar.make(rvTripList, "Error: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                        });
                }
        });
    }
}