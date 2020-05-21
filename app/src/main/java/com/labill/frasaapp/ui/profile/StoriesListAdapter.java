package com.labill.frasaapp.ui.profile;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.labill.frasaapp.Stories;

import java.util.List;

public class StoriesListAdapter extends RecyclerView.Adapter {

    public List<Stories> storiesList;

    public StoriesListAdapter(List<Stories> storiesList) {

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
