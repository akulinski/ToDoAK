package com.akulinski.todoak.retorift;

import com.google.gson.JsonArray;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Interface that create schema of endpoints
 */
public interface ApiInterface {

    @GET("todos/")
    Call<JsonArray> getNotes();

}
