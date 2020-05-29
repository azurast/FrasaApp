package com.labill.frasaapp.ui.bookmarks;

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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.labill.frasaapp.R;
import com.labill.frasaapp.Stories;
import com.labill.frasaapp.StoriesListAdapter;
import com.labill.frasaapp.ui.reading_mode.ReadingActivity;

public class BookmarksFragment extends Fragment implements StoriesListAdapter.OnItemClickListener {
    /*
    * Pertama berarti harus cek id user yg skrg
    * Dari instansi user berarti get array of strings yang isinya id stories dalam
    * variable bookmark
    * kemudian tampilkan stories-stories tersebut.
    * */
    private static final String TAG = "BookmarksFragmentLog";
    private FirebaseFirestore firebaseFirestore;
    StorageReference references;
    private FirebaseAuth mAuth;
    private RecyclerView mainList;
    private StoriesListAdapter storiesListAdapter;
    private List<Stories> storiesList;
    private List<String> bookmarkList;

    // Realtime
    private DatabaseReference databaseBookmark;

    public BookmarksFragment() {
        // Required empty public constructor
    }


    public static BookmarksFragment newInstance(String param1, String param2) {
        BookmarksFragment fragment = new BookmarksFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bookmarks, container, false);

        storiesList = new ArrayList<>();
        mainList = (RecyclerView) view.findViewById(R.id.rvPost);
        bookmarkList = new ArrayList<>();
        storiesListAdapter = new StoriesListAdapter(storiesList, this);
        firebaseFirestore = FirebaseFirestore.getInstance();

        references = FirebaseStorage.getInstance().getReference();
        firebaseFirestore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        databaseBookmark = FirebaseDatabase.getInstance().getReference().child("bookmarks");

        // Get list of story ids yg masuk ke dalam bookmark
        databaseBookmark.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.hasChild(mAuth.getCurrentUser().getUid())){
                    collectBookmarks((Map<String, Object>) dataSnapshot.getValue());
                }
                getBookmarkList();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        ((LinearLayoutManager) layoutManager).setOrientation(LinearLayoutManager.VERTICAL);
        mainList.setLayoutManager(layoutManager);
        mainList.setAdapter(storiesListAdapter);
        return view;

    }

    private void collectBookmarks(Map<String, Object> user){
        for(Map.Entry<String, Object> entry : user.entrySet()){
            Map singleUser = (Map) entry.getValue();
            for(Object id : singleUser.keySet()){
                bookmarkList.add(id.toString());
            }
            Log.d(TAG, "bookmark list :" +bookmarkList.toString());
        }
    }

    private void getBookmarkList(){
        // add only stories that are in the bookmark list
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
                        Log.d(TAG, "bookmark list in firbase firestore:" +bookmarkList.toString());
                        for(String id : bookmarkList){
                            if (doc.getDocument().getId().equals(id)){
                                Stories stories = doc.getDocument().toObject(Stories.class);
                                Log.d(TAG, "stories : "+stories);
                                storiesList.add(stories);
                                storiesListAdapter.notifyDataSetChanged();
                            }
                        }
                        //String storyId = doc.getDocument().getId();
                    }
                }
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
        intent.putExtra("title", storiesList.get(position).getTitle());
        intent.putExtra("name", name);
        intent.putExtra("content", storiesList.get(position).getContent());
        //intent.putExtra("photo", idStory);

        startActivity(intent);
    }
}
