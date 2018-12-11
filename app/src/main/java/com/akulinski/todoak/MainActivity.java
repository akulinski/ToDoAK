package com.akulinski.todoak;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.akulinski.todoak.core.NoteAdapter;
import com.akulinski.todoak.core.application.ToDoCore;
import com.akulinski.todoak.core.dbmanagment.CRUDOperationManager;
import com.akulinski.todoak.events.AddNoteEvent;
import com.akulinski.todoak.events.ChangeStatusEvent;
import com.akulinski.todoak.events.GetNotesEvent;
import com.akulinski.todoak.events.RemoveNoteEvent;
import com.akulinski.todoak.events.SaveNoteEvent;
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
import butterknife.OnItemSelected;
import butterknife.OnTextChanged;
import retrofit2.Call;

public class MainActivity extends AppCompatActivity {

    private EventBus eventBus;

    private ApiInterface apiService;

    private GetNotesCallback getPhotosCallback;

    private JsonArrayToDb jsonArrayToDb;

    private CRUDOperationManager<NoteDAO> crudOperationManager;

    private NoteDAOToStringArrayParser noteDAOToStringArrayParser;

    /**
     * Spinner status
     */
    private final static String STATUS_TITLE = "Title";

    /**
     * Spinner status
     */
    private final static String STATUS_USER_ID = "User ID";

    /**
     * Current spinner status
     */
    private String SPINNER_STATUS;

    private boolean isInitialized; //OnTextChanged gets called on application startup and references null list

    private final static String NOTE_SAVED = "NOTE_SAVED";

    private final static String NOTE_ADDED = "NOTE_ADDED";

    @BindView(R.id.notes_list)
    RecyclerView notesRecyclerView;

    /**
     * Show successful top filter
     */
    @BindView(R.id.done)
    CheckBox successful;

    /**
     * Show not successful top filter
     */
    @BindView(R.id.not_done)
    CheckBox notSuccessful;

    @BindView(R.id.add_note)
    ImageButton addNoteButton;

    @BindView(R.id.search_box)
    EditText bottomFilter;

    @BindView(R.id.userid_title_spinner)
    Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ((ToDoCore) getApplication()).getMainActivityComponent().inject(this);
        ButterKnife.bind(this);

        subscribeToEventBus();
        initSpinner();

