package com.taxiao.cn.commonlibrary.view.refresh;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;


import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.taxiao.cn.commonlibrary.R;
import com.taxiao.cn.commonlibrary.base.BaseFragment;
import com.taxiao.cn.commonlibrary.base.BasePresenter;
import com.taxiao.cn.commonlibrary.uitl.ui.UIUtil;
import com.taxiao.cn.commonlibrary.view.custom.CustomFrameLayout;
import com.taxiao.cn.commonlibrary.view.recyclerview.GridVerticalItemDecoration;


/**
 * 下拉加载 recyclerview fragment
 * Created by yin13 on 2019/8/29
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class SmartRefreshFragment extends BaseFragment {

    SmartRefreshLayout smart;
    CustomFrameLayout customFrameLayout;
    RecyclerView ry;

    private ISwipRefreshInterface refreshInterface;
    private Context activity;
    public RecyclerView.Adapter adapter;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = context;
    }

    @Override
    protected int getLayoutID() {
        return R.layout.library_include_swip_refresh;
    }

    @Override
    protected void initView(View view, Bundle savedInstanceState) {
        smart = view.findViewById(R.id.smart);
        customFrameLayout = view.findViewById(R.id.fl_loading);
        ry = view.findViewById(R.id.ry);
        if (refreshInterface != null) {
            RecyclerView.LayoutManager layoutManager = refreshInterface.getLayoutManager();
            ry.setLayoutManager(layoutManager);
            if (layoutManager instanceof GridLayoutManager) {
                ry.setPadding(UIUtil.dip2px(10), 0, 0, 0);
                ry.addItemDecoration(new GridVerticalItemDecoration());
            }
            adapter = refreshInterface.getAdapter();
            ry.setAdapter(adapter);
            customFrameLayout.setReplaceText(refreshInterface.getReplaceText());
            customFrameLayout.setReplaceDrawable(refreshInterface.getReplaceDrawable());
        }

        smart.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(1000);
                loadingData();
            }
        });

        smart.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(1000);
                loadingData();
            }
        });

        if (refreshInterface != null && refreshInterface.initLoading()) {
            loadingData();
        }
    }

    @Override
    protected BasePresenter getPresenter() {
        return null;
    }

    @Override
    protected Object getContract() {
        return null;
    }

    private void loadingData() {
        hideFrameView();
        if (refreshInterface != null) {
            refreshInterface.clear();
        }
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
        if (refreshInterface != null) {
            refreshInterface.getData();
        }
    }

    public void setInterface(ISwipRefreshInterface searchInterface) {
        this.refreshInterface = searchInterface;
    }

    /**
     * 展示 frame
     *
     * @param netVisible true 网络 false 空数据
     */
    public void showFrameView(boolean netVisible) {
        if (customFrameLayout != null && ry != null) {
            if (netVisible) {
                setNetErrorVisible(true);
            } else {
                setEmptVisible(true);
            }
            ry.setVisibility(View.GONE);
        }
    }

    /**
     * 隐藏 frame
     */
    public void hideFrameView() {
        if (customFrameLayout != null && ry != null) {
            ry.setVisibility(View.VISIBLE);
            setEmptVisible(false);
            setNetErrorVisible(false);
        }
    }

    /**
     * 设置网络异常按钮隐藏和显示
     *
     * @param netVisible true 显示
     */
    private void setNetErrorVisible(boolean netVisible) {
        if (customFrameLayout != null) {
            customFrameLayout.setNetErrorVisible(netVisible);
        }
    }

    /**
     * 数据为空隐藏和显示
     */
    private void setEmptVisible(boolean emptVisible) {
        if (customFrameLayout != null) {
            customFrameLayout.setDataEmptyVisible(emptVisible);
        }
    }
}
