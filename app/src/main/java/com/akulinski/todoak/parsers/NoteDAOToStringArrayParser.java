package com.akulinski.todoak.parsers;

import java.util.LinkedList;

public final class NoteDAOToStringArrayParser implements IParser {

    private LinkedList<NoteDAO> noteDAOLinkedList;

    private String[] result;

    public NoteDAOToStringArrayParser(){
        this.noteDAOLinkedList = new LinkedList<>();
    }

    @Override
    public void loadData(Object data) {
        this.noteDAOLinkedList = (LinkedList<NoteDAO>) data;
        this.result = new String[this.noteDAOLinkedList.size()];
    }

    @Override
    public void parse() {
        for(int i=0; i<this.noteDAOLinkedList.size();i++){
            result[i]=this.noteDAOLinkedList.get(i).getTitle();
        }
    }

    @Override
    public Object getResult() {
        return result;
    }
}
