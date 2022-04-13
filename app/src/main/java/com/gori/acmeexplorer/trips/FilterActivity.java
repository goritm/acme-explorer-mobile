package com.gori.acmeexplorer.trips;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.gori.acmeexplorer.R;

import java.time.LocalDate;
import java.util.Calendar;

public class FilterActivity extends AppCompatActivity {

    Calendar calendar = Calendar.getInstance();
    int yy = calendar.get(Calendar.YEAR);
    int mm = calendar.get(Calendar.MONTH);
    int dd = calendar.get(Calendar.DAY_OF_MONTH);

    String selectedStartDate, selectedEndDate;

    DatePickerDialog datePickerDialog;

    TextView tvFilterStartDate, tvFilterEndDate;
    EditText editTextMaxPrice, editTextMinPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        tvFilterStartDate = findViewById(R.id.tvFilterStartDate);
        tvFilterEndDate = findViewById(R.id.tvFilterEndDate);
        editTextMinPrice = findViewById(R.id.editTextMinPrice);
        editTextMaxPrice = findViewById(R.id.editTextMaxPrice);
    }

    public void selectStartDate(View view){
        datePickerDialog = new DatePickerDialog(this, (datePicker, year, month, day) -> {
            selectedStartDate = LocalDate.of(year, month + 1, day).toString();
            tvFilterStartDate.setText(selectedStartDate);
            yy = year;
            mm = month;
            dd = day;
        }, yy, mm, dd);

        datePickerDialog.show();
    }

    public void selectEndDate(View view){
        datePickerDialog = new DatePickerDialog(this, (datePicker, year, month, day) -> {
            selectedEndDate = LocalDate.of(year, month + 1, day).toString();
            tvFilterEndDate.setText(selectedEndDate);
            yy = year;
            mm = month;
            dd = day;
        }, yy, mm, dd);

        datePickerDialog.show();
    }

    public void sendData(View view) {
        Intent intent = new Intent();

        intent.putExtra("filterMinDate", selectedStartDate);
        intent.putExtra("filterMaxDate", selectedEndDate);
        intent.putExtra("filterMinPrice", editTextMinPrice.getText().toString());
        intent.putExtra("filterMaxPrice", editTextMaxPrice.getText().toString());

        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}