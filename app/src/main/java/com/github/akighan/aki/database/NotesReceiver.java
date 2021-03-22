package com.github.akighan.aki.database;

import com.github.akighan.aki.Note;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class NotesReceiver{
    private static NotesReceiver notesReceiver;
    private List<Note> notes;
    private final AppDatabase appDatabase;
    private NotesReceiver() {
        appDatabase = DBSingleton.getInstance().getDatabase();
        notes = new ArrayList<>();
        fillCollectionFromDatabase();
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
        notesReceiver.notes.add(new Note(text));
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Note note = new Note (text.trim());
//                appDatabase.noteDao().insert(note);
//                notes = appDatabase.noteDao().getAll();
//            }
//        }).start();
    }

    public void updateNote (int position, Note note) {
        notes.set(position,note);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                appDatabase.noteDao().update(note);
//                notes = appDatabase.noteDao().getAll();
//            }
//        }).start();
    }

    public void remove(int position) {
        notes.remove(position);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                appDatabase.noteDao().delete(notes.get(position));
//                notes = appDatabase.noteDao().getAll();
//            }
//        }).start();
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
}
