package com.gori.acmeexplorer.utils;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.gori.acmeexplorer.models.Trip;

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
        mDatabase.collection("users").document(userId).collection("trip").add(trip).addOnCompleteListener(listener);
    }
}
