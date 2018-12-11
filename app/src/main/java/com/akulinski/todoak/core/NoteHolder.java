package com.akulinski.todoak.core;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.akulinski.todoak.R;
import com.akulinski.todoak.events.ChangeStatusEvent;
import com.akulinski.todoak.events.RemoveNoteEvent;
import com.akulinski.todoak.events.SaveNoteEvent;
import com.akulinski.todoak.parsers.NoteDAO;
import com.google.common.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public final class NoteHolder extends RecyclerView.ViewHolder implements View.OnCreateContextMenuListener {

    /**
     * Note title
     */
    @BindView(R.id.note_text)
    EditText textToShow;

    /**
     * Button that changes state of note to done
     */
    @BindView(R.id.change_status)
    Button buttonDone;

    /**
     * Button that changes state of note to not done
     */
    @BindView(R.id.change_status_not_done)
    Button buttonUndo;

    /**
     * Saves note
     */
    @BindView(R.id.save)
    Button saveButton;

    /**
     * Displays number of note
     */
    @BindView(R.id.note_number)
    TextView viewCounter;

    private EventBus eventBus;
    private NoteDAO noteDAO;
    private Context context;

    /**
     * Status while displaying title
     */
    private static final String SHOWING_TEXT = "SHOWING";

    /**
     * Status acquired while editing
     */
    private static final String EDITING_TEXT = "EDITING";

    /**
     * Current title
     */
    private String status = SHOWING_TEXT;

    private static final String DELETE = "Delete";
    private static final String EDIT = "Edit";

    public NoteHolder(@NonNull View itemView, EventBus eventBus, Context context) {
        super(itemView);
        ButterKnife.bind(this, itemView);
        this.eventBus = eventBus;
        this.context = context;
        itemView.setOnCreateContextMenuListener(this::onCreateContextMenu);
    }

    /**
     * Changes state of note to successful
     */
    @OnClick(R.id.change_status)
    void setStatusToDone() {
        eventBus.post(new ChangeStatusEvent(textToShow.getText().toString(), "true"));
    }

    /**
     * Changes state of note to not successful
     */
    @OnClick(R.id.change_status_not_done)
    void setStatusToNotDone() {
        eventBus.post(new ChangeStatusEvent(textToShow.getText().toString(), "false"));
    }

    /**
     * saves note, and turns off save button and edit text while turning on {@code buttonDone}
     */
    @OnClick(R.id.save)
    void saveNote() {
        eventBus.post(new SaveNoteEvent(this.textToShow.getText().toString(), this.noteDAO.getId(), noteDAO.isCompleted()));
        toggleSaveButton();
        changePropertiesOfEditText(false);
    }

    public void setNoteDAO(NoteDAO noteDAO) {
        this.noteDAO = noteDAO;

        if (noteDAO.isCompleted()) {
            showButton(buttonUndo, context.getResources().getDrawable(R.drawable.button_shape_red));
            hideButton(buttonDone);

        } else {
            showButton(buttonDone, context.getResources().getDrawable(R.drawable.button_shape));
            hideButton(buttonUndo);
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        MenuItem delete = menu.add(Menu.NONE, v.getId(),
                Menu.NONE, DELETE);
        MenuItem edit = menu.add(Menu.NONE, v.getId(),
                Menu.NONE, EDIT);

        delete.setOnMenuItemClickListener(onEditMenu);
        edit.setOnMenuItemClickListener(onEditMenu);
    }

    /**
     * Listener that allows to edit and delete note
     */
    private final MenuItem.OnMenuItemClickListener onEditMenu = item -> {
        String title = (String) item.getTitle();

        switch (title) {

            case DELETE:
                eventBus.post(new RemoveNoteEvent(noteDAO.getId()));
                break;

            case EDIT:
                if (noteDAO.isCompleted()) {

                    //you cannot edit successful note
                    showToast("Sorry task is already finished, please add new");
                } else {
                    changePropertiesOfEditText(true);
                    showKeyboard();
                    textToShow.requestFocus();
                    toggleSaveButton();
                }
                break;
        }
        return true;
    };

    public void changePropertiesOfEditText(boolean value) {
        textToShow.setFocusable(value);
        textToShow.setClickable(value);
        textToShow.setFocusableInTouchMode(value);
        textToShow.setShowSoftInputOnFocus(true);
        textToShow.setCursorVisible(value);
    }

    private void hideButton(Button toHide) {
        toHide.setEnabled(false);
        toHide.setClickable(false);
        toHide.setBackgroundColor(Color.TRANSPARENT);
        toHide.setVisibility(View.INVISIBLE);
        toHide.setTextColor(Color.TRANSPARENT);
    }

    private void showButton(Button toShow, Drawable drawable) {
        toShow.setEnabled(true);
        toShow.setClickable(true);
        toShow.setBackground(drawable);
        toShow.setVisibility(View.VISIBLE);
        toShow.setTextColor(Color.WHITE);
    }

    /**
     * Turns on and off save button depending on current status
     */
    public void toggleSaveButton() {
        switch (this.status) {
            case SHOWING_TEXT:
                hideButton(buttonDone);
                showButton(saveButton, context.getResources().getDrawable(R.drawable.button_shape));
                this.status = EDITING_TEXT;
                break;

            case EDITING_TEXT:
                hideButton(saveButton);
                showButton(buttonDone, context.getResources().getDrawable(R.drawable.button_shape));
                this.status = SHOWING_TEXT;
        }
    }

    public void showToast(String text) {
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    private void showKeyboard() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(textToShow, InputMethodManager.SHOW_FORCED);
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public NoteDAO getNoteDAO() {
        return noteDAO;
    }

    public EditText getTextToShow() {
        return textToShow;
    }

    public TextView getViewCounter() {
        return viewCounter;
    }

    public static String getShowingText() {
        return SHOWING_TEXT;
    }

    public static String getEditingText() {
        return EDITING_TEXT;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public static String getDELETE() {
        return DELETE;
    }

    public static String getEDIT() {
        return EDIT;
    }

    public MenuItem.OnMenuItemClickListener getOnEditMenu() {
        return onEditMenu;
    }
}