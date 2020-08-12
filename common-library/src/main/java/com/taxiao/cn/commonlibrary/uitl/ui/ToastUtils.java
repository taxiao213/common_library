package com.taxiao.cn.commonlibrary.uitl.ui;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.widget.Toast;

import com.taxiao.cn.commonlibrary.base.BaseApplication;


/**
 * 吐司相关工具类
 * Created by hanqq on 2020/06/30
 */
public class ToastUtils {

    public static void show(Context context, int stringID) {
        show(context, context.getString(stringID), Toast.LENGTH_SHORT);
    }

    public static void show(Context context, String text) {
        show(context, text, Toast.LENGTH_SHORT);
    }

    public static void showLong(Context context, String text) {
        show(context, text, Toast.LENGTH_LONG);
    }

    private static void show(Context context, final String text, final int duration) {
        if (TextUtils.isEmpty(text)) return;
        if (android.os.Process.myTid() == BaseApplication.getMainThreadId()) {
            showToast(context, text, duration);
        } else {
            new Handler(Looper.getMainLooper()).post(new Runnable() {
                @Override
                public void run() {
                    showToast(context, text, duration);
                }
            });
        }
    }

    private static void showToast(Context context, String text, int duration) {
        Toast.makeText(context, text, duration).show();
    }

}