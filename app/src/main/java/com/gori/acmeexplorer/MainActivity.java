package com.gori.acmeexplorer;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    Calendar calendar = Calendar.getInstance();
    int yy = calendar.get(Calendar.YEAR);
    int mm = calendar.get(Calendar.MONTH);
    int dd = calendar.get(Calendar.DAY_OF_MONTH);
    int hh = calendar.get(Calendar.HOUR_OF_DAY);
    int mmm = calendar.get(Calendar.MINUTE);

    TextView textViewDate, textViewTime;
    ImageView imageViewPicasso;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewDate = findViewById(R.id.textViewDate);
        textViewTime = findViewById(R.id.textViewTime);

        imageViewPicasso = findViewById(R.id.imageViewPicassoExample);
        Picasso.with(this).load("https://i.imgur.com/tGbaZCY.jpg").into(imageViewPicasso);
    }

    public void selectDate(View view){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                textViewDate.setText(day + "/" + (month + 1) + "/" + year);
                yy = year;
                mm = month;
                dd = day;
            }
        }, yy, mm, dd);

        datePickerDialog.show();
    }

    public void selectTime(View view){
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minutes) {

                if(minutes < 10)
                {
                    minutes = '0' + minutes;
                }

                textViewTime.setText(hour + ":" + minutes);
                hh = hour;
                mmm = minutes;
            }
        }, hh, mmm, true);

        timePickerDialog.show();
    }

    public void goToAdapters(View view){
        Intent intent = new Intent(MainActivity.this, MainMenu.class);
        startActivity(intent);
    }
}