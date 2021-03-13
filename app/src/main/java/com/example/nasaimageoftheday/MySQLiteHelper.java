package com.example.nasaimageoftheday;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class MySQLiteHelper extends SQLiteOpenHelper {

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "DATE";
    public static final String COLUMN_DESCRIPTION = "DESCRIPTION";
    public static final String COLUMN_URL = "URL";

    public static final String TABLE_NAME = "IMAGE";

    private static final String DATABASE_NAME = "images.db";
    private static final int DATABASE_VERSION = 1;

    public MySQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " ( _id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_DATE + " text, "
                + COLUMN_DESCRIPTION + " text, "
                + COLUMN_URL + " text); ");

        Log.i("DATABASE","successfully created!");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(MySQLiteHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
        onCreate(db);
    }
}
