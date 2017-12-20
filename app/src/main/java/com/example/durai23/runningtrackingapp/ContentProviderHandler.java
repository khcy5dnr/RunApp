package com.example.durai23.runningtrackingapp;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by Durai23 on 16/12/2017.
 */

public class ContentProviderHandler extends ContentProvider{

    private DBHandler dbHandler;
    private static final String AUTHORITY = "com.example.durai23.runningtrackingapp.ContentProviderHandler";
    private static final String TRACKER_TABLE = "trackerTable";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + TRACKER_TABLE);

    public static final int TRACKER = 1;
    public static final int TRACKER_ID = 2;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        sURIMatcher.addURI(AUTHORITY, TRACKER_TABLE, TRACKER);
        sURIMatcher.addURI(AUTHORITY, TRACKER_TABLE + "/#", TRACKER_ID);
    }

    public ContentProviderHandler(){
    }

    @Override
    public boolean onCreate() {
        dbHandler = new DBHandler(getContext(), null, null, 1);
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DBHandler.TRACKER_TABLE);

        int uriType = sURIMatcher.match(uri);

        switch (uriType) {
            case TRACKER_ID:
                queryBuilder.appendWhere(DBHandler.COLUMN_ID + "=" + uri.getLastPathSegment());
                break;
            case TRACKER:
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }

        Cursor cursor = queryBuilder.query(dbHandler.getReadableDatabase(),
                projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(Uri uri,ContentValues values) {
        int uriType = sURIMatcher.match(uri);

        SQLiteDatabase sqlDB = dbHandler.getWritableDatabase();

        long id = 0;
        switch (uriType) {
            case TRACKER:
                id = sqlDB.insert(dbHandler.TRACKER_TABLE, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(TRACKER_TABLE + "/" + id);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = dbHandler.getWritableDatabase();
        int rowsDeleted = 0;

        switch (uriType) {
            case TRACKER:
                rowsDeleted = sqlDB.delete(dbHandler.TRACKER_TABLE,
                        selection,
                        selectionArgs);
                break;

            case TRACKER_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(dbHandler.TRACKER_TABLE,
                            dbHandler.COLUMN_ID + "=" + id, null);
                } else {
                    rowsDeleted = sqlDB.delete(dbHandler.TRACKER_TABLE,
                            dbHandler.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = dbHandler.getWritableDatabase();
        int rowsUpdated = 0;

        switch (uriType) {
            case TRACKER:
                rowsUpdated =
                        sqlDB.update(dbHandler.TRACKER_TABLE, values, selection, selectionArgs);
                break;
            case TRACKER_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated =
                            sqlDB.update(dbHandler.TRACKER_TABLE,
                                    values,
                                    dbHandler.COLUMN_ID + "=" + id,
                                    null);
                } else {
                    rowsUpdated =
                            sqlDB.update(dbHandler.TRACKER_TABLE,
                                    values,
                                    dbHandler.COLUMN_ID + "=" + id
                                            + " and "
                                            + selection,
                                    selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
