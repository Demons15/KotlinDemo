package com.example.kotlindemo;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnLongClick;

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
        imageShow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Util util = Util.INSTANCE;
                util.startShareImage(ImageShowActivity.this);
                return true;
            }
        });
    }
//    @OnLongClick({R.id.image_show})
//    public boolean onLongClickListener(View view) {
//        switch (view.getId()) {
//            case R.id.image_show:
//
//                break;
//            default:
//                break;
//        }
//        return true;
//    }
}
