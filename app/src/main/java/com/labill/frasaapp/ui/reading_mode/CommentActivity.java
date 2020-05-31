package com.labill.frasaapp.ui.reading_mode;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.labill.frasaapp.CommentListAdapter;
import com.labill.frasaapp.Comments;
import com.labill.frasaapp.R;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CommentActivity extends AppCompatActivity {

    private static final String TAG = "CommentActivityLog";
    FirebaseAuth mAuth;
    DatabaseReference databaseComment;

    private Button send;
    private EditText comment;
    private String idStory;
    private RecyclerView recyclerView;
    FirebaseFirestore db;
    private CommentListAdapter commentListAdapter;
    private List<Comments> commentsList;
    String temp = "User";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Intent onClickIntent = getIntent();
        idStory = onClickIntent.getStringExtra("id");

        recyclerView = findViewById(R.id.rvFeedback);
        commentsList = new ArrayList<>();
        commentListAdapter = new CommentListAdapter(commentsList);
        send = findViewById(R.id.send);
        comment = findViewById(R.id.cc);

        mAuth = FirebaseAuth.getInstance();
        databaseComment = FirebaseDatabase.getInstance().getReference().child("comments").child(idStory);
        db = FirebaseFirestore.getInstance();


        databaseComment.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Log.d(TAG, "data snapshot :"+dataSnapshot.getValue());
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    Log.d(TAG, "ds key :"+ds.getKey());
                    Log.d(TAG, "ds value "+ds.getValue());


                    Comments c = new Comments();
                    c.setIdStory(ds.getKey());
//                    Map tempMap = (Map) ds.getValue();
//                    for(Object id : tempMap.keySet()){

                    c.setIdAuthor(ds.getKey());
                    c.setComment(ds.getValue().toString());


                   // c.setIdAuthor(ds.getKey());
//                    }
//                    for(Object comment : tempMap.values()){

                   // }

                    Log.d(TAG, "comment content "+c.getComment());
                    Log.d(TAG, "comment author "+c.getIdAuthor());
                    Log.d(TAG, "comment story id "+c.getIdStory());

                    commentsList.add(c);
                    commentListAdapter.notifyDataSetChanged();

                }
                Log.d(TAG, "comment list :"+commentsList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "comment list di dlm send:"+commentsList);
                // kalau feedback kosong
                if(comment.getText().toString().equals(""))
                {
                    Toast.makeText(CommentActivity.this, "Feedback can't be empty", Toast.LENGTH_SHORT).show();
                }
                else // kalau ada isinya
                {
                    databaseComment.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            databaseComment.child(mAuth.getCurrentUser().getUid()).setValue(comment.getText().toString());
                            Toast.makeText(CommentActivity.this, "Feedback posted", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(commentListAdapter);
    }
}
