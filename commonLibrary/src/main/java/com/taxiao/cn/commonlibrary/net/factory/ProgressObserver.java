package com.taxiao.cn.commonlibrary.net.factory;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;


import com.taxiao.cn.commonlibrary.uitl.data.LogUtils;

import java.util.ArrayList;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


/**
 * 观察者
 * Created by hanqq on 2020/6/30
 */
public class ProgressObserver<T> implements Observer<T> {

    private static ArrayList<ProgressDialog> mDialogArrayList = new ArrayList<>();
    private BaseObserverOnNextListener mObserverOnNextListener;
    private Disposable mDisposable;
    private boolean mShowProgress;

    public ProgressObserver(Context context, BaseObserverOnNextListener observerOnNextListener, boolean showProgress) {
        this.mObserverOnNextListener = observerOnNextListener;
        this.mShowProgress = showProgress;
        dialogInit(context);
    }

    @Override
    public void onSubscribe(@NonNull Disposable d) {
        LogUtils.e("onSubscribe");
        this.mDisposable = d;
        if (mObserverOnNextListener != null) {
            mObserverOnNextListener.onSubscribe(d);
        }
        dialogShow();
    }

    @Override
    public void onNext(@NonNull T t) {
        LogUtils.e("onNext" + t.toString());
        if (mObserverOnNextListener != null) {
            mObserverOnNextListener.onNext(t);
        }
    }

    @Override
    public void onError(@NonNull Throwable e) {
        LogUtils.e("onError" + e.getMessage());
        if (mObserverOnNextListener != null) {
            mObserverOnNextListener.onError(e);
        }
        dialogDismiss();
    }

    @Override
    public void onComplete() {
        LogUtils.e("onComplete");
        if (mDisposable != null) {
            mDisposable.dispose();
        }
        dialogDismiss();
    }

    /**
     * 进度条初始化
     *
     * @param context
     */
    private void dialogInit(Context context) {
        if (mDialogArrayList.size() == 0) {
            ProgressDialog dialog = new ProgressDialog(context);
            mDialogArrayList.add(dialog);
            dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                @Override
                public void onDismiss(DialogInterface dialog) {
                    dialogDismiss();
                }
            });
        }
    }

    /**
     * 进度条显示
     */
    private void dialogShow() {
        if (mDialogArrayList.size() != 0) {
            ProgressDialog progressDialog = mDialogArrayList.get(0);
            if (progressDialog != null && mShowProgress) {
                progressDialog.show();
            }
        }
    }

    /**
     * 进度条隐藏
     */
    private void dialogDismiss() {
        if (mDialogArrayList.size() != 0) {
            synchronized (ProgressObserver.class) {
                ProgressDialog progressDialog = mDialogArrayList.get(0);
                if (progressDialog != null) {
                    progressDialog.dismiss();
                    mDialogArrayList.remove(progressDialog);
                    progressDialog = null;
                }
            }
        }
    }
}
