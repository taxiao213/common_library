package com.taxiao.cn.commonlibrary.uitl.ui;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.taxiao.cn.commonlibrary.uitl.data.LogUtils;


/**
 * dp 转换
 * Created by Han on 2018/8/25
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class UIUtil {
    public static final String TAG = UIUtil.class.getSimpleName();

    /**
     * dp 转 px
     */
    public static int dip2px(float dpValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px 转 dp
     */
    public static int px2dp(float pxValue) {
        final float scale = Resources.getSystem().getDisplayMetrics().density;
        return (int) (pxValue / scale);
    }

    /**
     * 开启键盘
     */
    public static void openBroad(Context mContext, View view) {
        if (mContext == null) return;
        view.setFocusable(true);
        view.setFocusableInTouchMode(true);
        view.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.showSoftInput(view, 0);
        }
    }

    /**
     * 关闭键盘
     */
    public static void closeBroad(Context mContext, View view) {
        if (mContext == null) return;
        InputMethodManager inputMethodManager = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 获取App的版本号
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {
        PackageManager mPackageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = mPackageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取App的版本名称
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {
        PackageManager mPackageManager = context.getPackageManager();
        try {
            PackageInfo packageInfo = mPackageManager.getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取屏幕分辨率
     *
     * @param context 去除了刘海屏 高度
     */
    public static int[] getScreenDisplay(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int width = windowManager.getDefaultDisplay().getWidth();// 手机屏幕的宽度
        int height = windowManager.getDefaultDisplay().getHeight();// 手机屏幕的高度
        int result[] = {width, height};
        LogUtils.d(TAG, " getScreenDisplay width = " + width + " height = " + height);
        return result;
    }

    /**
     * 获取屏幕分辨率
     * 包括刘海屏高度
     */
    public static Point getRealSize(Context context) {
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Point outSize = new Point();
        windowManager.getDefaultDisplay().getRealSize(outSize);
        LogUtils.d(TAG, " getScreenDisplay width = " + outSize.x + " height = " + outSize.y);
        return outSize;
    }

    /**
     * 获取屏幕分辨率
     * 包括刘海屏高度
     */
    public static DisplayMetrics getDisplayMetrics(Context context) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        windowManager.getDefaultDisplay().getRealMetrics(outMetrics);
        LogUtils.d(TAG, " getDisplayMetrics width = " + outMetrics.widthPixels + " height = " + outMetrics.heightPixels);
        return outMetrics;
    }
}
