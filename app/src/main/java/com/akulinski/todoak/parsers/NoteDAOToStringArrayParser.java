package com.akulinski.todoak.parsers;

import java.util.ArrayList;

public final class NoteDAOToStringArrayParser implements IParser {

    private ArrayList<NoteDAO> noteDAOArrayList;


    public NoteDAOToStringArrayParser(){
        this.noteDAOArrayList = new ArrayList<>();
    }

    @Override
    public void loadData(Object data) {
        this.noteDAOArrayList = (ArrayList<NoteDAO>) data;
    }

    @Override
    public void parse() {

    }

    @Override
    public Object getResult() {
        return this.noteDAOArrayList;
    }
}
