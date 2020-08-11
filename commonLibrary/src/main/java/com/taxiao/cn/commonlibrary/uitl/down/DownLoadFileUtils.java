package com.taxiao.cn.commonlibrary.uitl.down;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;


import com.taxiao.cn.commonlibrary.base.BaseApplication;
import com.taxiao.cn.commonlibrary.constant.LibraryConstant;
import com.taxiao.cn.commonlibrary.net.ApiSercice;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.concurrent.Executors;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * 下载文件 图片
 * Created by Han on 2017/6/1.
 */

public class DownLoadFileUtils {

    private static volatile DownLoadFileUtils downLoadIconUtil;
    private final OkHttpClient.Builder builder;
    private String FOLDER_NAME = null;//根据type 切换存储的文件夹

    public static DownLoadFileUtils getInstance() {
        if (downLoadIconUtil == null) {
            synchronized (DownLoadFileUtils.class) {
                if (downLoadIconUtil == null) {
                    downLoadIconUtil = new DownLoadFileUtils();
                }
            }
        }
        return downLoadIconUtil;
    }

    private DownLoadFileUtils() {
        builder = new OkHttpClient.Builder();
    }


    /**
     * 保存文件 图片
     *
     * @param url           下载路径
     * @param fileName      文件名称
     * @param isCompression 是否需要压缩 只有个人头像能用上
     */
    public void saveFile(Context context, String url, String fileName, final int type, final boolean isCompression, final Function<String> function) {
        if (!TextUtils.isEmpty(url) && url.contains("/") && url.startsWith("http")) {
            int indexOf = url.lastIndexOf("/");
            final String baseUrl = url.substring(0, indexOf + 1);
            final String urlpic = url.substring(indexOf + 1);
            String end = "";
            end = LibraryConstant.PIC_SUFFIX_PNG;
            File appDir = new File(context.getCacheDir(), FOLDER_NAME);
            if (!appDir.exists()) {
                appDir.mkdirs();
            }
            final File downloadFile = new File(appDir, fileName + end);
            if (android.os.Process.myTid() == BaseApplication.getMainThreadId()) {
                Executors.newSingleThreadExecutor().execute(new Runnable() {
                    @Override
                    public void run() {
                        downFile(baseUrl, urlpic, downloadFile, isCompression, type, function);
                    }
                });
            } else {
                downFile(baseUrl, urlpic, downloadFile, isCompression, type, function);
            }
        } else {
            callFailure(function);
        }
    }

    /**
     * 返回失败的结果  0
     *
     * @param function Function<String>
     */
    private void callFailure(Function<String> function) {
        if (function != null) {
            function.action("0");
        }
    }


    /**
     * 保存图片
     *
     * @param baseUrl       跟地址
     * @param urlpic        路径
     * @param isCompression 是否需要压缩 只有图片能用上
     * @param type          压缩保存到不同的路径下
     */
    private void downFile(String baseUrl, String urlpic, File filePic, boolean isCompression, int type, Function<String> function) {
        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(builder.build())
                    .build();

            ApiSercice fileDownloadApi = retrofit.create(ApiSercice.class);
            Response<ResponseBody> response = fileDownloadApi.downloadPicWithUrl(urlpic).execute();
            if (response != null && response.isSuccessful()) {
                ResponseBody body = response.body();
                if (body != null) {
                    InputStream is = body.byteStream();
                    if (filePic != null && !filePic.exists()) {
                        filePic.createNewFile();
                    }
                    long fileSizeDownloaded = 0;
                    if (isSDCardEnable()) {
                        FileOutputStream fos = null;
                        try {
                            fos = new FileOutputStream(filePic);
                            int count = 0;
                            byte[] buffer = new byte[1024];
                            while ((count = is.read(buffer)) != -1) {
                                fos.write(buffer, 0, count);
                                fileSizeDownloaded += count;
                            }
                            fos.flush();
                        } finally {
                            if (fos != null) {
                                fos.close();
                            }
                            if (is != null) {
                                is.close();
                            }
                        }

                        if (body.contentLength() != fileSizeDownloaded) {
                            if (filePic != null && filePic.exists()) {
                                filePic.delete();
                            }
                        } else {
                            if (isCompression) {
                                if (filePic != null && filePic.exists()) {
                                    String absolutePath = filePic.getAbsolutePath();
                                    if (!TextUtils.isEmpty(absolutePath)) {
                                        CompressPicUtil.getCompToRealPath(absolutePath, type);
                                    }
                                }
                            }
                        }
                        if (function != null)
                            if (filePic != null && filePic.exists()) {
                                function.action(filePic.getAbsolutePath());
                                return;
                            }
                    }
                }
            }
        } catch (Exception e) {

        }
        callFailure(function);
    }

    private boolean isSDCardEnable() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }
}
