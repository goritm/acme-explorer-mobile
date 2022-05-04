package com.gori.acmeexplorer.trips;

import static com.gori.acmeexplorer.utils.Utils.parseDate;
import static com.gori.acmeexplorer.utils.Utils.twoDigits;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.snackbar.Snackbar;
import com.gori.acmeexplorer.R;
import com.gori.acmeexplorer.models.Trip;
import com.gori.acmeexplorer.utils.DatePickerFragment;
import com.gori.acmeexplorer.utils.FirestoreService;

import java.util.Date;

public class AddTripActivity extends AppCompatActivity {
    private FirestoreService firestoreService;
    private EditText etStartCity, etEndCity, etPrice, etStartDate, etEndDate, etImageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);

        firestoreService = FirestoreService.getServiceInstance();

        etStartCity = findViewById(R.id.et_StartCity);
        etEndCity = findViewById(R.id.et_EndCity);
        etPrice = findViewById(R.id.et_Price);
        etStartDate = findViewById(R.id.et_StartDate);
        etEndDate = findViewById(R.id.et_EndDate);
        etImageUrl = findViewById(R.id.et_imageUrl);

        etStartDate.setOnClickListener(view -> {
            showDatePickerDialog(etStartDate);
        });

        etEndDate.setOnClickListener(view -> {
            showDatePickerDialog(etEndDate);
        });
    }

    private void showDatePickerDialog(EditText editText) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance((datePicker, year, month, day) -> {
            final String selectedDate = twoDigits(day) + "-" + twoDigits(month + 1) + "-" + year;
            editText.setText(selectedDate);
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void addTrip(View view) {
        String startCity = etStartCity.getText().toString().trim();
        String endCity = etEndCity.getText().toString().trim();
        Double price = Double.valueOf(etPrice.getText().toString().trim());
        Date startDate = parseDate(etStartDate.getText().toString());
        Date endDate = parseDate(etEndDate.getText().toString());
        String imageUrl = etImageUrl.getText().toString().trim();

        Trip newTrip = new Trip(startCity, endCity, price, startDate, endDate, false, imageUrl);

        if(!newTrip.isValid()){
            Snackbar.make(etStartCity,  "Debes llenar todos los campos", Snackbar.LENGTH_SHORT).show();
            return;
        }

        firestoreService.saveTrip(newTrip).addOnSuccessListener(documentReference -> {
            Intent returnIntent = new Intent();
            returnIntent.putExtra("documentReference", documentReference.getId());
            setResult(RESULT_OK, returnIntent);
            finish();
        }).addOnFailureListener(e -> {
            Snackbar.make(etStartCity,  "Error adding document" + e, Snackbar.LENGTH_SHORT).show();
        });
    }
}