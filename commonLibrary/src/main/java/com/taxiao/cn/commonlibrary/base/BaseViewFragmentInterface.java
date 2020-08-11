package com.taxiao.cn.commonlibrary.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * fragment 基类接口
 * {@link BaseViewRootInterface}
 * Created by taxiao on 2020/7/5
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/taxiao213
 * 微信公众号:他晓
 */
public interface BaseViewFragmentInterface extends BaseViewRootInterface {
    void onAttach(Context context);

    View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);
}
