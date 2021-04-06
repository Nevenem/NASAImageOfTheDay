package com.example.nasaimageoftheday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.File;

public class ImageFromDatabaseActivity extends BaseActivity implements View.OnClickListener {

    TextView urlTextView;
    TextView imageUrlTextView;
    ImageView imageView;
    TextView imageDescriptionTextView;
    TextView imageDateTextView;
    MaterialButton removeFromFavoritesButton;
    MaterialButton goToImageListButton;

    private Long imageId;
    private String imageFileName;
    private String imageDate;
    private String imageUrlString;
    private String imageDescription;
    private Bitmap bmp;
    private SQLiteDatabase database;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_from_database);

        // Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Connect to the database
        MySQLiteHelper mySQLiteHelper = new MySQLiteHelper(this);
        database = mySQLiteHelper.getWritableDatabase();

        // Get information from intent
        imageId = getIntent().getLongExtra("IMAGE_ID", 0);
        imageFileName = getIntent().getStringExtra("IMAGE_NAME");
        imageUrlString = getIntent().getStringExtra("IMAGE_URL");
        imageDate = getIntent().getStringExtra("IMAGE_DATE");
        imageDescription = getIntent().getStringExtra("IMAGE_DESCRIPTION");

        // Get the imageUrl TextView
        urlTextView = findViewById(R.id.url);
        urlTextView.setText(imageUrlString);

        // Get the imageDate TextView
        imageDateTextView = findViewById(R.id.image_date);
        imageDateTextView.setText(imageDate);

        //Get the imageView
        imageView = findViewById(R.id.image);

        // Get the image from the filesystem
        FileInputStream fis = null;
        try {
            fis = openFileInput(imageFileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap imageBitmap = BitmapFactory.decodeStream(fis);
        imageView.setImageBitmap(imageBitmap);

        // Get the addToFavoritesButton
        removeFromFavoritesButton = findViewById(R.id.remove_from_favorites);
        removeFromFavoritesButton.setOnClickListener(this);

        // Get the goToImageListButton
        goToImageListButton = findViewById(R.id.go_to_list);
        goToImageListButton.setOnClickListener(this);

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

    @Override
    public void onClick(View v) {
        // Return the result to the calling activity
        Intent returnIntent = new Intent(getBaseContext(), ImageListActivity.class);
        if (v.getId() == R.id.remove_from_favorites) {

            Log.i("delete", "this button pressed ");
            // delete the image
            deleteImage();
            deleteFile(imageFileName);
            returnIntent.putExtra("DELETED_IMAGE", imageFileName);
            setResult(RESULT_OK, returnIntent);
        }   else if (v.getId() == R.id.go_to_list) {
            setResult(RESULT_CANCELED, returnIntent);
            Log.i("show", "this button pressed");
        }

        finish();
    }

    private void deleteImage() {
        if (deleteImageFromDatabase()) {
            Toast.makeText(this, "Delete successful.", Toast.LENGTH_LONG)
                    .show();
        }
    }

    private boolean deleteImageFromDatabase() {
       return  database.delete(MySQLiteHelper.TABLE_NAME, MySQLiteHelper.COLUMN_ID + "=" + imageId, null) > 0;
    }
}