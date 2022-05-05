package com.gori.acmeexplorer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toolbar;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.gori.acmeexplorer.adapters.MainMenuAdapter;
import com.gori.acmeexplorer.auth.LoginActivity;
import com.gori.acmeexplorer.models.MenuItem;

import java.util.ArrayList;

public class MainMenuActivity extends AppCompatActivity {
    private ArrayList<MenuItem> menuItems;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MaterialToolbar topAppBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        menuItems = MenuItem.createTripsList();
        MainMenuAdapter adapter = new MainMenuAdapter(menuItems, this);
        ListView lvMainMenu = findViewById(R.id.lvMainMenu);
        lvMainMenu.setAdapter(adapter);

        drawerLayout = findViewById(R.id.drawerLayout);
        navigationView = findViewById(R.id.navigationView);
        topAppBar = findViewById(R.id.topAppBar);

        topAppBar.setNavigationOnClickListener(view -> {
            drawerLayout.open();
        });

        navigationView.setNavigationItemSelectedListener(item -> {
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
        });
    }
}