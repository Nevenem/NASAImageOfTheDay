package com.example.nasaimageoftheday;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class DisplayImageDetailsActivity extends AppCompatActivity {

    TextView imageUrlTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image_details);

        String imageUrl = getIntent().getStringExtra("IMAGE_URL");

        // Get the imageUrl textview
        imageUrlTextView = findViewById(R.id.image_url);
        imageUrlTextView.setText(imageUrl);
    }
}