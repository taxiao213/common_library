package com.taxiao.cn.commonlibrary.base;

import android.app.Application;
import android.content.Context;
import android.os.Process;

import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshFooterCreator;
import com.scwang.smart.refresh.layout.listener.DefaultRefreshHeaderCreator;
import com.taxiao.cn.commonlibrary.R;
import com.tencent.bugly.crashreport.CrashReport;

/**
 * Application 类 初始化数据
 * Created by yin13 on 2020/6/30
 */
public abstract class BaseApplication extends Application {
    // 获取到主线程的id
    private static int mMainThreadId;
    private static Context mApplication;
    private static String BUGLY_APPID = "3052ef1305";

    static {
        //设置全局的Header构建器
        SmartRefreshLayout.setDefaultRefreshHeaderCreator(new DefaultRefreshHeaderCreator() {
            @Override
            public RefreshHeader createRefreshHeader(Context context, RefreshLayout layout) {
                layout.setPrimaryColorsId(R.color.colorWindowBg, R.color.grey_66);//全局设置主题颜色
                return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
            }
        });
        //设置全局的Footer构建器
        SmartRefreshLayout.setDefaultRefreshFooterCreator(new DefaultRefreshFooterCreator() {
            @Override
            public RefreshFooter createRefreshFooter(Context context, RefreshLayout layout) {
                //指定为经典Footer，默认是 BallPulseFooter
                return new ClassicsFooter(context).setDrawableSize(20);
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mMainThreadId = Process.myTid();
        mApplication = getApplicationContext();
        CrashReport.initCrashReport(getApplicationContext(), BUGLY_APPID, true);
        init();
    }

    public static Context getApplication() {
        return mApplication;
    }

    public static int getMainThreadId() {
        return mMainThreadId;
    }

    // 子类去实现
    protected abstract void init();

}
