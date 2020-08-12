package com.taxiao.cn.commonlibrary.uitl.down;

import android.content.Context;
import android.text.TextUtils;


import com.taxiao.cn.commonlibrary.constant.LibraryConstant;
import com.taxiao.cn.commonlibrary.net.ApiManager;
import com.taxiao.cn.commonlibrary.uitl.ui.ToastUtils;

import org.json.JSONObject;

import java.io.File;
import java.util.concurrent.Executors;

import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 上传文件的工具类
 * Created by Han on 2018/9/5
 * Email:yin13753884368@163.com
 * type 切换不同的action地址
 */
public class UpLoadFileUtils {

    private static String action = null;

    /**
     * @param imgPath  文件路径
     * @param type     1 上传个人头像(需要提示)  2上传身份证照片(需要提示)    3聊天界面上传文件(不需要提示)
     * @param consumer 回调
     */
    public static void updateLoadFile(Context context, final String imgPath, final int type, final Function<String> consumer) {
        if (TextUtils.isEmpty(imgPath)) return;
        if (type == LibraryConstant.HANDLER_RESULT1) {
            action = ApiManager.UPLOAD_PIC_ACTION;
        } else if (type == LibraryConstant.HANDLER_RESULT2) {

        } else if (type == LibraryConstant.HANDLER_RESULT3) {

        }
        Executors.newSingleThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                String upload = upload(ApiManager.UPLOAD_PIC_ACTION, new File(imgPath), type);
                try {
                    if (upload != null) {
                        JSONObject object = new JSONObject(upload);
                        String resMsg = object.getString("resMsg");
                        if (object.has("resCode")) {
                            String code = object.getString("resCode");
                            if (type == LibraryConstant.HANDLER_RESULT1) {
                                if (TextUtils.equals(code, "2") || TextUtils.equals(code, "-2")) {
                                    if (resMsg != null) {
                                        ToastUtils.show(context, resMsg);
                                    }
                                    if (TextUtils.equals(code, "2")) {
                                        String url = object.getString("data");
                                        consumer.action(url);
                                    } else {
                                        consumer.action("0");
                                    }
                                } else {
                                    consumer.action("0");
                                }
                            } else {
                                consumer.action("0");
                            }
                        } else {
                            consumer.action("0");
                        }
                    } else {
                        consumer.action("0");
                    }
                } catch (Exception e) {
                    consumer.action("0");
                    e.printStackTrace();
                }
            }
        });

    }

    /**
     * @param url  上传地址
     * @param file 文件
     * @param type action不一样替换不同的参数 1 上传个人头像(需要提示)  2聊天界面上传文件(不需要提示)
     * @return
     */
    public static String upload(final String url, File file, int type) {
        /**
         * 表头:"Content-Disposition","form-data; name=\"mFile\"; filename=\"" + "file" + "\""
         * name=\"mFile\"  与后台接口保持一致
         */
        OkHttpClient.Builder mOkHttpClient = new OkHttpClient.Builder();
        RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addPart(Headers.of(
                        "Content-Disposition",
                        "form-data; name=\"mFile\"; filename=\"" + "file" + "\""), fileBody);
        String flieType = "jpg";

        String name = file.getName();
        if (!TextUtils.isEmpty(name)) {
            if (name.contains(".")) {
                flieType = name.substring(name.lastIndexOf(".") + 1);
            }
        }

        Request request = new Request.Builder()
                .url(url)
                .post(builder.build())
                .build();

        Response response = null;
        try {
            response = mOkHttpClient.build().newCall(request).execute();
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (response != null && response.body() != null) {
                response.body().close();
            }
        }
        return null;
    }

}
