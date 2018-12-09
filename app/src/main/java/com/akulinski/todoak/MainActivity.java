package com.akulinski.todoak;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.akulinski.todoak.core.NoteAdapter;
import com.akulinski.todoak.core.application.ToDoCore;
import com.akulinski.todoak.core.dbmanagment.CRUDOperationManager;
import com.akulinski.todoak.events.GetNotesEvent;
import com.akulinski.todoak.parsers.JsonArrayToDb;
import com.akulinski.todoak.parsers.NoteDAO;
import com.akulinski.todoak.parsers.NoteDAOToStringArrayParser;
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

    private String[] listOfItems ;

    private EventBus eventBus;

    private ApiInterface apiService;

    private GetNotesCallback getPhotosCallback;

    private RecyclerView notesRecyclerView;

    private JsonArrayToDb jsonArrayToDb;

    private CRUDOperationManager<NoteDAO> crudOperationManager;

    private NoteDAOToStringArrayParser noteDAOToStringArrayParser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((ToDoCore)getApplication()).getMainActivityComponent().inject(this);

        ButterKnife.bind(this);

        if(!crudOperationManager.checkIfTableIsNotEmpty()) {
            Call<JsonArray> call = apiService.getNotes();
            call.enqueue(getPhotosCallback);
        }else{
            try {
                noteDAOToStringArrayParser.loadData(crudOperationManager.readAllFromDb());
                noteDAOToStringArrayParser.parse();
                listOfItems = (String[]) noteDAOToStringArrayParser.getResult();
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        subscribeToEventBus();
    }

    private void createRecyclerView(){

        notesRecyclerView = (RecyclerView) findViewById(R.id.notes_list);

        NoteAdapter quotesListAdapter = new NoteAdapter(listOfItems);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        notesRecyclerView.setLayoutManager(manager);
        notesRecyclerView.setAdapter(quotesListAdapter);
    }

    /**
     * Registering MainActivity class to the EventBus
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

    @Inject
    public void setCrudOperationManager(CRUDOperationManager<NoteDAO> crudOperationManager) {
        this.crudOperationManager = crudOperationManager;
    }

    @Inject
    public void setNoteDAOToStringArrayParser(NoteDAOToStringArrayParser noteDAOToStringArrayParser) {
        this.noteDAOToStringArrayParser = noteDAOToStringArrayParser;
    }

    /**
     * Class that handles arrived event through EventBus
     */
    private final class GetPhotosEventListener{

        @Subscribe
        public void handleEvent(GetNotesEvent event) throws IllegalAccessException, InstantiationException {
            jsonArrayToDb.loadData(event.getJsonArray());
            jsonArrayToDb.parse();

            noteDAOToStringArrayParser.loadData(crudOperationManager.readAllFromDb());
            noteDAOToStringArrayParser.parse();
            listOfItems = (String[]) noteDAOToStringArrayParser.getResult();

            createRecyclerView();
        }

    }

}
