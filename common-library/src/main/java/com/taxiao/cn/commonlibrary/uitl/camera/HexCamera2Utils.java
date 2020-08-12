package com.taxiao.cn.commonlibrary.uitl.camera;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;
import android.view.WindowManager;


import com.taxiao.cn.commonlibrary.R;
import com.taxiao.cn.commonlibrary.uitl.data.LogUtils;
import com.taxiao.cn.commonlibrary.uitl.down.Function;
import com.taxiao.cn.commonlibrary.uitl.permission.XXPermissionsUtils;
import com.taxiao.cn.commonlibrary.uitl.ui.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * CAMERA2 工具类
 * Created by hanqq on 2020/7/15
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/taxiao213
 */
public class HexCamera2Utils {
    public static final String TAG = HexCamera2Utils.class.getSimpleName();
    private static volatile HexCamera2Utils mHexCameraUtils;
    private CaptureRequest.Builder captureRequest;
    private Surface mSurface;
    private Surface mTextureSurface;
    private CameraCaptureSession mSession;
    private ImageReader mImageReader = null;
    private CameraDevice.StateCallback mCameraDeviceCallback;

    private HexCamera2Utils() {
    }

    public static HexCamera2Utils getInstance() {
        if (mHexCameraUtils == null) {
            synchronized (HexCamera2Utils.class) {
                if (mHexCameraUtils == null) {
                    mHexCameraUtils = new HexCamera2Utils();
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
    @SuppressLint("MissingPermission")
    public void initCamera2(Context context, TextureView textureView, boolean isMirror, boolean isFront, Point point, Function<Image> function, IPreviewInterface previewInterface) {
        if (context == null || textureView == null) return;
        if (!checkCameraHardware(context)) return;
        String cameraID = getCameraID(context, isFront);
        if (cameraID == null) return;
        CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
        if (cameraManager == null) return;
        textureView.setSurfaceTextureListener(new TextureView.SurfaceTextureListener() {

            @Override
            public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
                XXPermissionsUtils.getInstances().hasCameraPermission(new Function<Boolean>() {
                    @Override
                    public void action(Boolean var) {
                        if (var != null && var) {
                            onSurfaceTextureAvailableSuccess(isMirror, width, height, cameraManager, cameraID, context, textureView, point, function, previewInterface);
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
            }

            @Override
            public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
                LogUtils.d(TAG, "onSurfaceTextureDestroyed ");
                release();
                return false;
            }

            @Override
            public void onSurfaceTextureUpdated(SurfaceTexture surface) {
            }
        });

    }

    /**
     * surface 可以用
     *
     * @param isMirror         是否需要镜像 true 打开 false 关闭
     * @param width            width
     * @param height           height
     * @param cameraManager    CameraManager
     * @param cameraID         cameraID
     * @param context          上下文
     * @param textureView      TextureView
     * @param point            预览界面尺寸
     * @param function         回调函数
     * @param previewInterface 回调函数
     */
    @SuppressLint("MissingPermission")
    private void onSurfaceTextureAvailableSuccess(boolean isMirror, int width, int height, CameraManager cameraManager, String cameraID, Context context, TextureView textureView, Point point, Function<Image> function, IPreviewInterface previewInterface) {
        try {
            LogUtils.d(TAG, "onSurfaceTextureAvailable ");
            if (isMirror) {
                Matrix matrix = new Matrix();
                matrix.setScale(-1, 1);
                matrix.postTranslate(textureView.getWidth(), 0);
                textureView.setTransform(matrix);
            }
            CameraCharacteristics cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraID);
            StreamConfigurationMap map = cameraCharacteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
            // 0, 90, 180, 270
            Integer orientation = cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
            // 是否支持闪光
            Boolean available = cameraCharacteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);

            configureTransform(context, textureView, orientation);
            //获得最接近的尺寸大小
            Size mPreviewSize = getOptimalSize(map.getOutputSizes(SurfaceTexture.class), width, height);
            LogUtils.d(TAG, "orientation " + orientation + " available " + available);
            HandlerThread thread = new HandlerThread("camera_thread");
            thread.start();
            Looper looper = thread.getLooper();
            Handler handler = new Handler(looper);

            Size largest = Collections.max(Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)), new Comparator<Size>() {
                @Override
                public int compare(Size l, Size r) {
                    return Long.signum((long) l.getWidth() * l.getHeight() - (long) r.getWidth() * r.getHeight());
                }
            });

            LogUtils.d(TAG, "Size largest " + largest.toString() + " point width " + point.x + " height " + point.y);

            if (point != null) {
                mImageReader = ImageReader.newInstance(point.x, point.y, ImageFormat.YUV_420_888, 10);
            } else {
                mImageReader = ImageReader.newInstance(largest.getWidth(), largest.getHeight(), ImageFormat.YUV_420_888, 10);
            }
            mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
                @Override
                public void onImageAvailable(ImageReader reader) {
//                            LogUtils.d(TAG, "onImageAvailable");
                    Image image = reader.acquireNextImage();
                    if (function != null) {
                        function.action(image);
                    }
                    image.close();
                }
            }, handler);

            mSurface = mImageReader.getSurface();
            mCameraDeviceCallback = new CameraDevice.StateCallback() {
                @Override
                public void onOpened(@NonNull CameraDevice camera) {
                    LogUtils.d(TAG, "onOpened");
                    try {
                        captureRequest = camera.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
                        SurfaceTexture surfaceTexture = textureView.getSurfaceTexture();
                        surfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
                        LogUtils.d(TAG, "setDefaultBufferSize Width " + mPreviewSize.getWidth() + "Height" + mPreviewSize.getHeight());
                        mTextureSurface = new Surface(surfaceTexture);
                        captureRequest.addTarget(mTextureSurface);
                        captureRequest.addTarget(mSurface);

                        CameraCaptureSession.StateCallback mCallback = new CameraCaptureSession.StateCallback() {
                            @Override
                            public void onConfigured(@NonNull CameraCaptureSession session) {
                                LogUtils.d(TAG, "onConfigured");
                                try {
                                    mSession = session;
                                    // CaptureRequest还可以配置很多其他信息，例如图像格式、图像分辨率、传感器控制、闪光灯控制、3A(自动对焦-AF、自动曝光-AE和自动白平衡-AWB)控制等。在createCaptureSession的回调中可以进行设置
                                    captureRequest.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                                    session.setRepeatingRequest(captureRequest.build(), null, handler);
                                    if (previewInterface != null) {
                                        previewInterface.onBind(cameraID);
                                    }
                                } catch (Exception e) {
                                    LogUtils.d(TAG, "onConfigured Exception " + e.getMessage());
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                                LogUtils.d(TAG, "onConfigureFailed");
                            }
                        };
                        camera.createCaptureSession(Arrays.asList(mTextureSurface, mSurface), mCallback, handler);
                    } catch (Exception e) {
                        LogUtils.d(TAG, "Exception " + e.getMessage());
                        e.printStackTrace();
                    }
                }

                @Override
                public void onDisconnected(@NonNull CameraDevice camera) {
                    LogUtils.d(TAG, "onDisconnected");
                    if (previewInterface != null) {
                        previewInterface.onUnbind();
                    }
                    camera.close();
                }

                @Override
                public void onError(@NonNull CameraDevice camera, int error) {
                    camera.close();
                    LogUtils.d(TAG, "onError " + error);
                }
            };
            cameraManager.openCamera(cameraID, mCameraDeviceCallback, new Handler(Looper.getMainLooper()));
        } catch (Exception e) {
            LogUtils.d(TAG, "CameraException " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Configures the necessary {@link Matrix} transformation to `mTextureView`.
     * This method should be called after the camera preview size is determined in
     * setUpCameraOutputs and also the size of `mTextureView` is fixed.
     * 上下镜像
     * matrix.setScale(1, -1);
     * matrix.postTranslate(0 , viewHeight);
     * <p>
     * 左右镜像
     * matrix.setScale(-1, 1);
     * matrix.postTranslate(viewWidth, 0);
     */
    private void configureTransform(Context context, TextureView textureView, int rotation) {
        // 获取AndroidManifest 方向
        int orientation = context.getResources().getConfiguration().orientation;
        boolean swappedDimensions = false;
        switch (orientation) {
            case Configuration.ORIENTATION_LANDSCAPE:
                swappedDimensions = true;
                break;
            case Configuration.ORIENTATION_PORTRAIT:
                swappedDimensions = false;
                break;
        }
        if (swappedDimensions) {
            int mRotation = rotation / 90;
            int viewWidth = textureView.getWidth();
            int viewHeight = textureView.getHeight();
            Matrix matrix = new Matrix();
            RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
            RectF bufferRect = new RectF(0, 0, viewHeight, viewWidth);
            float centerX = viewRect.centerX();
            float centerY = viewRect.centerY();
            if (Surface.ROTATION_0 == mRotation) {
                // 左右镜像
//                matrix.setScale(-1, 1);
//                matrix.postTranslate(viewWidth, 0);
            } else if (Surface.ROTATION_90 == mRotation || Surface.ROTATION_270 == mRotation) {
                bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
                matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
                matrix.postRotate(-90 * (mRotation - 2), centerX, centerY);
            } else if (Surface.ROTATION_180 == mRotation) {
                matrix.postRotate(180, centerX, centerY);
            }
            textureView.setTransform(matrix);
        }
    }

    /**
     * 选择sizeMap中大于并且最接近width和height的size
     */
    private Size getOptimalSize(Size[] sizeMap, int width, int height) {
        List<Size> sizeList = new ArrayList<>();
        for (Size option : sizeMap) {
            if (width > height) {
                if (option.getWidth() > width && option.getHeight() > height) {
                    sizeList.add(option);
                }
            } else {
                if (option.getWidth() > height && option.getHeight() > width) {
                    sizeList.add(option);
                }
            }
        }
        if (sizeList.size() > 0) {
            return Collections.min(sizeList, new Comparator<Size>() {
                @Override
                public int compare(Size lhs, Size rhs) {
                    return Long.signum(lhs.getWidth() * lhs.getHeight() - rhs.getWidth() * rhs.getHeight());
                }
            });
        }
        return sizeMap[0];
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
     * 获取cameraID
     *
     * @param isFront true 前置 false 后置  默认前置
     */
    private String getCameraID(Context context, boolean isFront) {
        String cameraID = null;
        if (context != null) {
            CameraManager cameraManager = (CameraManager) context.getSystemService(Context.CAMERA_SERVICE);
            if (cameraManager != null) {
                try {
                    String[] cameraIdList = cameraManager.getCameraIdList();
                    int size = cameraIdList.length;
                    if (size == 1) {
                        cameraID = cameraIdList[0];
                    } else if (size == 2) {
                        if (isFront) {
                            cameraID = String.valueOf(CameraCharacteristics.LENS_FACING_FRONT);
                        } else {
                            cameraID = String.valueOf(CameraCharacteristics.LENS_FACING_BACK);
                        }
                    } else {
                        cameraID = "0";
                    }
                } catch (CameraAccessException e) {
                    LogUtils.d(TAG, "getCameraID CameraAccessException " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
        return cameraID;
    }

    /**
     * 释放资源
     */
    public void release() {
        if (captureRequest != null) {
            captureRequest.removeTarget(mTextureSurface);
            captureRequest.removeTarget(mSurface);
            captureRequest = null;
        }
        if (mTextureSurface != null) {
            mTextureSurface.release();
            mTextureSurface = null;
        }
        if (mSurface != null) {
            mSurface.release();
            mSurface = null;
        }

        if (mSession != null) {
            try {
                mSession.stopRepeating();
                mSession.abortCaptures();
                mSession.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            mSession = null;
        }

        if (mCameraDeviceCallback != null) {
            mCameraDeviceCallback = null;
        }

        if (mImageReader != null) {
            mImageReader.close();
            mImageReader = null;
        }
    }
}
