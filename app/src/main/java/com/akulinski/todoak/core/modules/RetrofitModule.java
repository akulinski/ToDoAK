package com.akulinski.todoak.core.modules;

import com.akulinski.todoak.retorift.ApiClient;
import com.akulinski.todoak.retorift.ApiInterface;
import com.akulinski.todoak.utils.Urls;

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
     * @param { @link #provideBaseUrl() }
     * @return instance of api client with base URL
     */
    @Provides
    @Singleton
    public ApiInterface provideApiInterface(@Named("baseUrl") String baseUrl){
        return new ApiClient(baseUrl).getClient().create(ApiInterface.class);
    }

}