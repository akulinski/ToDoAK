package com.akulinski.todoak.core.modules;

import android.content.Context;

import dagger.Module;
import dagger.Provides;

/**
 * Provides context (from ToDoCore)
 */

@Module
public class ContextModule {
    private Context context;

    public ContextModule(Context context) {
        this.context = context;
    }

    @Provides
    public Context provideContext() {
        return context;
    }
}