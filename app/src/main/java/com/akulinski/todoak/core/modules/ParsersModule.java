package com.akulinski.todoak.core.modules;

import com.akulinski.todoak.core.dbmanagment.CRUDOperationManager;
import com.akulinski.todoak.parsers.IParser;
import com.akulinski.todoak.parsers.JsonArrayToDb;
import com.akulinski.todoak.parsers.NoteDAOToStringArrayParser;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class ParsersModule {

    @Provides
    @Singleton
    public NoteDAOToStringArrayParser provideNoteDaoToStringArrayParser(){
        return new NoteDAOToStringArrayParser();
    }

    @Provides
    @Singleton
    @Named("JsonArrayToDb")
    public IParser<JsonArray> provideJsonArrayToDbParser(@Named("basicGson") Gson gson, CRUDOperationManager crudOperationManager){
        return new JsonArrayToDb(gson,crudOperationManager);
    }
}
