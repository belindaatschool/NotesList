package com.example.loginactivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;

public class NotesActivity extends BaseActivity {
    RecyclerView rvNotes;
    NotesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes);

        rvNotes = findViewById(R.id.rvNotes);
        findViewById(R.id.btnAddNote).setOnClickListener(v -> {
            startActivity(new Intent(this,EditNoteActivity.class));
        });

        setupRecyclerView();
    }
    private void setupRecyclerView() {
        Query query = FireStoreHelper.getCollectionRef().orderBy("timestamp", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Note> options = new FirestoreRecyclerOptions.Builder<Note>()
                .setQuery(query, Note.class)
                .build();
        rvNotes.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotesAdapter(options, this);
        rvNotes.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}