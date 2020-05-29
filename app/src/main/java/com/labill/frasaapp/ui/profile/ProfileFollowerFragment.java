package com.labill.frasaapp.ui.profile;

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
import com.labill.frasaapp.User;
import com.labill.frasaapp.UserListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFollowerFragment extends Fragment {

    // For checking logs regarded firebase
    private static final String TAG = "ProfileFollowingLog";

    // Declare Needed Variables
    private FirebaseFirestore firebaseFirestore;
    private List<User> followingList;
    private List<String> idList;
    private RecyclerView recyclerView;
    private UserListAdapter userListAdapter;
    private String currentUserId = FirebaseAuth.getInstance().getUid();

    public static ProfileFollowerFragment newInstance(String param1, String param2) {
        ProfileStoriesFragment fragment = new ProfileStoriesFragment();
        Bundle args = new Bundle();
        return null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // List view yang mau di inflate
        View view = inflater.inflate(R.layout.fragment_profile_following, container, false);

        // Data yang mau di display
        String[] followers = {"Azura", "Cyntia", "Aaron"};

        // Find list view
        ListView followingLv = (ListView) view.findViewById(R.id.followingList);

        // Array adapter
        ArrayAdapter<String> followingListViewAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                followers
        );

        // Draw on screen
        followingLv.setAdapter(followingListViewAdapter);

        return view;
    }
}
