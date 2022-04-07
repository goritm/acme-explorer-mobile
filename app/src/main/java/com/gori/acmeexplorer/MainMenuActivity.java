package com.gori.acmeexplorer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

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
        MainMenuAdapter adapter = new MainMenuAdapter(menuItems);

        RecyclerView rvMainMenu = findViewById(R.id.rvMainMenu);
        rvMainMenu.setAdapter(adapter);
        rvMainMenu.setLayoutManager(new LinearLayoutManager(this));
    }
}