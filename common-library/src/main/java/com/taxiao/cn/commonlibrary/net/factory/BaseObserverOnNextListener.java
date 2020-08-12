package com.taxiao.cn.commonlibrary.net.factory;

import io.reactivex.disposables.Disposable;

/**
 * 基类 接口
 * Created by yin13 on 2020/6/30
 */
public interface BaseObserverOnNextListener<T> {

    void onSubscribe(Disposable disposable);

    void onNext(T t);

    void onError(Throwable e);

}
