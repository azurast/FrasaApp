package com.labill.frasaapp.ui.write;

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
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.labill.frasaapp.R;

import java.io.ByteArrayOutputStream;

import static android.widget.TextView.BufferType.SPANNABLE;

public class write_mode extends Fragment {

    private WriteModeViewModel mViewModel;
    public EditText dwEdit;
    public TextView prev;
    public TextView publish;
    public TextView title;
    String[] stickers;
    Float[] xPos;
    Float[] yPos;

    public static write_mode newInstance() {
        return new write_mode();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_write_mode, container, false);
        prev = view.findViewById(R.id.preview);
        dwEdit = view.findViewById(R.id.write_input);
        title = view.findViewById(R.id.choose_layo);
        publish = view.findViewById(R.id.publish);

        final String genre  = getArguments().getString("genre");
        final String title2  = getArguments().getString("title");
        final String image  = getArguments().getString("image");
        final Integer color  = getArguments().getInt("color");
        final Integer TotalStickers  = getArguments().getInt("totalstickers");
        stickers = new String[TotalStickers];
        xPos = new Float[TotalStickers];
        yPos = new Float[TotalStickers];
        for (int i = 0; i< TotalStickers; i++){
            stickers[i]  = getArguments().getString("stickers"+i);
            xPos[i]  = getArguments().getFloat("xPos"+i);
            yPos[i]  = getArguments().getFloat("yPos"+i);
        }

        Log.i("test3", image);
        Log.i("test2", genre);
        Log.i("test", title2);
        Log.i("test", String.valueOf(yPos[0]));
        Log.i("test", stickers[0]);
        Log.i("test", String.valueOf(xPos[0]));
        title.setText(title2);
//        int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
//        int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
//
//        ConstraintLayout container1 = (ConstraintLayout) view.findViewById(R.id.write3);//caste the view into LinearLayout as our drag acceptable layout is LinearLayout
//
//        ImageView imageView = new ImageView(this.getContext());
//        imageView.setId(View.generateViewId());
//        Bitmap convert1 = stringToBitmap(stickers[0]);
//        imageView.setImageBitmap(convert1);
//        imageView.setAdjustViewBounds(true);
////        imageView.animate().translationX(xPos[0]).translationY(yPos[0]).setDuration(0).start();
//        imageView.setX(xPos[0]);
//        imageView.setY(yPos[0]);
//        container1.addView(imageView);
//        imageView.getLayoutParams().width = width;
//        imageView.getLayoutParams().height = height;
//        imageView.setScaleType(ImageView.ScaleType.FIT_XY);

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
                Bundle args = new Bundle();
                args.putString("title", title.getText().toString());
                args.putString("genre", genre);
                args.putString("image", image);
                fragment.setArguments(args);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
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
                for (int i = 0; i< TotalStickers; i++){
                    args.putString("stickers"+i, stickers[i]);
                    args.putFloat("xPos"+i,xPos[i]);
                    args.putFloat("yPos"+i,yPos[i]);
                }
                args.putInt("total", TotalStickers);
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
    }

    public final static Bitmap stringToBitmap(String in){
        byte[] bytes = Base64.decode(in, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(WriteModeViewModel.class);
        // TODO: Use the ViewModel
    }

}
