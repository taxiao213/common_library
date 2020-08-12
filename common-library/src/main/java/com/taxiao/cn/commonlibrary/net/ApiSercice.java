package com.taxiao.cn.commonlibrary.net;


import com.taxiao.cn.commonlibrary.model.BaseEnity;
import com.taxiao.cn.commonlibrary.model.MySeetingEnity;

import java.util.HashMap;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * 接口类
 * Created by taxiao on 2020/6/28
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/taxiao213
 * 微信公众号:他晓
 */
public interface ApiSercice {


    @FormUrlEncoded
    @POST("modify/name.action")
    Observable<BaseEnity<MySeetingEnity>> updateNickName(@FieldMap HashMap<String, Object> map);

    // 下载图片
    @Streaming
    @GET
    Call<ResponseBody> downloadPicWithUrl(@Url String url);

}

