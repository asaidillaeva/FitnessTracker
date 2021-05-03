package com.example.fitnesstracker.data;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.fitnesstracker.RunItem;


@Database(entities = {RunItem.class}, version = 2,  exportSchema = false )
public abstract  class RunDatabase extends RoomDatabase {

    private static String LOG_TAG = RunDatabase.class.getSimpleName();
    private static RunDatabase db;
    public abstract RunDao runDao();

    public static RunDatabase getDatabase(Context context)
    {
        if (db==null) {
            Log.i(LOG_TAG, "No database found, a new will be created");
            db = Room.databaseBuilder(
                    context.getApplicationContext(),
                    RunDatabase.class,
                    "runItem")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build();
        } else {
            Log.i(LOG_TAG, "getStoredDatabase: a database already exists");
        }
        return db;
    }


}
