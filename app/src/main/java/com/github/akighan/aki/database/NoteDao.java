package com.github.akighan.aki.database;

import androidx.room.*;

import com.github.akighan.aki.Note;

import java.util.List;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM note")
    List<Note> getAll ();

    @Query("SELECT * FROM note WHERE id = :id")
    Note getById (long id);

    @Query("DELETE FROM note")
    void deleteTable ();

    @Insert
    void insert (Note note);

    @Update
    void update (Note note);

    @Delete
    void delete (Note note);
}
