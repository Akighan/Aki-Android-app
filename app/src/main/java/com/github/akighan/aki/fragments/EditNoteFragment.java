package com.github.akighan.aki.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.github.akighan.aki.notes.Note;
import com.github.akighan.aki.R;
import com.github.akighan.aki.notes.NotesReceiver;

public class EditNoteFragment extends Fragment {
    private int position;
    private NotesReceiver notesReceiver;


    public EditNoteFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notesReceiver = NotesReceiver.getInstance();
        if (getArguments() != null) {
            position = getArguments().getInt("position");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_edit_note, container, false);
        EditText editText = (EditText) view.findViewById(R.id.edit_text);
        Note note = notesReceiver.get(position);
        editText.setText(note.getNote());
        view.findViewById(R.id.save_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.clearFocus();
                String enteredText = editText.getText().toString().trim();
                if (!enteredText.equals("")) {
                    note.setNote(enteredText);
                    notesReceiver.updateNote(position, note);
                }
                NavHostFragment.findNavController(EditNoteFragment.this).navigate(R.id.action_editNoteFragment_to_mainFragment);
            }
        });
        return view;
    }

    @Override
    public void onPause() {
        getView().clearFocus();
        super.onPause();
    }
}
