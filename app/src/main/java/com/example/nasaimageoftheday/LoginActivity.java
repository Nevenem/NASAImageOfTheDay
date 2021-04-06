package com.example.nasaimageoftheday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    SharedPreferences prefs = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Get the nameTextView and the passwordTextView
        nameTextView = findViewById(R.id.editName);
        passwordTextView = findViewById(R.id.editPassword);

        // Get the Login button
        MaterialButton loginButton = findViewById(R.id.login_button);

        // Prepopulate the fields with values from shared preferences
        prefs = getSharedPreferences("mypreference",
                Context.MODE_PRIVATE);
        String savedName = prefs.getString("Name", "");

        if (prefs.contains("Name")) {
            nameTextView.setText(savedName);
        }
        if (prefs.contains("Password")) {
            passwordTextView.setText(prefs.getString("Password", ""));

        }

        nameTextView.setText(savedName);

        String savedPass = prefs.getString("Password", "");
        passwordTextView.setText(savedPass);

        // Set the listener
        loginButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("Name", nameTextView.getText().toString());
            editor.putString("Password", passwordTextView.getText().toString());
            editor.commit();

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