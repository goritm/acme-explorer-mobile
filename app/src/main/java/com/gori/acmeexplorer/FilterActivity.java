package com.gori.acmeexplorer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class FilterActivity extends AppCompatActivity {

    Calendar calendar = Calendar.getInstance();
    int yy = calendar.get(Calendar.YEAR);
    int mm = calendar.get(Calendar.MONTH);
    int dd = calendar.get(Calendar.DAY_OF_MONTH);

    String selectedStartDate, selectedEndDate;

    DatePickerDialog datePickerDialog;

    TextView tvFilterStartDate, tvFilterEndDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        tvFilterStartDate = findViewById(R.id.tvFilterStartDate);
        tvFilterEndDate = findViewById(R.id.tvFilterEndDate);
    }

    public void selectStartDate(View view){
        datePickerDialog = new DatePickerDialog(this, (datePicker, year, month, day) -> {
            selectedStartDate = day + "/" + (month + 1) + "/" + year;
            tvFilterStartDate.setText(selectedStartDate);
            yy = year;
            mm = month;
            dd = day;
        }, yy, mm, dd);

        datePickerDialog.show();
    }

    public void selectEndDate(View view){
        datePickerDialog = new DatePickerDialog(this, (datePicker, year, month, day) -> {
            selectedEndDate = day + "/" + (month + 1) + "/" + year;
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
        intent.putExtra("filterMinPrice", tvFilterStartDate.getText().toString());
        intent.putExtra("filterMaxPrice", tvFilterEndDate.getText().toString());

        setResult(1, intent);
        finish();
    }
}