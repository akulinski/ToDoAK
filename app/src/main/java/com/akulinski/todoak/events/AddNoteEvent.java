package com.akulinski.todoak.events;

import com.akulinski.todoak.parsers.NoteDAO;

public class AddNoteEvent {
    private NoteDAO noteDAO;

    public AddNoteEvent(NoteDAO noteDAO) {
        this.noteDAO = noteDAO;
    }

    public NoteDAO getNoteDAO() {
        return noteDAO;
    }

    public void setNoteDAO(NoteDAO noteDAO) {
        this.noteDAO = noteDAO;
    }
}
