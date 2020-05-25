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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Future;

public class Profile2Activity extends AppCompatActivity {

    private static final String TAG = "Profile Activity Log";
    private TabLayout tabLayout;
    private ViewPager viewPager;
    public PagerAdapter pagerAdapter;
    private FirebaseFirestore firebaseFirestore;
    public Map temp;
    private String userId;
    List<Object> test;
    Map<String, Long> total;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);

        tabLayout = (TabLayout) findViewById(R.id.tlProfileTabs);
        viewPager = findViewById(R.id.viewPager);
        firebaseFirestore = FirebaseFirestore.getInstance();
        userId = FirebaseAuth.getInstance().getUid();
        test = new ArrayList<>();
        total = new HashMap<>();

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("users").document(userId);

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
