package com.taxiao.cn.commonlibrary.uitl.ui;

import android.app.Activity;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Activity 管理类
 * Created by hanqq on 2020/7/2
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/taxiao213
 */
public class ActivityManager {

    private static volatile ActivityManager mActivityManager;
    private List<Activity> mActivityList;

    private ActivityManager() {
        if (mActivityList == null) {
            mActivityList = new ArrayList<>();
        }
    }

    public static ActivityManager getActivityManager() {
        if (mActivityManager == null) {
            synchronized (ActivityManager.class) {
                if (mActivityManager == null) {
                    mActivityManager = new ActivityManager();
                }
            }
        }
        return mActivityManager;
    }

    // 将 activity 加入到集合 除了主界面
    public void addActivity(Activity activity) {
//        if (activity instanceof MainActivity) return;
        WeakReference<Activity> reference = new WeakReference(activity);
        if (mActivityList != null) {
            mActivityList.add(reference.get());
        }
    }

    // 将 activity 移除集合
    public void removeActivity(Activity activity) {
        if (mActivityList != null && mActivityList.contains(activity)) {
            mActivityList.remove(activity);
            activity.finish();
        }
    }

    // 移除所有的 activity
    public void removeAllActivity() {
        if (mActivityList != null && mActivityList.size() > 0) {
            Iterator<Activity> iterator = mActivityList.iterator();
            while (iterator.hasNext()) {
                Activity next = iterator.next();
                iterator.remove();
                next.finish();
            }
        }
    }

    // 获取当前集合的 size
    public int currentSize() {
        if (mActivityList != null) {
            return mActivityList.size();
        }
        return 0;
    }
}
