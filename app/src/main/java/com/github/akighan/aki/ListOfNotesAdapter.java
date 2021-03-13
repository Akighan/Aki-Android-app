package com.github.akighan.aki;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.akighan.aki.database.NotesReceiver;

public class ListOfNotesAdapter extends RecyclerView.Adapter <ListOfNotesAdapter.ViewHolder> {

    private final LayoutInflater inflater;
    private final NotesReceiver notesReceiver;
    private final NotesAdapterOnClickListener onClickListener;

    interface NotesAdapterOnClickListener {
        void onNoteAdapterClickListener(Note note, int position, ListOfNotesAdapter onClickListener);
    }

    public ListOfNotesAdapter(Context context,NotesReceiver notesReceiver, NotesAdapterOnClickListener onClickListener) {
        this.onClickListener = onClickListener;
        this.inflater = LayoutInflater.from(context);
        this.notesReceiver = notesReceiver;
    }

    @NonNull
    @Override
    public ListOfNotesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_list_of_notes,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ListOfNotesAdapter.ViewHolder holder, int position) {

        Note note  = notesReceiver.get(position);
        holder.note.setText(note.getNote());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickListener.onNoteAdapterClickListener(note, position, ListOfNotesAdapter.this);
            }
        });
    }

    @Override
    public int getItemCount() {
       return notesReceiver.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final TextView note;
        ViewHolder (View view) {
            super(view);
            note = (TextView)view.findViewById(R.id.rv_text);
        }
    }
}
