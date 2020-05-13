package com.labill.frasaapp.ui.profile;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.labill.frasaapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFollowerFragment extends Fragment {

    public ProfileFollowerFragment() {
        // Required empty public constructor
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
