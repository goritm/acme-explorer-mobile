package com.gori.acmeexplorer;

import static com.gori.acmeexplorer.utils.Utils.parseDate;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gori.acmeexplorer.adapters.MainMenuAdapter;
import com.gori.acmeexplorer.auth.LoginActivity;
import com.gori.acmeexplorer.models.MenuItem;
import com.gori.acmeexplorer.models.Trip;
import com.gori.acmeexplorer.utils.FirestoreService;

import java.util.ArrayList;

public class MainMenuActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    private ArrayList<MenuItem> menuItems;
    private BottomNavigationView bottomNav;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        menuItems = MenuItem.createTripsList();

        MainMenuAdapter adapter = new MainMenuAdapter(menuItems, this);

        ListView lvMainMenu = findViewById(R.id.lvMainMenu);
        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(this);

        lvMainMenu.setAdapter(adapter);

        FirestoreService firestoreService = FirestoreService.getServiceInstance();
        firestoreService.saveTrip(new Trip("Barquisimeto", "Caracas", 100, parseDate("2023-01-01"), parseDate("2023-01-01"), false, "https://dynamic-media-cdn.tripadvisor.com/media/photo-o/15/33/fe/d4/caracas.jpg?w=700&h=500&s=1"), task -> {
            if (task.isSuccessful()){
                Log.i("epic", "firestore almacenado completado: " + task.getResult().getId());
            } else {
                Log.i("epic", "firestore almacenado fallado");
            }
        });
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
        switch(item.getItemId()){
            case R.id.page_profile:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;
        }

        return false;
    }
}