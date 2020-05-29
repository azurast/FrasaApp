package com.labill.frasaapp.ui.profile;

import android.content.Intent;
import android.hardware.usb.UsbRequest;
import android.icu.lang.UScript;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;
import com.labill.frasaapp.R;
import com.labill.frasaapp.StoriesListAdapter;
import com.labill.frasaapp.User;
import com.labill.frasaapp.UserListAdapter;
import com.labill.frasaapp.ui.reading_mode.ReadingActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class SeeFollowingFragment extends Fragment implements UserListAdapter.OnItemClickListener {

    // For checking logs regarded firebase
    private static final String TAG = "ProfileFollowingLog";

    // Declare Needed Variables
    private FirebaseFirestore firebaseFirestore;
    private List<User> followingList;
    private List<String> idList;
    private RecyclerView recyclerView;
    private UserListAdapter userListAdapter;
    private String currentUserId;
    private Bundle bundle;

    public static SeeFollowingFragment  newInstance(String param1, String param2) {
        SeeFollowingFragment  fragment = new SeeFollowingFragment ();
        Bundle args = new Bundle();
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_profile_following, container, false);
        followingList = new ArrayList<>();
        idList = new ArrayList<>();
        recyclerView = (RecyclerView) view.findViewById(R.id.rvFollowing);
        userListAdapter = new UserListAdapter(followingList, this);
        Log.d(TAG, "Recycler View : "+recyclerView);
        firebaseFirestore = FirebaseFirestore.getInstance();

        bundle = this.getArguments();
        currentUserId = bundle.getString("id");

        // Get Ids of People we follow
        firebaseFirestore.collection("users").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot != null){
                        Map temp = (Map) documentSnapshot.get("following");
                        Log.d(TAG, "Temp : "+temp);
                        for(Object key : temp.keySet()){
                            idList.add((String)key);
                        }
                    }
                }
                Log.d(TAG, "Id List : "+idList);
            }
        });

        // Show the People we follow
        CollectionReference showRef = firebaseFirestore.collection("users");
        showRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    QuerySnapshot querySnapshot = task.getResult();
                    if(querySnapshot != null){
                        for(QueryDocumentSnapshot doc : querySnapshot) {
                            for (String id : idList) {
                                if (doc.getId().equals(id)) {
                                    User user = doc.toObject(User.class);
                                    user.setId(doc.getId());
                                    followingList.add(user);
                                    userListAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                }
                Log.d(TAG, "following list : "+followingList.toString());
            }
        });

        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(userListAdapter);

        return view;
    }

    @Override
    public void onItemClick(int position, String id) {
        Intent intent = new Intent(getActivity(), SeeProfile.class);
        Log.d(TAG, "id on click : "+id);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}
