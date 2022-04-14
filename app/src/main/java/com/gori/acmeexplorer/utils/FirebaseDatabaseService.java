package com.gori.acmeexplorer.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDatabaseService {
    private static String userId;
    private static FirebaseDatabaseService service;
    private static FirebaseDatabase mDatabase;

    public static FirebaseDatabaseService getServiceInstance() {
        if (service == null || mDatabase == null) {
            service = new FirebaseDatabaseService();
            mDatabase = FirebaseDatabase.getInstance("https://acme-explorer-2a8ae-default-rtdb.europe-west1.firebasedatabase.app");
            mDatabase.setPersistenceEnabled(true);
        }

        if (userId == null || userId.isEmpty()) {
            userId = FirebaseAuth.getInstance().getCurrentUser() != null ? FirebaseAuth.getInstance().getCurrentUser().getUid() : "";
        }

        return service;
    }

    public DatabaseReference getTrip(String tripId) {
        return mDatabase.getReference("users/u123/trips/" + tripId);
    }
}
