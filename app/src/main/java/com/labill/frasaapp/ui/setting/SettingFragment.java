package com.labill.frasaapp.ui.setting;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.labill.frasaapp.MainActivity;
import com.labill.frasaapp.R;
import com.labill.frasaapp.ui.login_and_signup.SignUpActivity;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    FirebaseFirestore db;
    FirebaseAuth mAuth;
    StorageReference references;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageView pp;
    private Button changepp, save;
    private EditText bio, name;
    private String id, newName, newBio;



    public SettingFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SettingFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SettingFragment newInstance(String param1, String param2) {
        SettingFragment fragment = new SettingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        references = FirebaseStorage.getInstance().getReference();
        id = mAuth.getCurrentUser().getUid();
        StorageReference profileRef = references.child("users/"+id+"/user.jpg");

        DocumentReference documentReference = db.collection("users").document(id);
        documentReference.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>()
        {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                name.setText(documentSnapshot.getString("name"));
                bio.setText(documentSnapshot.getString("bio"));

            }
        });

        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(pp);
            }
        });
/*
        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                FirebaseUser user = mAuth.getCurrentUser();

                Toast.makeText(SignUpActivity.this, "Authentication success.",
                        Toast.LENGTH_SHORT).show();


                user.update("name", name);
                newuser.put("email", email);
                newuser.put("bio", "Hello, enjoy my stories");
                newuser.put("follow", null);
                newuser.put("bookmark", null);
                newuser.put("photo", "user.jpg");
                documentReference.set(newuser).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d("l", "addded");
                    }
                });
            }
        });
*/
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        pp = (ImageView) getView().findViewById(R.id.pp);
        changepp = getView().findViewById(R.id.change);
        save = getView().findViewById(R.id.buttSave);
        name = getView().findViewById(R.id.etName);
        bio = getView().findViewById(R.id.etBio);

        changepp.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //open gallery
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(openGalleryIntent, 1000);
            }
        });
}

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1000)
        {
            if(resultCode== Activity.RESULT_OK){
                Uri imgUri = data.getData();
                pp.setImageURI(imgUri);

                uploadImageToFirebase(imgUri);
            }
        }
    }

    private void uploadImageToFirebase(Uri imgUri) {
        //upload
        final StorageReference fileRef = references.child("users/"+id+"/user.jpg");
        fileRef.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getActivity(), "Image Uploaded", Toast.LENGTH_SHORT).show();

                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(pp);
                    }
                });
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_setting, container, false);
    }
}
