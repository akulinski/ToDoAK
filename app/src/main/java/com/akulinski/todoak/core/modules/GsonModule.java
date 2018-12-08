package com.akulinski.todoak.core.modules;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public class GsonModule {

    @Provides
    @Singleton
    public GsonBuilder provideBuilder(){
        return new GsonBuilder();
    }

    @Provides
    @Singleton
    @Named("basicGson")
    public Gson provideBasicGson(GsonBuilder gsonBuilder){
        return gsonBuilder.create();
    }
}
