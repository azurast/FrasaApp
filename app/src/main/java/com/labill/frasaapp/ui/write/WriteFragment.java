package com.labill.frasaapp.ui.write;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.labill.frasaapp.R;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class WriteFragment extends Fragment {

    private WriteViewModel mViewModel;
    public Spinner genre;
    public ImageView upload;
    public TextView next;
    public EditText title;
    public Bundle args;
    private static final int PICK_IMAGE = 100;
    Uri imageUri;

    public static WriteFragment newInstance() {
        return new WriteFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_write, container, false);
        genre = (Spinner) view.findViewById(R.id.genre);
        upload = view.findViewById(R.id.choose_image);
        next = view.findViewById(R.id.next);
        title = view.findViewById(R.id.title_input);
        imageUri = null;
        upload.setImageURI(null);
        Bundle args = new Bundle();
        args.clear();
//        title.setText(title2);
//        genre.setPrompt(genre2);
//        upload.setImageURI(Uri.parse(image));
        List<String> items = new ArrayList<String>();
        items.add("Children");
        items.add("Comedy");
        items.add("Drama");
        items.add("Horror");
        items.add("Mystery");
        items.add("Non Fiction");
        items.add("Romance");
        items.add("Sci-fi");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        genre.setAdapter(adapter);
        genre.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = parent.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                write_second fragment = new write_second();
                Bundle args = new Bundle();
                String convert = "";
                if(upload.getDrawable() == null ){
                }else{
                    Bitmap bitmap = ((BitmapDrawable)upload.getDrawable()).getBitmap();
                    convert = bitmapToString(bitmap);
                }
                args.putString("title", title.getText().toString());
                args.putString("genre", genre.getSelectedItem().toString());
                args.putString("image", convert);
                fragment.setArguments(args);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.write1, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        Log.i("test","masukk");
        return view;
    }
    private void openGallery() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        gallery.setType("image/*");
        String[] mimeTypes = {"image/jpeg", "image/png", "image/jpg"};
        gallery.putExtra(Intent.EXTRA_MIME_TYPES,mimeTypes);
        startActivityForResult(gallery, PICK_IMAGE);
        Log.d("test","ini cuma test");
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("test","ini cuma test lagi");
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE){
            imageUri = data.getData();
            Log.d("test","ini cuma test lagi lagi");
            upload.setImageURI(imageUri);
            int height = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 221, getResources().getDisplayMetrics());
            int width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 312, getResources().getDisplayMetrics());
            upload.getLayoutParams().height = height;
            upload.getLayoutParams().width = width;
            upload.setScaleType(ImageView.ScaleType.FIT_XY);
            data.setData(null);
        }
    }

    public final static String bitmapToString(Bitmap in){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        in.compress(Bitmap.CompressFormat.JPEG.PNG, 100, bytes);
        return Base64.encodeToString(bytes.toByteArray(),Base64.DEFAULT);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(WriteViewModel.class);
        // TODO: Use the ViewModel
    }

}
