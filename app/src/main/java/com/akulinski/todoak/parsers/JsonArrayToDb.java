package com.akulinski.todoak.parsers;

import com.akulinski.todoak.core.dbmanagment.CRUDOperationManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * takes data from JsonArray and inserts to database
 */
public class JsonArrayToDb implements IParser {

    private JsonArray toParse;
    private Gson gson;
    private CRUDOperationManager crudOperationManager;

    @Inject
    public JsonArrayToDb(@Named("basicGson") Gson gson, CRUDOperationManager crudOperationManager) {
        this.gson = gson;
        this.crudOperationManager = crudOperationManager;
    }


    @Override
    public void loadData(Object data) {
        this.toParse = (JsonArray) data;
    }

    /**
     * Inserts JSON elements from JSONArray to database
     */
    @Override
    public void parse() {

        toParse.forEach(jsonElement -> {
            NoteDAO noteDAO = gson.fromJson(jsonElement, NoteDAO.class);
            crudOperationManager.insert(noteDAO);
        });

    }

    @Override
    public Object getResult() {
        throw new IllegalStateException("This function is not available for this class");
    }
}
