package com.taxiao.cn.commonlibrary.view.refresh;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;


/**
 * Created by yin13 on 2019/10/12
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/taxiao213
 */
public interface ISwipRefreshInterface extends IBaseInterface {

    RecyclerView.LayoutManager getLayoutManager();

    RecyclerView.Adapter getAdapter();//获取适配器

    void clear();//清除数据

    String getReplaceText();//占位语

    Drawable getReplaceDrawable();//占位图

    boolean initLoading();//初始化是否加载数据
}
