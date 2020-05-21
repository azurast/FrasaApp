package com.labill.frasaapp.ui.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.labill.frasaapp.R;
import com.labill.frasaapp.database.constructorClass.User;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFollowingFragment extends Fragment {

    public ProfileFollowingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // List view yang mau di inflate
        View view = inflater.inflate(R.layout.fragment_profile_following, container, false);

        // Data yang mau di display
        String[] followings = {"Azura", "Cyntia", "Aaron"};

        // Test pake class user
//        com.labill.frasaapp.User user1 = new com.labill.frasaapp.User("Azura", "photo");
//        com.labill.frasaapp.User user2 = new com.labill.frasaapp.User("Cyntia", "photo");
//        com.labill.frasaapp.User user3= new com.labill.frasaapp.User("Aaron", "photo");
//        ArrayList<com.labill.frasaapp.User> following_list = new ArrayList<com.labill.frasaapp.User>();
//        following_list.add(user1);
//        following_list.add(user2);
//        following_list.add(user3);

        // Find list view
        ListView followingLv = (ListView) view.findViewById(R.id.followingList);

        // Array adapter
        ArrayAdapter<String> followingListViewAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.simple_list_item_1,
                followings
        );

        // Draw on screen
        followingLv.setAdapter(followingListViewAdapter);

        return view;
    }
}
