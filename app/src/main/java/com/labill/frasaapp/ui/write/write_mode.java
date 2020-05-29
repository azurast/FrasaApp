package com.labill.frasaapp.ui.write;

<<<<<<< HEAD
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
=======
<<<<<<< Updated upstream
<<<<<<< Updated upstream
=======
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
>>>>>>> Stashed changes
import androidx.lifecycle.ViewModelProviders;

>>>>>>> c4d9ebe0eb3f8cceef47a4d892d364bfa0a03aa4
=======
import androidx.lifecycle.ViewModelProviders;

>>>>>>> 7436de52d37f0ad5d204ba1bf6f32d22412db8a6
>>>>>>> Stashed changes
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

<<<<<<< HEAD
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
<<<<<<< Updated upstream
=======
<<<<<<< Updated upstream
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.labill.frasaapp.R;

public class write_mode extends Fragment {

    private WriteModeViewModel mViewModel;
=======
import android.util.Log;
>>>>>>> c4d9ebe0eb3f8cceef47a4d892d364bfa0a03aa4
=======
>>>>>>> Stashed changes
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
<<<<<<< Updated upstream
<<<<<<< HEAD
import android.widget.ImageView;
=======
>>>>>>> c4d9ebe0eb3f8cceef47a4d892d364bfa0a03aa4
=======
import android.widget.ImageView;
>>>>>>> Stashed changes
import android.widget.TextView;

import com.labill.frasaapp.R;

<<<<<<< Updated upstream
<<<<<<< HEAD
import java.io.ByteArrayOutputStream;

=======
>>>>>>> c4d9ebe0eb3f8cceef47a4d892d364bfa0a03aa4
=======
import java.io.ByteArrayOutputStream;

>>>>>>> Stashed changes
import static android.widget.TextView.BufferType.SPANNABLE;

public class write_mode extends Fragment {

    private WriteModeViewModel mViewModel;
    public EditText dwEdit;
    public TextView prev;
    public TextView publish;
    public TextView save;
    public TextView title;
<<<<<<< Updated upstream
<<<<<<< HEAD
=======
>>>>>>> Stashed changes
    String[] stickers;
    Float[] xPos;
    Float[] yPos;
=======
<<<<<<< Updated upstream
>>>>>>> Stashed changes
>>>>>>> c4d9ebe0eb3f8cceef47a4d892d364bfa0a03aa4
=======
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.labill.frasaapp.R;

public class write_mode extends Fragment {

    private WriteModeViewModel mViewModel;
>>>>>>> 7436de52d37f0ad5d204ba1bf6f32d22412db8a6
>>>>>>> Stashed changes

    public static write_mode newInstance() {
        return new write_mode();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
<<<<<<< HEAD
<<<<<<< Updated upstream
=======
<<<<<<< Updated upstream
        return inflater.inflate(R.layout.fragment_write_mode, container, false);
=======
>>>>>>> c4d9ebe0eb3f8cceef47a4d892d364bfa0a03aa4
=======
>>>>>>> Stashed changes
        View view = inflater.inflate(R.layout.fragment_write_mode, container, false);
        prev = view.findViewById(R.id.preview);
        dwEdit = view.findViewById(R.id.write_input);
        title = view.findViewById(R.id.choose_layo);
        publish = view.findViewById(R.id.publish);
        save = view.findViewById(R.id.save);

        final String genre  = getArguments().getString("genre");
        final String title2  = getArguments().getString("title");
        final String image  = getArguments().getString("image");
<<<<<<< Updated upstream
<<<<<<< HEAD
=======
>>>>>>> Stashed changes
        final Integer TotalStickers  = getArguments().getInt("totalstickers");
        stickers = new String[TotalStickers];
        xPos = new Float[TotalStickers];
        yPos = new Float[TotalStickers];
        for (int i = 0; i< TotalStickers; i++){
            stickers[i]  = getArguments().getString("stickers"+i);
            xPos[i]  = getArguments().getFloat("xPos"+i);
            yPos[i]  = getArguments().getFloat("yPos"+i);
        }
<<<<<<< Updated upstream
=======
>>>>>>> c4d9ebe0eb3f8cceef47a4d892d364bfa0a03aa4
=======
>>>>>>> Stashed changes

        Log.i("test3", image);
        Log.i("test2", genre);
        Log.i("test", title2);
<<<<<<< Updated upstream
<<<<<<< HEAD
=======
>>>>>>> Stashed changes
        Log.i("test", String.valueOf(yPos[0]));
        Log.i("test", stickers[0]);
        Log.i("test", String.valueOf(xPos[0]));
        title.setText(title2);
        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());


//        ConstraintLayout container1 = (ConstraintLayout) view.findViewById(R.id.write3);//caste the view into LinearLayout as our drag acceptable layout is LinearLayout
//
//        ImageView imageView = new ImageView(this.getContext());
//        imageView.setId(View.generateViewId());
//        Bitmap convert1 = stringToBitmap(stickers[0]);
//        imageView.setImageBitmap(convert1);
//        imageView.setContentDescription(view.getResources().getString(R.string.cover));
//        imageView.setAdjustViewBounds(true);
////        imageView.animate().translationX(xPos[0]).translationY(yPos[0]).setDuration(0).start();
//        imageView.setX(xPos[0]);
//        imageView.setY(yPos[0]);
//        container1.addView(imageView);
//        imageView.getLayoutParams().width = width;
//        imageView.getLayoutParams().height = height;
//        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
<<<<<<< Updated upstream
=======
        title.setText(title2);
>>>>>>> c4d9ebe0eb3f8cceef47a4d892d364bfa0a03aa4
=======
>>>>>>> Stashed changes

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
<<<<<<< Updated upstream
<<<<<<< HEAD
                Bundle args = new Bundle();
=======
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle args = new Bundle();
//                args.putString("edit", dwEdit.getText().toString());
>>>>>>> c4d9ebe0eb3f8cceef47a4d892d364bfa0a03aa4
=======
                Bundle args = new Bundle();
>>>>>>> Stashed changes
                args.putString("title", title.getText().toString());
                args.putString("genre", genre);
                args.putString("image", image);
                fragment.setArguments(args);
<<<<<<< Updated upstream
<<<<<<< HEAD
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
=======
>>>>>>> c4d9ebe0eb3f8cceef47a4d892d364bfa0a03aa4
=======
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
<<<<<<< HEAD
=======
>>>>>>> Stashed changes
                for (int i = 0; i< TotalStickers; i++){
                    args.putString("stickers"+i, stickers[i]);
                    args.putFloat("xPos"+i,xPos[i]);
                    args.putFloat("yPos"+i,yPos[i]);
                }
                args.putInt("total", TotalStickers);
<<<<<<< Updated upstream
=======
>>>>>>> c4d9ebe0eb3f8cceef47a4d892d364bfa0a03aa4
=======
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
<<<<<<< HEAD
=======
>>>>>>> Stashed changes
    }

    public final static Bitmap stringToBitmap(String in){
        byte[] bytes = Base64.decode(in, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


=======
<<<<<<< Updated upstream
>>>>>>> Stashed changes
    }

>>>>>>> c4d9ebe0eb3f8cceef47a4d892d364bfa0a03aa4
=======
        return inflater.inflate(R.layout.fragment_write_mode, container, false);
    }

>>>>>>> 7436de52d37f0ad5d204ba1bf6f32d22412db8a6
>>>>>>> Stashed changes
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(WriteModeViewModel.class);
        // TODO: Use the ViewModel
    }

}
