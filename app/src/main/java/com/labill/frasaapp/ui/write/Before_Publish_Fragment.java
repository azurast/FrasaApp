package com.labill.frasaapp.ui.write;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.labill.frasaapp.R;
import com.labill.frasaapp.ui.home.HomeFragment;

public class Before_Publish_Fragment extends Fragment {

    public ImageView cover;
    TextView text, title;
    String[] stickers;
    Float[] xPos;
    Float[] yPos;
    Button publish;
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
        final Integer color  = getArguments().getInt("color");
        final String isi  = getArguments().getString("edit");
        final Integer TotalStickers  = getArguments().getInt("total");
        stickers = new String[TotalStickers];
        xPos = new Float[TotalStickers];
        yPos = new Float[TotalStickers];
        for (int i = 0; i< TotalStickers; i++){
            stickers[i]  = getArguments().getString("stickers"+i);
            xPos[i]  = getArguments().getFloat("xPos"+i);
            yPos[i]  = getArguments().getFloat("yPos"+i);
        }
        cover = view.findViewById(R.id.cover);
        title = view.findViewById(R.id.title2);
        text = view.findViewById(R.id.my_write);
        publish = view.findViewById(R.id.publish_button);
        text.setText(isi);
        title.setText(title2);
        if(color==0){

        }else{
            view.findViewById(R.id.bpublish).setBackgroundColor(color);
        }
        int height1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());
        int width1 = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getResources().getDisplayMetrics());

        ConstraintLayout container1 = (ConstraintLayout) view.findViewById(R.id.bpublish);//caste the view into LinearLayout as our drag acceptable layout is LinearLayout

        for (int i = 0; i< TotalStickers; i++){
            ImageView imageView = new ImageView(this.getContext());
            imageView.setId(View.generateViewId());
            Bitmap convert1 = stringToBitmap(stickers[i]);
            imageView.setImageBitmap(convert1);
            imageView.setX(xPos[i]);
            imageView.setY(yPos[i]);
            container1.addView(imageView);
            imageView.getLayoutParams().width = width1;
            imageView.getLayoutParams().height = height1;
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        if(image == "") {

        }else{
            Bitmap pic = stringToBitmap(image);
            cover.setImageBitmap(pic);
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 221, getResources().getDisplayMetrics());
            int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 312, getResources().getDisplayMetrics());
            cover.getLayoutParams().height = height;
            cover.getLayoutParams().width = width;
            cover.setScaleType(ImageView.ScaleType.FIT_XY);
        }

        publish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(),"Yay! Your Story\nHas Been Published!", Toast.LENGTH_SHORT).show();
                //buat save ke firebase


                //ini buat pindah ke home
                HomeFragment fragment = new HomeFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.bpublish, fragment);
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
        mViewModel = ViewModelProviders.of(this).get(BeforePublishViewModel.class);
        // TODO: Use the ViewModel
    }

}
