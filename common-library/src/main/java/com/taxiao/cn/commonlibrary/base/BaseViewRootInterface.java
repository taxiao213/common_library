package com.taxiao.cn.commonlibrary.base;

import android.os.Bundle;
import android.support.annotation.Nullable;


/**
 * Fragment 和 Activity 基类接口
 * Created by taxiao on 2020/7/5
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/taxiao213
 * 微信公众号:他晓
 */
public interface BaseViewRootInterface {

    void onCreate(@Nullable Bundle savedInstanceState);

    void onDestroy();

}
