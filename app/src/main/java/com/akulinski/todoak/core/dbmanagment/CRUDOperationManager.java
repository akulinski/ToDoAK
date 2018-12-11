package com.akulinski.todoak.core.dbmanagment;

import android.content.ContentValues;
import android.database.Cursor;

import com.akulinski.todoak.parsers.IResource;
import com.akulinski.todoak.utils.DbInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

/**
 * Allows to do needed CRUD operations on SQLite database
 *
 * @param <T> Class that can be parsed and populated using Reflections eg. NoteDao
 */
public class CRUDOperationManager<T extends IResource> {

    private NotesDbManager notesDbManager;

    private Class<T> type;

    /**
     * @param notesDbManager object that creates database,tables
     * @param tClass         Class object of {@code <T>}
     */
    @Inject
    public CRUDOperationManager(NotesDbManager notesDbManager, @Named("class") Class<T> tClass) {
        this.notesDbManager = notesDbManager;
        this.type = tClass;
    }

    /**
     * Inserts resource to Database
     *
     * @param resource object that is wished to be inserted
     */
    public void insert(IResource resource) {

        ContentValues values = new ContentValues();

        HashMap<String, String> toInsert = resource.generateObjectInfo();

        for (Map.Entry<String, String> entry : toInsert.entrySet()) {
            values.put(entry.getKey(), entry.getValue());
        }

        notesDbManager.getWritableDatabase().insert(DbInfo.TABLE_NAME.getValue(), null, values);
    }

    /**
     * @return Reads all data from table specified in {@code DbInfo} enum
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
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

    /**
     * Allows to get all rows with specific status
     *
     * @param status - status of note
     * @return list of resources
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
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

    /**
     * Function used to get all objects that contain {@code titleFragment} in title row, if status is other than
     * none then where clause is added
     *
     * @param titleFragment part of title
     * @param status        status of note eg. successful
     * @return list of resources
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public List<T> findByTitle(String titleFragment, String status) throws InstantiationException, IllegalAccessException {

        ArrayList<T> returnList = new ArrayList<>();

        String query = "SELECT * FROM " + DbInfo.TABLE_NAME.getValue() + " WHERE title Like ?";
        String[] values;
        if (!status.equals("none")) {
            query += " AND completed = ?";
            values = new String[]{"%" + titleFragment + "%", status};
        } else {
            values = new String[]{"%" + titleFragment + "%"};
        }

        Cursor cursor = notesDbManager.getReadableDatabase().rawQuery(query, values);

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

    /**
     * Allows to find all notes with poster id equal to {@code id} if status is other than
     * none then where clause is added
     *
     * @param id     - id of poster
     * @param status status of note
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public List<T> findById(String id, String status) throws InstantiationException, IllegalAccessException {

        ArrayList<T> returnList = new ArrayList<>();

        String query = "SELECT * FROM " + DbInfo.TABLE_NAME.getValue() + " WHERE " + DbInfo.COLUMN_USER_ID.getValue() + " = ?";
        String[] values;

        if (!status.equals("none")) {
            query += " AND completed = ?";
            values = new String[]{id, status};
        } else {
            values = new String[]{id};
        }

        Cursor cursor = notesDbManager.getReadableDatabase().rawQuery(query, values);

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

    /**
     * Allows to update row by id of row in db
     *
     * @param id       - id of note in db
     * @param newTitle - new title
     */
    public void updateTitle(int id, String newTitle) {

        String query = "UPDATE " + DbInfo.TABLE_NAME.getValue() + " SET title = ? WHERE " + DbInfo.COLUMN_ID.getValue() + " = ?";
        String[] values = new String[]{newTitle, String.valueOf(id)};

        Cursor cursor = notesDbManager.getReadableDatabase().rawQuery(query, values);

        cursor.moveToFirst();

        cursor.close();
    }

    /**
     * changes status of note
     *
     * @param status new status
     * @param title  - title of note
     */
    public void setStatus(String status, String title) {

        String query = "Update note set completed = ? WHERE title=?";

        Cursor cursor = notesDbManager.getReadableDatabase().rawQuery(query, new String[]{status, title});
        cursor.moveToFirst();

        cursor.close();
    }

    /**
     * Checks if database contains data
     *
     * @return
     */
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

    /**
     * deletes row with specific id of note
     *
     * @param id - note id
     */
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
