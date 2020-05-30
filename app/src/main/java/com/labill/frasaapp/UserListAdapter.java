package com.labill.frasaapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.core.content.ContextCompat;
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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class UserListAdapter extends RecyclerView.Adapter{

    private static final String TAG = "UserAdapterLog";

    private OnItemClickListener listener;
    private List<User> userList;

    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    StorageReference references;

    public UserListAdapter(List<User> userList, OnItemClickListener onItemClickListener){
        this.userList = userList;
        this.listener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.follow_item, parent, false);
        return new ListViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ListViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    // Check if user is following that person
    private void isFollowing(final String userId, final Button followBtn){

        Log.d(TAG, "final user id : "+userId);
        //DatabaseReference isFollowRef = FirebaseDatabase.getInstance().getReference().child("users").child(mAuth.getUid()).child("following");
        DocumentReference followingReference = firebaseFirestore.collection("users").document(mAuth.getUid());
        followingReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot!=null){
                        Map followList = (Map) documentSnapshot.get("following");
                        Log.d(TAG, "contains user id ? "+followList.containsKey(userId));
                        if(followList.containsKey(userId)){
                            followBtn.setText("Following");
                        }else{
                            followBtn.setText("Follow");
                            followBtn.setBackgroundColor(Color.rgb(229,229,229));
                            followBtn.setTextColor(Color.rgb(191,102, 52));
                        }
                    }
                }
            }
        });
    }

    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        OnItemClickListener onItemClickListener;
        private CircleImageView profileImage;
        private TextView userName;
        private Button btnFollowing;
        private String id;

        // Assign
        public ListViewHolder(View itemView, OnItemClickListener onItemClickListener){
            super(itemView);

            this.onItemClickListener = onItemClickListener;
            profileImage = itemView.findViewById(R.id.profile_image);
            userName = itemView.findViewById(R.id.user_name);
            btnFollowing = itemView.findViewById(R.id.btn_following);

            mAuth = FirebaseAuth.getInstance();
            firebaseFirestore = FirebaseFirestore.getInstance();
            references = FirebaseStorage.getInstance().getReference();

            itemView.setOnClickListener(this);
        }

        // Bind
        public void bindView(final int position){

            // User who is logged in
            firebaseUser = mAuth.getCurrentUser();
            // User in list
            final User user = userList.get(position);
            //Log.d(TAG, "bind user id : "+userList.get(position).getId());
            id = userList.get(position).getId();
            //Log.d(TAG, "bind user name : "+userList.get(position).getName());
            userName.setText(user.getName());
            btnFollowing.setVisibility(View.VISIBLE);

            isFollowing(user.getId(), btnFollowing);

            if(user.getId().equals(firebaseUser.getUid())) {
                btnFollowing.setVisibility(View.GONE);
            }

            //When following button is clicked
            btnFollowing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "following btn is clicked :"+btnFollowing);
                    Log.d(TAG, "user dlm on click :"+user);

                    // Punya user
                    final DocumentReference userRef= firebaseFirestore.collection("users").document(firebaseUser.getUid());
                    // Punya follower
                    final DocumentReference followerRef = firebaseFirestore.collection("users").document(user.getId());

                    // Belum di follow, mau follow
                    if(btnFollowing.getText().toString().equalsIgnoreCase("Follow")){
                        Log.d(TAG, "tulisan di button "+btnFollowing.getText().toString());
                        Log.d(TAG, "btn tulisan follow");

                        // tambah
                        userRef.update("total.following", 1);
                        followerRef.update("total.followers", 1);

                        userRef.update("following."+user.getId(), true);
                        followerRef.update("followers."+firebaseUser.getUid(), true);

                        // set text button jd following
                        btnFollowing.setText("Following");
                        btnFollowing.setBackgroundColor(Color.rgb(191,102, 52));
                        btnFollowing.setTextColor(Color.rgb(255,255,255));

                    }else{ // Sudah di follow, mau unfollow
                        Log.d(TAG, "tulisan di button "+btnFollowing.getText().toString());
                        Log.d(TAG, "btn tulisan following");
                        // hapus
                        userRef.update("total.following", 1);
                        followerRef.update("total.followers", 1);

                        userRef.update("following."+user.getId(), FieldValue.delete());
                        followerRef.update("followers."+firebaseUser.getUid(), FieldValue.delete());

                        btnFollowing.setText("Follow");
                        btnFollowing.setBackgroundColor(Color.rgb(229,229,229));
                        btnFollowing.setTextColor(Color.rgb(191,102, 52));
                    }
                }
            });

            StorageReference profileRef = references.child("users/"+userList.get(position).getId()+"/user.jpg");
            profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(profileImage);
                }
            });
        }

        @Override
        public void onClick(View v) {
            onItemClickListener.onItemClick(getAdapterPosition(), id);
        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, String id);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}