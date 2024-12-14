package com.example.loginactivity.activities;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.loginactivity.activities.Adapters.EditNoteActivity;
import com.example.loginactivity.model.Note;
import com.example.loginactivity.R;
import com.example.loginactivity.utils.ImageUtils;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class NotesAdapter extends FirestoreRecyclerAdapter<Note, NotesAdapter.NoteViewHolder> {
    Context context;

    public NotesAdapter(FirestoreRecyclerOptions<Note> options, Context context) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull NoteViewHolder holder, int position, @NonNull Note note) {
        holder.tvItemTitle.setText(note.getTitle());
        holder.tvItemContent.setText(note.getContent());
        holder.tvItemTimestamp.setText(note.getTimestamp());
        if(note.getImage() != null && !note.getImage().isEmpty())
            holder.ivImage.setImageBitmap(ImageUtils.convertStringToBitmap(note.getImage()));

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, EditNoteActivity.class);
            intent.putExtra("title", note.getTitle());
            intent.putExtra("content", note.getContent());
            intent.putExtra("timestamp", note.getTimestamp());
            intent.putExtra("image", note.getImage());

            String docId = this.getSnapshots().getSnapshot(position).getId();
            intent.putExtra("docId", docId);
            context.startActivity(intent);
        } );
    }

    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_item, parent, false);
        return new NoteViewHolder(view);
    }

    public class NoteViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemTitle, tvItemContent, tvItemTimestamp;
        ImageView ivImage;

        public NoteViewHolder(@NonNull View itemView) {
            super(itemView);
            tvItemTitle = itemView.findViewById(R.id.tvItemTitle);
            tvItemContent = itemView.findViewById(R.id.tvItemContent);
            tvItemTimestamp = itemView.findViewById(R.id.tvItemTimestamp);
            ivImage = itemView.findViewById(R.id.imageView);
        }
    }
}
