package com.example.android.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

/**
 * Created by Susy on 25/02/2017.
 */

public class MovieContentProvider extends ContentProvider {
    private DBHelper database;
    private static final int MOVIES = 10;
    private static final int REVIEW = 20;
    private final static int TRAILER = 30;
    SQLiteDatabase db;
    private static final String AUTHORITY = "com.example.android.popularmovies";
    private static final String BASE_PATH = "movies";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
            + "/" + BASE_PATH);

    private static final UriMatcher sURIMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY,"favorites",1);
        sURIMatcher.addURI(AUTHORITY,"favorites/#",2);
    }

    @Override
    public boolean onCreate() {
        database = new DBHelper(getContext());
        db = database.getWritableDatabase();
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder =   new SQLiteQueryBuilder();
        queryBuilder.setTables("movies");
        String id=  uri.getPathSegments().get(1);
        String[] args = new String[]{id};
        switch (sURIMatcher.match(uri)) {
            case 1:
                break;
            case 2: {
                queryBuilder.appendWhere("id" + "=" +
                        uri.getLastPathSegment());
                break;
            }
        }
        Cursor cursor = queryBuilder.query (
                db,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );
        cursor.setNotificationUri(getContext().getContentResolver(),CONTENT_URI);
        return cursor;
    }
    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        /** Add a new favorites record */

        long rowID = db.insert("movies", "", values);
        APISingleton.favImages=database.findall();
        if (rowID > 0) {
            Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
            getContext().getContentResolver().notifyChange(_uri, null);
            return _uri;
        }
        try {
            throw new SQLException("Failed to add new record into "+uri);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;

        switch (sURIMatcher.match(uri)) {
            case 1:
                count = db.delete("movies", selection, selectionArgs);
                break;
            case 2:
                String id = uri.getPathSegments().get(1);
                count = db.delete("movies","id" + " = " + id +
                        (!TextUtils.isEmpty(selection) ? " AND (" + selection + ')' : ""), selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

   return 0;
    }

}