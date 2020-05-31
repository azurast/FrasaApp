package com.labill.frasaapp;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.model.Document;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.labill.frasaapp.ui.home.HomeFragment;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.labill.frasaapp.ui.login_and_signup.LoginActivity;
import com.labill.frasaapp.ui.profile.Profile2Activity;
import com.labill.frasaapp.ui.profile.ProfileFollowingFragment;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.ListFragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements UserListAdapter.OnItemClickListener {

    private static final String TAG = "MainActivityLog";
    private UserListAdapter adapter;
    private List<User> userList;
    private AppBarConfiguration mAppBarConfiguration;
    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    String id;
    String name, email, bio, follow, bookmark, photo;
    StorageReference references;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        final NavigationView navigationView = findViewById(R.id.nav_view);

        // Menu menu = findViewById(R.menu.activity_main_drawer);

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            Intent so = new Intent(MainActivity.this, LoginActivity.class);
            so.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(so);
        }

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        id = mAuth.getCurrentUser().getUid();

        //fetch data from database
//        DocumentReference documentReference = db.collection("users").document(id);
//        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
//            @OverrideonEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
////
//            public void
//            }
//        });

        db.collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if(e!=null){
                    Log.d(TAG, "Error : "+ e.getMessage());
                }else{
                    for(DocumentChange doc : queryDocumentSnapshots.getDocumentChanges()){
                        QueryDocumentSnapshot currDoc = doc.getDocument();
                        name = currDoc.getString("name");
                        email = currDoc.getString("email");
                        bio = currDoc.getString("bio");
                        photo = currDoc.getString("photo");
                        // Navbar Header
                        View navHeader = navigationView.getHeaderView(0);
                        TextView userName = navHeader.findViewById(R.id.user_name);
                        if(doc.getType() == DocumentChange.Type.ADDED){
                            if(currDoc.getId().equals(id)) {
                                Log.d(TAG, "added");

                                userName.setText(name);
                                final CircleImageView profilePhoto = navHeader.findViewById(R.id.profile_image);
                                StorageReference profileRef = FirebaseStorage.getInstance().getReference().child("users/" + id + "/user.jpg");
                                profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Picasso.get().load(uri).into(profilePhoto);
                                    }
                                });
                            }
                        }else if(doc.getType() == DocumentChange.Type.MODIFIED){
                            if(currDoc.getId().equals(id)) {
                                Log.d(TAG, "modified");
//                                name = currDoc.getString("name");
//                                email = currDoc.getString("email");
//                                bio = currDoc.getString("bio");
//                                photo = currDoc.getString("photo");
//                                // Navbar Header
//                                View navHeader = navigationView.getHeaderView(0);
//                                TextView userName = navHeader.findViewById(R.id.user_name
                                userName.setText(name);
                                final CircleImageView profilePhoto = navHeader.findViewById(R.id.profile_image);
                                StorageReference profileRef = FirebaseStorage.getInstance().getReference().child("users/" + id + "/user.jpg");
                                profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                        Picasso.get().load(uri).into(profilePhoto);
                                    }
                                });
                            }
                        }
                    }
                }
            }
        });

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_bookmarks, R.id.nav_about, R.id.nav_write)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

    }

    // Remove 3 dot
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        searchItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        final SearchView searchView = (SearchView) searchItem.getActionView();
        Log.d(TAG, "search view created");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.d(TAG, "onQueryTexSubmit");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                Log.d(TAG, "onQueryTexChange");
                return true;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d("menu","selected");
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.action_search:
                item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
                SearchView searchView = (SearchView) item.getActionView();
//                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//                    @Override
//                    public boolean onQueryTextSubmit(String query) {
//                        Bundle bundle = new Bundle();
//                        bundle.putString("query", query);
//                        Fragment fragment = new ProfileFollowingFragment();
//                        fragment.setArguments(bundle);
//                        FragmentManager fragmentManager = getSupportFragmentManager();
//                        //fragmentManager.beginTransaction().replace(R.id.)
//                        return false;
//                    }
//
//                    @Override
//                    public boolean onQueryTextChange(String newText) {
//                        return false;
//                    }
//                });
                //searchUser();
                Log.d(TAG, "search view selected");
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    private void searchUser(String searchText) {
        Query query = FirebaseDatabase.getInstance().getReference("users").orderByChild("name").startAt(searchText).endAt(searchText+"\uf8ff");
    }

    private void SetUpRecyclerView(){
        RecyclerView recyclerView = findViewById(R.id.rvFollowing);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        adapter = new UserListAdapter(userList, this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }
    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    // Go to profile
    public void openProfile(View view) {
        Intent openProfileIntent = new Intent(this, Profile2Activity.class);
        startActivity(openProfileIntent);
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        //FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    private void signout(){
        FirebaseAuth.getInstance().signOut();
        Intent so = new Intent(getApplicationContext(), MainActivity.class);
        so.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(so);
    }

    @Override
    public void onItemClick(int position, String id) {

    }
}
