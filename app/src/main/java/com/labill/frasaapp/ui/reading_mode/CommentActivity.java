package com.labill.frasaapp.ui.reading_mode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.labill.frasaapp.R;

public class CommentActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    DatabaseReference databaseComment;

    private Button send;
    private EditText comment;
    private String idStory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        Intent onClickIntent = getIntent();
        idStory = onClickIntent.getStringExtra("id");

        send = findViewById(R.id.send);
        comment = findViewById(R.id.cc);

        mAuth = FirebaseAuth.getInstance();
        databaseComment = FirebaseDatabase.getInstance().getReference().child("comments");

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(comment.getText().toString().equals(""))
                {
                    Toast.makeText(CommentActivity.this, "Feedback can't be empty", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    databaseComment.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            databaseComment.child(idStory).child(mAuth.getCurrentUser().getUid()).setValue(comment.getText().toString());
                            Toast.makeText(CommentActivity.this, "Feedback posted", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }
}
