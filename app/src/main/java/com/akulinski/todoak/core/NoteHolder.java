package com.akulinski.todoak.core;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.akulinski.todoak.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public final class NoteHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.note_text) TextView textToShow;

    public NoteHolder(@NonNull View itemView) {
        super(itemView);
        ButterKnife.bind(this,itemView);
    }

    public TextView getTextToShow() {
        return textToShow;
    }

    public void setTextToShow(TextView textToShow) {
        this.textToShow = textToShow;
    }
}