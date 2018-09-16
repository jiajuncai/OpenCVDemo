package com.junecai.opencvdemo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatDrawableManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.junecai.opencvdemo.util.PermissionUtil;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    /**
     * 拍照需要的权限
     */
    private static final String[] CAMERA_PERMISSION_STR = {Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE};
    private Context mContext;
    private static final int CAMERA_PERMISSION_REQUEST_CODE = 1111;
    private ImageView mIvOutput;
    private Button mBtnWrap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_main);
        mIvOutput = findViewById(R.id.iv_ouput);
        mIvOutput.setImageBitmap(getImageFromAssetsFile("WechatIMG5.jpeg"));
        mBtnWrap = findViewById(R.id.btn_warp);
        mBtnWrap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mIvOutput.setImageBitmap(warp(getImageFromAssetsFile("WechatIMG5.jpeg"), new MyPoint(200, 180), new MyPoint(150, 1142), new MyPoint(932, 1108), new MyPoint(830, 187)));
            }
        });
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


//    private void deal(Bitmap bitmap) {
//        Mat src = new Mat();
//        Mat dst = new Mat();
//        Mat M = new Mat();
//        Size dsize = new Size();
//        // bitmap转为Mat
//        Utils.bitmapToMat(bitmap, src);
//        Imgproc.warpPerspective();
//    }


    public static Bitmap warp(Bitmap image, MyPoint p1, MyPoint p2, MyPoint p3, MyPoint p4) {
        int resultWidth = 500;
        int resultHeight = 500;

        Mat inputMat = new Mat(image.getHeight(), image.getHeight(), CvType.CV_8UC4);
        Utils.bitmapToMat(image, inputMat);
        Mat outputMat = new Mat(resultWidth, resultHeight, CvType.CV_8UC4);

        // 输入坐标点
        Point ocvPIn1 = new Point(p1.getX(), p1.getY());
        Point ocvPIn2 = new Point(p2.getX(), p2.getY());
        Point ocvPIn3 = new Point(p3.getX(), p3.getY());
        Point ocvPIn4 = new Point(p4.getX(), p4.getY());
        List<Point> source = new ArrayList<Point>();
        source.add(ocvPIn1);
        source.add(ocvPIn2);
        source.add(ocvPIn3);
        source.add(ocvPIn4);
        Mat startM = Converters.vector_Point2f_to_Mat(source);

        // 输出坐标点
        Point ocvPOut1 = new Point(0, 0);
        Point ocvPOut2 = new Point(0, resultHeight);
        Point ocvPOut3 = new Point(resultWidth, resultHeight);
        Point ocvPOut4 = new Point(resultWidth, 0);
        List<Point> dest = new ArrayList<Point>();
        dest.add(ocvPOut1);
        dest.add(ocvPOut2);
        dest.add(ocvPOut3);
        dest.add(ocvPOut4);
        Mat endM = Converters.vector_Point2f_to_Mat(dest);

        Mat perspectiveTransform = Imgproc.getPerspectiveTransform(startM, endM);
//        Mat perspectiveTransform = new Mat(3, 3, CvType.CV_32FC1);
//        Core.perspectiveTransform(startM, endM, perspectiveTransform);
        Imgproc.warpPerspective(inputMat,
                outputMat,
                perspectiveTransform,
                new Size(resultWidth, resultHeight),
                Imgproc.CV_WARP_FILL_OUTLIERS);

        Bitmap output = Bitmap.createBitmap(resultWidth, resultHeight, Bitmap.Config.RGB_565);
        Utils.matToBitmap(outputMat, output);
        return output;
    }

    /**
     * 从Assets中读取图片
     */
    private Bitmap getImageFromAssetsFile(String fileName) {
        Bitmap image = null;
        AssetManager am = getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return image;

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION, this, mLoaderCallback);
        } else {
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    //openCV4Android 需要加载用到
    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
//                    mIvOutput.setImageBitmap(warp(getImageFromAssetsFile("WechatIMG5.jpeg"), new MyPoint(200, 180), new MyPoint(150, 1142), new MyPoint(932, 1108), new MyPoint(830, 187)));
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };
}
