package com.labill.frasaapp.ui.login_and_signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.ProviderQueryResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.labill.frasaapp.MainActivity;
import com.labill.frasaapp.R;
import com.labill.frasaapp.database.constructorClass.User;
import com.labill.frasaapp.ui.home.HomeFragment;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference database;
    private String name, email, password, conpassword;
    private TextView boxname, boxemail, boxpassword, boxconpassword;
    private ImageButton signup;
    private TextView toLogin;
    FirebaseFirestore db;
    String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().hide();

        boxname = findViewById(R.id.name);
        boxemail = findViewById(R.id.email);
        boxpassword = findViewById(R.id.password);
        boxconpassword = findViewById(R.id.password2);
        signup = findViewById(R.id.signup);
        toLogin = findViewById(R.id.move2);

        db = FirebaseFirestore.getInstance();

        final ImageView loadingProgressBar = findViewById(R.id.loading);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        //Pinah ke login
        toLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goLogin = new Intent(SignUpActivity.this, LoginActivity.class);
                startActivity(goLogin);
                finish();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                name = boxname.getText().toString();
                email = boxemail.getText().toString().trim();
                password = boxpassword.getText().toString();
                conpassword = boxconpassword.getText().toString();

                if (email.equals("") || password.equals("") || conpassword.equals("") || name.equals("")) {
                    Toast.makeText(SignUpActivity.this, "Please fill up all fields ",
                            Toast.LENGTH_SHORT).show();
                    if (name.equals("")) {
                        boxname.requestFocus();
                    } else if (email.equals("")) {
                        boxemail.requestFocus();
                    } else if (password.equals("")) {
                        boxpassword.requestFocus();
                    } else if (conpassword.equals("")) {
                        boxconpassword.requestFocus();
                    }

                } else {
                    mAuth.fetchProvidersForEmail(email).addOnCompleteListener(new OnCompleteListener<ProviderQueryResult>() {
                        @Override
                        public void onComplete(@NonNull Task<ProviderQueryResult> task) {
                            if (task.isSuccessful()) {
                                if (password.length() < 6) {
                                    Toast.makeText(SignUpActivity.this, "Password must at least 6 characters",
                                            Toast.LENGTH_SHORT).show();
                                    boxpassword.requestFocus();
                                } else if (!conpassword.equals(password)) {
                                    Toast.makeText(SignUpActivity.this, "Password not matching",
                                            Toast.LENGTH_SHORT).show();
                                    boxconpassword.requestFocus();
                                } else {

                                    mAuth.createUserWithEmailAndPassword(email, password)
                                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        // Sign in success, update UI with the signed-in user's information
                                                        FirebaseUser user = mAuth.getCurrentUser();

                                                        Toast.makeText(SignUpActivity.this, "Authentication success.",
                                                                Toast.LENGTH_SHORT).show();

                                                        id = mAuth.getCurrentUser().getUid();
                                                        DocumentReference documentReference = db.collection("users").document(id);
                                                        Map<String, Object> newuser = new HashMap<>();
                                                        newuser.put("name", name);
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

                                                        updateUI(user);

                                                    } else {
                                                        // If sign in fails, display a message to the user.
                                                        Toast.makeText(SignUpActivity.this, "Email already registered or not valid",
                                                                Toast.LENGTH_SHORT).show();

                                                    }

                                                }
                                            });
                                }
                            }
                        }
                    });
                }


            }
        });


    }

    private void submitUser(User user) {
        database.child("com.labill.frasaapp.User")
                .push()
                .setValue(user)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        boxname.setText("");
                        boxemail.setText("");
                    }
                });
    }

    @Override
    public void onBackPressed() {
        Intent goLogin = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(goLogin);
        finish();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent home = new Intent(SignUpActivity.this, MainActivity.class);
            startActivity(home);
            finish();
        }
    }
}