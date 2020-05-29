package com.labill.frasaapp.ui.write;

<<<<<<< Updated upstream
=======
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
>>>>>>> Stashed changes
import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

<<<<<<< Updated upstream
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.labill.frasaapp.R;

public class write_mode extends Fragment {

    private WriteModeViewModel mViewModel;
=======
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.labill.frasaapp.R;

import static android.widget.TextView.BufferType.SPANNABLE;

public class write_mode extends Fragment {

    private WriteModeViewModel mViewModel;
    public EditText dwEdit;
    public TextView prev;
    public TextView publish;
    public TextView save;
    public TextView title;
>>>>>>> Stashed changes

    public static write_mode newInstance() {
        return new write_mode();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
<<<<<<< Updated upstream
        return inflater.inflate(R.layout.fragment_write_mode, container, false);
=======
        View view = inflater.inflate(R.layout.fragment_write_mode, container, false);
        prev = view.findViewById(R.id.preview);
        dwEdit = view.findViewById(R.id.write_input);
        title = view.findViewById(R.id.choose_layo);
        publish = view.findViewById(R.id.publish);
        save = view.findViewById(R.id.save);

        final String genre  = getArguments().getString("genre");
        final String title2  = getArguments().getString("title");
        final String image  = getArguments().getString("image");

        Log.i("test3", image);
        Log.i("test2", genre);
        Log.i("test", title2);
        title.setText(title2);

        dwEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    handled = true;
                }
                return handled;
            }
        });


        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                write_second fragment = new write_second();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle args = new Bundle();
//                args.putString("edit", dwEdit.getText().toString());
                args.putString("title", title.getText().toString());
                args.putString("genre", genre);
                args.putString("image", image);
                fragment.setArguments(args);
                fragmentTransaction.replace(R.id.write3, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Before_Publish_Fragment fragment = new Before_Publish_Fragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle args = new Bundle();
                args.putString("edit", dwEdit.getText().toString());
                args.putString("title", title.getText().toString());
                args.putString("genre", genre);
                args.putString("image", image);
                fragment.setArguments(args);
                fragmentTransaction.replace(R.id.write3, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        return view;
>>>>>>> Stashed changes
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(WriteModeViewModel.class);
        // TODO: Use the ViewModel
    }

}
