package com.taxiao.cn.commonlibrary.base;


/**
 * BaseModel 层
 * Created by taxiao on 2020/6/28
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/taxiao213
 * 微信公众号:他晓
 */
public abstract class BaseModel<P extends BasePresenter, CONTRACT> {
    protected P p;

    public BaseModel(P p) {
        this.p = p;
    }

    //  获取子类具体的契约
    protected abstract CONTRACT getContract();
}
