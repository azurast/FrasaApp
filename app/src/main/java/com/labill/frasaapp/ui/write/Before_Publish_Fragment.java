package com.labill.frasaapp.ui.write;

import androidx.lifecycle.ViewModelProviders;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.labill.frasaapp.R;

public class Before_Publish_Fragment extends Fragment {

    public ImageView cover;
    private BeforePublishViewModel mViewModel;

    public static Before_Publish_Fragment newInstance() {
        return new Before_Publish_Fragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_before_publish, container, false);
        final String genre  = getArguments().getString("genre");
        final String title2  = getArguments().getString("title");
        final String image  = getArguments().getString("image");
        final String isi  = getArguments().getString("edit");

        Log.i("test4", isi);
        Log.i("test3", image);
        Log.i("test2", genre);
        Log.i("test", title2);

        cover = view.findViewById(R.id.cover);

        if(image == "") {

        }else{
            Bitmap pic = stringToBitmap(image);
            cover.setImageBitmap(pic);
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 221, getResources().getDisplayMetrics());
            int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 350, getResources().getDisplayMetrics());
            cover.getLayoutParams().height = height;
            cover.getLayoutParams().width = width;
            cover.setScaleType(ImageView.ScaleType.FIT_XY);
        }
        return view;
    }

    public final static Bitmap stringToBitmap(String in){
        byte[] bytes = Base64.decode(in, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(BeforePublishViewModel.class);
        // TODO: Use the ViewModel
    }

}
