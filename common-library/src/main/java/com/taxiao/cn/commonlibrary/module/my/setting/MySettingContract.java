package com.taxiao.cn.commonlibrary.module.my.setting;

import android.content.Context;

import com.taxiao.cn.commonlibrary.model.BaseEnity;


/**
 * 我的设置界面 contract
 * Created by hanqq on 2020/7/1
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/taxiao213
 */
public interface MySettingContract {

    interface Model {
        // 选择头像
        void chooseAvatar(Context context, String filePath);

        // 修改昵称
        void modifyName(Context context, String name);
    }

    interface View<T extends BaseEnity> {
        // 结果回调
        void handlerResult(T t, int type);
    }

    interface Presenter<T extends BaseEnity> {
        // 选择头像
        void chooseAvatar(Context context, String filePath);

        // 修改昵称
        void modifyName(Context context, String name);

        // 将结果返回
        void responseResult(T t, int type);
    }

}
