package com.akulinski.todoak.core.modules;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.akulinski.todoak.core.dbmanagment.CRUDOperationManager;
import com.akulinski.todoak.core.dbmanagment.NotesDbManager;
import com.akulinski.todoak.parsers.NoteDAO;
import com.akulinski.todoak.utils.DbInfo;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DbModule {

    private static String TABLE_NAME = "note";

    @Provides
    @Singleton
    @Named("columns")
    public String[] provideColumns(){
        return new String[]{DbInfo.COLUMN_ID.getValue(),DbInfo.COLUMN_TITLE.getValue(),DbInfo.COLUMN_USER_ID.getValue(),DbInfo.COLUMN_COMPLETED.getValue()};
    }

    @Provides
    @Singleton
    @Named("table")
    public String provideTable(){
        return TABLE_NAME;
    }

    @Provides
    @Singleton
    @Named("class")
    public Class<NoteDAO> provideClass(){
        return NoteDAO.class;
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Provides
    @Singleton
    public NotesDbManager provideNotesDbManager(Context context){
        return new NotesDbManager(context);
    }

    @Provides
    @Singleton
    public CRUDOperationManager crudOperationManager(NotesDbManager notesDbManager, @Named("columns") String[] columns,
                                                     @Named("class") Class<NoteDAO> noteDAOClass){
        return new CRUDOperationManager(notesDbManager,columns,noteDAOClass);
    }

    @Provides
    @Singleton
    @Named("writable")
    public SQLiteDatabase provideWritableSQLiteDatabase(NotesDbManager notesDbManager){
        return notesDbManager.getWritableDatabase();
    }
}
