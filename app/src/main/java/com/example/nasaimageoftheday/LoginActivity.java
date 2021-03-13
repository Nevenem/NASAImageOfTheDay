package com.example.nasaimageoftheday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class LoginActivity extends AppCompatActivity {

    TextView nameTextView;
    TextView passwordTextView;
    MaterialButton LoginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

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
}