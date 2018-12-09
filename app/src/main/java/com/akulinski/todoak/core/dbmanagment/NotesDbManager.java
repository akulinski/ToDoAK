package com.akulinski.todoak.core.dbmanagment;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.akulinski.todoak.utils.DbInfo;

import javax.inject.Inject;

public class NotesDbManager extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "notes.db";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + DbInfo.TABLE_NAME.getValue() + "(" +
                    DbInfo.COLUMN_ID.getValue() + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    DbInfo.COLUMN_TITLE.getValue() + " TEXT," +
                    DbInfo.COLUMN_USER_ID.getValue() + " INTEGER,"+
                    DbInfo.COLUMN_COMPLETED.getValue() +" INTEGER);";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + DbInfo.TABLE_NAME.getValue();

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Inject
    public NotesDbManager(Context context) {
        super(context, DATABASE_NAME, null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_DELETE_ENTRIES);
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
