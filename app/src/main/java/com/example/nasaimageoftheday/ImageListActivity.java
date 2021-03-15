package com.example.nasaimageoftheday;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public class ImageListActivity extends AppCompatActivity {

    private ArrayList<NASAImage> images = new ArrayList<>();
    SQLiteDatabase database;
    MyViewListAdapter adapter;
    NASAImage currentImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);

        // Connect to the database
        MySQLiteHelper mySQLiteHelper = new MySQLiteHelper(this);
        database = mySQLiteHelper.getWritableDatabase();

        // Get the ListView
        ListView imageListView = findViewById(R.id.image_list);
        adapter = new MyViewListAdapter();
        imageListView.setAdapter(adapter);

        loadDataFromDatabase();

        // Update the adapter
        adapter.notifyDataSetChanged();
    }

    private void loadDataFromDatabase() {

        // Get the columns
        String[] columns = {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_IMAGE_NAME, MySQLiteHelper.COLUMN_DATE, MySQLiteHelper.COLUMN_DESCRIPTION, MySQLiteHelper.COLUMN_URL};

        // Get all rows from the database
        try
            (Cursor results = database.query(false, MySQLiteHelper.TABLE_NAME, columns, null, null, null, null, null, null)) {

            // Get column indices
            int idColIndex = results.getColumnIndex(MySQLiteHelper.COLUMN_ID);
            int imageNameColIndex = results.getColumnIndex(MySQLiteHelper.COLUMN_IMAGE_NAME);
            int dateColIndex = results.getColumnIndex(MySQLiteHelper.COLUMN_DATE);
            int descriptionColIndex = results.getColumnIndex(MySQLiteHelper.COLUMN_DESCRIPTION);
            int urlColIndex = results.getColumnIndex(MySQLiteHelper.COLUMN_URL);

            // Fetch the data from the database
            while (results.moveToNext()) {
                long id = results.getLong(idColIndex);
                String imageFileName = results.getString(imageNameColIndex);
                String date = results.getString(dateColIndex);
                String description = results.getString(descriptionColIndex);
                String url = results.getString(urlColIndex);

                Log.i("LOADING DATA", "successful");

                // Put each image in the ArrayList images
                NASAImage newNASAImage = new NASAImage(id, date, description, url);
                newNASAImage.setImageName(imageFileName);
                images.add(newNASAImage);

            }
            }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i("onActivityResult", "Helloo");
        if (requestCode == 1) {
            
            deleteImageFromArrayList(currentImage);
            adapter.notifyDataSetChanged();

            if (resultCode == RESULT_OK) {
            }
        }
    }

    private void deleteImageFromArrayList(NASAImage currentImage) {

        for (NASAImage image: images) {
            if (currentImage.equals(image)) {
                images.remove(image);
            }
        }
    }

    private class MyViewListAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Object getItem(int position) {
            return images.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            // Get the layout for one currentImage item
            View newView;
            LayoutInflater layoutInflater = getLayoutInflater();

            // Inflate the currentImage item layout
            currentImage = (NASAImage) getItem(position);
            newView = layoutInflater.inflate(R.layout.image_item, parent, false);

            // Put the information for each currentImage into the layout fields
            ImageView image = newView.findViewById(R.id.image);
            FileInputStream fis = null;
            try {
                fis = openFileInput(currentImage.getImageName());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            Bitmap imageBitmap = BitmapFactory.decodeStream(fis);

            image.setImageBitmap(imageBitmap);

            TextView date = newView.findViewById(R.id.image_date);
            date.setText(currentImage.getDate());
            TextView description = newView.findViewById(R.id.image_description);
            description.setText(currentImage.getDescription());


            // Set the ClickListener on each image row
            newView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    // Send details about the clicked image to ImageFromDatabaseActivity
                    Intent intent = new Intent(ImageListActivity.this, ImageFromDatabaseActivity.class);
                    intent.putExtra("IMAGE_ID", currentImage.getId());
                    intent.putExtra("IMAGE_NAME", currentImage.getImageName());
                    intent.putExtra("IMAGE_URL", currentImage.getUrl());
                    intent.putExtra("IMAGE_DATE", currentImage.getDate());
                    intent.putExtra("IMAGE_DESCRIPTION", currentImage.getDescription());

                    startActivityForResult(intent, 1);
                }
            });
            return newView;
        }
    }

}