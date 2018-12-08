package com.akulinski.todoak.core.application;

import android.app.Application;

import com.akulinski.todoak.core.components.DaggerMainActivityComponent;
import com.akulinski.todoak.core.components.MainActivityComponent;
import com.akulinski.todoak.core.modules.ContextModule;
import com.akulinski.todoak.core.modules.DbModule;
import com.akulinski.todoak.core.modules.EventBusModule;
import com.akulinski.todoak.core.modules.GsonModule;
import com.akulinski.todoak.core.modules.ParsersModule;
import com.akulinski.todoak.core.modules.RetrofitModule;

public class ToDoCore extends Application {

    private MainActivityComponent mainActivityComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        mainActivityComponent = DaggerMainActivityComponent.builder().eventBusModule(new EventBusModule()).retrofitModule(new RetrofitModule()).contextModule(new ContextModule(this))
                .dbModule(new DbModule()).gsonModule(new GsonModule()).parsersModule(new ParsersModule()).build();
    }

    public MainActivityComponent getMainActivityComponent() {
        return mainActivityComponent;
    }

    public void setMainComponent(MainActivityComponent mainActivityComponent) {
        this.mainActivityComponent = mainActivityComponent;
    }


}