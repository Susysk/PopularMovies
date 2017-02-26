package com.example.android.popularmovies.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Susy on 25/02/2017.
 */

public class DBHelper extends SQLiteOpenHelper {
    Context c;
    public DBHelper(Context context) {
        super(context, "POPMOVIES", null,39);
        this.c=context;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create table movies " +
                        "(id real primary key, title text," +
                        "release text,path text, vote_average real)"
        );
    }

    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        if(newVersion>oldVersion) {
        }
    }

    public Map<Integer,String> findall() {
      Map<Integer,String> path = new HashMap<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String[] params = new String[]{"*"};
        Cursor cursor = db.rawQuery("select * from movies", null);
        int i = cursor.getCount();
        if(cursor.getCount()==0)
            return new HashMap<>();
        if (cursor.moveToFirst()) {
            do{
                String m= cursor.getString(3);
                int id= cursor.getInt(0);
                path.put(id,m);
            }
            while ( cursor.moveToNext());
        }
        db.close();

        return path;
    }
}