package com.labill.frasaapp;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.labill.frasaapp.ui.reading_mode.CommentActivity;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Comment;

import java.util.List;
import java.util.concurrent.Executor;


public class CommentListAdapter extends RecyclerView.Adapter {

    private FirebaseFirestore firebaseFirestore;
    private DatabaseReference databaseComment, databaseContent;

    StorageReference references;

    public List<Comments> commentList;

    public CommentListAdapter(List<Comments> commentList) {
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentListAdapter.ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((CommentListAdapter.ListViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        private TextView commenter, commentContent;

        public ListViewHolder(View itemView) {
            super(itemView);
            commenter = itemView.findViewById(R.id.commenter);
            commentContent = itemView.findViewById(R.id.commentContent);
        }


        // Bind with data from firebase
        public void bindView(int position) {

            final Comments comment = commentList.get(position);
            String idStory = comment.getIdStory();
            String idAuthor = comment.getIdAuthor();
            String content = comment.getComment();

            firebaseFirestore = FirebaseFirestore.getInstance();


            DocumentReference documentReference = firebaseFirestore.collection("users").document(idAuthor);

            documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                            // Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            commenter.setText(document.getString("name"));
                        } else {
                            //Log.d(TAG, "No such document");
                        }
                    } else {
                        //Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });

//            documentReference.addSnapshotListener((Executor) CommentListAdapter.this, new EventListener<DocumentSnapshot>()
//            {
//                @Override
//                public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                    commenter.setText(documentSnapshot.getString("name"));
//                    Log.d("commenter", documentSnapshot.getString("name"));
//                }
//            });

            //commenter.setText(idAuthor);
            commentContent.setText(content);

        }
    }
}

