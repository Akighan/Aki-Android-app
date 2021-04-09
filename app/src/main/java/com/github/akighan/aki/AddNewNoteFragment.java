package com.github.akighan.aki;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.github.akighan.aki.database.NotesReceiver;
import com.github.akighan.aki.server.LaptopServer;


public class AddNewNoteFragment extends Fragment {

    NotesReceiver notesReceiver;
    LaptopServer server = new LaptopServer();

    public AddNewNoteFragment() {
        // Required empty public constructor
    }


    public static AddNewNoteFragment newInstance() {
        AddNewNoteFragment fragment = new AddNewNoteFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(false);
        notesReceiver = NotesReceiver.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        setHasOptionsMenu(true);
        return inflater.inflate(R.layout.fragment_add_new_note, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStop() {
        getView().clearFocus();
        super.onStop();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.add_new_note_menu, menu);
        super.onCreateOptionsMenu(menu,menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int clickedItem = item.getItemId();
        if (clickedItem == R.id.add_new_note_menu_button) {
            EditText editText = (EditText) getView().findViewById(R.id.pt_enter_new_note);
            String enteredText = editText.getText().toString().trim();
            if (!enteredText.equals("")) {
                notesReceiver.setNote(enteredText);
            }
            NavHostFragment.findNavController(AddNewNoteFragment.this).navigate(R.id.action_newNoteFragment_to_mainFragment);

        }
        return true;
//        int id = item.getItemId();
//        if (id == R.id.menuPurchasesListNewRecord) {
//            EditText editText = (EditText) getView().findViewById(R.id.pt_enter_new_note);
//            editText.clearFocus();
//            String enteredText = editText.getText().toString();
//            if (!enteredText.equals("")) {
//               notesReceiver.setNote(enteredText);
//               new Thread(new Runnable() {
//                   @Override
//                   public void run() {
//                       try {
//                           server.sendData();
//                           server.closeConnection();
//                       } catch (Exception e) {
//                           e.printStackTrace();
//                       }
//                   }
//               }).start();
//            }
//            NavHostFragment.findNavController(AddNewNoteFragment.this).navigate(R.id.action_newNoteFragment_to_mainFragment);
//        }
//        return true;
    }
}