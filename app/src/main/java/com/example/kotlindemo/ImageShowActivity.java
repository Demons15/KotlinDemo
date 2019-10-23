package com.example.kotlindemo;

import android.os.Bundle;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageShowActivity extends AppCompatActivity {
    @BindView(R.id.image_show)
    ImageView imageShow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_show_layout);
        ButterKnife.bind(this);
        String image = getIntent().getStringExtra("image");
        Glide.with(this).load(image).into(imageShow);
    }
}
