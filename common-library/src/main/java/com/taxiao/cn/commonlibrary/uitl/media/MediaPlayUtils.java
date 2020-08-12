package com.taxiao.cn.commonlibrary.uitl.media;

import android.content.Context;
import android.media.MediaPlayer;

import com.taxiao.cn.commonlibrary.uitl.data.LogUtils;


/**
 * 铃声播放
 * Created by hanqq on 2020/7/17
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/taxiao213
 */
public class MediaPlayUtils {
    private static final String TAG = MediaPlayUtils.class.getSimpleName();

    private static volatile MediaPlayUtils mMediaPlayUtils;

    private MediaPlayUtils() {
    }

    public static MediaPlayUtils getInstance() {
        if (mMediaPlayUtils == null) {
            synchronized (MediaPlayUtils.class) {
                if (mMediaPlayUtils == null) {
                    mMediaPlayUtils = new MediaPlayUtils();
                }
            }
        }
        return mMediaPlayUtils;
    }

    /**
     * 开始播放
     */
    public MediaPlayer beginToRing(Context context, boolean isLoop, int musicResId) {
        MediaPlayer mediaPlayer = MediaPlayer.create(context, musicResId);
        try {
            if (mediaPlayer != null) {
                mediaPlayer.setLooping(isLoop);
                mediaPlayer.start();
            }
            return mediaPlayer;
        } catch (Exception e) {
            LogUtils.d(TAG, " exception " + e.getMessage());
            e.printStackTrace();
            releaseRingtone(mediaPlayer);
        }
        return null;
    }

    /**
     * 释放资源
     */
    public void releaseRingtone(MediaPlayer mediaPlayer) {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
        }
    }

}