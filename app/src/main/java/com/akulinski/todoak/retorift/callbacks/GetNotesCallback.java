package com.akulinski.todoak.retorift.callbacks;

import com.akulinski.todoak.events.GetNotesEvent;
import com.google.common.eventbus.EventBus;
import com.google.gson.JsonArray;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public final class GetNotesCallback implements Callback<JsonArray> {

    private EventBus eventBus;

    @Inject
    public GetNotesCallback(EventBus eventBus){
        this.eventBus = eventBus;
    }

    @Override
    public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

        if (response.isSuccessful()){
            eventBus.post(new GetNotesEvent(true,response.body()));
        }else {
            eventBus.post(new GetNotesEvent(false,response.body()));
        }
    }

    @Override
    public void onFailure(Call<JsonArray> call, Throwable t) {

        eventBus.post(new GetNotesEvent(false));
        throw new IllegalStateException(t.getLocalizedMessage());
    }
}
