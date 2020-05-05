package com.labill.frasaapp.ui.landing;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.labill.frasaapp.R;
import com.labill.frasaapp.ui.login_and_signup.LoginActivity;
import com.labill.frasaapp.ui.login_and_signup.SignUpActivity;

public class LandingActivity extends AppCompatActivity {

    private ImageButton start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);

        start = findViewById(R.id.login);

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startNow = new Intent(LandingActivity.this, LoginActivity.class);
                startActivity(startNow);
                finish();

            }
        });
    }
}
