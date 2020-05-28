package com.labill.frasaapp.ui.profile;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.labill.frasaapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Future;

public class SeeProfile extends AppCompatActivity {

    private static final String TAG = "See Profile Log";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public PagerAdapter pagerAdapter;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    StorageReference references;

    private ImageView pp;
    private TextView name, bio;
    private String id;
    private FirebaseFirestore firebaseFirestore;
    public Map temp;
    private String userId;
    List<Object> test;
    Map<String, Long> total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_see_profile);
        Intent onClickIntent = getIntent();
        String recvId= onClickIntent.getStringExtra("id");

        tabLayout = (TabLayout) findViewById(R.id.tlProfileTabs);
        viewPager = findViewById(R.id.viewPager);
        pp = findViewById(R.id.profile_image);
        name = findViewById(R.id.tvAuthorName);
        bio = findViewById(R.id.tvAuthorBio);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        references = FirebaseStorage.getInstance().getReference();
        id = recvId;

        StorageReference profileRef = references.child("users/"+id+"/user.jpg");

        final DocumentReference documentReference = db.collection("users").document(id);

        documentReference.addSnapshotListener(SeeProfile.this, new EventListener<DocumentSnapshot>()
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
        firebaseFirestore = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getUid();
        test = new ArrayList<>();
        total = new HashMap<>();

        // Get each total
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        temp = (Map) documentSnapshot.get("total");
                        Log.d(TAG, "temp : "+temp);
                        tabLayout.getTabAt(0).setText("Stories\n"+temp.get("stories"));
                        tabLayout.getTabAt(1).setText("Followers\n"+temp.get("followers"));
                        tabLayout.getTabAt(2).setText("Following\n"+temp.get("following"));
                    }else{
                        Log.d(TAG, "Document does not exist");
                    }
                }else{
                    Log.d(TAG, "Task is not successfull");
                }
            }
        });

        pagerAdapter = new PageAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                if(tab.getPosition() == 0){
                    pagerAdapter.notifyDataSetChanged();
                } else if(tab.getPosition() == 1){
                    pagerAdapter.notifyDataSetChanged();
                } else if(tab.getPosition() == 2){
                    pagerAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
