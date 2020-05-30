package com.labill.frasaapp.ui.write;

import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.labill.frasaapp.R;

public class fragment_stickers extends Fragment implements View.OnLongClickListener {

    private FragmentStickersViewModel mViewModel;
    public ImageView imageView5,imageView6,imageView7,imageView8,imageView9,imageView10,imageView11,imageView12,imageView13,
    imageView14,imageView15,imageView16,imageView17,imageView18,imageView19,imageView20,imageView21,imageView22;

    private static final String IMAGE_VIEW_TAG = "LAUNCHER LOGO";

    public static fragment_stickers newInstance() {
        return new fragment_stickers();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stickers, container, false);

        imageView5 = (ImageView) view.findViewById(R.id.imageView5);
        imageView5.setTag(IMAGE_VIEW_TAG);
        imageView6 = (ImageView) view.findViewById(R.id.imageView6);
        imageView6.setTag(IMAGE_VIEW_TAG);
        imageView7 = (ImageView) view.findViewById(R.id.imageView7);
        imageView7.setTag(IMAGE_VIEW_TAG);
        imageView8 = (ImageView) view.findViewById(R.id.imageView8);
        imageView8.setTag(IMAGE_VIEW_TAG);
        imageView9 = (ImageView) view.findViewById(R.id.imageView9);
        imageView9.setTag(IMAGE_VIEW_TAG);
        imageView10 = (ImageView) view.findViewById(R.id.imageView10);
        imageView10.setTag(IMAGE_VIEW_TAG);
        imageView11 = (ImageView) view.findViewById(R.id.imageView11);
        imageView11.setTag(IMAGE_VIEW_TAG);
        imageView12 = (ImageView) view.findViewById(R.id.imageView12);
        imageView12.setTag(IMAGE_VIEW_TAG);
        imageView13 = (ImageView) view.findViewById(R.id.imageView13);
        imageView13.setTag(IMAGE_VIEW_TAG);
        imageView14 = (ImageView) view.findViewById(R.id.imageView14);
        imageView14.setTag(IMAGE_VIEW_TAG);
        imageView15 = (ImageView) view.findViewById(R.id.imageView15);
        imageView15.setTag(IMAGE_VIEW_TAG);
        imageView16 = (ImageView) view.findViewById(R.id.imageView16);
        imageView16.setTag(IMAGE_VIEW_TAG);
        imageView17 = (ImageView) view.findViewById(R.id.imageView17);
        imageView17.setTag(IMAGE_VIEW_TAG);
        imageView18 = (ImageView) view.findViewById(R.id.imageView18);
        imageView18.setTag(IMAGE_VIEW_TAG);
        imageView19 = (ImageView) view.findViewById(R.id.imageView19);
        imageView19.setTag(IMAGE_VIEW_TAG);
        imageView20 = (ImageView) view.findViewById(R.id.imageView20);
        imageView20.setTag(IMAGE_VIEW_TAG);
        imageView21 = (ImageView) view.findViewById(R.id.imageView21);
        imageView21.setTag(IMAGE_VIEW_TAG);
        imageView22 = (ImageView) view.findViewById(R.id.imageView22);
        imageView22.setTag(IMAGE_VIEW_TAG);

        imageView5.setOnLongClickListener(this);
        imageView6.setOnLongClickListener(this);
        imageView7.setOnLongClickListener(this);
        imageView8.setOnLongClickListener(this);
        imageView9.setOnLongClickListener(this);
        imageView10.setOnLongClickListener(this);
        imageView11.setOnLongClickListener(this);
        imageView12.setOnLongClickListener(this);
        imageView13.setOnLongClickListener(this);
        imageView14.setOnLongClickListener(this);
        imageView15.setOnLongClickListener(this);
        imageView16.setOnLongClickListener(this);
        imageView17.setOnLongClickListener(this);
        imageView18.setOnLongClickListener(this);
        imageView19.setOnLongClickListener(this);
        imageView20.setOnLongClickListener(this);
        imageView21.setOnLongClickListener(this);
        imageView22.setOnLongClickListener(this);
        return view;
    }

    @Override
    public boolean onLongClick(View view) {
        // Create a new ClipData.
        // This is done in two steps to provide clarity. The convenience method
        // ClipData.newPlainText() can create a plain text ClipData in one step.

        // Create a new ClipData.Item from the ImageView object's tag
        ClipData.Item item = new ClipData.Item((CharSequence) view.getTag());

        // Create a new ClipData using the tag as a label, the plain text MIME type, and
        // the already-created item. This will create a new ClipDescription object within the
        // ClipData, and set its MIME type entry to "text/plain"
        String[] mimeTypes = {ClipDescription.MIMETYPE_TEXT_PLAIN};

        ClipData data = new ClipData(view.getTag().toString(), mimeTypes, item);

        // Instantiates the drag shadow builder.
        View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);

        // Starts the drag
        view.startDrag(data//data to be dragged
                , shadowBuilder //drag shadow
                , view//local data about the drag and drop operation
                , 0//no needed flags
        );

        return true;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(FragmentStickersViewModel.class);
        // TODO: Use the ViewModel
    }

}
