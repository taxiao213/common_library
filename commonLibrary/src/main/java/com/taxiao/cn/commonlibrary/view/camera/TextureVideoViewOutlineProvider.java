package com.taxiao.cn.commonlibrary.view.camera;

import android.graphics.Outline;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewOutlineProvider;

import com.taxiao.cn.commonlibrary.uitl.data.LogUtils;

/**
 * 圆角 或者 椭圆
 * Created by hanqq on 2020/7/30
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/taxiao213
 */
public class TextureVideoViewOutlineProvider extends ViewOutlineProvider {
    private float mRadius;

    public TextureVideoViewOutlineProvider(float radius) {
        this.mRadius = radius;
    }

    public TextureVideoViewOutlineProvider() {
    }

    @Override
    public void getOutline(View view, Outline outline) {
        if (mRadius != 0) {
            Rect rect = new Rect();
            view.getGlobalVisibleRect(rect);
            LogUtils.d("rect " + rect.toString());
            int leftMargin = 0;
            int topMargin = 0;
            Rect selfRect = new Rect(leftMargin, topMargin, rect.right - rect.left - leftMargin, rect.bottom - rect.top - topMargin);
            outline.setRoundRect(selfRect, mRadius);
        } else {
            int left = 0;
            int top = (view.getHeight() - view.getWidth()) / 2;
            int right = view.getWidth();
            int bottom = (view.getHeight() - view.getWidth()) / 2 + view.getWidth();
            outline.setOval(left, top, right, bottom);
        }
    }

}
