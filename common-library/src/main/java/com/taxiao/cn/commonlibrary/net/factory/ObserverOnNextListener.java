package com.taxiao.cn.commonlibrary.net.factory;

import io.reactivex.disposables.Disposable;

/**
 * 对外提供接口回调
 * Created by yin13 on 2020/6/30
 */
public abstract class ObserverOnNextListener<T> implements BaseObserverOnNextListener<T> {

    @Override
    public void onSubscribe(Disposable disposable) {

    }

}
