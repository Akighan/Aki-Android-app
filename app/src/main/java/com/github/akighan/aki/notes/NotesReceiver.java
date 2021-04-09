package com.github.akighan.aki.notes;

import com.github.akighan.aki.database.AppDatabase;
import com.github.akighan.aki.database.DBSingleton;

import java.util.ArrayList;
import java.util.List;

public class NotesReceiver{

    private static NotesReceiver notesReceiver;
    private List<Note> notes;
    private final AppDatabase appDatabase;
    private boolean isChanged;


    private NotesReceiver() {
        appDatabase = DBSingleton.getInstance().getDatabase();
        notes = new ArrayList<>();
        fillCollectionFromDatabase();
        isChanged = false;
    }


    public static NotesReceiver getInstance() {
        if (notesReceiver == null) {
            notesReceiver = new NotesReceiver();
        }
        return notesReceiver;
    }


    public Note get(int position) {
        return notes.get(position);
    }

    public int size() {
        return notes.size();
    }

    public void updateDBFromReceiver () {
        new Thread(new Runnable() {
            @Override
            public void run() {
                appDatabase.noteDao().deleteTable();
                for (int i =0; i< notes.size(); i++) {
                    Note note = new Note(notes.get(i).getNote());
                    appDatabase.noteDao().insert(note);
                }
            }
        }).start();

    }

    public void setNote(String text) {
        notes.add(new Note(text));
        isChanged = true;
    }

    public void updateNote (int position, Note note) {
        notes.set(position,note);
        isChanged = true;
    }

    public void remove(int position) {
        notes.remove(position);
        isChanged = true;
    }

    public List <Note> getNotes () throws NullPointerException {
        if (notes != null) {
            return notes;
        }
        else throw new NullPointerException();
    }

    private void fillCollectionFromDatabase () {
        new Thread(new Runnable() {
            @Override
            public void run() {
                notes = appDatabase.noteDao().getAll();
            }
        }).start();
    }

    public boolean isChanged () {
        return isChanged;
    }

    public void notesSentToServer () {
        isChanged = false;
    }
}
