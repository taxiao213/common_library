package com.taxiao.cn.commonlibrary.uitl.camera;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.media.Image;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Gravity;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.taxiao.cn.commonlibrary.R;
import com.taxiao.cn.commonlibrary.uitl.data.LogUtils;
import com.taxiao.cn.commonlibrary.uitl.down.Function;
import com.taxiao.cn.commonlibrary.uitl.down.Function2;
import com.taxiao.cn.commonlibrary.uitl.permission.XXPermissionsUtils;
import com.taxiao.cn.commonlibrary.uitl.ui.ToastUtils;
import com.taxiao.cn.commonlibrary.uitl.ui.UIUtil;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * CAMERA1 工具类
 * Created by hanqq on 2020/7/15
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/taxiao213
 */
public class HexCamera1Utils {
    public static final String TAG = HexCamera1Utils.class.getSimpleName();
    private static volatile HexCamera1Utils mHexCameraUtils;
    private boolean isInit = false;
    private Camera camera;

    private HexCamera1Utils() {
    }

    public static HexCamera1Utils getInstance() {
        if (mHexCameraUtils == null) {
            synchronized (HexCamera1Utils.class) {
                if (mHexCameraUtils == null) {
                    mHexCameraUtils = new HexCamera1Utils();
                }
            }
        }
        return mHexCameraUtils;
    }


