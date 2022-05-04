package com.gori.acmeexplorer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.gori.acmeexplorer.adapters.MainMenuAdapter;
import com.gori.acmeexplorer.auth.LoginActivity;
import com.gori.acmeexplorer.models.MenuItem;

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
        lvMainMenu.setAdapter(adapter);

        bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull android.view.MenuItem item) {
        switch(item.getItemId()){
            case R.id.page_profile:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                return true;

            case R.id.page_upload:
                startActivity(new Intent(this, FirebaseStorageActivity.class));
                return true;
        }

        return false;
    }
}