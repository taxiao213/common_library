package com.taxiao.cn.commonlibrary.module.my.setting;

import android.content.Context;

import com.taxiao.cn.commonlibrary.base.BasePresenter;
import com.taxiao.cn.commonlibrary.model.MySeetingEnity;

/**
 * 我的设置界面 presenter
 * Created by hanqq on 2020/7/1
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/taxiao213
 */
public class MySettingPresenter extends BasePresenter<MySettingActivity, MySettingModel, MySettingContract.Presenter> {
    @Override
    protected MySettingModel getModel() {
        return new MySettingModel(this);
    }

    @Override
    protected MySettingContract.Presenter getContract() {
        return new MySettingContract.Presenter<MySeetingEnity>() {
            @Override
            public void chooseAvatar(Context context, String filePath) {
                m.getContract().chooseAvatar(context, filePath);
            }

            @Override
            public void modifyName(Context context, String name) {
                m.getContract().modifyName(context, name);
            }

            @Override
            public void responseResult(MySeetingEnity mySeetingEnity, int type) {
                getView().getContract().handlerResult(mySeetingEnity, type);
            }
        };
    }
}
