package com.labill.frasaapp.ui.reading_mode;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.labill.frasaapp.R;
import com.labill.frasaapp.ui.profile.SeeProfile;

import javax.annotation.Nullable;

import static java.lang.Integer.valueOf;

public class ReadingActivity extends AppCompatActivity {

    private static final String TAG = "ReadingActivityLog";
    FirebaseFirestore firebaseFirestore;
    StorageReference references;
    private FirebaseAuth mAuth;
    private boolean processLike = false;
    private boolean processBookmark = false;
    private DatabaseReference databaseLike, databaseBookmark;
    ConstraintLayout colorLayout;

    private TextView tvTitle, tvAuthor, tvContent, tvGenre, numOfLike;
    private ImageView storyImg;
    private String idStory, idAuthor, photo;
    private Integer color;
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

        colorLayout = findViewById(R.id.border);
        tvTitle = findViewById(R.id.tvPostTitle);
        tvAuthor = findViewById(R.id.tvPostAuthor);
        tvContent = findViewById(R.id.tvPostContent);
        tvGenre = findViewById(R.id.tvGenre);
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
        Log.d("tvtitle", tvTitle.getText().toString());
        tvAuthor.setText(recvAuthor);
        tvContent.setText(recvContent);

        //fetch sticker
        com.google.firebase.firestore.Query loadSticker = firebaseFirestore.collection("stickers").
                whereEqualTo("atitle", recvTitle);

        loadSticker.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {

                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    String stickers = doc.getDocument().get("sticker").toString();
                    float xPos = Float.parseFloat(doc.getDocument().get("xPos").toString());
                    float yPos = Float.parseFloat(doc.getDocument().get("yPos").toString());

                    int height1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
                    int width1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());

                    ConstraintLayout container1 = (ConstraintLayout) findViewById(R.id.border);

                    ImageView imageView = new ImageView(ReadingActivity.this);
                    imageView.setId(View.generateViewId());
                    Bitmap convert1 = stringToBitmap(stickers);
                    imageView.setImageBitmap(convert1);
                    imageView.setX(xPos);
                    imageView.setY(yPos);
                    container1.addView(imageView);
                    imageView.getLayoutParams().width = width1;
                    imageView.getLayoutParams().height = height1;
                    imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                }

            }
        });

        //Retrieve author's id
        Query loadAuthor = firebaseFirestore.collection("users").
                whereEqualTo("name", recvAuthor);

        loadAuthor.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()){

                    if (doc.getType() == DocumentChange.Type.ADDED){

                        idAuthor = doc.getDocument().getId();
                        Log.d("author", idAuthor);

                    }
                    else
                    {
                        Log.d("ini", "gagal");
                    }
                }

            }
        });

        //Retrieve story's id
        com.google.firebase.firestore.Query loadStory = firebaseFirestore.collection("stories").
                whereEqualTo("title", recvTitle);

        // Image
        loadStory.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot documentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                Log.d("coba", "masuk");
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()){

                    if (doc.getType() == DocumentChange.Type.ADDED){

                        idStory = doc.getDocument().getId();
                        Log.d("cekkkkk", idStory);

                        String img = doc.getDocument().get("photo").toString();

                        if(img == "") {

                        }else{
                            Bitmap pic = stringToBitmap(img);
                            storyImg.setImageBitmap(pic);
                            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 221, getResources().getDisplayMetrics());
                            int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 355, getResources().getDisplayMetrics());
                            storyImg.getLayoutParams().height = height;
                            storyImg.getLayoutParams().width = width;
                            storyImg.setScaleType(ImageView.ScaleType.CENTER_CROP);
                        }

                        Integer color = valueOf(doc.getDocument().get("color").toString());
                        colorLayout.setBackgroundColor(color);
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
                            if(dataSnapshot.child(mAuth.getCurrentUser().getUid()).hasChild(idStory))
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

        //klik nama pengarang
        tvAuthor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReadingActivity.this, SeeProfile.class);
                intent.putExtra("id", idAuthor);
                startActivity(intent);
            }
        });

        buttComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReadingActivity.this, CommentActivity.class);
                intent.putExtra("id", idStory);
                startActivity(intent);
            }
        });

    }

    public final static Bitmap stringToBitmap(String in){
        byte[] bytes = Base64.decode(in, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
