package com.example.kotlindemo;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ImageShowActivity extends AppCompatActivity {
    @BindView(R.id.image_show)
    ImageView imageShow;
    private String path;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_show_layout);
        ButterKnife.bind(this);
        String image = getIntent().getStringExtra("image");
        Glide.with(this).load(image).into(imageShow);
        new Thread(new Runnable() {
            @Override
            public void run() {
                FutureTarget<File> future = Glide.with(ImageShowActivity.this)
                        .load(image).downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
                try {
                    File cacheFile = future.get();
                    path = cacheFile.getAbsolutePath();
                } catch (Exception e) {
                    e.printStackTrace();

                }
            }
        }).start();

        imageShow.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (path.isEmpty()) {
                    Toast.makeText(ImageShowActivity.this, "图片加载中,请稍后再试!", Toast.LENGTH_SHORT).show();
                } else {
                    String imageUri = ImageUtils.insertImageToSystem(ImageShowActivity.this, path);
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, "一些文字");
                    shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(imageUri));
                    shareIntent.setType("image/*");
                    startActivity(shareIntent);//这样分享就不带标题了
                }
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
