package com.example.nasaimageoftheday;

import androidx.appcompat.app.AppCompatActivity;

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

        // Put some fake data in the ArrayList
        for (int i = 0; i < 20; i++) {
            images.add(new NASAImage("2020-3-4", "description" + i, "url"));
        }

        //update the adapter
        adapter.notifyDataSetChanged();
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
            description.setText(image.getDescription());

            return newView;
        }
    }
}