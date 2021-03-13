package com.github.akighan.aki;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Note {
    @PrimaryKey (autoGenerate = true)
    public long id;
    public String note;

    public Note(String note) {
        this.note = note;
    }

    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }

    public String getNote () {
        return this.note;
    }
    public void setNote(String note) {
        this.note = note;
    }
}
