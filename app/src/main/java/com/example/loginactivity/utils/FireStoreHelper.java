package com.example.loginactivity.utils;

import android.util.Log;

import com.example.loginactivity.model.Note;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.ArrayList;

public class FireStoreHelper {
    private static final String TAG = "FireStoreHelper Tag";
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    private static CollectionReference collectionRef = db.collection("notes").document(currentUser.getUid()).collection("my_notes");;
    private FireStoreHelper.FBReply fbReply;

    public interface FBReply {
        void getAllSuccess(ArrayList<Note> notes);
        void getOneSuccess(Note note);
    }

    public FireStoreHelper(FireStoreHelper.FBReply fbReply) {
        this.fbReply = fbReply;
    }

    public void add(Note note) {
        collectionRef.add(note).addOnSuccessListener(documentReference -> {
            Log.d(TAG, "DocumentSnapshot added with ID: " + documentReference.getId());
        }).addOnFailureListener(e -> {
            Log.w(TAG, "Error adding document", e);
            });
    }

    public void update(String id, Note note) {
        collectionRef.document(id).update(
                "title", note.getTitle(),
                "content", note.getContent(),
                "image", note.getImage()).addOnSuccessListener(aVoid -> {
            Log.d(TAG, "DocumentSnapshot updated with ID: " + id);
        }).addOnFailureListener(e -> {
            Log.w(TAG, "Error updating document", e);
            });
    }
    public void delete(String id) {
        collectionRef.document(id).delete().addOnSuccessListener(aVoid -> {
            Log.d(TAG, "DocumentSnapshot deleted with ID: " + id);
        }).addOnFailureListener(e -> {
            Log.w(TAG, "Error deleting document", e);
        });
    }
    public void getAll() {
        collectionRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                ArrayList<Note> notes = new ArrayList<>();
                for (com.google.firebase.firestore.DocumentSnapshot document : task.getResult()) {
                    Log.d(TAG, document.getId() + " => " + document.getData());
                    Note note = document.toObject(Note.class);
                    notes.add(note);
                }
                fbReply.getAllSuccess(notes);
            } else {
                Log.w(TAG, "Error getting documents.", task.getException());
            }
        });

    }
    public void getOne(String id) {
        collectionRef.document(id).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                com.google.firebase.firestore.DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                    Note note = document.toObject(Note.class);
                    fbReply.getOneSuccess(note);
                } else {
                    Log.d(TAG, "No such document");
                }
            } else {
                Log.d(TAG, "get failed with ", task.getException());
            }
        });
    }

    public static CollectionReference getCollectionRef() {
        return collectionRef;
    }
}
