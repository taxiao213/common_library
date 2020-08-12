package com.taxiao.cn.commonlibrary.module.my.setting;

import android.content.Context;


import com.taxiao.cn.commonlibrary.R;
import com.taxiao.cn.commonlibrary.base.BaseModel;
import com.taxiao.cn.commonlibrary.constant.LibraryConstant;
import com.taxiao.cn.commonlibrary.model.MySeetingEnity;
import com.taxiao.cn.commonlibrary.net.ApiMethod;
import com.taxiao.cn.commonlibrary.net.factory.ObserverOnNextListener;
import com.taxiao.cn.commonlibrary.net.factory.ProgressObserver;
import com.taxiao.cn.commonlibrary.uitl.data.HexMap;
import com.taxiao.cn.commonlibrary.uitl.down.Function;
import com.taxiao.cn.commonlibrary.uitl.down.UpLoadFileUtils;
import com.taxiao.cn.commonlibrary.uitl.net.NetWorkUtils;
import com.taxiao.cn.commonlibrary.uitl.ui.ToastUtils;

import java.util.HashMap;

/**
 * 我的设置界面 model
 * Created by hanqq on 2020/7/1
 * Email:yin13753884368@163.com
 */
public class MySettingModel extends BaseModel<MySettingPresenter, MySettingContract.Model> {

    public MySettingModel(MySettingPresenter mySettingPresenter) {
        super(mySettingPresenter);
    }

    @Override
    protected MySettingContract.Model getContract() {
        return new MySettingContract.Model() {
            @Override
            public void chooseAvatar(Context context, String filePath) {
                UpLoadFileUtils.updateLoadFile(context, filePath, LibraryConstant.HANDLER_RESULT1, new Function<String>() {
                    @Override
                    public void action(String url) {
                        MySeetingEnity enity = new MySeetingEnity();
                        enity.setUrl(url);
                        p.getContract().responseResult(enity, LibraryConstant.HANDLER_RESULT1);

                    }
                });
            }

            @Override
            public void modifyName(Context context, String name) {
                updateNickName(context, name);
            }
        };
    }

    /**
     * 修改昵称接口
     *
     * @param nickName
     */
    private void updateNickName(Context context, final String nickName) {
        if (!NetWorkUtils.isNetConnected(context)) {
            ToastUtils.show(context, context.getResources().getString(R.string.library_net_error));
            return;
        }

        ObserverOnNextListener observerOnNextListener = new ObserverOnNextListener<MySeetingEnity>() {

            @Override
            public void onNext(MySeetingEnity model) {
                if (model != null) {
                    p.getContract().responseResult(model, LibraryConstant.HANDLER_RESULT2);
                }
            }

            @Override
            public void onError(Throwable e) {

            }
        };

        HashMap<String, Object> map = new HexMap();
        map.put("nickName", nickName);

        ApiMethod.updateNickName(new ProgressObserver<MySeetingEnity>(context, observerOnNextListener, true), map);
    }
}
