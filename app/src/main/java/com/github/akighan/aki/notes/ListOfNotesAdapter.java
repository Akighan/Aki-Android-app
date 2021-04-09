package com.github.akighan.aki.notes;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.akighan.aki.R;
import com.github.akighan.aki.recyclerview.RVClickListener;
import com.github.akighan.aki.recyclerview.RecyclerViewTouch;

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

        holder.note.setOnClickListener(v -> rvClickListener.onClick(holder.getAdapterPosition()));
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
