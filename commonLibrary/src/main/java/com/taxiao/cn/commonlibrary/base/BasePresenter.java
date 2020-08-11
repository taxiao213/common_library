package com.taxiao.cn.commonlibrary.base;

import java.lang.ref.WeakReference;

/**
 * BasePresenter 层
 * Created by taxiao on 2020/6/28
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/taxiao213
 * 微信公众号:他晓
 */
public abstract class BasePresenter<V extends BaseViewRootInterface, M extends BaseModel, CONTRACT> {
    protected M m;
    private WeakReference<V> weakReference;

    public BasePresenter() {
        m = getModel();
    }

    // 绑定BaseView
    public void bindView(V v) {
        weakReference = new WeakReference<>(v);
    }

    // 解绑BaseView
    public void unBindView() {
        if (weakReference != null) {
            weakReference.clear();
            weakReference = null;
            System.gc();
        }
    }

    // 获取 view 层
    public V getView() {
        if (weakReference != null) {
            return weakReference.get();
        }
        return null;
    }

    // 获取子类具体的 model 层
    protected abstract M getModel();

    // 获取子类具体的契约
    protected abstract CONTRACT getContract();

}
