package com.akulinski.todoak.parsers;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import com.akulinski.todoak.utils.DbInfo;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import javax.inject.Inject;
import javax.inject.Named;

public class JsonArrayToDb implements IParser {

    private JsonArray toParse;

    private SQLiteDatabase db;
    private Gson gson;

    @Inject
    public JsonArrayToDb(@Named("writable") SQLiteDatabase sqLiteDatabase,@Named("basicGson") Gson gson){
        this.db = sqLiteDatabase;
        this.gson = gson;
    }

    @Override
    public void loadData(JsonArray jsonArray) {
        this.toParse = jsonArray;
    }

    @Override
    public void parse() {
        toParse.forEach(jsonElement -> {

            ContentValues values = new ContentValues();

            NoteDAO note = gson.fromJson(jsonElement, NoteDAO.class);

            values.put(DbInfo.COLUMN_TITLE.getValue(), note.getTitle());

            values.put(DbInfo.COLUMN_USER_ID.getValue(), note.getUserId());

            if(note.isCompleted()){
                values.put(DbInfo.COLUMN_COMPLETED.getValue(),Boolean.TRUE);
            }else{
                values.put(DbInfo.COLUMN_COMPLETED.getValue(),Boolean.FALSE);
            }

            db.insert(DbInfo.TABLE_NAME.getValue(), null, values);
        });
    }
}
