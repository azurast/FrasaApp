package com.labill.frasaapp;

import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserListAdapter extends RecyclerView.Adapter implements Filterable {

    private static final String TAG = "UserAdapterLog";

    private OnItemClickListener listener;
    private List<User> userList;
    private List<User> userListFull;
    private FirebaseFirestore firebaseFirestore;
    private FirebaseUser firebaseUser;
    private FirebaseAuth mAuth;
    StorageReference references;

//    Map<String, Object> totalFollower;
//    Map<String, Object> totalUser;

    List<Map> totalUser;
    List<Map> totalFollower;

    QueryDocumentSnapshot totalU;

    public UserListAdapter(List<User> userList, OnItemClickListener onItemClickListener){
        this.userList = userList;
        userListFull = new ArrayList<>(userList);
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

        //Log.d(TAG, "final user id : "+userId);
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

    @Override
    public Filter getFilter() {
        return userFilter;
    }

    private Filter userFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<User> filteredList = new ArrayList<>();
            if(constraint == null || constraint.length() == 0){
                filteredList.addAll(userListFull);
            }else{
                String filterPattern = constraint.toString().toLowerCase().trim();
                for(User user : userListFull){
                    if(user.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(user);
                    }
                }
            }
            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            userList.clear();
            userList.addAll((List) results.values);
            notifyDataSetChanged();
        }
    };

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

            totalUser = new ArrayList();
            totalFollower = new ArrayList();
            isFollowing(user.getId(), btnFollowing);

            if(user.getId().equals(firebaseUser.getUid())) {
                btnFollowing.setVisibility(View.GONE);
            }

            firebaseFirestore.collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if(e != null){
                        Log.d(TAG, "Error : "+ e.getMessage());
                    }else{
                        totalUser.clear();
                        totalFollower.clear();
                        for(QueryDocumentSnapshot doc : queryDocumentSnapshots){
                            if(doc.getId().equals(firebaseUser.getUid())){
                                Map<String, Object> tempUser = (Map<String, Object>) doc.get("total");
                                totalUser.add(tempUser);
                                Log.d(TAG, "total user:"+totalUser);
                            }else if(doc.getId().equals(user.getId())){
                                Map<String, Object> tempFollower = (Map<String, Object>) doc.get("total");
                                totalFollower.add(tempFollower);
                                Log.d(TAG, "total follower:"+totalFollower);
                            }
                        }
                    }
                    //Log.d(TAG, "total list :"+totalList);
                    //totalList.add((Map) doc.get("total"));
                }
            });


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

                    // User
                    long uFollowing = (long) totalUser.get(0).get("following");
                    // Follower
                    long fFollowers =  (long) totalUser.get(0).get("followers");

                    Log.d(TAG, "uFollowing"+uFollowing);
                    Log.d(TAG, "fFollowers"+fFollowers);

                    // Belum di follow, mau follow
                    if(btnFollowing.getText().toString().equalsIgnoreCase("Follow")){
                        Log.d(TAG, "tulisan di button "+btnFollowing.getText().toString());
                        Log.d(TAG, "btn tulisan follow");

                        uFollowing++;
                        fFollowers++;

                        // tambah
                        userRef.update("total.following", uFollowing);
                        followerRef.update("total.followers", fFollowers);

                        userRef.update("following."+user.getId(), true);
                        followerRef.update("followers."+firebaseUser.getUid(), true);

                        // set text button jd following
                        btnFollowing.setText("Following");
                        btnFollowing.setBackgroundColor(Color.rgb(191,102, 52));
                        btnFollowing.setTextColor(Color.rgb(255,255,255));

                    }else{ // Sudah di follow, mau unfollow
                        Log.d(TAG, "tulisan di button "+btnFollowing.getText().toString());
                        Log.d(TAG, "btn tulisan following");

                        uFollowing--;
                        fFollowers--;

                        if(uFollowing<=0){
                            uFollowing = 0;
                        }

                        if(fFollowers<=0){
                            fFollowers =0;
                        }

                        // hapus
                        userRef.update("total.following", uFollowing);
                        followerRef.update("total.followers", fFollowers);

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