package com.taxiao.cn.commonlibrary.view.recyclerview;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.taxiao.cn.commonlibrary.uitl.ui.UIUtil;


/**
 * 网格分割线 由于宽和高相同，所有分割时只能设置单边距离  竖向
 * Create by Han on 2019/5/21
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class GridVerticalItemDecoration extends RecyclerView.ItemDecoration {
    private int mSpace;

    public GridVerticalItemDecoration(int space) {
        this.mSpace = space;
    }

    public GridVerticalItemDecoration() {
        mSpace = UIUtil.dip2px(10);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int position = parent.getChildLayoutPosition(view);
        GridLayoutManager manager = (GridLayoutManager) parent.getLayoutManager();
        if (manager != null) {
            int gridSize = manager.getSpanCount();//每列数
            outRect.right = mSpace;
            if (position < gridSize) {
                outRect.top = mSpace;
                setBottom(outRect, mSpace);
            } else {
                setBottom(outRect, mSpace);
            }
        }
    }

    /**
     * 设置底部边距
     *
     * @param outRect Rect
     * @param spacing int
     */
    private void setBottom(@NonNull Rect outRect, int spacing) {
        outRect.bottom = spacing;
    }
}
