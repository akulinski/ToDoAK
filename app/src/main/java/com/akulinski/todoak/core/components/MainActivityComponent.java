package com.akulinski.todoak.core.components;

import com.akulinski.todoak.MainActivity;
import com.akulinski.todoak.core.modules.EventBusModule;
import com.akulinski.todoak.core.modules.RetrofitModule;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Injects to LoginActivity
 */
@Singleton
@Component(modules = {EventBusModule.class, RetrofitModule.class})
public interface MainActivityComponent {
    void inject(MainActivity activity);
}
