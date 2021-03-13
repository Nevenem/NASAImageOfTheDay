package com.example.nasaimageoftheday;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class DisplayImageDetailsActivity extends AppCompatActivity {

    TextView urlTextView;
    TextView imageUrlTextView;
    TextView imageDescriptionTextView;
    TextView imageDateTextView;


    private String url;
    private String imageDescription;
    private String imageUrl;
    private String imageDate;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image_details);

        // set the visibility of the progress bar
        ProgressBar progressBar = findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.VISIBLE);

        // Connect to the database
        MySQLiteHelper mySQLiteHelper = new MySQLiteHelper(this);
        database = mySQLiteHelper.getWritableDatabase();

        url = getIntent().getStringExtra("IMAGE_URL");

        // Get the imageDate TextView
        urlTextView = findViewById(R.id.url);

        // Get the imageUrl TextView
        imageUrlTextView = findViewById(R.id.image_url);

        // Get the imageDescription TextView
        imageDescriptionTextView = findViewById(R.id.image_description);

        // Get the imageDate TextView
        imageDateTextView = findViewById(R.id.image_date);

        MyHTTPRequest req = new MyHTTPRequest();
        req.execute(url);
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
                publishProgress(25);
                publishProgress(50);
                publishProgress(75);

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
                imageUrl = imageDetailsJSON.getString("hdurl");

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

            //set the TextView fields to show image details
            urlTextView.setText(url);
            imageDateTextView.setText(imageDate);
            imageDescriptionTextView.setText(imageDescription);
            imageUrlTextView.setText(imageUrl);

            // put the data into database
            putImageDetailsIntoDatabase();

            ProgressBar progressBar = findViewById(R.id.progress_bar);
            progressBar.setVisibility(ProgressBar.INVISIBLE);

        }
    }

    private void putImageDetailsIntoDatabase() {
        ContentValues newImageValue = new ContentValues();

        // define values for each column in the database
        newImageValue.put(MySQLiteHelper.COLUMN_DATE, imageDate);
        newImageValue.put(MySQLiteHelper.COLUMN_DESCRIPTION, imageDescription);
        newImageValue.put(MySQLiteHelper.COLUMN_URL, imageUrl);

        Log.i("URL to insert: ", imageUrl);
        //insert into database
        long newId = database.insert(MySQLiteHelper.TABLE_NAME, null, newImageValue);

    }
}