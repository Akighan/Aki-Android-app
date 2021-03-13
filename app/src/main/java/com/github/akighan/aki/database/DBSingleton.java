package com.github.akighan.aki.database;

import android.app.Application;

import androidx.room.Room;

public class DBSingleton extends Application {
    public static DBSingleton dbInstance;
    private AppDatabase database;

    @Override
    public void onCreate() {
        super.onCreate();
        dbInstance = this;
        database = Room.databaseBuilder(this, AppDatabase.class, "database")
                .build();
    }

    public static DBSingleton getInstance() {
        return dbInstance;
    }

    public AppDatabase getDatabase (){
        return database;
    }
}
