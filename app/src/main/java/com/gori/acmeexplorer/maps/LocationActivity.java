package com.gori.acmeexplorer.maps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.snackbar.Snackbar;
import com.gori.acmeexplorer.R;

public class LocationActivity extends AppCompatActivity {

    private static final int LOCATION_REQUEST_CODE = 0x123;
    private TextView tv_Helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        tv_Helper = findViewById(R.id.tv_Example);

        String[] permissions = new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION
        };

        if (ContextCompat.checkSelfPermission(this, permissions[0]) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                Snackbar.make(tv_Helper, "Necesitamos tu localización para esta funcionalidad", Snackbar.LENGTH_SHORT).setAction("Aceptar", view -> {
                    ActivityCompat.requestPermissions(LocationActivity.this, permissions, LOCATION_REQUEST_CODE);
                }).show();
            } else {
                ActivityCompat.requestPermissions(LocationActivity.this, permissions, LOCATION_REQUEST_CODE);
            }
        } else {
            StartLocationService();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                StartLocationService();
            } else {
                Toast.makeText(this, "Dale permisos che", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void StartLocationService() {
        FusedLocationProviderClient locationServices = LocationServices.getFusedLocationProviderClient(this);
        // uno solo
//                locationServices.getLastLocation().addOnCompleteListener(task -> {
//                    if(task.isSuccessful() && task.getResult() != null){
//                        Location location = task.getResult();
//                        Toast.makeText(this,
//                                "Location: " + location.getLatitude()
//                                        + ", " + location.getLongitude()
//                                        + ", " + location.getAccuracy(),
//                                Toast.LENGTH_SHORT)
//                                .show();
//                    }
//                });

        // continuo
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(3000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationServices.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    public void stopService(){
        LocationServices.getFusedLocationProviderClient(this).removeLocationUpdates(locationCallback);
    }

    LocationCallback locationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(@NonNull LocationResult locationResult) {
            super.onLocationResult(locationResult);

            if (locationResult == null || locationResult.getLastLocation() == null || !locationResult.getLastLocation().hasAccuracy()) {
                return;
            }

            Location location = locationResult.getLastLocation();

            Log.i("epic", "Location: " + location.getLatitude() + ", " + location.getLongitude() + ", " + location.getAccuracy());
        }
    };
}