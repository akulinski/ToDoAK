package com.akulinski.todoak.core.modules;

import com.google.common.eventbus.EventBus;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Module that provides event bus
 */
@Module
public final class EventBusModule {

    @Provides
    @Singleton
    public EventBus provideEventBus() {
        return new EventBus();
    }
}
