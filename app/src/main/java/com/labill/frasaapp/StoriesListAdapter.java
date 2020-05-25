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
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.labill.frasaapp.R;
import com.labill.frasaapp.Stories;
import com.labill.frasaapp.ui.home.HomeFragment;
import com.labill.frasaapp.ui.home.HomeViewModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class StoriesListAdapter extends RecyclerView.Adapter {

    private FirebaseFirestore firebaseFirestore;
    private FirebaseAuth mAuth;
    StorageReference references;

    public List<Stories> storiesList;
    public String idStory;
    private boolean processLike = false;
    private boolean processBookmark = false;
    private DatabaseReference databaseLike;

    public StoriesListAdapter(List<Stories> storiesList) {
        this.storiesList = storiesList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
       return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ListViewHolder) holder).bindView(position);

        //isLike(storiesList.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return storiesList.size();
    }

    /*private void isLike(String postId)
    {
        final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference likeReference = FirebaseDatabase.getInstance().getReference()
                .child("likes").child(postId);

        likeReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(firebaseUser.getUid()).exists()){
                    btnLike.setBackgroundResource(R.drawable.like_t);
                }
                else{
                    btnLike.setBackgroundResource(R.drawable.like_f);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void numOfLike (final TextView numOfLikes, String postid)
    {
        DatabaseReference numLike = FirebaseDatabase.getInstance().getReference().child("likes")
                .child(postid);

        numLike.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                numOfLikes.setText(dataSnapshot.getChildrenCount()+"");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }*/


    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView postImage;
        private CircleImageView profileImage;
        private TextView postTitle, userName, postPreview, numOfLikes;
        private Button btnLike, btnBookmark;

        public ListViewHolder(View itemView){
            super(itemView);

            postImage = (ImageView) itemView.findViewById(R.id.post_image);
            profileImage = (CircleImageView) itemView.findViewById(R.id.profile_image);
            postTitle = (TextView) itemView.findViewById(R.id.post_title);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            postPreview = (TextView) itemView.findViewById(R.id.post_preview);
            numOfLikes = (TextView) itemView.findViewById(R.id.number_of_likes);
            btnBookmark = (Button) itemView.findViewById(R.id.btn_bookmark);
            btnLike = (Button) itemView.findViewById(R.id.btn_like);
            itemView.setOnClickListener(this);

            mAuth = FirebaseAuth.getInstance();
            firebaseFirestore = FirebaseFirestore.getInstance();
            references = FirebaseStorage.getInstance().getReference();
            databaseLike = FirebaseDatabase.getInstance().getReference().child("likes");
            databaseLike.keepSynced(true);
            btnLike.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    processLike = true;

                        databaseLike.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(processLike) {
                                    if (dataSnapshot.child(idStory).hasChild(mAuth.getCurrentUser().getUid())) {
                                        databaseLike.child(idStory).child(mAuth.getCurrentUser().getUid()).removeValue();
                                        btnLike.setBackgroundResource(R.drawable.like_f);
                                        processLike = false;
                                    } else {

                                        databaseLike.child(idStory).child(mAuth.getCurrentUser().getUid()).setValue("aa");
                                        Log.d("inilike", "likedNih");
                                        btnLike.setBackgroundResource(R.drawable.like_t);
                                        processLike = false;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                }
            });


        }

        // Bind with data from firebase
        public void bindView(int position){
            String id = storiesList.get(position).getAuthor();
            String title = storiesList.get(position).getTitle();
            postTitle.setText(storiesList.get(position).getTitle());
            postPreview.setText(storiesList.get(position).getContent());
            //Log.d("TAG", "stories length : "+storiesList.size());

            DocumentReference ref = firebaseFirestore.collection("users").document(id);
            StorageReference profileRef = references.child("users/"+id+"/user.jpg");

            ref.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if(task.isSuccessful()){
                        DocumentSnapshot document = task.getResult();
                        if (document.exists()) {
                           // Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            userName.setText(document.getString("name"));
                        } else {
                            //Log.d(TAG, "No such document");
                        }
                    } else {
                        //Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });

            profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(profileImage);
                }
            });

            //Retrieve story's id
            Query loadStory = firebaseFirestore.collection("stories").
                    whereEqualTo("title", title);

            loadStory.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot documentSnapshots, @Nullable FirebaseFirestoreException e) {
                    for (DocumentChange doc : documentSnapshots.getDocumentChanges()){

                        if (doc.getType() == DocumentChange.Type.ADDED){

                            idStory = doc.getDocument().getId();
                            Log.d(postTitle.getText().toString(), idStory);
                            StorageReference storyRef = references.child("stories/"+idStory+"/stories.jpg");
                            Log.d("link", storyRef.toString());

                            storyRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Picasso.get().load(uri).into(postImage);
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


        @Override
        public void onClick(View v) {

        }
    }
}
