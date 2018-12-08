package com.akulinski.todoak.core.modules;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.akulinski.todoak.core.dbmanagment.NotesFeedHelper;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class DbModule {

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Provides
    @Singleton
    public NotesFeedHelper provideNotesFeedHelper(Context context){
        NotesFeedHelper notesFeedHelper = new NotesFeedHelper(context);
        notesFeedHelper.onCreate(notesFeedHelper.getWritableDatabase());

        return notesFeedHelper;
    }

    @Provides
    @Singleton
    @Named("writable")
    public SQLiteDatabase provideWritableSQLiteDatabase(NotesFeedHelper notesFeedHelper){
        return notesFeedHelper.getWritableDatabase();
    }
}
