package com.labill.frasaapp.ui.reading_mode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
import com.labill.frasaapp.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import io.grpc.Context;

public class ReadingActivity extends AppCompatActivity {

    private static final String TAG = "ReadingActivityLog";
    FirebaseFirestore firebaseFirestore;
    StorageReference references;
    private FirebaseAuth mAuth;
    private boolean processLike = false;
    private boolean processBookmark = false;
    private DatabaseReference databaseLike, databaseBookmark;

    private TextView tvTitle, tvAuthor, tvContent, numOfLike;
    private ImageView storyImg;
    private String idStory;
    private Button buttComment, buttLike, buttBookmark;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        references = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        databaseLike = FirebaseDatabase.getInstance().getReference().child("likes");
       // databaseLike.keepSynced(true);
        databaseBookmark = FirebaseDatabase.getInstance().getReference().child("bookmarks");
        //databaseBookmark.keepSynced(true);

        tvTitle = findViewById(R.id.tvPostTitle);
        tvAuthor = findViewById(R.id.tvPostAuthor);
        tvContent = findViewById(R.id.tvPostContent);
        storyImg = findViewById(R.id.imageView2);
        buttComment = findViewById(R.id.buttComment);
        buttLike = findViewById(R.id.buttLike);
        buttBookmark = findViewById(R.id.buttBookmark);
        numOfLike = findViewById(R.id.numOfLike);


        Intent onClickIntent = getIntent();
        String recvTitle = onClickIntent.getStringExtra("title");
        String recvAuthor = onClickIntent.getStringExtra("name");
        String recvContent = onClickIntent.getStringExtra("content");
        //String img = onClickIntent.getStringExtra("photo");

        tvTitle.setText(recvTitle);
        tvAuthor.setText(recvAuthor);
        tvContent.setText(recvContent);

        //Retrieve story's id
        com.google.firebase.firestore.Query loadStory = firebaseFirestore.collection("stories").
                whereEqualTo("title", recvTitle);

        // Image
        loadStory.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot documentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()){

                    if (doc.getType() == DocumentChange.Type.ADDED){

                        idStory = doc.getDocument().getId();
                        Log.d("cekkkkk", idStory);
                        StorageReference storyRef = references.child("stories/"+idStory+"/stories.jpg");

                        storyRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Picasso.get().load(uri).into(storyImg);
                            }
                        });
                    }
                    else
                    {
                        Log.d("ini", "gagal");
                    }
                }

            }
        });

        databaseLike.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // get total available quest
                if(dataSnapshot.hasChild(idStory))
                {
                    int size = (int) dataSnapshot.child(idStory).getChildrenCount();
                    Log.d("size", String.valueOf(size));
                    numOfLike.setText(String.valueOf(size));
                }
                else
                {
                    numOfLike.setText("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        //cek udh kelike belum
        databaseLike.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // get total available quest
                if(dataSnapshot.child(idStory).hasChild(mAuth.getCurrentUser().getUid()))
                {
                    buttLike.setBackgroundResource(R.drawable.like_t);
                }
                else
                {
                    buttLike.setBackgroundResource(R.drawable.like_f);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //cek udh bookmark belum
        databaseBookmark.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // get total available quest
                if(dataSnapshot.hasChild(mAuth.getCurrentUser().getUid()))
                {
                    if(dataSnapshot.child(mAuth.getCurrentUser().getUid()).hasChild(idStory)) {
                        buttBookmark.setBackgroundResource(R.drawable.bookmark_t);
                    }
                }
                else
                {
                    buttBookmark.setBackgroundResource(R.drawable.bookmark_f);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        buttLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                processLike = true;

                if(processLike)
                {
                    databaseLike.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child(idStory).hasChild(mAuth.getCurrentUser().getUid()))
                            {
                                databaseLike.child(idStory).child(mAuth.getCurrentUser().getUid()).removeValue();
                                buttLike.setBackgroundResource(R.drawable.like_f);
                                processLike = false;
                            }
                            else
                            {
                                databaseLike.child(idStory).child(mAuth.getCurrentUser().getUid()).setValue("aaa");
                                buttLike.setBackgroundResource(R.drawable.like_t);

                                processLike = false;
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });

        buttBookmark.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                processBookmark = true;

                if(processBookmark)
                {
                    databaseBookmark.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if(dataSnapshot.child(idStory).hasChild(mAuth.getCurrentUser().getUid()))
                            {
                                databaseBookmark.child(mAuth.getCurrentUser().getUid()).child(idStory).removeValue();
                                buttBookmark.setBackgroundResource(R.drawable.bookmark_f);
                                processBookmark = false;
                            }
                            else
                            {
                                databaseBookmark.child(mAuth.getCurrentUser().getUid()).child(idStory).setValue("aaa");
                                buttBookmark.setBackgroundResource(R.drawable.bookmark_t);

                                processBookmark = false;
                            }
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
