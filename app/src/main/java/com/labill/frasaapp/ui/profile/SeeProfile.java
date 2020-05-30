package com.labill.frasaapp.ui.profile;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.labill.frasaapp.R;
import com.labill.frasaapp.User;
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
    StorageReference references;

    private ImageView pp;
    private TextView name, bio;
    private String id;
    private FirebaseFirestore firebaseFirestore;
    public Map temp;
    private String userId;
    private Button buttFollow;
    private List<String> idList;
    private String currentUserId = FirebaseAuth.getInstance().getUid();

    List<Object> test;
    Map<String, Long> total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_see_profile);
        Intent onClickIntent = getIntent();
        final String recvId= onClickIntent.getStringExtra("id");

        tabLayout = (TabLayout) findViewById(R.id.tlProfileTabs);
        viewPager = findViewById(R.id.viewPager);
        pp = findViewById(R.id.profile_image);
        name = findViewById(R.id.tvAuthorName);
        bio = findViewById(R.id.tvAuthorBio);
        buttFollow = findViewById(R.id.buttFollow);
        idList = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        references = FirebaseStorage.getInstance().getReference();
        id = recvId;
        Log.d("idnih", currentUserId);

        final DocumentReference documentReference = db.collection("users").document(id);
        StorageReference profileRef = references.child("users/"+id+"/user.jpg");

        DocumentReference userLogedin = db.collection("users").document(currentUserId);

        // Get Ids of People we follow
        db.collection("users").document(currentUserId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot != null){
                        Map temp = (Map) documentSnapshot.get("following");
                        Log.d(TAG, "Temp : "+temp);
                        for(Object key : temp.keySet()){
                            idList.add((String) key);
                        }
                    }
                }
                Log.d(TAG, "Id List : "+idList);
            }
        });

        //cek punya following ngga, kalo punya baru cek dia follow user ini ngga
        userLogedin.collection("following").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){
                    QuerySnapshot querySnapshot = task.getResult();

                    if(querySnapshot != null){
                        Log.d("followingcek", "yes");
                        for(QueryDocumentSnapshot doc : querySnapshot) {
                            Log.d("cekdoc", "document :" + doc.getId());
                            for (String idUser : idList) {
                                if (doc.getId().equals(id)) {
                                    buttFollow.setText("Followed");
                                    Log.d("followz","followed");
                                }else{
                                    buttFollow.setText("Follow");
                                    Log.d("followz","follow");
                                }

                            }
                        }
                    }
                }
                else
                {
                    buttFollow.setText("Follow");
                    Log.d("followz","follow");
                }
            }
        });

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
        userId = recvId;
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

        pagerAdapter = new SeePageAdapter(getSupportFragmentManager(), tabLayout.getTabCount(), userId);
        viewPager.setAdapter(pagerAdapter);

//        // Yang mau dikirim ke 3 tab fragment
//        Bundle bundle = new Bundle();
//        bundle.putString("id", userId);
//        Log.d(TAG, "current user id :"+userId);
//
//        // Define fragments
//        SeeStoriesFragment fra1 = new SeeStoriesFragment();
//        SeeFollowersFragment fra2 = new SeeFollowersFragment();
//        SeeFollowingFragment fra3 = new SeeFollowingFragment();
//
//        // Kirim ke fragment
//        fra1.setArguments(bundle);
//        fra2.setArguments(bundle);
//        fra3.setArguments(bundle);





        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
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
