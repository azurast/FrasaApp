package com.labill.frasaapp.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.labill.frasaapp.R;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Profile2Activity extends AppCompatActivity {

    private static final String TAG = "Profile Activity Log";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private TabItem storiesTab, followerTab, followingTab;
    public PagerAdapter pagerAdapter;
    private FirebaseFirestore firebaseFirestore;
    public long nFollowing, nFollowers, nStories;
    //private Map<String, Object> profileTotal;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);

        tabLayout = (TabLayout) findViewById(R.id.tlProfileTabs);
        storiesTab = (TabItem) findViewById(R.id.storiesTab);
        followerTab = (TabItem) findViewById(R.id.followerTab);
        followingTab = (TabItem) findViewById(R.id.followingTab);
        viewPager = findViewById(R.id.viewPager);
        firebaseFirestore = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getUid();

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(userId);

        // Get each total
        documentReference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot.exists()){
                        Map temp = (Map) documentSnapshot.get("total");
                        nStories = (long) temp.get("stories");
                        nFollowing = (long) temp.get("following");
                        nFollowers = (long) temp.get("followers");
                    }else{
                        Log.d(TAG, "Document does not exist");
                    }
                }else{
                    Log.d(TAG, "Task is not successfull");
                }
            }
        });

        // Check setiap variable udh masuk blm
        Log.d(TAG, "jumlah stories : "+nStories);
        Log.d(TAG, "jumlah following : "+nFollowing);
        Log.d(TAG, "jumlah followers : "+nFollowers);

        // Nanti kalo udah cast ke TabItem
        tabLayout.getTabAt(0).setText("Stories\n"+nStories);


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
