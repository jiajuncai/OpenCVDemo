package com.junecai.opencvdemo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatDrawableManager;

import com.junecai.opencvdemo.util.PermissionUtil;

public class MainActivity extends AppCompatActivity {

    /**
     * 拍照需要的权限
     */
    private static final String[] CAMERA_PERMISSION_STR = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private Context mContext;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
    }

    /**
     * 检测相机权限
     *
     * @param permissionStr
     */
    private void checkCameraPermission(String[] permissionStr) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (PermissionUtil.hasPermission(mContext, permissionStr)) {
                goCamera();
            } else {
                ActivityCompat.requestPermissions(this, CAMERA_PERMISSION_STR, CAMERA_PERMISSION_REQUEST_CODE);
            }
        } else {
            goCamera();
        }
    }

    private void goCamera() {

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST_CODE:
                break;
        }


    }
}