    /**
     * 初始化相机
     *
     * @param context          上下文
     * @param textureView      TextureView
     * @param isMirror         是否需要镜像 true 打开 false 关闭
     * @param isFront          true 前置 false 后置  默认前置
     * @param point            预览界面尺寸
     * @param function         回调函数
     * @param previewInterface 回调函数
     */
    public void initCamera1(Context context, TextureView textureView, boolean isMirror, boolean isFront, Point point, Function2<Image, byte[]> function, IPreviewInterface previewInterface) {
        if (!checkCameraHardware(context)) return;
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {
            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                LogUtils.d(TAG, "onSurfaceTextureAvailable width: " + width + " height: " + height + " loop :" + (Looper.myLooper() == Looper.getMainLooper()));
                if (isMirror) {
                    Matrix matrix = new Matrix();
                    matrix.setScale(-1, 1);
                    matrix.postTranslate(textureView.getWidth(), 0);
                    textureView.setTransform(matrix);
                }
                XXPermissionsUtils.getInstances().hasCameraPermission(new Function<Boolean>() {
                    @Override
                    public void action(Boolean var) {
                        if (var != null && var) {
                            HandlerThread cameraHandler = new HandlerThread("camera1");
                            cameraHandler.start();
                            Looper looper = cameraHandler.getLooper();
                            Handler handler = new Handler(looper);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    // 切换到子线程
                                    initCamera(context, null, textureView.getSurfaceTexture(), isFront, point, function, previewInterface);
                                }
                            });
                        } else {
                            if (context instanceof Activity) {
                                ((Activity) context).finish();
                            }
                        }
                    }
                }, context);
            }

            @Override
            public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {
                LogUtils.d(TAG, "onSurfaceTextureSizeChanged ");
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                release();
                LogUtils.d(TAG, "onSurfaceTextureDestroyed ");
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
//                LogUtils.d(TAG, "onSurfaceTextureUpdated ");
            }
        });
    }


    /**
     * 初始化相机
     *
     * @param context          上下文
     * @param surfaceView      SurfaceView
     * @param isMirror         未用到
     * @param isFront          true 前置 false 后置  默认前置
     * @param point            预览界面尺寸
     * @param function         回调函数
     * @param previewInterface 回调函数
     */
    public void initCamera1(Context context, SurfaceView surfaceView, boolean isMirror, boolean isFront, Point point, Function2<Image, byte[]> function, IPreviewInterface previewInterface) {
        if (!checkCameraHardware(context)) return;
        SurfaceHolder holder = surfaceView.getHolder();
        SurfaceHolder.Callback callback = new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                LogUtils.d(TAG, "surfaceCreated thread " + Thread.currentThread().getName() + " id: " + Thread.currentThread().getId() + " loop :" + (Looper.myLooper() == Looper.getMainLooper()));
                XXPermissionsUtils.getInstances().hasCameraPermission(new Function<Boolean>() {
                    @Override
                    public void action(Boolean var) {
                        if (var != null && var) {
                            HandlerThread cameraHandler = new HandlerThread("camera1");
                            cameraHandler.start();
                            Looper looper = cameraHandler.getLooper();
                            Handler handler = new Handler(looper);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    // 切换到子线程
                                    initCamera(context, holder, null, isFront, point, function, previewInterface);
                                }
                            });
                        } else {
                            if (context instanceof Activity) {
                                ((Activity) context).finish();
                            }
                        }
                    }
                }, context);
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
                LogUtils.d(TAG, "surfaceChanged ");
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                LogUtils.d(TAG, "surfaceDestroyed ");
                try {
                    release();
                    if (previewInterface != null) {
                        previewInterface.onUnbind();
                    }
                    isInit = false;
                } catch (Exception e) {
                    e.printStackTrace();
                    LogUtils.d(TAG, "exception surfaceDestroyed " + e.getMessage());
                }
            }
        };
        holder.addCallback(callback);
    }

    /**
     * 初始化相机
     *
     * @param context          上下文
     * @param holder           SurfaceHolder
     * @param isFront          true 前置 false 后置  默认前置
     * @param point            预览界面尺寸
     * @param function         回调函数
     * @param previewInterface 回调函数
     */
    private void initCamera(Context context, SurfaceHolder holder, SurfaceTexture surfaceTexture, boolean isFront, Point point, Function2<Image, byte[]> function, IPreviewInterface previewInterface) {
        try {
            camera = Camera.open(getCameraID(isFront));
            Camera.Parameters parameters = camera.getParameters();
            if (parameters != null) {
                parameters.setRecordingHint(true);
                setRotation(context, parameters);
                DisplayMetrics displayMetrics = UIUtil.getDisplayMetrics(context);
                // 获取宽高比 会报错
//                 parameters.setPreviewSize(4,3);
                // getSupportedPreviewFormats 返回 [17, 842094169]，可以查到这两种正是NV21 和YV12 格式的十进制表示
                // public static final int NV21 = 0x11;
                // public static final int YV12 = 0x32315659;
                parameters.setPreviewFormat(ImageFormat.NV21);
                if (point != null) {
                    parameters.setPreviewSize(point.x, point.y);
                }
                camera.setParameters(parameters);
            }

            if (camera != null) {
                camera.setPreviewCallback((data, camera) -> {
                    LogUtils.d(TAG, "setPreviewCallback thread: " + Thread.currentThread().getName() + " id: " + Thread.currentThread().getId() + " loop :" + (Looper.myLooper() == Looper.getMainLooper()));
                    if (function != null) {
                        function.action(null, data);
                    }
                });
            }

            if (isInit) {
                setPreview(holder, surfaceTexture);
            }

            if (previewInterface != null) {
                previewInterface.onBind("");
            }

            if (camera != null) {
                setPreview(holder, surfaceTexture);
            }

            if (camera != null) {
                camera.startPreview();
            }

            isInit = true;
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.d(TAG, "exception " + e.getMessage());
        }
    }

    /**
     * 设置预览
     *
     * @param holder         SurfaceHolder
     * @param surfaceTexture SurfaceTexture
     * @throws IOException
     */
    private void setPreview(SurfaceHolder holder, SurfaceTexture surfaceTexture) throws IOException {
        if (holder != null) {
            camera.setPreviewDisplay(holder);
        } else {
            camera.setPreviewTexture(surfaceTexture);
        }
    }

    /***
     * 设置旋转角度
     */
    private void setRotation(Context context, Camera.Parameters parameters) {
        WindowManager windowManagerService = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManagerService != null) {
            Display defaultDisplay = windowManagerService.getDefaultDisplay();
            if (defaultDisplay != null) {
                int rotation = defaultDisplay.getRotation();
                LogUtils.d(TAG, "rotation: " + rotation);
                switch (rotation) {
                    case Surface.ROTATION_0:
                        rotation = 0;
                        break;
                    case Surface.ROTATION_90:
                        rotation = 90;
                        break;
                    case Surface.ROTATION_180:
                        rotation = 180;
                        break;
                    case Surface.ROTATION_270:
                        rotation = 270;
                        break;
                }
                parameters.setRotation(rotation);
            }
        }
    }

    /**
     * 获取cameraID
     *
     * @param isFront true 前置 false 后置  默认前置
     */
    private int getCameraID(boolean isFront) {
        int cameraID = Camera.CameraInfo.CAMERA_FACING_FRONT;
        int numberOfCameras = Camera.getNumberOfCameras();
        if (numberOfCameras == 1) {
            // 如果只有一个摄像头，默认是0
            cameraID = Camera.CameraInfo.CAMERA_FACING_BACK;
        } else if (numberOfCameras == 2) {
            if (isFront) {
                cameraID = Camera.CameraInfo.CAMERA_FACING_FRONT;
            } else {
                cameraID = Camera.CameraInfo.CAMERA_FACING_BACK;
            }
        }
        return cameraID;
    }

    /**
     * 是否有摄像头
     */
    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            return true;
        } else {
            ToastUtils.show(context, context.getString(R.string.library_camera_not_exist));
            return false;
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        if (camera != null) {
            camera.setPreviewCallback(null);
            camera.stopPreview();
            camera.release();
            camera = null;
        }
    }
}
