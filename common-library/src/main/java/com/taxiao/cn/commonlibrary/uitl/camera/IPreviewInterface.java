package com.taxiao.cn.commonlibrary.uitl.camera;


/**
 * camera 回调
 * Created by hanqq on 2020/7/16
 */
public interface IPreviewInterface {

    void onBind(String cameraId);

    void onUnbind();
}
