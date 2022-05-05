package com.gori.acmeexplorer;

import static com.gori.acmeexplorer.utils.Utils.LOGGER_NAME;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.utils.widget.ImageFilterView;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.gori.acmeexplorer.adapters.MainMenuAdapter;
import com.gori.acmeexplorer.auth.LoginActivity;
import com.gori.acmeexplorer.models.MenuItem;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class MainMenuActivity extends AppCompatActivity {
    private FirebaseUser user;

    private ArrayList<MenuItem> menuItems;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private MaterialToolbar topAppBar;
    private ImageFilterView iv_profilePicture;

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
        iv_profilePicture = findViewById(R.id.iv_profilePicture);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null && user.getPhotoUrl() != null) {
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .placeholder(R.drawable.ic_launcher_background)
                    .centerCrop()
                    .into(iv_profilePicture);
        }

        topAppBar.setNavigationOnClickListener(view -> drawerLayout.open());

        navigationView.setNavigationItemSelectedListener(item -> {
            switch(item.getItemId()){
                case R.id.menuItem_Profile:
                    startActivity(new Intent(this, ProfileActivity.class));
                    return true;

                case R.id.menuItem_Logout:
                    FirebaseAuth.getInstance().signOut();
                    startActivity(new Intent(this, LoginActivity.class));
                    return true;
            }
            return false;
        });
    }
}