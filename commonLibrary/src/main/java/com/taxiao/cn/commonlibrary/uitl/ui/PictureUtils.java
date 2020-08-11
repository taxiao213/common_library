package com.taxiao.cn.commonlibrary.uitl.ui;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.taxiao.cn.commonlibrary.GlideApp;
import com.taxiao.cn.commonlibrary.R;
import com.taxiao.cn.commonlibrary.constant.LibraryConstant;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

/**
 * 加载图片 图库 的工具类
 * Created by Han on 2018/9/5
 * Email:yin13753884368@163.com
 */
public class PictureUtils {
    // 图片路径
    public static String mCurrentPhotoPath;

    /**
     * 添加到图库刷新
     */
    public static void galleryAddPic(Context context, String path) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(path);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);
    }

    /**
     * 创建 cache 图片文件夹
     * /data/user/0/vl.vision.app/cache/hex_meet
     */
    @NotNull
    private static File createCachePicFile(Context context, String cacheFileName) throws IOException {
        String cacheFileRoot = context.getCacheDir().getAbsolutePath();
        File file = new File(cacheFileRoot, cacheFileName);
        if (!file.exists()) {
            file.mkdir();
        }
        return file;
    }


    /**
     * 压缩图片 目录
     * 照片的命名规则为：hex_meet_20130125_173729.jpg
     * 目录 {@link LibraryConstant#CACHE_FILE_NAME}
     * 文件前缀{@link LibraryConstant#CACHE_PIC_PREFIX}
     * /data/user/0/vl.vision.app/cache/hex_meet_compress_pic
     */
    @SuppressLint("SimpleDateFormat")
    public static File createCompressImageFile(Context context) throws IOException {
        File file = createCachePicFile(context, LibraryConstant.CACHE_COMPRESS_FILE_NAME);
        String imageFileName = LibraryConstant.CACHE_PIC_PREFIX + "_" + UUID.randomUUID().toString();
        File image = File.createTempFile(imageFileName, LibraryConstant.PIC_SUFFIX_JPG, file);
        return image;
    }

    /**
     * 获取低版本的照片路径
     */
    public static String getImagePath(Context context, Uri uri, String selection) {
        String path = null;
        //通过Uri和selection获取真实的图片路径
        Cursor cursor = context.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }


    /**
     * 返回照片的uri
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String returnPath(Context context, Uri uri) {
        String path = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            //如果是document类型的uri,则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                path = PictureUtils.getImagePath(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
            } else if ("com.android.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                path = PictureUtils.getImagePath(context, contentUri, null);
            }
        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
            //如果是content类型的Uri，则使用普通方式处理
            path = PictureUtils.getImagePath(context, uri, null);
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            //如果是file类型的Uri,直接获取图片路径即可
            path = uri.getPath();
        }
        return path;
    }


    /**
     * 获取照片路径
     * 兼容高低版本
     */
    public static String getPicPath(Context context, Uri uri) {
        String imagePath = null;
        if (Build.VERSION.SDK_INT >= 19) {
            if (uri != null) {
                imagePath = returnPath(context, uri);
            }
        } else {
            if (uri != null) {
                imagePath = getImagePath(context, uri, null);
            }
        }
        return imagePath;
    }

    /**
     * 根据路径删除图片
     */
    public static void deleteTempFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    /**
     * 加载Uri图片 圆形图片
     */
    public static void loadCirclePic(Context mContext, String uri, ImageView imageView) {
        if (imageView == null) return;
        GlideApp.with(mContext)
                .load(uri)
                .placeholder(R.mipmap.library_pic_error_img)
                .error(R.mipmap.library_pic_error_img)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .optionalCircleCrop()
                .circleCrop()
                .into(imageView);
    }

    /**
     * 加载Uri图片 圆形图片
     */
    public static void loadCirclePic(Context mContext, Bitmap uri, ImageView imageView) {
        if (imageView == null) return;
        GlideApp.with(mContext)
                .load(uri)
                .placeholder(R.mipmap.library_default_avatar)
                .error(R.mipmap.library_default_avatar)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .override(imageView.getWidth(), imageView.getHeight())
                .optionalCircleCrop()
                .circleCrop()
                .into(imageView);
    }

    /**
     * 加载 图片
     */
    public static void loadRectPic(Context mContext, String uri, ImageView imageView) {
        if (imageView == null) return;
        GlideApp.with(mContext)
                .load(uri)
                .override(UIUtil.dip2px(140), UIUtil.dip2px(100))
                .placeholder(R.mipmap.library_pic_error_img)
                .error(R.mipmap.library_pic_error_img)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .fitCenter()
                .into(imageView);
    }

    /**
     * 加载 图片
     */
    public static void loadRectPic(Context mContext, Bitmap uri, ImageView imageView) {
        if (imageView == null) return;
        GlideApp.with(mContext)
                .load(uri)
                .override(UIUtil.dip2px(140), UIUtil.dip2px(100))
                .placeholder(R.mipmap.library_pic_error_img)
                .error(R.mipmap.library_pic_error_img)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .fitCenter()
                .into(imageView);
    }

    /**
     * 加载drawable图片 圆形图片
     */
    public static void loadPic(Context mContext, Drawable drawable, Drawable defaultDrawable, ImageView imageView) {
        if (imageView == null) return;
        GlideApp.with(mContext)
                .load(drawable)
                .error(defaultDrawable)
                .placeholder(defaultDrawable)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .fitCenter()
                .into(imageView);
    }

    /**
     * 加载drawable图片 圆形图片
     */
    public static void loadPic(Context mContext, String url, Drawable defaultDrawable, ImageView imageView) {
        if (imageView == null) return;
        GlideApp.with(mContext)
                .load(url)
                .error(defaultDrawable)
                .placeholder(defaultDrawable)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .optionalCircleCrop()
                .circleCrop()
                .into(imageView);
    }

    public static void loadPic(Context mContext, int drawableID, ImageView imageView) {
        if (imageView == null) return;
        GlideApp.with(mContext)
                .load(drawableID)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(imageView);
    }

}
