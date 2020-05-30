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
import com.squareup.picasso.Picasso;

import java.util.List;

import javax.annotation.Nullable;


public class CommentListAdapter extends RecyclerView.Adapter {
    private StoriesListAdapter.OnItemClickListener listener;

    private FirebaseFirestore firebaseFirestore;
    private DatabaseReference databaseComment, databaseContent;

    StorageReference references;

    public List<Comments> commentList;
    public String idComment;


    public CommentListAdapter(List<Comments> commentList, CommentListAdapter.OnItemClickListener onItemClickListener) {
        this.commentList = commentList;
        //this.listener = onItemClickListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item, parent, false);
        return new CommentListAdapter.ListViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((CommentListAdapter.ListViewHolder) holder).bindView(position);

        //isLike(storiesList.get().toString());
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        private TextView commenter, commentContent;
        private int position;

        public ListViewHolder(View itemView, StoriesListAdapter.OnItemClickListener onItemClickListener){
            super(itemView);

            commenter = itemView.findViewById(R.id.commenter);


            firebaseFirestore = FirebaseFirestore.getInstance();
            references = FirebaseStorage.getInstance().getReference();
            //itemView.setOnClickListener(this);
        }


        // Bind with data from firebase
        public void bindView(int position){
            final String idAuthor = commentList.get(position).getIdAuthor();
            final String idStory = commentList.get(position).getIdStory();
            String content = commentList.get(position).getComment();

            databaseComment = FirebaseDatabase.getInstance().getReference().child("comments");

            databaseComment.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    // get total available quest
                    if(dataSnapshot.hasChild(idStory))
                    {
                        /*databaseContent = FirebaseDatabase.getInstance().getReference().child("comments").child(idStory);
                        databaseContent.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                int size = (int) dataSnapshot.child(idStory).getChildrenCount();
                                for (String id : size) {
                                    if (doc.getId().equals(id)) {
                                        User user = doc.toObject(User.class);
                                        user.setId(doc.getId());
                                        followingList.add(user);
                                        userListAdapter.notifyDataSetChanged();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });*/
                    }
                    else
                    {

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }

//        @Override
//        public void onClick(View v) {
//            onItemClickListener.onItemClick(getAdapterPosition(), tempName);
//        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position, String name);
    }
    public void setOnItemClickListener(StoriesListAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
