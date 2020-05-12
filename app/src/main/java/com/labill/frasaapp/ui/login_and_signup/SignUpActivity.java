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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.labill.frasaapp.R;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String name, email, password, conpassword;
    private TextView boxname, boxemail, boxpassword, boxconpassword;
    private ImageButton signup;
    private TextView toLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        boxname = findViewById(R.id.name);
        boxemail = findViewById(R.id.email);
        boxpassword = findViewById(R.id.password);
        boxconpassword = findViewById(R.id.password2);
        signup = findViewById(R.id.signup);
        toLogin = findViewById(R.id.move2);
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
                email = boxemail.getText().toString();
                password = boxpassword.getText().toString();
                conpassword = boxconpassword.getText().toString();

                if (email.equals("") || password.equals("") || conpassword.equals("") || name.equals("")) {
                    Toast.makeText(SignUpActivity.this, "Please fill up all fields ",
                            Toast.LENGTH_SHORT).show();
                }

                else if (!conpassword.equals(password)) {
                    Toast.makeText(SignUpActivity.this, "Password not matching",
                            Toast.LENGTH_SHORT).show();
                }

                else {

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        Toast.makeText(SignUpActivity.this, "Authentication success.",
                                                Toast.LENGTH_SHORT).show();

                                    }

                                    else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();

                                    }

                                }
                            });
                }

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
    }
}
