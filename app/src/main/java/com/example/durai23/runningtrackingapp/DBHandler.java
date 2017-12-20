package com.example.durai23.runningtrackingapp;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Durai23 on 16/12/2017.
 */

public class DBHandler extends SQLiteOpenHelper{
    private ContentResolver contentResolver;

    private ArrayList<String> dateList = new ArrayList<String>();

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "trackerDB.db";
    public static final String TRACKER_TABLE = "trackerTable";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_DISTANCE = "distance";
    public static final String COLUMN_TOTAL_TIME = "time";

    public DBHandler(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);

        contentResolver = context.getContentResolver();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_RECIPE_TABLE = "CREATE TABLE " +
                TRACKER_TABLE + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_DATE + " TEXT,"
                + COLUMN_DISTANCE + " TEXT,"
                + COLUMN_TOTAL_TIME + " TEXT" +")";
        db.execSQL(CREATE_RECIPE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TRACKER_TABLE);
        onCreate(db);
    }

    public void addData(RunTrackerData runTrackerData) {

        ContentValues values = new ContentValues();
        values.put(COLUMN_DATE, runTrackerData.getDate());
        values.put(COLUMN_DISTANCE, runTrackerData.getDistance());
        values.put(COLUMN_TOTAL_TIME, runTrackerData.getTotalTime());

        contentResolver.insert(ContentProviderHandler.CONTENT_URI, values);
    }

    public RunTrackerData findSingleData(String date) {
        String[] projection = {COLUMN_ID, COLUMN_DATE, COLUMN_DISTANCE, COLUMN_TOTAL_TIME};

        String selection = "date = \"" + date + "\"";

        Cursor cursor = contentResolver.query(ContentProviderHandler.CONTENT_URI,projection, selection, null, null);

        RunTrackerData data = new RunTrackerData();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            data.setID(Integer.parseInt(cursor.getString(0)));
            data.setDate(cursor.getString(1));
            data.setDistance(cursor.getString(2));
            data.setTotalTime(cursor.getString(3));
            cursor.close();
        } else {
            data = null;
        }
        return data;
    }

    public RunTrackerData listAllData() {
        String[] projection = {COLUMN_ID, COLUMN_DATE, COLUMN_DISTANCE, COLUMN_TOTAL_TIME};

        Cursor cursor = contentResolver.query(ContentProviderHandler.CONTENT_URI,projection, null, null, null);

        RunTrackerData data = new RunTrackerData();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            int i = 0;
            do{
                dateList.add(cursor.getString(1));//gets all the recipe that contains keyword from edit text field

            }while(cursor.moveToNext());

            cursor.close();
        } else {
            data = null;
        }
        return data;
    }

    public boolean deleteRecipe(int tracker_ID) {
        boolean result = false;

        String selection = "_id = " + Integer.toString(tracker_ID);

        int rowsDeleted = contentResolver.delete(ContentProviderHandler.CONTENT_URI,
                selection, null);//delete recipe based on ID

        if (rowsDeleted > 0)
            result = true;

        return result;
    }

    public ArrayList getDataList(){
        return dateList;
    }
}
