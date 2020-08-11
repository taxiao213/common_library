package com.taxiao.cn.commonlibrary.module.my.setting;

import android.os.Bundle;

import com.taxiao.cn.commonlibrary.R;
import com.taxiao.cn.commonlibrary.base.BaseActivity;
import com.taxiao.cn.commonlibrary.constant.LibraryConstant;
import com.taxiao.cn.commonlibrary.model.MySeetingEnity;


/**
 * 我的设置界面
 * Created by hanqq on 2020/7/1
 * Email:yin13753884368@163.com
 */
public class MySettingActivity extends BaseActivity<MySettingPresenter, MySettingContract.View> {

    @Override
    protected int getLayoutID() {
        return R.layout.library_activity_mysetting;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        setTitleName(getString(R.string.library_my_setting_title));
    }

    @Override
    protected MySettingPresenter getPresenter() {
        return new MySettingPresenter();
    }

    @Override
    protected MySettingContract.View getContract() {
        return new MySettingContract.View<MySeetingEnity>() {
            @Override
            public void handlerResult(MySeetingEnity baseEnity, int type) {
                if (baseEnity != null) {
                    switch (type) {
                        case LibraryConstant.HANDLER_RESULT1:
                            // 上传头像

                            break;
                        case LibraryConstant.HANDLER_RESULT2:
                            // 修改昵称

                            break;
                    }
                }
            }
        };
    }
}
