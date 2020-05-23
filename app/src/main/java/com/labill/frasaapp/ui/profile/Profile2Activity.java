package com.labill.frasaapp.ui.profile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
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
import com.labill.frasaapp.R;
import com.squareup.picasso.Picasso;

public class Profile2Activity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem postTab, followerTab, followingTab;
    public PagerAdapter pagerAdapter;
    FirebaseFirestore db;
    FirebaseAuth mAuth;
    StorageReference references;

    private ImageView pp;
    private TextView name, bio;
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);

        tabLayout = (TabLayout) findViewById(R.id.tlProfileTabs);
        postTab = (TabItem) findViewById(R.id.postTab);
        followerTab = (TabItem) findViewById(R.id.followerTab);
        followingTab = (TabItem) findViewById(R.id.followingTab);
        viewPager = findViewById(R.id.viewPager);
        pp = findViewById(R.id.profile_image);
        name = findViewById(R.id.tvAuthorName);
        bio = findViewById(R.id.tvAuthorBio);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        references = FirebaseStorage.getInstance().getReference();
        id = mAuth.getCurrentUser().getUid();

        StorageReference profileRef = references.child("users/"+id+"/user.jpg");

        final DocumentReference documentReference = db.collection("users").document(id);

        documentReference.addSnapshotListener(Profile2Activity.this, new EventListener<DocumentSnapshot>()
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
