package com.akulinski.todoak.core;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akulinski.todoak.R;
import com.akulinski.todoak.parsers.NoteDAO;
import com.google.common.eventbus.EventBus;

import java.util.ArrayList;

public final class NoteAdapter extends RecyclerView.Adapter<NoteHolder> {

    private ArrayList<NoteDAO> listOfItems;
    private EventBus eventBus;

    public NoteAdapter(ArrayList<NoteDAO> listOfItems, EventBus eventBus) {
        this.listOfItems = listOfItems;
        this.eventBus = eventBus;
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Boolean attachViewImmediatelyToParent = false;
        View singleItemLayout = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notelayout, viewGroup, attachViewImmediatelyToParent);

        return new NoteHolder(singleItemLayout, eventBus, singleItemLayout.getContext());
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder noteHolder, int i) {
        noteHolder.getTextToShow().setText(listOfItems.get(i).getTitle());
        noteHolder.setNoteDAO(listOfItems.get(i));

        //allows to number notes
        noteHolder.getViewCounter().setText(String.valueOf(i + 1));
    }


    @Override
    public void onViewDetachedFromWindow(@NonNull NoteHolder holder) {
        super.onViewDetachedFromWindow(holder);

        //when user edits note and decides to filter, note shouldn't be editable
        if (holder.getStatus().equals(NoteHolder.getEditingText())) {
            holder.toggleSaveButton();
            holder.setStatus(NoteHolder.getShowingText());
            holder.changePropertiesOfEditText(false);
        }
    }

    @Override
    public int getItemCount() {
        return listOfItems.size();
    }

    public ArrayList<NoteDAO> getListOfItems() {
        return listOfItems;
    }

}
