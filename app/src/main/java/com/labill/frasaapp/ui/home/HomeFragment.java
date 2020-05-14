package com.labill.frasaapp.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.labill.frasaapp.R;
import com.labill.frasaapp.Stories;
import com.labill.frasaapp.database.constructorClass.User;

import java.util.ArrayList;
import java.util.List;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class HomeFragment extends Fragment {

    // For checking logs regarded firebase
    private static final String TAG = "FirebaseLog";

    private HomeViewModel homeViewModel;

    // Firebase & List Adapter
    private FirebaseFirestore firebaseFirestore;
    private RecyclerView mainList;
    private ListAdapter listAdapter;
    private List<Stories> storiesList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        storiesList = new ArrayList<>();
        //listAdapter = new ListAdapter(storiesList);
        //mainList = (RecyclerView) mainList.findViewById(R.id.rvPost);
        firebaseFirestore = FirebaseFirestore.getInstance();

        firebaseFirestore.collection("stories").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                // Check if have permission
                if(e != null){
                    Log.d(TAG, "Error : "+ e.getMessage());
                }
                for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                    Log.d(TAG,"Title : "+doc.getDocument().getString("title"));
                    Log.d(TAG, "doc.getType :"+doc.getType());
                    if(doc.getType() == DocumentChange.Type.ADDED){
                        /* IMPORTANT!
                        * to be able to retrieve the data,
                        * the attribute names in class Stories
                        * must match the ones in the database
                        * */
                        String storyId = doc.getDocument().getId();
                        Stories stories = doc.getDocument().toObject(Stories.class);
                        Log.d(TAG, "stories : "+stories);
                        storiesList.add(stories);
                        listAdapter.notifyDataSetChanged();
                    }
                }
            }
        });
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        mainList = (RecyclerView) view.findViewById(R.id.rvPost);
        listAdapter = new ListAdapter(storiesList);
        mainList.setAdapter(listAdapter);

        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        mainList.setLayoutManager(layoutManager);

        return view;
    }
}
