package com.gori.acmeexplorer;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.gori.acmeexplorer.adapters.MainMenuAdapter;
import com.gori.acmeexplorer.models.MenuItem;

import java.util.ArrayList;

public class MainMenuActivity extends AppCompatActivity {
    private ArrayList<MenuItem> menuItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        menuItems = MenuItem.createTripsList();

        MainMenuAdapter adapter = new MainMenuAdapter(menuItems, this);

        ListView lvMainMenu = findViewById(R.id.lvMainMenu);

        lvMainMenu.setAdapter(adapter);
    }
}