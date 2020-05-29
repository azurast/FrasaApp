package com.labill.frasaapp.ui.profile;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.labill.frasaapp.R;
import com.labill.frasaapp.Stories;
import com.labill.frasaapp.StoriesListAdapter;
import com.labill.frasaapp.ui.reading_mode.ReadingActivity;

import java.util.ArrayList;
import java.util.List;

public class SeeStoriesFragment extends Fragment implements StoriesListAdapter.OnItemClickListener {

    // For checking logs regarded firebase
    private static final String TAG = "ProfileStoriesLog";

    // Firebase & List Adapter
    private FirebaseFirestore firebaseFirestore;
    private String currentUserId;
    private List<Stories> storiesList;
    private RecyclerView recyclerView;
    private StoriesListAdapter storiesListAdapter;
    private String userName;
    private Bundle bundle;

    public static SeeStoriesFragment newInstance(String param1, String param2) {
        SeeStoriesFragment fragment = new SeeStoriesFragment();
        Bundle args = new Bundle();
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // The view we want to pass the data on
        View view = inflater.inflate(R.layout.fragment_profile_stories, container, false);
        // Array of user's stories
        storiesList = new ArrayList<>();
        // Get Recycler View
        recyclerView = (RecyclerView) view.findViewById(R.id.rvPost);
        // Get List Adapter to hold our data
        storiesListAdapter = new StoriesListAdapter(storiesList, this);

        // Get instance of firebase
        firebaseFirestore = FirebaseFirestore.getInstance();

        bundle = this.getArguments();
        currentUserId = bundle.getString("id");

        Log.d(TAG, "current user id :"+currentUserId);
        // Buat Akses Data user, in this case username
        //Log.d(TAG, "CurrentUserId : "+ currentUserId.toString());
        final DocumentReference documentReference = firebaseFirestore.collection("users").document(currentUserId.toString());

        // Query to Retrieve Stories that the current user writes
        firebaseFirestore.collection("stories").whereEqualTo("author", currentUserId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "Task Successfull");
                    for(QueryDocumentSnapshot documentSnapshot : task.getResult()){
                        Log.d(TAG, "document id :"+documentSnapshot.getId());
                        Stories story = documentSnapshot.toObject(Stories.class);
                        storiesList.add(story);
                        storiesListAdapter.notifyDataSetChanged();
                        Log.d(TAG, "Query returns : "+documentSnapshot.getData());
                    }
                }else{
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
            }
        });

        //Pass to RecyclerView
        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(storiesListAdapter);

        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onItemClick(int position, String name) {
        storiesList.get(position);
        Intent intent = new Intent(getActivity(), ReadingActivity.class);
        // kurang pass photo
        intent.putExtra("title", storiesList.get(position).getTitle());
        intent.putExtra("name", name);
        intent.putExtra("content", storiesList.get(position).getContent());
        startActivity(intent);
    }
}
