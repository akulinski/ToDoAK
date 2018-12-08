package com.akulinski.todoak.core.modules;

import com.akulinski.todoak.retorift.ApiClient;
import com.akulinski.todoak.retorift.ApiInterface;
import com.akulinski.todoak.retorift.callbacks.GetNotesCallback;
import com.akulinski.todoak.utils.Urls;
import com.google.common.eventbus.EventBus;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Module that provides retrofit obj. and
 * related objects.
 */

@Module
public final class RetrofitModule {

    /**
     *
     * @return base url of server
     */
    @Provides
    @Singleton
    @Named("baseUrl")
    public String provideBaseUrl(){
        return Urls.SERVER.url;
    }

    /**
     *
     * @param baseUrl provided by provideBaseUrl
     * @return instance of api client with base URL
     */
    @Provides
    @Singleton
    public ApiInterface provideApiInterface(@Named("baseUrl") String baseUrl){
        return new ApiClient(baseUrl).getClient().create(ApiInterface.class);
    }

    /**
     *
     * @param eventBus provided by event bus module
     * @return callback for login screen
     */
    @Provides
    @Singleton
    public GetNotesCallback provideGetNotesCallback(EventBus eventBus){
        return new GetNotesCallback(eventBus);
    }

}