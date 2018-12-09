package com.akulinski.todoak;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.CheckBox;
import android.widget.ImageButton;

import com.akulinski.todoak.core.NoteAdapter;
import com.akulinski.todoak.core.NoteHolder;
import com.akulinski.todoak.core.application.ToDoCore;
import com.akulinski.todoak.core.dbmanagment.CRUDOperationManager;
import com.akulinski.todoak.events.ChangeStatusEvent;
import com.akulinski.todoak.events.GetNotesEvent;
import com.akulinski.todoak.parsers.JsonArrayToDb;
import com.akulinski.todoak.parsers.NoteDAO;
import com.akulinski.todoak.parsers.NoteDAOToStringArrayParser;
import com.akulinski.todoak.retorift.ApiInterface;
import com.akulinski.todoak.retorift.callbacks.GetNotesCallback;
import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.gson.JsonArray;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

    private EventBus eventBus;

    private ApiInterface apiService;

    private GetNotesCallback getPhotosCallback;

    private JsonArrayToDb jsonArrayToDb;

    private CRUDOperationManager<NoteDAO> crudOperationManager;

    private NoteDAOToStringArrayParser noteDAOToStringArrayParser;

    @BindView(R.id.notes_list)
    RecyclerView notesRecyclerView;

    @BindView(R.id.filter)
     ImageButton imageButton;

    @BindView(R.id.done)
     CheckBox successful;

    @BindView(R.id.not_done)
    CheckBox notSuccessful;

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
                createRecyclerView(crudOperationManager.readAllFromDb());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        subscribeToEventBus();
    }

    private void createRecyclerView(List<NoteDAO> listOfItems){
        NoteAdapter notesListAdapter = new NoteAdapter((ArrayList<NoteDAO>) listOfItems,eventBus);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        notesRecyclerView.setLayoutManager(manager);
        notesRecyclerView.setAdapter(notesListAdapter);
    }

    /**
     * Registering MainActivity class to the EventBus
     */
    private void subscribeToEventBus(){

        eventBus.register(new GetPhotosEventListener());
        eventBus.register(new ChangeStatusEventListener());
    }

    @OnClick(R.id.done) void showSuccessful(){

        try {
            if(successful.isChecked()) {
                ((NoteAdapter) notesRecyclerView.getAdapter()).getListOfItems().clear();
                ((NoteAdapter) notesRecyclerView.getAdapter()).getListOfItems().addAll(crudOperationManager.readAllWithStatus("true"));
                notesRecyclerView.getAdapter().notifyDataSetChanged();
                notSuccessful.setChecked(false);
            }else{
                ((NoteAdapter) notesRecyclerView.getAdapter()).getListOfItems().clear();
                ((NoteAdapter) notesRecyclerView.getAdapter()).getListOfItems().addAll(crudOperationManager.readAllFromDb());
                notesRecyclerView.getAdapter().notifyDataSetChanged();
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }


    }

    @OnClick(R.id.not_done) void showNotDone(){

        try {
            if(notSuccessful.isChecked()) {
                ((NoteAdapter) notesRecyclerView.getAdapter()).getListOfItems().clear();
                ((NoteAdapter) notesRecyclerView.getAdapter()).getListOfItems().addAll(crudOperationManager.readAllWithStatus("false"));
                notesRecyclerView.getAdapter().notifyDataSetChanged();
                successful.setChecked(false);
            }else{
                ((NoteAdapter) notesRecyclerView.getAdapter()).getListOfItems().clear();
                ((NoteAdapter) notesRecyclerView.getAdapter()).getListOfItems().addAll(crudOperationManager.readAllFromDb());
                notesRecyclerView.getAdapter().notifyDataSetChanged();
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void showAll(){

        try {
            ((NoteAdapter) notesRecyclerView.getAdapter()).getListOfItems().clear();
            ((NoteAdapter) notesRecyclerView.getAdapter()).getListOfItems().addAll(crudOperationManager.readAllFromDb());
            notesRecyclerView.getAdapter().notifyDataSetChanged();
            successful.setChecked(false);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
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
        public void handleAddNotesEvent(GetNotesEvent event) throws IllegalAccessException, InstantiationException {
            jsonArrayToDb.loadData(event.getJsonArray());
            jsonArrayToDb.parse();
            createRecyclerView(crudOperationManager.readAllFromDb());
        }
    }

    private final class ChangeStatusEventListener{
        @Subscribe
        public void handleChangeStatusEvent(ChangeStatusEvent changeStatusEvent){
            crudOperationManager.setStatus(changeStatusEvent.getNewStatus(),changeStatusEvent.getTitle());
            if(successful.isChecked()){
                showSuccessful();
            }else if (notSuccessful.isChecked()){
                showNotDone();
            }else{
                showAll();
            }
        }
    }

}
