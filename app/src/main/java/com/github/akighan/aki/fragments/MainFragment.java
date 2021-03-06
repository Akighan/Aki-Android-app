package com.github.akighan.aki.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.github.akighan.aki.recyclerview.ListOfNotesAdapter;
import com.github.akighan.aki.R;
import com.github.akighan.aki.recyclerview.RVClickListener;
import com.github.akighan.aki.recyclerview.RVTouchHelper;
import com.github.akighan.aki.notes.NotesReceiver;


public class MainFragment extends Fragment implements RVClickListener {
    NotesReceiver notesReceiver = NotesReceiver.getInstance();

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
        view.findViewById(R.id.iv_plus_button)
                .setOnClickListener(v -> NavHostFragment.findNavController(MainFragment.this)
                        .navigate(R.id.action_mainFragment_to_addNewNoteFragment));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater menuInflater) {
        menuInflater.inflate(R.menu.top_right_menu, menu);
        super.onCreateOptionsMenu(menu, menuInflater);
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