package com.akulinski.todoak.core;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akulinski.todoak.R;

public final class NoteAdapter extends RecyclerView.Adapter<NoteHolder> {

    private String[] listOfItems;

    public NoteAdapter(String[] listOfItems) {
        this.listOfItems = listOfItems;
    }

    @NonNull
    @Override
    public NoteHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Boolean attachViewImmediatelyToParent = false;
        View singleItemLayout = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.notelayout,viewGroup,attachViewImmediatelyToParent);
        return new NoteHolder(singleItemLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteHolder noteHolder, int i) {
        noteHolder.getTextToShow().setText(listOfItems[i]);
    }

    @Override
    public int getItemCount() {
        return listOfItems.length;
    }
}
