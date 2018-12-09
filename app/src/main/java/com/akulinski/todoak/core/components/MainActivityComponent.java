package com.akulinski.todoak.core.components;

import com.akulinski.todoak.MainActivity;
import com.akulinski.todoak.core.modules.ContextModule;
import com.akulinski.todoak.core.modules.DbModule;
import com.akulinski.todoak.core.modules.EventBusModule;
import com.akulinski.todoak.core.modules.GsonModule;
import com.akulinski.todoak.core.modules.ParsersModule;
import com.akulinski.todoak.core.modules.RetrofitModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Injects to LoginActivity
 */
@Singleton
@Component(modules = {EventBusModule.class, RetrofitModule.class,ContextModule.class,DbModule.class,GsonModule.class,ParsersModule.class})
public interface MainActivityComponent {
    void inject(MainActivity activity);
}
