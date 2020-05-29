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
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
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

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

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
        DocumentReference documentReference = db.collection("users").document(id);
        documentReference.addSnapshotListener(this, new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                name = documentSnapshot.getString("name");
                email = documentSnapshot.getString("email");
                bio = documentSnapshot.getString("bio");
                photo = documentSnapshot.getString("photo");

                // Navbar Header
                View navHeader = navigationView.getHeaderView(0);
                TextView userName = navHeader.findViewById(R.id.user_name);
                userName.setText(name);
                final CircleImageView profilePhoto = navHeader.findViewById(R.id.profile_image);
                StorageReference profileRef = FirebaseStorage.getInstance().getReference().child("users/"+id+"/user.jpg");
                profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.get().load(uri).into(profilePhoto);
                    }
                });
            }
        });




        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_bookmarks, R.id.nav_about, R.id.nav_drafts, R.id.nav_write)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        //FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference myRef = database.getReference("message");

        //myRef.setValue("Hello, World!");

        //Home List Stories
//        HomeFragment homeFragment = new HomeFragment();
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        fragmentManager.beginTransaction().add(R.id.drawer_layout, homeFragment).commit();
    }

    // Remove 3 dot
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.activity_main_drawer, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.d("menu","selected");
        // Handle item selection
        switch (item.getItemId()) {

            case R.id.nav_signout:

                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

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
}
