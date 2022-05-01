package com.gori.acmeexplorer.trips;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.gori.acmeexplorer.R;
import com.gori.acmeexplorer.adapters.TripsAdapter;
import com.gori.acmeexplorer.models.Trip;
import com.gori.acmeexplorer.utils.FirestoreService;

import java.util.ArrayList;
import java.util.List;

public class SelectedTripListActivity extends AppCompatActivity implements TripsAdapter.OnTripListener {
    private FirestoreService firestoreService;
    private ArrayList<Trip> selectedTrips = new ArrayList<>();

    private TripsAdapter selectedTripsAdapter;
    private GridLayoutManager gridLayoutManager;
    private RecyclerView rvSelectedTripList;

    private ProgressBar loadingPB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_trip_list);

        firestoreService = FirestoreService.getServiceInstance();

        rvSelectedTripList = findViewById(R.id.rvSelectedTripList);
        selectedTripsAdapter = new TripsAdapter(selectedTrips,this);
        rvSelectedTripList.setAdapter(selectedTripsAdapter);
        gridLayoutManager = new GridLayoutManager(this, 1);
        rvSelectedTripList.setLayoutManager(gridLayoutManager);
        loadingPB = findViewById(R.id.selectedLoadingPB);

        firestoreService.getSelectedTrips().addOnSuccessListener(queryDocumentSnapshots -> {
            List<DocumentSnapshot> documentSnapshotList = queryDocumentSnapshots.getDocuments();
            for(DocumentSnapshot snapshot: documentSnapshotList){
                Trip trip = snapshot.toObject(Trip.class);
                trip.setId(snapshot.getId());
                selectedTrips.add(trip);
            }
            loadingPB.setVisibility(View.GONE);
            selectedTripsAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            Snackbar.make(rvSelectedTripList, "Error: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
        });
    }

    @Override
    public void onTripClick(int position) {
        Intent intent = new Intent(this, TripDetailActivity.class);
        intent.putExtra("trip", selectedTrips.get(position));
        startActivity(intent);
    }

    @Override
    public void onSelectTrip(int position) {
        Trip selectedTrip = selectedTrips.get(position);

        selectedTrip.setIsSelected(false);
        selectedTrips.remove(selectedTrip);

        firestoreService.selectTrip(selectedTrip.getId(), false).addOnSuccessListener(queryDocumentSnapshots -> {
            Snackbar.make(rvSelectedTripList, "Removed trip from list", Snackbar.LENGTH_SHORT).show();
            selectedTripsAdapter.notifyItemRemoved(position);
            selectedTripsAdapter.notifyItemRangeChanged(position, selectedTripsAdapter.getItemCount());
        }).addOnFailureListener(e -> {
            Snackbar.make(rvSelectedTripList, "Error: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
        });
    }
}