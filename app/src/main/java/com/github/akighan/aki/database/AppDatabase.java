package com.github.akighan.aki.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.github.akighan.aki.notes.Note;

@Database(entities = {Note.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract NoteDao noteDao ();
}
