package com.labill.frasaapp.ui.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.labill.frasaapp.R;
import com.labill.frasaapp.database.constructorClass.User;

import java.sql.Array;
import java.util.ArrayList;

public class UserListAdapter extends ArrayAdapter<User> {
    private static final String TAG = "UserListAdapter";
    private Context mContext;
    int mResource;

    public UserListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<User> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        String name = getItem(position).getName();
        String photo = getItem(position).getPhoto();

        User user = new User(name, photo);
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        TextView tvName = (TextView) convertView.findViewById(R.id.user_name);
        TextView tvPhoto = (TextView) convertView.findViewById(R.id.profile_image);

        tvName.setText(name);
        tvPhoto.setText(photo);

        return convertView;
    }
}
