package com.akulinski.todoak;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.akulinski.todoak.core.NoteAdapter;
import com.akulinski.todoak.core.application.ToDoCore;
import com.akulinski.todoak.events.GetNotesEvent;
import com.akulinski.todoak.parsers.IParser;
import com.akulinski.todoak.parsers.JsonArrayToDb;
import com.akulinski.todoak.retorift.ApiInterface;
import com.akulinski.todoak.retorift.callbacks.GetNotesCallback;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.gson.JsonArray;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.ButterKnife;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

    //Defining dummy data to be shown
    private String[] listOfItem = {"Be yourself; everyone else is already taken.",
            "No one can make you feel inferior without your consent.",
            "Always forgive your enemies; nothing annoys them so much.",
            "It is during our darkest moments that we must focus to see the light.",
            "Don't judge each day by the harvest you reap but by the seeds that you plant.",
            "The pessimist complains about the wind; the optimist expects it to change; the realist adjusts the sails."};

    private EventBus eventBus;

    private ApiInterface apiService;

    private GetNotesCallback getPhotosCallback;

    private RecyclerView quotesRecyclerView;

    private JsonArrayToDb jsonArrayToDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((ToDoCore)getApplication()).getMainActivityComponent().inject(this);

        ButterKnife.bind(this);

        quotesRecyclerView = (RecyclerView) findViewById(R.id.rv_show_list);
        //Instantiating LinearLayoutManager and MyListAdapter
        NoteAdapter quotesListAdapter = new NoteAdapter(listOfItem);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //Hooking up to RecyclerView
        quotesRecyclerView.setLayoutManager(manager);
        quotesRecyclerView.setAdapter(quotesListAdapter);

        subscribeToEventBus();

        Call<JsonArray> call = apiService.getNotes();
        call.enqueue(getPhotosCallback);
    }


    /**
     * Registering LoginActivity class to the EventBus
     */
    private void subscribeToEventBus(){
        eventBus.register(new GetPhotosEventListener());
    }

    /**
     * Creates Dialog with an OK button
     * @param message message displayed on Dialog
     */
    private void showDialog(String message) {

        AlertDialog alertDialog = new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", (dialogInterface, i) -> {}).show();

    }


    public void setListOfItem(String[] listOfItem) {
        this.listOfItem = listOfItem;
    }

    @Inject
    public void setEventBus(EventBus eventBus) {
        this.eventBus = eventBus;
    }

    @Inject
    public void setApiService(ApiInterface apiService) {
        this.apiService = apiService;
    }

    @Inject
    public void setGetPhotosCallback(GetNotesCallback getPhotosCallback) {
        this.getPhotosCallback = getPhotosCallback;
    }

    @Inject
    @Named("JsonArrayToDb")
    public void setJsonArrayToDb(JsonArrayToDb jsonArrayToDb) {
        this.jsonArrayToDb = jsonArrayToDb;
    }

    /**
     * Class that handles arrived event through EventBus
     */
    private final class GetPhotosEventListener{

        @Subscribe
        public void getStatus(GetNotesEvent event) {
            Log.d("MESSAGE",event.getJsonArray().toString());
            jsonArrayToDb.loadData(event.getJsonArray());
            jsonArrayToDb.parse();
        }

    }

}
