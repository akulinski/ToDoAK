package com.akulinski.todoak.core.dbmanagment;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.akulinski.todoak.parsers.IResource;
import com.akulinski.todoak.parsers.NoteDAO;
import com.akulinski.todoak.utils.DbInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

public class CRUDOperationManager<T extends IResource> {

    private NotesDbManager notesDbManager;

    private String[] columns;


    private Class<T> type;

    @Inject
    public CRUDOperationManager(NotesDbManager notesDbManager, @Named("columns") String[] columns, @Named("class") Class<T> tClass) {
        this.notesDbManager = notesDbManager;
        this.columns = columns;
        this.type = tClass;
    }

    public void insert(IResource resource) {

        ContentValues values = new ContentValues();

        HashMap<String, String> toInsert = resource.generateObjectInfo();

        for (Map.Entry<String, String> entry : toInsert.entrySet()) {
            values.put(entry.getKey(), entry.getValue());
        }

        notesDbManager.getWritableDatabase().insert(DbInfo.TABLE_NAME.getValue(), null, values);
    }

    public List<T> readAllFromDb() throws InstantiationException, IllegalAccessException {

        ArrayList<T> returnList = new ArrayList<>();

        Cursor cursor = notesDbManager.getReadableDatabase().rawQuery("SELECT * FROM " + DbInfo.TABLE_NAME.getValue() + ";", null);

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> stringHashMap = new HashMap<>();

                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    stringHashMap.put(cursor.getColumnName(i), cursor.getString(i));
                }

                T element = type.newInstance();
                element.fromMap(stringHashMap);
                returnList.add(element);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return returnList;
    }


    public List<T> readAllWithStatus(String status) throws InstantiationException, IllegalAccessException {

        ArrayList<T> returnList = new ArrayList<>();

        String query = "SELECT * FROM " + DbInfo.TABLE_NAME.getValue() + " WHERE completed=?";

        Cursor cursor = notesDbManager.getReadableDatabase().rawQuery(query, new String[]{status});

        if (cursor.moveToFirst()) {
            do {

                HashMap<String, String> stringHashMap = new HashMap<>();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    stringHashMap.put(cursor.getColumnName(i), cursor.getString(i));
                }
                T element = type.newInstance();
                element.fromMap(stringHashMap);
                returnList.add(element);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return returnList;
    }

    public void setStatus(String status, String title) {

        String query = "Update note set completed = ? WHERE title=?";

        Cursor cursor = notesDbManager.getReadableDatabase().rawQuery(query, new String[]{status, title});
        cursor.moveToFirst();

        cursor.close();
    }


    public boolean checkIfTableIsEmpty() {
        Cursor cursor = notesDbManager.getReadableDatabase().rawQuery("SELECT COUNT(*) FROM " + DbInfo.TABLE_NAME.getValue(), null);


        if (cursor.moveToFirst()) {
            int count = cursor.getInt(0);

            if (count > 0) {
                return false;
            }
        }

        cursor.close();
        return true;
    }

    public void removeNoteWithId(int id) {
        String query = "DELETE FROM note WHERE id_note = ?";

        Cursor cursor = notesDbManager.getReadableDatabase().rawQuery(query, new String[]{String.valueOf(id)});
        cursor.moveToFirst();
        cursor.close();

    }

    public CRUDOperationManager(Class<T> type) {
        this.type = type;
    }

    public Class<T> getType() {
        return type;
    }

}
