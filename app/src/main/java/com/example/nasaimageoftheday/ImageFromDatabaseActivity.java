package com.example.nasaimageoftheday;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

public class ImageFromDatabaseActivity extends AppCompatActivity {

    private SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image_details);

    }
}