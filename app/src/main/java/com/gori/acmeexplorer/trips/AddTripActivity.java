package com.gori.acmeexplorer.trips;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.gori.acmeexplorer.R;
import com.gori.acmeexplorer.utils.DatePickerFragment;

public class AddTripActivity extends AppCompatActivity {

    private EditText etStartDate, etEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        etStartDate = findViewById(R.id.et_StartDate);
        etEndDate = findViewById(R.id.et_EndDate);


        etStartDate.setOnClickListener(view -> {
            showDatePickerDialog(etStartDate);
        });

        etEndDate.setOnClickListener(view -> {
            showDatePickerDialog(etEndDate);
        });



        //        FirestoreService firestoreService = FirestoreService.getServiceInstance();
//        firestoreService.saveTrip(new Trip("Barquisimeto", "Caracas", 100, parseDate("2023-01-01"), parseDate("2023-01-01"), false, "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/15/33/fe/d4/caracas.jpg?w=700&h=500&s=1"), task -> {
//            if (task.isSuccessful()){
//                DocumentReference documentReference = task.getResult();
//
//                Log.i("epic", "firestore almacenado completado: " + documentReference.getId());
//
//                documentReference.get().addOnCompleteListener(task1 -> {
//                    if(task1.isSuccessful()){
//                        DocumentSnapshot document = task1.getResult();
//                        Trip trip = document.toObject(Trip.class);
//                        Log.i("epic", "firestore almacenado feedback: " + trip.toString());
//                    }
//                });
//            } else {
//                Log.i("epic", "firestore almacenado fallado");
//            }
//        });
//
    }

    private void showDatePickerDialog(EditText editText) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance((datePicker, year, month, day) -> {
            final String selectedDate = twoDigits(day) + " / " + twoDigits(month + 1) + " / " + year;
            editText.setText(selectedDate);
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    private String twoDigits(int n) {
        return (n < 10) ? ("0" + n) : String.valueOf(n);
    }

    public void addTrip(View view) {
    }
}