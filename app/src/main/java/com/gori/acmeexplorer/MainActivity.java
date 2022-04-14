package com.gori.acmeexplorer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.gori.acmeexplorer.auth.LoginActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            Toast.makeText(this, user.getEmail(), Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, MainMenuActivity.class));
        } else {
            startActivity(new Intent(this, LoginActivity.class));
        }

        super.onStart();
    }

}