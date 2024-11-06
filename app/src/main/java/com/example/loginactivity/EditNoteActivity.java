package com.example.loginactivity;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;

public class EditNoteActivity extends BaseActivity {
    FireStoreHelper fireStoreHelper;
    EditText etNoteTitle;
    EditText etNoteContent;
    TextView tvNoteTimestamp;
    boolean isEditMode = false;
    String docId;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        // Initialize FireStoreHelper
        fireStoreHelper = new FireStoreHelper(null);

        // Set up UI components
        etNoteTitle = findViewById(R.id.etNoteTitle);
        etNoteContent = findViewById(R.id.etNoteContent);
        tvNoteTimestamp = findViewById(R.id.tvNoteTimestamp);

        // get docId from intent
        docId = getIntent().getStringExtra("docId");
        // Check if docId is not null and not empty
        if(docId != null && !docId.isEmpty()) {
            isEditMode = true;
            etNoteTitle.setText(getIntent().getStringExtra("title"));
            etNoteContent.setText(getIntent().getStringExtra("content"));
            tvNoteTimestamp.setText(getIntent().getStringExtra("timestamp"));
        }

        findViewById(R.id.btnSave).setOnClickListener(v -> {
            // Validate input
            if(validateInput()) {
                saveNote(etNoteTitle.getText().toString(), etNoteContent.getText().toString());
                finish();
            }
        });

    }
    private boolean validateInput() {
        boolean isValid = true;
        if (etNoteTitle.getText().toString().isEmpty()) {
            etNoteTitle.setError("Please enter a title");
            //Toast.makeText(this, "Please enter a title", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        if (etNoteContent.getText().toString().isEmpty()) {
            etNoteContent.setError("Please enter some content");
            //Toast.makeText(this, "Please enter some content", Toast.LENGTH_SHORT).show();
            isValid = false;
        }
        return isValid;
    }
    private void saveNote(String title, String content) {
        if (isEditMode)
            fireStoreHelper.update(docId, new Note(title, content));
        else
            fireStoreHelper.add(new Note(title, content));
    }
}