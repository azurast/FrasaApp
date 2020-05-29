package com.labill.frasaapp.ui.write;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.labill.frasaapp.R;

import yuku.ambilwarna.AmbilWarnaDialog;

public class FragmentSticker extends AppCompatActivity {

    ConstraintLayout colorLayout;
    private Button buttColor;
    int defaultColor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_sticker);
        getSupportActionBar().hide();

        buttColor = findViewById(R.id.color);
        colorLayout = findViewById(R.id.colorLayout);
        defaultColor = getResources().getColor(R.color.colorPrimary);

        buttColor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenColorPicker(false);
            }
        });
    }

    private void OpenColorPicker (boolean AlphaSupport)
    {
        AmbilWarnaDialog awd = new AmbilWarnaDialog(this, defaultColor,
                new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                        Toast.makeText(FragmentSticker.this, "Canceled", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        defaultColor = color;
                        colorLayout.setBackgroundColor(color);
                    }
                });

        awd.show();
    }
}

