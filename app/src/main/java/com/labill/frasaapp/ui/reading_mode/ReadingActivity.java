package com.labill.frasaapp.ui.reading_mode;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.labill.frasaapp.R;

import org.w3c.dom.Text;

public class ReadingActivity extends AppCompatActivity {

    private static final String TAG = "ReadingActivityLog";

    private TextView tvTitle, tvAuthor, tvContent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reading);

        tvTitle = findViewById(R.id.tvPostTitle);
        tvAuthor = findViewById(R.id.tvPostAuthor);
        tvContent = findViewById(R.id.tvPostContent);

        Intent onClickIntent = getIntent();
        String recvTitle = onClickIntent.getStringExtra("title");
        String recvAuthor = onClickIntent.getStringExtra("name");
        String recvContent = onClickIntent.getStringExtra("content");

        tvTitle.setText(recvTitle);
        tvAuthor.setText(recvAuthor);
        tvContent.setText(recvContent);
    }
}
