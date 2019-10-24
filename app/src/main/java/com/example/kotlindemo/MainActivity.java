package com.example.kotlindemo;

import android.Manifest;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.kotlindemo.cache_6.CacheFragment;
import com.example.kotlindemo.elementary_1.ElementaryFragment;
import com.example.kotlindemo.map_2.MapFragment;
import com.example.kotlindemo.token_4.TokenFragment;
import com.example.kotlindemo.token_advanced_5.TokenAdvancedFragment;
import com.example.kotlindemo.zip_3.ZipFragment;
import com.google.android.material.tabs.TabLayout;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    @BindView(android.R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.toolBar)
    Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        checkPermissions();
//        setSupportActionBar(toolBar);

        viewPager.setAdapter(new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public int getCount() {
                return 6;
            }

            @Override
            public Fragment getItem(int position) {
                switch (position) {
                    case 0:
                        return new ElementaryFragment();
                    case 1:
                        return new MapFragment();
                    case 2:
                        return new ZipFragment();
                    case 3:
                        return new TokenFragment();
                    case 4:
                        return new TokenAdvancedFragment();
                    case 5:
                        return new CacheFragment();
                    default:
                        return new ElementaryFragment();
                }
            }

            @Override
            public CharSequence getPageTitle(int position) {
                switch (position) {
                    case 0:
                        return getString(R.string.title_elementary);
                    case 1:
                        return getString(R.string.title_map);
                    case 2:
                        return getString(R.string.title_zip);
                    case 3:
                        return getString(R.string.title_token);
                    case 4:
                        return getString(R.string.title_token_advanced);
                    case 5:
                        return getString(R.string.title_cache);
                    default:
                        return getString(R.string.title_elementary);
                }
            }
        });
        tabLayout.setupWithViewPager(viewPager);
    }

    /**
     * 动态权限管理
     */
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] permissions;
            if (!checkPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
                } else {
                    permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
                }
                ActivityCompat.requestPermissions(MainActivity.this, permissions, KeyConstants.PERMISSION_STORAGE_CODE);
            }
        }
    }

    /**
     * 判断权限是否授予
     *
     * @param permission 权限
     */
    private boolean checkPermissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(getApplicationContext(), permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length == 0) {
            return;
        }
        boolean isOk = true;
        for (int i = 0; i < grantResults.length; i++) {
            int result = grantResults[i];
            Log.w("requestCode : ", requestCode + " ,result = " + result);
            if (result != PackageManager.PERMISSION_GRANTED) {
                String permission = null;
                if (i < permissions.length) {
                    permission = permissions[i];
                }
                if (!TextUtils.isEmpty(permission)) {
                    Log.w("permission : ", permission);
                    if (!ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this, permission)) {
                        finish();
                    } else {
                        showRequestPermissionDialog(permission, requestCode);
                    }
                    isOk = false;
                    break;
                }
            }
        }
        if (isOk) {
            checkPermissions();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    /**
     * 显示权限请求提示框
     *
     * @param permission  权限
     * @param requestCode 请求码
     */
    private void showRequestPermissionDialog(String permission, int requestCode) {
        if (TextUtils.isEmpty(permission)) return;
        String explanation = "";
        switch (requestCode) {
            case KeyConstants.PERMISSION_STORAGE_CODE:
                explanation = getString(R.string.request_sdcard_permission);
                break;
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        AlertDialog alertDialog = builder.create();
        alertDialog.setTitle(explanation);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkPermissions();
            }
        });
        alertDialog.show();
    }
}