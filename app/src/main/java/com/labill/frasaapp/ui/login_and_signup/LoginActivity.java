package com.labill.frasaapp.ui.login_and_signup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.labill.frasaapp.MainActivity;
import com.labill.frasaapp.R;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private EditText etUsername, etPassword;
    private TextView regist;
    private ImageButton login;
    private CheckBox remember;
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    private Boolean checked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.email);
        etPassword = findViewById(R.id.password);
        remember = findViewById(R.id.remember);
        regist = findViewById(R.id.move2);

        regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goRegister = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(goRegister);
                finish();

            }
        });

        login = findViewById(R.id.login);

        preferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        editor = preferences.edit();

        checked = preferences.getBoolean("saveLogin", false);

        if(checked == true)
        {
            etUsername.setText(preferences.getString("email", ""));
            etPassword.setText(preferences.getString("password", ""));
            remember.setChecked(true);
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etUsername.getWindowToken(), 0);

                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                if (remember.isChecked()) {
                    editor.putBoolean("saveLogin", true);
                    editor.putString("email", username);
                    editor.putString("password", password);
                    editor.commit();
                }

                else {
                    editor.clear();
                    editor.commit();
                }


                if (username.equals("")){
                    Toast.makeText(LoginActivity.this, "Email can't be empty", Toast.LENGTH_SHORT).show();
                    etUsername.requestFocus();

                } else if (password.equals("")){
                    Toast.makeText(LoginActivity.this, "Password can't be empty", Toast.LENGTH_SHORT).show();
                    etPassword.requestFocus();
                }

                else {

                    mAuth.signInWithEmailAndPassword(username, password)
                            .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information\
                                        FirebaseUser user = mAuth.getCurrentUser();

                                        Toast.makeText(LoginActivity.this, "Authentication Success",
                                                Toast.LENGTH_SHORT).show();

                                        updateUI(user);


                                    }

                                    else {
                                        // If sign in fails, display a message to the user.
                                        Toast.makeText(LoginActivity.this, "Email or Password wrong",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                    // ...
                                }
                            });

                }

            }
        });

        remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){

            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    SharedPreferences preferences = getSharedPreferences("checkbox", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("remember", "true");
                    editor.apply();
                }
            }
        });


    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);

    }

    private void updateUI(FirebaseUser user) {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            Intent home = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(home);
            finish();
        }
    }
}
