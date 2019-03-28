package com.lee.fooddiary.Activity;

import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lee.fooddiary.R;

public class FullscreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fullscreen);

        ImageView fullscreenImageView = findViewById(R.id.fullscreenImageView);
        fullscreenImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        Intent intent = getIntent();
        if (intent!=null)
        {
            String urlString = intent.getStringExtra("FullscreenURL");
            if (urlString !=null && fullscreenImageView!=null)
            {
                Glide.with(this).load(urlString).into(fullscreenImageView);
            }
        }
    }
}
