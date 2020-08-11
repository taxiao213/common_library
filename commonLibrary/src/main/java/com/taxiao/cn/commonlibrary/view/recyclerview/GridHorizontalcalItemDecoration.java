package com.taxiao.cn.commonlibrary.view.recyclerview;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.taxiao.cn.commonlibrary.uitl.data.LogUtils;
import com.taxiao.cn.commonlibrary.uitl.ui.UIUtil;


/**
 * 网格分割线 由于宽和高相同，所有分割时只能设置单边距离  横向
 * Create by Han on 2019/5/21
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class GridHorizontalcalItemDecoration extends RecyclerView.ItemDecoration {
    protected static final String TAG = GridHorizontalcalItemDecoration.class.getSimpleName();

    private int mSpace;

    public GridHorizontalcalItemDecoration(int space) {
        this.mSpace = space;
    }

    public GridHorizontalcalItemDecoration() {
        mSpace = UIUtil.dip2px(10);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildLayoutPosition(view);
        GridLayoutManager manager = (GridLayoutManager) parent.getLayoutManager();
        if (manager != null) {
            int gridSize = manager.getSpanCount();//每列数
            LogUtils.d(TAG, "gridSize " + gridSize);
            outRect.left = mSpace;
            outRect.right = mSpace;
            outRect.top = mSpace;
            outRect.bottom = mSpace;
        }
    }
}
