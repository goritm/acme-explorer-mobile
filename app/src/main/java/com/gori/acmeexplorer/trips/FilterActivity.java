package com.gori.acmeexplorer.trips;

import static com.gori.acmeexplorer.utils.Utils.LOGGER_NAME;
import static com.gori.acmeexplorer.utils.Utils.twoDigits;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.gori.acmeexplorer.R;
import com.gori.acmeexplorer.utils.DatePickerFragment;

public class FilterActivity extends AppCompatActivity {
    private EditText et_StartDate, et_EndDate, et_MinPrice, et_MaxPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        et_MinPrice = findViewById(R.id.et_MinPrice);
        et_MaxPrice = findViewById(R.id.et_MaxPrice);
        et_StartDate = findViewById(R.id.et_StartDate);
        et_EndDate = findViewById(R.id.et_EndDate);

        et_StartDate.setOnClickListener(view -> {
            showDatePickerDialog(et_StartDate);
        });

        et_EndDate.setOnClickListener(view -> {
            showDatePickerDialog(et_EndDate);
        });
    }

    private void showDatePickerDialog(EditText editText) {
        DatePickerFragment newFragment = DatePickerFragment.newInstance((datePicker, year, month, day) -> {
            final String selectedDate = twoDigits(day) + "-" + twoDigits(month + 1) + "-" + year;
            editText.setText(selectedDate);
        });
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public void filterTrips(View view) {
        Intent intent = new Intent();

        intent.putExtra("filterMinPrice", et_MinPrice.getText().toString());
        intent.putExtra("filterMaxPrice", et_MaxPrice.getText().toString());
        intent.putExtra("filterMinDate", et_StartDate.getText().toString());
        intent.putExtra("filterMaxDate", et_EndDate.getText().toString());

        setResult(RESULT_OK, intent);
        finish();
    }
}