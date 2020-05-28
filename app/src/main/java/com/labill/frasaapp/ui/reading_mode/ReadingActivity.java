package com.labill.frasaapp.ui.reading_mode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.labill.frasaapp.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class ReadingActivity extends AppCompatActivity {

    private static final String TAG = "ReadingActivityLog";
    FirebaseFirestore db;
    StorageReference references;

    private TextView tvTitle, tvAuthor, tvContent;
    private FirebaseFirestore firebaseFirestore;
    private ImageView storyImg;
    private String idStory;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        references = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();

        tvTitle = findViewById(R.id.tvPostTitle);
        tvAuthor = findViewById(R.id.tvPostAuthor);
        tvContent = findViewById(R.id.tvPostContent);
        storyImg = findViewById(R.id.imageView2);


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

                        StorageReference storyRef = references.child("stories/"+idStory+"/stories.jpg");
                        Log.d("link", storyRef.toString());

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

    }
}
