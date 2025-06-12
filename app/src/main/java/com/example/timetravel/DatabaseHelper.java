package com.example.timetravel;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "travel.db";
    private static final int DB_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE travel_history (id INTEGER PRIMARY KEY AUTOINCREMENT, datetime INTEGER, activities TEXT, period TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS travel_history");
        onCreate(db);
    }

    public void insertHistory(long datetime, List<String> activities, String period) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("datetime", datetime);
        cv.put("activities", String.join(",", activities));
        cv.put("period", period);
        db.insert("travel_history", null, cv);
    }
}
