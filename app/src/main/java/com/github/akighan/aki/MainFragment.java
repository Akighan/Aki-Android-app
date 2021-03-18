package com.github.akighan.aki;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaRouter;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.github.akighan.aki.database.NotesReceiver;
import com.github.akighan.aki.server.LaptopServer;


public class MainFragment extends Fragment implements RVClickListener {
    NotesReceiver notesReceiver = NotesReceiver.getInstance();
    LaptopServer server = new LaptopServer();


    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.rv_notes);
        RVClickListener rvClickListener = this;
        ListOfNotesAdapter adapter = new ListOfNotesAdapter(rootView.getContext(), notesReceiver, rvClickListener);
        recyclerView.setAdapter(adapter);

        ItemTouchHelper.Callback callback =
                new RVTouchHelper(adapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);
        return rootView;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.iv_plus_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(MainFragment.this).navigate(R.id.action_mainFragment_to_addNewNoteFragment);
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.top_right_menu, menu);
        super.onCreateOptionsMenu(menu,menuInflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.settings_menu) {
            NavHostFragment.findNavController(MainFragment.this).navigate(R.id.action_mainFragment_to_settingsFragment);
        }
        return true;
    }

    @Override
    public void onClick(int position) {

        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        NavHostFragment.findNavController(MainFragment.this).navigate(R.id.action_mainFragment_to_editNoteFragment, bundle);
    }
}