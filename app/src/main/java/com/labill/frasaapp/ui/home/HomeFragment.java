package com.labill.frasaapp.ui.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.labill.frasaapp.MainActivity;
import com.labill.frasaapp.R;
import com.labill.frasaapp.Stories;
import com.labill.frasaapp.StoriesListAdapter;
import com.labill.frasaapp.UserListAdapter;
import com.labill.frasaapp.ui.reading_mode.ReadingActivity;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment implements StoriesListAdapter.OnItemClickListener {

    // For checking logs regarded firebase
    private static final String TAG = "HomeFragmentLog";
    private String idStory;

    private HomeViewModel homeViewModel;

    // Firebase & List Adapter
    private FirebaseFirestore firebaseFirestore;
    StorageReference references;
    private RecyclerView mainList;
    private StoriesListAdapter storiesListAdapter;
    private List<Stories> storiesList;
    private List<Stories> searchList;
    private Context mContext = this.getContext();
    private String idAuthor;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //homeViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        setHasOptionsMenu(true);
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        storiesList = new ArrayList<>();
        mainList = (RecyclerView) view.findViewById(R.id.rvPost);
        storiesListAdapter = new StoriesListAdapter(storiesList, this);
        firebaseFirestore = FirebaseFirestore.getInstance();
        Log.d(TAG, "Recycler View Main List: "+mainList);

        firebaseFirestore.collection("stories").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@javax.annotation.Nullable QuerySnapshot queryDocumentSnapshots, @javax.annotation.Nullable FirebaseFirestoreException e) {
                // Check if have permission
                if(e != null){
                    Log.d(TAG, "Error : "+ e.getMessage());
                }
                for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
//                    Log.d(TAG,"Title : "+doc.getDocument().getString("title"));
//                    Log.d(TAG, "doc.getType :"+doc.getType());
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
                        storiesListAdapter.notifyDataSetChanged();
                    }
                }
            }
        });

        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        mainList.setLayoutManager(layoutManager);
        mainList.setAdapter(storiesListAdapter);

        return view;
        //return null;
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        Log.d(TAG, "search view created");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTexSubmit");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.d(TAG, "newText : "+newText);
                //Log.d(TAG, "cek dlm line : "+storiesListAdapter.getFilter().filter(newText));
                storiesListAdapter.getFilter().filter(newText);
                Log.d(TAG, "onQueryTexChange");
                return true;
            }
        });
    }

    private void searchStories (String s){
        Query query = FirebaseDatabase.getInstance().getReference("stories").orderByChild("name").startAt(s).endAt(s+"\uf8ff");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                searchList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Stories story = snapshot.getValue(Stories.class);
                    searchList.add(story);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onItemClick(int position, String name) {
        storiesList.get(position);
        String title = storiesList.get(position).getTitle();
        references = FirebaseStorage.getInstance().getReference();

        Intent intent = new Intent(getActivity(), ReadingActivity.class);
        // kurang pass photo
//        Log.d(TAG, "name :"+storiesList.get(position).getName());
        intent.putExtra("title", title);
        intent.putExtra("name", name);
        intent.putExtra("content", storiesList.get(position).getContent());
        //intent.putExtra("photo", idStory);

        startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle oldInstanceState)
    {
        super.onSaveInstanceState(oldInstanceState);
        oldInstanceState.clear();
    }
}
