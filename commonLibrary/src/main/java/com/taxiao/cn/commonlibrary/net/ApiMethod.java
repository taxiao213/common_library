package com.taxiao.cn.commonlibrary.net;



import com.taxiao.cn.commonlibrary.model.MySeetingEnity;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;


/**
 * 接口方法
 * Created by taxiao on 2020/6/28
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/taxiao213
 * 微信公众号:他晓
 */
public class ApiMethod {

    /**
     * 封装线程管理和订阅的过程
     * observable 被观察者
     * observer 观察者
     */
    private static void ApiSubscribe(Observable observable, Observer observer) {
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }


    /**
     * 修改昵称
     */
    public static void updateNickName(Observer<MySeetingEnity> observer, HashMap<String, Object> map) {
        ApiSubscribe(ApiManager.getApiService().updateNickName(map), observer);
    }

}
