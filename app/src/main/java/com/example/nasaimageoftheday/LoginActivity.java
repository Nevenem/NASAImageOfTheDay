package com.example.nasaimageoftheday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class LoginActivity extends BaseActivity{

    TextView nameTextView;
    TextView passwordTextView;
    MaterialButton LoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get the nameTextView and the passwordTextView
        nameTextView = findViewById(R.id.name);
        passwordTextView = findViewById(R.id.password);

        // Get the Login button
        MaterialButton loginButton = findViewById(R.id.login_button);


        loginButton.setOnClickListener(v -> {
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        menu.removeItem(R.id.login);
        menu.removeItem(R.id.image_list);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}