package com.akulinski.todoak.core;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.akulinski.todoak.R;
import com.akulinski.todoak.events.ChangeStatusEvent;
import com.akulinski.todoak.parsers.NoteDAO;
import com.google.common.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public final class NoteHolder extends RecyclerView.ViewHolder{

    @BindView(R.id.note_text) TextView textToShow;
    @BindView(R.id.change_status) Button buttonDone;
    @BindView(R.id.change_status_not_done) Button buttonNotDone;
    private EventBus eventBus;
    private NoteDAO noteDAO;
    private Context context;

    public NoteHolder(@NonNull View itemView, EventBus eventBus,Context context) {
        super(itemView);
        ButterKnife.bind(this,itemView);
        this.eventBus = eventBus;
        this.context = context;
    }

    @OnClick(R.id.change_status) void setStatusToDone(){
        eventBus.post(new ChangeStatusEvent(textToShow.getText().toString(),"true"));
    }

    @OnClick(R.id.change_status_not_done) void setStatusToNotDone(){
        eventBus.post(new ChangeStatusEvent(textToShow.getText().toString(),"false"));
    }

    public TextView getTextToShow() {
        return textToShow;
    }

    public void setTextToShow(TextView textToShow) {
        this.textToShow = textToShow;
    }

    public void setNoteDAO(NoteDAO noteDAO) {
        this.noteDAO = noteDAO;
        if(noteDAO.isCompleted()){
            this.buttonNotDone.setEnabled(true);
            this.buttonNotDone.setClickable(true);
            this.buttonNotDone.setBackgroundColor(Color.parseColor("#3C54FE"));
            this.buttonNotDone.setTextColor(Color.WHITE);
            this.buttonNotDone.setBackground(context.getResources().getDrawable(R.drawable.button_shape));


            this.buttonDone.setEnabled(false);
            this.buttonDone.setClickable(false);
            this.buttonDone.setBackgroundColor(Color.TRANSPARENT);
            this.buttonDone.setTextColor(Color.TRANSPARENT);

        }else{
            this.buttonDone.setEnabled(true);
            this.buttonDone.setClickable(true);
            this.buttonDone.setBackgroundColor(Color.parseColor("#3C54FE"));
            this.buttonDone.setBackground(context.getResources().getDrawable(R.drawable.button_shape));
            this.buttonDone.setTextColor(Color.WHITE);

            this.buttonNotDone.setEnabled(false);
            this.buttonNotDone.setClickable(false);
            this.buttonNotDone.setBackgroundColor(Color.TRANSPARENT);
            this.buttonNotDone.setTextColor(Color.TRANSPARENT);

        }
    }

}