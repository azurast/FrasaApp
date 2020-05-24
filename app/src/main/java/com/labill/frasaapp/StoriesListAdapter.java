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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.labill.frasaapp.R;
import com.labill.frasaapp.Stories;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class StoriesListAdapter extends RecyclerView.Adapter {

    private FirebaseFirestore firebaseFirestore;
    StorageReference references;
    public List<Stories> storiesList;

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
    }

    @Override
    public int getItemCount() {
        return storiesList.size();
    }

    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView postImage;
        private CircleImageView profileImage;
        private TextView postTitle, userName, postPreview, numOfLikes;
        private Button btnBookmark, btnLike;

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

            firebaseFirestore = FirebaseFirestore.getInstance();
            references = FirebaseStorage.getInstance().getReference();



        }

        // Bind with data from firebase
        public void bindView(int position){
            String id = storiesList.get(position).getAuthor();
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
                            Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                            userName.setText(document.getString("name"));
                        } else {
                            Log.d(TAG, "No such document");
                        }
                    } else {
                        Log.d(TAG, "get failed with ", task.getException());
                    }
                }
            });

            profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(profileImage);
                }
            });

        }

        @Override
        public void onClick(View v) {

        }
    }
}
