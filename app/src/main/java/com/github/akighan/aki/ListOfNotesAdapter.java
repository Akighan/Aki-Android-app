package com.github.akighan.aki;

import android.content.Context;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.akighan.aki.database.NotesReceiver;

import java.util.Collections;

public class ListOfNotesAdapter
        extends RecyclerView.Adapter<ListOfNotesAdapter.ViewHolder>
        implements RecyclerViewTouch {

    private final LayoutInflater inflater;
    private final NotesReceiver notesReceiver;

    @Override
    public void onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(notesReceiver.getNotes(), i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(notesReceiver.getNotes(), i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
    }

    @Override
    public void onItemDismiss(int position) {
        notesReceiver.remove(position);
        notifyItemRemoved(position);
    }

    interface NotesAdapterOnClickListener {
        void onNoteAdapterClickListener(Note note, int position, ListOfNotesAdapter onClickListener);
    }

    public ListOfNotesAdapter(Context context, NotesReceiver notesReceiver) {
        this.inflater = LayoutInflater.from(context);
        this.notesReceiver = notesReceiver;
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
        EditText text = holder.note.findViewById(R.id.rv_text);
        text.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager methodManager = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    methodManager.hideSoftInputFromWindow(text.getWindowToken(),0);
                    v.clearFocus();
                    note.setNote(text.getText().toString());
                    notesReceiver.updateNote(note);
                }
                return true;
            }
        });
    }


    @Override
    public int getItemCount() {
        return notesReceiver.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        final EditText note;

        ViewHolder(View view) {
            super(view);
            note = (EditText) view.findViewById(R.id.rv_text);
        }
    }
}
