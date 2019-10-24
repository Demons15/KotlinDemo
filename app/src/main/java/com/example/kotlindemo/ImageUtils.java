package com.example.kotlindemo;
import android.content.Context;
import android.provider.MediaStore;
import java.io.FileNotFoundException;

public class ImageUtils {
    public static String insertImageToSystem(Context context, String imagePath) {
        String url = "";
        try {
            url = MediaStore.Images.Media.insertImage(context.getContentResolver(), imagePath, System.currentTimeMillis() + "", "分享");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return url;
    }
}
