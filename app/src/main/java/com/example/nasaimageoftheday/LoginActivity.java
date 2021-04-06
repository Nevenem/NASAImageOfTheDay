package com.example.nasaimageoftheday;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.android.material.button.MaterialButton;

public class LoginActivity extends BaseActivity{

    EditText name;
    EditText password;
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
        name = findViewById(R.id.editName);
        password = findViewById(R.id.editPassword);

        // Get the Login button
        MaterialButton loginButton = findViewById(R.id.login_button);

        // Prepopulate the fields with values from shared preferences
        prefs = getSharedPreferences("mypreference",
                Context.MODE_PRIVATE);
        String savedName = prefs.getString("Name", "");

        if (prefs.contains("Name")) {
            name.setText(savedName);
        }
        if (prefs.contains("Password")) {
            password.setText(prefs.getString("Password", ""));

        }

        name.setText(savedName);

        String savedPass = prefs.getString("Password", "");
        password.setText(savedPass);

        // Set the listener
        loginButton.setOnClickListener(v -> {
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("Name", name.getText().toString());
            editor.putString("Password", password.getText().toString());
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