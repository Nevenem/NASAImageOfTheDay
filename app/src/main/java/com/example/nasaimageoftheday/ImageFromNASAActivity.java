package com.example.nasaimageoftheday;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class ImageFromNASAActivity extends AppCompatActivity implements View.OnClickListener {

    TextView urlTextView;
    TextView imageUrlTextView;
    ImageView imageView;
    TextView imageDescriptionTextView;
    TextView imageDateTextView;
    MaterialButton addToFavoritesButton;
    MaterialButton goToImageListButton;

    private long newId;
    private String url;
    private String imageDescription;
    private String imageUrlString;
    private URL imageUrl;
    private String imageFileName;
    private Bitmap bmp;
    private String imageDate;
    private SQLiteDatabase database;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_from_nasa);

        // Set the visibility of the progress bar
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        // Connect to the database
        MySQLiteHelper mySQLiteHelper = new MySQLiteHelper(this);
        database = mySQLiteHelper.getWritableDatabase();

        url = getIntent().getStringExtra("IMAGE_URL");

        // Get the imageUrl TextView
        urlTextView = findViewById(R.id.url);

        //Get the imageView
        imageView = findViewById(R.id.image);
        // Get the imageDescription TextView
        imageDescriptionTextView = findViewById(R.id.image_description);

        // Get the imageDate TextView
        imageDateTextView = findViewById(R.id.image_date);

        // Get the addToFavoritesButton
        addToFavoritesButton = findViewById(R.id.add_to_favorites);
        addToFavoritesButton.setOnClickListener(this);

        // Get the goToImageListButton
        goToImageListButton = findViewById(R.id.go_to_list);
        goToImageListButton.setOnClickListener(this);

        MyHTTPRequest req = new MyHTTPRequest();
        req.execute(url);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.add_to_favorites) {

            // put the data into database
            downloadImage();
            putImageDetailsIntoDatabase();

            Snackbar.make(v, "Image saved!", Snackbar.LENGTH_LONG)
                    .show();
        } else if (v.getId() == R.id.go_to_list) {

            Intent intent = new Intent(getBaseContext(), ImageListActivity.class);
            startActivity(intent);

        }

    }

    private void downloadImage() {

        imageFileName = imageUrlString.substring(imageUrlString.lastIndexOf('/') + 1);
        try {
            FileOutputStream out = openFileOutput(imageFileName, Context.MODE_PRIVATE);
            bmp.compress(Bitmap.CompressFormat.PNG, 80, out);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void putImageDetailsIntoDatabase() {
        ContentValues newImageValue = new ContentValues();

        // define values for each column in the database
        newImageValue.put(MySQLiteHelper.COLUMN_IMAGE_NAME, imageFileName);
        newImageValue.put(MySQLiteHelper.COLUMN_DATE, imageDate);
        newImageValue.put(MySQLiteHelper.COLUMN_DESCRIPTION, imageDescription);
        newImageValue.put(MySQLiteHelper.COLUMN_URL, imageUrlString);

        //insert into database
        newId = database.insert(MySQLiteHelper.TABLE_NAME, null, newImageValue);

    }

    private class MyHTTPRequest extends AsyncTask<String,Integer,String> {

        @Override
        protected String doInBackground(String... strings) {

            //create a URL object of what server to contact:
            URL url = null;
            try {
                url = new URL(strings[0]);

                // open the connection
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();


                // Update progress bar
                for (int i = 0; i < 100; i++) {
                    publishProgress(i);
                }
                          // wait for data
                InputStream response = urlConnection.getInputStream();

                //JSON reading
                BufferedReader reader = new BufferedReader(new InputStreamReader(response, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null ) {
                    sb.append(line + "\n");
                }
                String result = sb.toString();

                // convert to JSON
                JSONObject imageDetailsJSON = new JSONObject(result);

                // get the description of the image
                imageDescription = imageDetailsJSON.getString("explanation");

                // get the image url
                imageUrlString = imageDetailsJSON.getString("url");
                imageUrl = new URL(imageUrlString);
                    bmp = BitmapFactory.decodeStream(imageUrl.openConnection().getInputStream());

                // get the image date
                imageDate = imageDetailsJSON.getString("date");

                publishProgress(100);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return "Done";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

               ProgressBar progressBar = findViewById(R.id.progress_bar);
                progressBar.setVisibility(ProgressBar.VISIBLE);
                progressBar.setProgress(values[0]);
            }


        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            // Set the TextView fields to show image details
            urlTextView.setText(url);
            imageDateTextView.setText(imageDate);

            // Set the ImageView
            imageView.setImageBitmap(bmp);
            ProgressBar progressBar = findViewById(R.id.progress_bar);
            progressBar.setVisibility(ProgressBar.INVISIBLE);

            // Set the listener to the image so it opens the image in the browser when clicked
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(imageUrlString));
                    startActivity(intent);
                }
            });

        }
    }


}