package com.example.nasaimageoftheday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;

import java.util.Calendar;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    MaterialButton btnDatePicker;
    MaterialButton btnGetNASAImage;

    private int mYear, mMonth, mDay;
    private String imageUrl;
    private AlertDialog.Builder builder;


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

        // Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set the NavigationDrawer
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.open, R.string.close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Get the header_layout
        View header = navigationView.getHeaderView(0);

        // Set the version textfield in the header
        TextView version = header.findViewById(R.id.version);
        version.setText(getActivityVersionNum());
    }

    private String getActivityVersionNum() {
        String version = "";
        try {
            PackageInfo pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.image_list:
                intent = new Intent(this, ImageListActivity.class);
                startActivity(intent);
                break;
            case R.id.login:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.help:
                builder = new AlertDialog.Builder(this);

                builder.setMessage(R.string.dialog_message).setTitle(R.string.dialog_title)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                                Toast.makeText(getApplicationContext(), "You clicked OK",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("AlertDialogExample");
                alert.show();
                break;
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_menu, menu);
        return true;
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
                }
            }, mYear, mMonth, mDay);

            datePickerDialog.show();
            imageUrl = "https://api.nasa.gov/planetary/apod?api_key=DgPLcIlnmN0Cwrzcg3e9NraFaYLIDI68Ysc6Zh3d&date=" + mYear + "-" + mMonth + "-" + mDay;
            Log.i("Date", imageUrl);

        } else if (v == btnGetNASAImage) {

            // Open DisplayImageDetailsActivity
            Intent intent = new Intent(getBaseContext(), ImageFromNASAActivity.class);
            intent.putExtra("IMAGE_URL", imageUrl);
            startActivity(intent);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Intent intent;
        switch (item.getItemId()) {
            case R.id.image_list:
                intent = new Intent(this, ImageListActivity.class);
                startActivity(intent);
                break;
            case R.id.login:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
        }

        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}