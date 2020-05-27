package com.labill.frasaapp.ui.setting;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.labill.frasaapp.BuildConfig;
import com.labill.frasaapp.MainActivity;
import com.labill.frasaapp.R;
import com.labill.frasaapp.ui.login_and_signup.SignUpActivity;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
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
    FirebaseUser user;
    StorageReference references;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private ImageView pp;
    private Button changepp, changepp2, save;
    private EditText bio, name, email;
    private String id, defBio;
    String currentPhotoPath;
    private Uri imageUri;

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

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        defBio = "Hi! Enjoy my stories!";

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        references = FirebaseStorage.getInstance().getReference();
        id = mAuth.getCurrentUser().getUid();
        user = mAuth.getCurrentUser();

        StorageReference profileRef = references.child("users/"+id+"/user.jpg");

        final DocumentReference documentReference = db.collection("users").document(id);

        documentReference.addSnapshotListener(getActivity(), new EventListener<DocumentSnapshot>()
        {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                name.setText(documentSnapshot.getString("name"));
                bio.setText(documentSnapshot.getString("bio"));
                email.setText(documentSnapshot.getString("email"));

            }
        });

        profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(pp);
            }
        });


    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {


        changepp.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //open gallery
                Intent openGalleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                Log.d("uri",MediaStore.Images.Media.EXTERNAL_CONTENT_URI.toString());
                startActivityForResult(openGalleryIntent, 1000);
            }
        });

        // content://media/external/images/media
        // content://com.labill.frasaapp.provider/external_files/user.jpg
        changepp2.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                //open camera

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File photo = new File(Environment.getExternalStorageDirectory(),  "user");
                imageUri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider", photo);
                Log.d("uri",imageUri.toString());
                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);

                startActivityForResult(intent, 2000);
                Log.d("permission","yo");
                //askCameraPermissions();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                if(name.getText().toString().isEmpty() || email.getText().toString().isEmpty())
                {
                    Toast.makeText(getActivity(), "Name and Email can't be empty!", Toast.LENGTH_SHORT).show();
                }

                user.updateEmail(email.getText().toString()).
                        addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                DocumentReference documentReference = db.collection("users").document(id);
                                Map<String, Object> edited = new HashMap<>();
                                edited.put("name", name.getText().toString());
                                edited.put("email", email.getText().toString());

                                if(bio.getText().toString().isEmpty())
                                {
                                    edited.put("bio",defBio);
                                }
                                else
                                {
                                    edited.put("bio",bio.getText().toString());
                                }

                                documentReference.update(edited);

                                Toast.makeText(getActivity(), "Profile Updated", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void askCameraPermissions() {
        if(ContextCompat.checkSelfPermission(getActivity(),Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            Log.d("perm", "t");
            ActivityCompat.requestPermissions(getActivity(),new String[] {Manifest.permission.CAMERA}, 101);
        }else {
            Log.d("perm", "f");
            dispatchTakePictureIntent();
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d("perm2", "in");
        if(requestCode == 101){
            Log.d("perm2", "in2");
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Log.d("perm2", "t");
                dispatchTakePictureIntent();
            }else {
                Log.d("perm2", "f");
                Toast.makeText(getActivity(), "Camera Permission is Required to Use camera.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {

            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getActivity(),
                        "net.smallacademy.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, 2000);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String imageFileName = "user.jpg";
//        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1000)
        {
            if(resultCode== Activity.RESULT_OK){
                Uri imgUri = data.getData();
                Log.d("inigambar",imgUri.toString());
                pp.setImageURI(imgUri);

                uploadImageToFirebase(imgUri);
            }
        }

        if(data!=null)
        {
            Log.d("pesen","isi");
            if(requestCode==2000)
            {
                if (resultCode == Activity.RESULT_OK) {
                    Uri selectedImage = data.getData();
                    Log.d("inigambar",selectedImage.toString());
                    pp.setImageURI(selectedImage);

                    uploadImageToFirebase(selectedImage);
                }
            }
        }
        else
        {
            Log.d("pesen","null");
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
        View RootView = inflater.inflate(R.layout.fragment_setting, container, false);
        pp = (ImageView) RootView.findViewById(R.id.pp);
        changepp = RootView.findViewById(R.id.change);
        changepp2 = RootView.findViewById(R.id.change2);
        save = RootView.findViewById(R.id.buttSave);
        name = RootView.findViewById(R.id.etName);
        bio = RootView.findViewById(R.id.etBio);
        email = RootView.findViewById(R.id.etEmail);


        return RootView;
    }
}
