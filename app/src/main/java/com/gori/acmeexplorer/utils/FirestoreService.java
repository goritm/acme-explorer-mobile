package com.gori.acmeexplorer.utils;

import com.google.android.gms.tasks.OnCompleteListener;
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

    public void saveTrip(Trip trip, OnCompleteListener<DocumentReference> listener){
        mDatabase.collection("users").document(userId).collection("trips").add(trip).addOnCompleteListener(listener);
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

    public Task<QuerySnapshot> getFilteredTrips(ArrayList<Filter> filters) {
        Query query = mDatabase.collection("users").document(userId).collection("trips");

        query = query.orderBy("endCity").limit(3);
//        if(filters.size() > 0){
//            if(filters.get(0))
//            query = query.whereGreaterThanOrEqualTo("minPrice", filters.get())
//        }

        return query.get();
    }

    public void getTrip(String id, EventListener<DocumentSnapshot> snapshotListener) {
        mDatabase.collection("users").document(userId).collection("trips").document(id).addSnapshotListener(snapshotListener);
    }
}
