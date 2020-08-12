package com.taxiao.cn.commonlibrary.net;


import com.taxiao.cn.commonlibrary.net.factory.CustomizeConverterFactory;
import com.taxiao.cn.commonlibrary.uitl.data.LogUtils;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okio.Buffer;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * 网络请求管理类
 * Created by taxiao on 2020/6/28
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/taxiao213
 * 微信公众号:他晓
 */
public class ApiManager {
    // 地址
    public static final String BASE_RUL = "http://www.zkhonry.com:9000/zkhonry-mobile-interface/";
    // 上传文件路径
    public final static String UPLOAD_PIC_ACTION = BASE_RUL + "savePicAction/savePic.action";
    // log false关闭  true打开
    public static final boolean LOG_SWITCH = true;

    // OkHttp 配置
    public static final Long READ_TIME_OUT = 1L;
    public static final Long WRITE_TIME_OUT = 1L;
    public static final Long CONNECT_TIME_OUT = 3L;
    private static volatile ApiSercice apiSercice;

    public ApiManager() {
        OkHttpClient okHttpClient = new OkHttpClient
                .Builder()
                .readTimeout(READ_TIME_OUT, TimeUnit.MINUTES)
                .writeTimeout(WRITE_TIME_OUT, TimeUnit.MINUTES)
                .connectTimeout(CONNECT_TIME_OUT, TimeUnit.MINUTES)
                .addNetworkInterceptor(httpInterceptor)
                .proxy(Proxy.NO_PROXY)
                .build();

        Retrofit retrofit = new Retrofit
                .Builder()
                .client(okHttpClient)
                .addConverterFactory(CustomizeConverterFactory.create())
                // 适配 rxjava2 暂不支持rxjava3
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(BASE_RUL)
                .build();
        apiSercice = retrofit.create(ApiSercice.class);
    }

    public static ApiSercice getApiService() {
        if (apiSercice == null) {
            synchronized (ApiManager.class) {
                if (apiSercice == null) {
                    new ApiManager();
                }
            }
        }
        return apiSercice;
    }

    /**
     * 拦截器可以添加公共参数 也可以输出请求链接
     */
    private Interceptor httpInterceptor = new Interceptor() {
        @NotNull
        @Override
        public Response intercept(@NotNull Chain chain) throws IOException {
            Request request = chain.request();
            Request.Builder newBuilder = request.newBuilder();
            RequestBody requestBody = request.body();
            String path = request.url().url().getPath();
            if (requestBody instanceof FormBody) {
                FormBody.Builder builder = new FormBody.Builder();
                FormBody body = (FormBody) requestBody;
                if (body.size() > 0) {
                    for (int i = 0; i < body.size(); i++) {
                        builder.addEncoded(body.encodedName(i), body.encodedValue(i));
                    }
                }
                // 加公共参数
                builder.add("type", "Android");
                newBuilder.method(request.method(), builder.build());
            }
            Request build = newBuilder.build();
            LogUtils.e("请求方式 method== " + build.method() + " , url== " + build.url() + "?" + bodyToString(build.body()));
            return chain.proceed(build);
        }
    };

    /**
     * 将公共参数拼装
     */
    private String bodyToString(RequestBody request) {
        try {
            final RequestBody copy = request;
            final Buffer buffer = new Buffer();
            if (copy != null)
                copy.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (final Exception e) {
            return "";
        }
    }

}
