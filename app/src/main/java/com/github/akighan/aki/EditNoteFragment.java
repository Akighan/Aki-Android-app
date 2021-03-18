package com.github.akighan.aki;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.github.akighan.aki.database.NotesReceiver;
import com.github.akighan.aki.server.LaptopServer;

public class EditNoteFragment extends Fragment {
    private int position;
    private NotesReceiver notesReceiver;
    LaptopServer server = new LaptopServer();


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
                String enteredText = editText.getText().toString();
                if (!enteredText.equals("")) {
                    note.setNote(enteredText.trim());
                    //notesReceiver.updateNote(note);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                server.sendData();
                                server.closeConnection();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }).start();
                }
                NavHostFragment.findNavController(EditNoteFragment.this).navigate(R.id.action_editNoteFragment_to_mainFragment);
            }
        });
        return view;
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }
}
