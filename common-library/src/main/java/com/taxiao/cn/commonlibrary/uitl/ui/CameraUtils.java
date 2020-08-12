package com.taxiao.cn.commonlibrary.uitl.ui;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;


import com.taxiao.cn.commonlibrary.uitl.IMUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * 调用相机工具类
 * Created by Han on 2018/9/5
 * Email:yin13753884368@163.com
 */
public class CameraUtils {

    // 是否是Android 10以上手机
    private boolean isAndroidQ = Build.VERSION.SDK_INT >= 29;

    private String FILEPROVIDER = ".fileprovider";// 获取content provider

    private String mPhotoString;// String 路径

    static Uri mPhotoUri;// Uri 路径

    private static volatile CameraUtils mCameraUtils;

    private CameraUtils() {
    }

    public static CameraUtils getInstance() {
        if (mCameraUtils == null) {
            synchronized (IMUtils.class) {
                if (mCameraUtils == null) {
                    mCameraUtils = new CameraUtils();
                }
            }
        }
        return mCameraUtils;
    }

    // 获取照片 Uri 路径
    public Uri getPhotoUri() {
        return mPhotoUri;
    }

    // 获取照片 String 路径
    public String getPhotoString() {
        return mPhotoString;
    }

    // 跳转相机
    public void JumpCamera(Context context, int requestCode) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断是否有相机
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            try {
                if (isAndroidQ) {
                    // 适配android 10
                    mPhotoUri = createImageUri(context);
                } else {
                    // 获取图片保存路径
                    File photoFile = createImageFile(context);
                    mPhotoString = photoFile.getAbsolutePath();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        //适配Android 7.0文件权限，通过FileProvider创建一个content类型的Uri
                        mPhotoUri = FileProvider.getUriForFile(context, context.getPackageName() + FILEPROVIDER, photoFile);
                    } else {
                        mPhotoUri = Uri.fromFile(photoFile);
                    }
                }
                if (mPhotoUri != null) {
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
                    ((Activity) context).startActivityForResult(intent, requestCode);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    // 跳转相册
    public void JumpGallery(Context context, int requestCode) {
        Intent iamgeInt = new Intent(Intent.ACTION_PICK, null);
        iamgeInt.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
        ((Activity) context).startActivityForResult(iamgeInt, requestCode);
    }


    /**
     * 裁剪图片方法实现 小米手机会出现闪退(已经更换方法)
     * Android 10 加权限
     * if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
     * intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
     * }
     */
    public Uri startPhotoZoom(Uri uri, Context mContext, int requestCode) {
        Uri uriTempFile = null;
        String mCarrier = Build.MANUFACTURER;//获取手机厂商
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        // true 可裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高 华为手机裁剪图片过大报错
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
        // 取消人脸识别
        intent.putExtra("noFaceDetection", true);
        intent.putExtra("return-data", true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        if ("xiaomi".equals(mCarrier.toLowerCase()) || "mi".equals(mCarrier.toLowerCase())) {
            uriTempFile = Uri.parse("file://" + "/" + Environment.getExternalStorageDirectory().getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uriTempFile);
            intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        } else {
            uriTempFile = uri;
        }
        ((Activity) mContext).startActivityForResult(intent, requestCode);
        return uriTempFile;
    }

    /**
     * Android 10以后使用这种方法
     * 创建图片地址uri,用于保存拍照后的照片
     */
    private Uri createImageUri(Context context) {
        String status = Environment.getExternalStorageState();
        // 判断是否有SD卡,优先使用SD卡存储,当没有SD卡时使用手机存储
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new ContentValues());
        } else {
            return context.getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI, new ContentValues());
        }
    }

    /**
     * 创建保存图片的文件
     */
    private File createImageFile(Context context) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File tempFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",  /* suffix */
                storageDir     /* directory */
        );
        return tempFile;
    }


}
