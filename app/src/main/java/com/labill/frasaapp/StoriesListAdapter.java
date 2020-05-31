package com.labill.frasaapp;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class StoriesListAdapter extends RecyclerView.Adapter {

    private OnItemClickListener listener;

    private FirebaseFirestore firebaseFirestore;

    StorageReference references;

    public List<Stories> storiesList;
    public String idStory;


    public StoriesListAdapter(List<Stories> storiesList, OnItemClickListener onItemClickListener) {
        this.storiesList = storiesList;
        this.listener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
       return new ListViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ListViewHolder) holder).bindView(position);

        //isLike(storiesList.get().toString());
    }

    @Override
    public int getItemCount() {
        return storiesList.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView postImage;
        private CircleImageView profileImage;
        private TextView postTitle, userName, postPreview, numOfLikes;
        private Button btnLike, btnBookmark;
        OnItemClickListener onItemClickListener;
        private String tempName, tempName2;
        private int position;

        public ListViewHolder(View itemView, OnItemClickListener onItemClickListener){
            super(itemView);

            this.onItemClickListener = onItemClickListener;
            postImage = (ImageView) itemView.findViewById(R.id.post_image);
            profileImage = (CircleImageView) itemView.findViewById(R.id.profile_image);
            postTitle = (TextView) itemView.findViewById(R.id.commenter);
            userName = (TextView) itemView.findViewById(R.id.user_name);
            postPreview = (TextView) itemView.findViewById(R.id.cmnt);
            itemView.setOnClickListener(this);

            firebaseFirestore = FirebaseFirestore.getInstance();
            references = FirebaseStorage.getInstance().getReference();
            itemView.setOnClickListener(this);
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
                            tempName = document.getString("name");
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

            // Image
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
            onItemClickListener.onItemClick(getAdapterPosition(), tempName);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, String name);
    }
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
