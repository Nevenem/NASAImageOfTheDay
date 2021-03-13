package com.example.nasaimageoftheday;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;

import com.google.android.material.button.MaterialButton;

import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener  {

    MaterialButton btnDatePicker;
    MaterialButton btnGetNASAImage;

    private int mYear, mMonth, mDay;
    private String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Get the DatePicker button
        btnDatePicker = findViewById(R.id.pick_date);
        btnDatePicker.setOnClickListener(this);

        // Get the GetNASAImage button
        btnGetNASAImage = findViewById(R.id.get_NASA_image);
        btnGetNASAImage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    month++;
                    imageUrl = "https://api.nasa.gov/planetary/apod?api_key=DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d&date=" + year + "-" + month + "-" + dayOfMonth;
                    Log.i("MONTH", String.valueOf(month));
                }
            }, mYear, mMonth, mDay);

            datePickerDialog.show();
            imageUrl = "https://api.nasa.gov/planetary/apod?api_key=DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d&date=" + mYear + "-" + mMonth + "-" + mDay;
            Log.i("Date", imageUrl);

        } else if (v == btnGetNASAImage) {

            // Open DisplayImageDetailsActivity
            Intent intent = new Intent(getBaseContext(), DisplayImageDetailsActivity.class);
            intent.putExtra("IMAGE_URL", imageUrl);
            startActivity(intent);
        }
    }

}