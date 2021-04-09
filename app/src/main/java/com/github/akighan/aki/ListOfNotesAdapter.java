package com.github.akighan.aki;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavHost;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.github.akighan.aki.database.NotesReceiver;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ListOfNotesAdapter
        extends RecyclerView.Adapter<ListOfNotesAdapter.ViewHolder>
        implements RecyclerViewTouch {

    private final LayoutInflater inflater;
    private final NotesReceiver notesReceiver;
    private final RVClickListener rvClickListener;

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        List<Note> notes = notesReceiver.getNotes();
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(notes, i, i + 1);

            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(notes, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        notesReceiver.remove(position);
        notifyItemRemoved(position);
    }

    public ListOfNotesAdapter(Context context, NotesReceiver notesReceiver, RVClickListener rvClickListener) {
        this.inflater = LayoutInflater.from(context);
        this.notesReceiver = notesReceiver;
        this.rvClickListener = rvClickListener;
    }

    @NonNull
    @Override
    public ListOfNotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_list_of_notes, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListOfNotesAdapter.ViewHolder holder, int position) {
        Note note = notesReceiver.get(position);
        holder.note.setText(note.getNote());

        holder.note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                List <Note> notes = notesReceiver.getNotes();
//                int positionOfElement = -1;
//                for (int i =0; i<notes.size();i++) {
//                    if (notes.get(i).getNote().equals(note.getNote())) {
//                        positionOfElement = i;
//                    }
//                }
                rvClickListener.onClick(holder.getAdapterPosition());
            }
        });
    }


    @Override
    public int getItemCount() {
        return notesReceiver.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView note;

        ViewHolder(View view) {
            super(view);
            note = (TextView) view.findViewById(R.id.rv_text);
        }
    }
}
