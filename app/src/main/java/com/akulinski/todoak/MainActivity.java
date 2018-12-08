package com.akulinski.todoak;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.akulinski.todoak.core.NoteAdapter;
import com.akulinski.todoak.core.NoteHolder;

import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {


    //Defining dummy data to be shown
    private String[] listOfItem = {"Be yourself; everyone else is already taken.",
            "No one can make you feel inferior without your consent.",
            "Always forgive your enemies; nothing annoys them so much.",
            "It is during our darkest moments that we must focus to see the light.",
            "Don't judge each day by the harvest you reap but by the seeds that you plant.",
            "The pessimist complains about the wind; the optimist expects it to change; the realist adjusts the sails."};

    private RecyclerView quotesRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        quotesRecyclerView = (RecyclerView) findViewById(R.id.rv_show_list);
        //Instantiating LinearLayoutManager and MyListAdapter
        NoteAdapter quotesListAdapter = new NoteAdapter(listOfItem);
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        //Hooking up to RecyclerView
        quotesRecyclerView.setLayoutManager(manager);
        quotesRecyclerView.setAdapter(quotesListAdapter);
    }
}