        checkIfDbIsEmptyElseCallAPI();

    }

    @Override
    protected void onResume() {
        super.onResume();

        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences("sharedPrefs", 0);

        String topFilter = context.getResources().getString(R.string.TOP_FILTER);

        String checked = sharedPref.getString(topFilter, null);

        if (checked != null) {
            if (checked.equals(String.valueOf(R.string.SUCCESSFUL))) {
                successful.setChecked(true);
                notSuccessful.setChecked(false);
            } else if (checked.equals(String.valueOf(R.string.UNSUCCESSFUL))) {
                successful.setChecked(false);
                notSuccessful.setChecked(true);
            } else {
                successful.setChecked(false);
                notSuccessful.setChecked(false);
            }
        }

        String spinnerString = context.getResources().getString(R.string.SPINNER);

        String spinnerStatus = sharedPref.getString(spinnerString, null);

        if (spinnerStatus != null) {
            if (spinnerStatus.equals(STATUS_TITLE)) {
                this.spinner.setSelection(0);
            } else if (spinnerStatus.equals(STATUS_USER_ID)) {
                this.spinner.setSelection(1);
            }
        }

        String filterString = context.getResources().getString(R.string.FILTER_TEXT);

        String filterText = sharedPref.getString(filterString, null);

        if (filterText != null) {
            this.bottomFilter.setText(filterText);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        Context context = getApplicationContext();
        SharedPreferences sharedPref = context.getSharedPreferences("sharedPrefs", 0);
        SharedPreferences.Editor editor = sharedPref.edit();

        if (successful.isChecked()) {
            editor.putString(getString(R.string.TOP_FILTER), String.valueOf(R.string.SUCCESSFUL));
        } else if (notSuccessful.isChecked()) {
            editor.putString(getString(R.string.TOP_FILTER), String.valueOf(R.string.UNSUCCESSFUL));
        } else {
            editor.putString(getString(R.string.TOP_FILTER), getString(R.string.NO_FILTER));
        }

        editor.putString(getString(R.string.SPINNER), this.SPINNER_STATUS);

        editor.putString(getString(R.string.FILTER_TEXT), this.bottomFilter.getText().toString());

        editor.commit();
    }

    private void createRecyclerView(List<NoteDAO> listOfItems) {
        NoteAdapter notesListAdapter = new NoteAdapter((ArrayList<NoteDAO>) listOfItems, eventBus);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);

        notesRecyclerView.setLayoutManager(manager);
        notesRecyclerView.setAdapter(notesListAdapter);
    }

    private void initSpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.filter_spinner, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        this.SPINNER_STATUS = STATUS_TITLE;
    }

    public void checkIfDbIsEmptyElseCallAPI() {

        if (crudOperationManager.checkIfTableIsEmpty()) {
            isInitialized = false;
            Call<JsonArray> call = apiService.getNotes();
            call.enqueue(getPhotosCallback);
        } else {
            isInitialized = true;
            try {
                createRecyclerView(crudOperationManager.readAllFromDb());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    private void subscribeToEventBus() {

        eventBus.register(new GetNotesEventListener());
        eventBus.register(new ChangeStatusEventListener());
        eventBus.register(new RemoveNoteEventListener());
        eventBus.register(new AddNoteEventListener());
        eventBus.register(new SaveNoteEventListener());
    }

    /**
     * When uses clicks top filter shows notes with corresponding status but
     * Checks Whether user as entered something in bottom filter previously
     */
    @OnClick(R.id.done)
    void showSuccessful() {

        if (!this.bottomFilter.getText().toString().equals("")) {
            notSuccessful.setChecked(false);
            filterByTitleOrUserId();
            return;
        }
        try {
            if (successful.isChecked()) {
                ((NoteAdapter) notesRecyclerView.getAdapter()).getListOfItems().clear();
                ((NoteAdapter) notesRecyclerView.getAdapter()).getListOfItems().addAll(crudOperationManager.readAllWithStatus("true"));
                notesRecyclerView.getAdapter().notifyDataSetChanged();
                notSuccessful.setChecked(false);
            } else {
                ((NoteAdapter) notesRecyclerView.getAdapter()).getListOfItems().clear();
                ((NoteAdapter) notesRecyclerView.getAdapter()).getListOfItems().addAll(crudOperationManager.readAllFromDb());
                notesRecyclerView.getAdapter().notifyDataSetChanged();
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }


    }

    /**
     * When uses clicks top filter shows notes with corresponding status but
     * Checks Whether user as entered something in bottom filter previously
     */
    @OnClick(R.id.not_done)
    void showNotDone() {
        if (!this.bottomFilter.getText().toString().equals("")) {
            successful.setChecked(false);
            filterByTitleOrUserId();
            return;
        }

        try {
            if (notSuccessful.isChecked()) {
                ((NoteAdapter) notesRecyclerView.getAdapter()).getListOfItems().clear();
                ((NoteAdapter) notesRecyclerView.getAdapter()).getListOfItems().addAll(crudOperationManager.readAllWithStatus("false"));
                notesRecyclerView.getAdapter().notifyDataSetChanged();
                successful.setChecked(false);
            } else {
                ((NoteAdapter) notesRecyclerView.getAdapter()).getListOfItems().clear();
                ((NoteAdapter) notesRecyclerView.getAdapter()).getListOfItems().addAll(crudOperationManager.readAllFromDb());
                notesRecyclerView.getAdapter().notifyDataSetChanged();
            }
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.add_note)
    void addNote() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add new Note");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("OK", (dialog, which) -> {

            String noteTitle = input.getText().toString();

            NoteDAO noteDAO = new NoteDAO(1, noteTitle, false);

            eventBus.post(new AddNoteEvent(noteDAO));

        });
        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());

        builder.show();
    }

    /**
     * Filters user text using bottom and top filter, but if edit text is empty shows data according to top filter
     */
    @OnTextChanged(R.id.search_box)
    void filterByTitleOrUserId() {
        if (isInitialized) {
            if (bottomFilter.getText().toString().equals("")) {
                showAccordingToTopFilter();
                return;
            }

            ((NoteAdapter) notesRecyclerView.getAdapter()).getListOfItems().clear();
            try {
                //Filtering by title
                if (this.SPINNER_STATUS.equals(STATUS_TITLE)) {
                    //checks whether top filter is checked
                    if (successful.isChecked()) {
                        ((NoteAdapter) notesRecyclerView.getAdapter()).getListOfItems().addAll(crudOperationManager.findByTitle(bottomFilter.getText().toString(), "true"));
                    } else if (notSuccessful.isChecked()) {//checks whether top filter is checked
                        ((NoteAdapter) notesRecyclerView.getAdapter()).getListOfItems().addAll(crudOperationManager.findByTitle(bottomFilter.getText().toString(), "false"));
                    } else {//is top filter is not checked then show all notes with corresponding title
                        ((NoteAdapter) notesRecyclerView.getAdapter()).getListOfItems().addAll(crudOperationManager.findByTitle(bottomFilter.getText().toString(), "none"));
                    }

                    //Filtering by user id
                } else if (this.SPINNER_STATUS.equals(STATUS_USER_ID)) {
                    //checks whether top filter is checked
                    if (successful.isChecked()) {
                        ((NoteAdapter) notesRecyclerView.getAdapter()).getListOfItems().addAll(crudOperationManager.findById(bottomFilter.getText().toString(), "true"));
                    } else if (notSuccessful.isChecked()) { //checks whether top filter is checked
                        ((NoteAdapter) notesRecyclerView.getAdapter()).getListOfItems().addAll(crudOperationManager.findById(bottomFilter.getText().toString(), "false"));
                    } else {//is top filter is not checked then show all notes with corresponding user id
                        ((NoteAdapter) notesRecyclerView.getAdapter()).getListOfItems().addAll(crudOperationManager.findById(bottomFilter.getText().toString(), "none"));
                    }
                }
            } catch (IllegalAccessException | InstantiationException e) {
                e.printStackTrace();
            }

            notesRecyclerView.getAdapter().notifyDataSetChanged();
        }
    }

    private void showAll() {

        try {
            ((NoteAdapter) notesRecyclerView.getAdapter()).getListOfItems().clear();
            ((NoteAdapter) notesRecyclerView.getAdapter()).getListOfItems().addAll(crudOperationManager.readAllFromDb());
            notesRecyclerView.getAdapter().notifyDataSetChanged();
            successful.setChecked(false);
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }


    public void showAccordingToTopFilter() {

        if (successful.isChecked()) {
            showSuccessful();
        } else if (notSuccessful.isChecked()) {
            showNotDone();
        } else {
            showAll();
        }
    }

    /**
     * Changes status of bottom filter, also reloads data when status is changed
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @OnItemSelected(R.id.userid_title_spinner)
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.SPINNER_STATUS = (String) parent.getItemAtPosition(position);
        filterByTitleOrUserId();
    }

    /**
     * Class that calls parser and inserts data do database using parser
     */
    private final class GetNotesEventListener {
        @Subscribe
        public void handleAddNotesEvent(GetNotesEvent event) throws IllegalAccessException, InstantiationException {
            jsonArrayToDb.loadData(event.getJsonArray());
            jsonArrayToDb.parse();
            createRecyclerView(crudOperationManager.readAllFromDb());
        }
    }

    /**
     * Listens for changing status of notes
     */
    private final class ChangeStatusEventListener {
        @Subscribe
        public void handleChangeStatusEvent(ChangeStatusEvent changeStatusEvent) {
            crudOperationManager.setStatus(changeStatusEvent.getNewStatus(), changeStatusEvent.getTitle());
            isInitialized = true;
            showAccordingToTopFilter();
        }
    }

    /**
     * Listens for removing of notes
     */
    private final class RemoveNoteEventListener {
        @Subscribe
        public void handleRemoveNoteEvent(RemoveNoteEvent removeNoteEvent) {
            crudOperationManager.removeNoteWithId(removeNoteEvent.getId());
            showAccordingToTopFilter();
        }
    }

    /**
     * Adds notes
     */
    private final class AddNoteEventListener {
        @Subscribe
        public void handleAddNoteEvent(AddNoteEvent addNoteEvent) {
            crudOperationManager.insert(addNoteEvent.getNoteDAO());
            showAccordingToTopFilter();
            showToast(NOTE_ADDED);
        }
    }

    /**
     * Saves edited title
     */
    private final class SaveNoteEventListener {
        @Subscribe
        public void handleSaveNoteEvent(SaveNoteEvent saveNoteEvent) {
            crudOperationManager.updateTitle(saveNoteEvent.getNoteId(), saveNoteEvent.getTitle());
            showToast(NOTE_SAVED);

        }
    }

    public void showToast(String type) {

        String textValue = "";

        if (type.equals(NOTE_ADDED))
            textValue = getResources().getString(R.string.note_added);
        else if (type.equals(NOTE_SAVED))
            textValue = getResources().getString(R.string.note_saved);

        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, textValue, duration);

        toast.show();
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

}
