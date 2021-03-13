package com.example.nasaimageoftheday;

import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ImageListActivity extends AppCompatActivity {

    private ArrayList<NASAImage> images = new ArrayList<>();
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);

        // Connect to the database
        MySQLiteHelper mySQLiteHelper = new MySQLiteHelper(this);
        database = mySQLiteHelper.getWritableDatabase();

        // Get the ListView
        ListView imageListView = findViewById(R.id.image_list);
        MyViewListAdapter adapter = new MyViewListAdapter();
        imageListView.setAdapter(adapter);

        loadDataFromDatabase();

        // Update the adapter
        adapter.notifyDataSetChanged();
    }

    private void loadDataFromDatabase() {

        // Get the columns
        String[] columns = {MySQLiteHelper.COLUMN_ID, MySQLiteHelper.COLUMN_DATE, MySQLiteHelper.COLUMN_DESCRIPTION, MySQLiteHelper.COLUMN_URL};

        // Get all rows from the database
        try
            (Cursor results = database.query(false, MySQLiteHelper.TABLE_NAME, columns, null, null, null, null, null, null)) {

            // Get column indices
            int idColIndex = results.getColumnIndex(MySQLiteHelper.COLUMN_ID);
            int dateColIndex = results.getColumnIndex(MySQLiteHelper.COLUMN_DATE);
            int descriptionColIndex = results.getColumnIndex(MySQLiteHelper.COLUMN_DESCRIPTION);
            int urlColIndex = results.getColumnIndex(MySQLiteHelper.COLUMN_URL);

            // Fetch the data from the database
            while (results.moveToNext()) {
                long id = results.getLong(idColIndex);
                String date = results.getString(dateColIndex);
                String description = results.getString(descriptionColIndex);
                String url = results.getString(urlColIndex);

                // Put each image in the ArrayList images
                images.add(new NASAImage(id, date, description, url));
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

            // Get the layout for one image item
            View newView;
            LayoutInflater layoutInflater = getLayoutInflater();

            // Inflate the image item layout
            NASAImage image = (NASAImage) getItem(position);
            newView = layoutInflater.inflate(R.layout.image_item, parent, false);

            // Put the information for each image into the layout fields
            TextView description = newView.findViewById(R.id.image_description);
            description.setText(image.getUrl());

            return newView;
        }
    }
}