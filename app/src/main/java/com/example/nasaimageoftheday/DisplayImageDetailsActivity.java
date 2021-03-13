package com.example.nasaimageoftheday;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_image_details);

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

                //get the image date
                imageDate = imageDetailsJSON.getString("date");

            } catch (Exception e) {
                e.printStackTrace();
            }

            return "Done";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            //set the TextView fields to show image details
            urlTextView.setText(url);
            imageDateTextView.setText(imageDate);
            imageUrlTextView.setText(imageUrl);
            imageDescriptionTextView.setText(imageDescription);

        }
    }
}