package com.example.loginactivity.activities;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.loginactivity.utils.FireStoreHelper;
import com.example.loginactivity.utils.ImageUtils;
import com.example.loginactivity.model.Note;
import com.example.loginactivity.R;
import android.graphics.drawable.BitmapDrawable;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.content.ContextCompat;

public class EditNoteActivity extends BaseActivity {
    FireStoreHelper fireStoreHelper;
    EditText etNoteTitle;
    EditText etNoteContent;
    TextView tvNoteTimestamp;
    ImageView ivImage;
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
        ivImage = findViewById(R.id.ivImage);

        // Set drawable for ivImage - Placeholder image
        Drawable drawable1 = ContextCompat.getDrawable(this, R.drawable.camera2);
        Bitmap bitmap1 = ((BitmapDrawable) drawable1).getBitmap();
        ivImage.setImageBitmap(bitmap1);

        // get docId from intent
        docId = getIntent().getStringExtra("docId");
        // Check if docId is not null and not empty
        if(docId != null && !docId.isEmpty()) {
            isEditMode = true;
            etNoteTitle.setText(getIntent().getStringExtra("title"));
            etNoteContent.setText(getIntent().getStringExtra("content"));
            tvNoteTimestamp.setText(getIntent().getStringExtra("timestamp"));
            Bitmap bitmap = ImageUtils.convertStringToBitmap(getIntent().getStringExtra("image"));
            if(bitmap != null)
                ivImage.setImageBitmap(bitmap);
        } else
            findViewById(R.id.ivDelete).setVisibility(View.GONE);
        //save
        findViewById(R.id.ivSave).setOnClickListener(v -> {
            if(isValidInput()) {
                String title = etNoteTitle.getText().toString();
                String content = etNoteContent.getText().toString();
                Bitmap bitmap = ImageUtils.getBitmapFromImageView(ivImage);
                saveNote(title, content, bitmap);
                finish();
            }
        });

        //image onClick
        registerCameraLauncher();
        findViewById(R.id.ivImage).setOnClickListener(v -> {
            cameraLauncher.launch(null);
        });
        //delete
        findViewById(R.id.ivDelete).setOnClickListener(v -> {
            fireStoreHelper.delete(docId);
            finish();
        });
        //back
        findViewById(R.id.ivBack).setOnClickListener(v -> finish());
    }

    private boolean isValidInput() {
        boolean isValid = true;
        if (etNoteTitle.getText().toString().isEmpty()) {
            etNoteTitle.setError("Please enter a title");
            isValid = false;
        }
        if (etNoteContent.getText().toString().isEmpty()) {
            etNoteContent.setError("Please enter some content");
            isValid = false;
        }
        return isValid;
    }
    private void saveNote(String title, String content, Bitmap bitmap) {
        if (isEditMode)
            fireStoreHelper.update(docId, new Note(title, content, ImageUtils.convertBitmapToString(bitmap)));

        else
            fireStoreHelper.add(new Note(title, content, ImageUtils.convertBitmapToString(bitmap)));
    }

    //get image from camera
    private ActivityResultLauncher<Void> cameraLauncher;
    private void registerCameraLauncher(){
        cameraLauncher = registerForActivityResult(new ActivityResultContracts.TakePicturePreview(),
                new ActivityResultCallback<Bitmap>(){
            @Override
            public void onActivityResult(Bitmap bitmap) {
                ivImage.setImageBitmap(bitmap);
            }
        });
    }
}