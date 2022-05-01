package com.gori.acmeexplorer.trips;

import static com.gori.acmeexplorer.utils.Utils.parseDate;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Switch;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.gori.acmeexplorer.R;
import com.gori.acmeexplorer.adapters.TripsAdapter;
import com.gori.acmeexplorer.models.Trip;
import com.gori.acmeexplorer.utils.FirestoreService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TripListActivity extends AppCompatActivity implements TripsAdapter.OnTripListener {

    private FirestoreService firestoreService;
    private GridLayoutManager gridLayoutManager;

    private ArrayList<Trip> trips = new ArrayList<>();
    private ArrayList<Trip> filteredTrips = new ArrayList<>();

    private Switch switchColumns;
    private Button filterButton;
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
        loadingPB = findViewById(R.id.loadingPB);

        tripsAdapter = new TripsAdapter(trips, this);
        rvTripList.setAdapter(tripsAdapter);
        gridLayoutManager = new GridLayoutManager(this, 1);
        rvTripList.setLayoutManager(gridLayoutManager);

        firestoreService.getTrips().addOnSuccessListener(queryDocumentSnapshots -> {
            // trips = (ArrayList<Trip>) queryDocumentSnapshots.toObjects(Trip.class);
            // We manually add the id to allow trip modification
            List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
            for(DocumentSnapshot snapshot: documentSnapshotList){
                Trip trip = snapshot.toObject(Trip.class);
                trip.setId(snapshot.getId());
                trips.add(trip);
            }
            loadingPB.setVisibility(View.GONE);
            tripsAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            Snackbar.make(rvTripList, "Error: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
        });

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

        trip.setIsSelected(!trip.getIsSelected());
        firestoreService.selectTrip(trip.getId(), trip.getIsSelected()).addOnSuccessListener(queryDocumentSnapshots -> {
            Snackbar.make(rvTripList, trip.getIsSelected() ? "Added trip to selected list" : "Removed trip from list", Snackbar.LENGTH_SHORT).show();
            tripsAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            Snackbar.make(rvTripList, "Error: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
        });
    }
}