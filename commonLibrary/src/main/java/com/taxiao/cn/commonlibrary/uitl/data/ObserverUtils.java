package com.taxiao.cn.commonlibrary.uitl.data;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * 观察者模式
 * Created by hanqq on 2020/7/4
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/taxiao213
 */
public class ObserverUtils<T extends IObserverInterface> {

    private ArrayList<T> mList;
    private static volatile ObserverUtils mObserverUtils;

    private ObserverUtils() {
        mList = new ArrayList<>();
    }

    public static ObserverUtils getInstance() {
        if (mObserverUtils == null) {
            synchronized (ObserverUtils.class) {
                if (mObserverUtils == null) {
                    mObserverUtils = new ObserverUtils();
                }
            }
        }
        return mObserverUtils;
    }

    // 注册接口
    public void register(T t) {
        if (t != null) {
            WeakReference<T> reference = new WeakReference(t);
            mList.add(reference.get());
        }
    }

    // 注销接口
    public void unRegister(T t) {
        if (t != null && mList != null && mList.size() > 0) {
            mList.remove(t);
        }
    }

    // 对外通知刷新
    public void notice() {
        if (mList != null && mList.size() > 0) {
            for (int i = 0; i < mList.size(); i++) {
                T t = mList.get(i);
                if (t != null) {
                    t.update();
                }
            }
        }
    }

}
