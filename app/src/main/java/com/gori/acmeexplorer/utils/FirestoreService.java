package com.gori.acmeexplorer.utils;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.gori.acmeexplorer.models.Trip;

import java.util.ArrayList;
import java.util.Date;

public class FirestoreService {
    private static String userId;
    private static FirebaseFirestore mDatabase;
    private static FirestoreService service;

    public static FirestoreService getServiceInstance(){
        if (service == null || mDatabase == null) {
            service = new FirestoreService();
            mDatabase = FirebaseFirestore.getInstance();
        }

        if (userId == null || userId.isEmpty()) {
            userId = FirebaseAuth.getInstance().getCurrentUser() != null ? FirebaseAuth.getInstance().getCurrentUser().getUid() : "";
        }

        return service;
    }

    public Task<DocumentReference> saveTrip(Trip trip){
         return mDatabase.collection("users").document(userId).collection("trips").add(trip);
    }

    public Task<Void> selectTrip(String id, boolean isSelected) {
        return mDatabase.collection("users").document(userId).collection("trips").document(id).update("isSelected", isSelected);
    }

    public Task<QuerySnapshot> getTrips() {
         return mDatabase.collection("users").document(userId).collection("trips").get();
    }

    public Task<QuerySnapshot> getSelectedTrips() {
        Query query = mDatabase.collection("users").document(userId).collection("trips");
        query = query.whereEqualTo("isSelected", true);
        return query.get();
    }

    public Task<QuerySnapshot> getFilteredTrips(Double minPrice, Double maxPrice, Date minDate, Date maxDate) {
        Query query = mDatabase.collection("users").document(userId).collection("trips");

        if (minPrice != -1) query = query.whereGreaterThanOrEqualTo("price", minPrice);
        if (maxPrice != -1) query = query.whereLessThanOrEqualTo("price", maxPrice);
        if (minDate != null) query = query.whereGreaterThanOrEqualTo("startDate", minDate);
        if (maxDate != null) query = query.whereLessThanOrEqualTo("endDate", maxDate);

        return query.get();
    }

    public void getTrip(String id, EventListener<DocumentSnapshot> snapshotListener) {
        mDatabase.collection("users").document(userId).collection("trips").document(id).addSnapshotListener(snapshotListener);
    }
}
