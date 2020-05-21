package com.labill.frasaapp.ui.bookmarks;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.labill.frasaapp.R;

public class BookmarksFragment extends Fragment {

    /*
    * Pertama berarti harus cek id user yg skrg
    * Dari instansi user berarti get array of strings yang isinya id stories dalam
    * variable bookmark
    * kemudian tampilkan stories-stories tersebut.
    * */

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bookmarks, container, false);
    }
}
