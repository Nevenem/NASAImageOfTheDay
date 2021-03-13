package com.example.nasaimageoftheday;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    TextView nameTextView;
    TextView passwordTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Get the nameTextView and the passwordTextView
        nameTextView = findViewById(R.id.name);
        passwordTextView = findViewById(R.id.password);

    }
}