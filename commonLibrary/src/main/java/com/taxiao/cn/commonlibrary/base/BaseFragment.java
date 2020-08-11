package com.taxiao.cn.commonlibrary.base;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * BaseFragment 基类
 * Created by hanqq on 2020/6/30
 */
public abstract class BaseFragment<P extends BasePresenter, CONTRACT> extends Fragment implements BaseViewFragmentInterface{

    protected P p;
    //获取宿主Activity
    protected Context mContext;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(getLayoutID(), container, false);
        p = getPresenter();
        if (p != null) {
            p.bindView(this);
        }
        initView(view, savedInstanceState);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (p != null) {
            p.unBindView();
        }
    }

    // 获取资源id
    protected abstract int getLayoutID();

    // 子类实现
    protected abstract void initView(View view, Bundle savedInstanceState);

    // 获取子类具体的 Presenter
    protected abstract P getPresenter();

    // 获取子类具体的契约
    protected abstract CONTRACT getContract();

}
