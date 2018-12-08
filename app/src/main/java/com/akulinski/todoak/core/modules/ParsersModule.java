package com.akulinski.todoak.core.modules;

import android.database.sqlite.SQLiteDatabase;

import com.akulinski.todoak.parsers.IParser;
import com.akulinski.todoak.parsers.JsonArrayToDb;
import com.google.gson.Gson;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ParsersModule {

    @Provides
    @Singleton
    @Named("JsonArrayToDb")
    public IParser provideJsonArrayToDbParser(@Named("writable") SQLiteDatabase sqLiteDatabase,@Named("basicGson") Gson gson){
        return new JsonArrayToDb(sqLiteDatabase,gson);
    }
}
