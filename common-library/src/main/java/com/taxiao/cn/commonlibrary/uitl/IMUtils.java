package com.taxiao.cn.commonlibrary.uitl;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.telephony.SmsManager;
import android.text.TextUtils;


import com.taxiao.cn.commonlibrary.R;
import com.taxiao.cn.commonlibrary.uitl.data.LogUtils;
import com.taxiao.cn.commonlibrary.uitl.data.SharePreferences;
import com.taxiao.cn.commonlibrary.uitl.data.StringUtils;
import com.taxiao.cn.commonlibrary.uitl.down.Function;
import com.taxiao.cn.commonlibrary.uitl.permission.XXPermissionsUtils;
import com.taxiao.cn.commonlibrary.uitl.ui.ToastUtils;
import com.taxiao.cn.commonlibrary.uitl.ui.UIUtil;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * 工具类
 * Created by hanqq on 2020/7/1
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/taxiao213
 */
public class IMUtils {
    protected static final String TAG = IMUtils.class.getSimpleName();
    private static volatile IMUtils imUtils;

    private IMUtils() {
    }

    public static IMUtils getInstance() {
        if (imUtils == null) {
            synchronized (IMUtils.class) {
                if (imUtils == null) {
                    imUtils = new IMUtils();
                }
            }
        }
        return imUtils;
    }

    /**
     * 名称
     *
     * @return String
     */
    public String getName() {
        return StringUtils.null2Length0(SharePreferences.getInstance().getName());
    }

    /**
     * 设置名称
     *
     * @return String
     */
    public void setName(String name) {
        SharePreferences.getInstance().setName(name);
    }

    /**
     * 头像地址
     *
     * @return String
     */
    public String getAvatarUrl() {
        return SharePreferences.getInstance().getAvatarUrl();
    }

    /**
     * 设置头像地址
     *
     * @param url
     */
    public void setAvatarUrl(String url) {
        SharePreferences.getInstance().setAvatarUrl(url);
    }

    /**
     * 手机
     *
     * @return String
     */
    public String getPhone() {
        return SharePreferences.getInstance().getPhone();
    }

    /**
     * 设置手机
     *
     * @param phone
     */
    public void setPhone(String phone) {
        SharePreferences.getInstance().setPhone(phone);
    }

    /**
     * 版本号
     *
     * @return String
     */
    public String getVersionName(Context context) {
        return UIUtil.getVersionName(context);
    }

    /**
     * 未开通提示语
     */
    public void noticeBuilding(Context context) {
        ToastUtils.show(context, R.string.library_notice_building);
    }

    /**
     * 会议申请时间转换
     */
    public String getTime(Long time) {

        return "10:00";
    }

    /**
     * 呼叫服务器选择
     *
     * @return 0 SIP  1 H.323
     */
    public int getSelectType() {
        return SharePreferences.getInstance().getCallServerSet();
    }

    /**
     * 呼叫服务器选择
     * 0 SIP  1 H.323
     */
    public void setSelectType(int type) {
        SharePreferences.getInstance().setCallServerSet(type);
    }

    /**
     * 预配置保存
     */
    public void setConfigures(String configures) {
        SharePreferences.getInstance().setConfiguresSet(configures);
    }

    /**
     * 预配置获取
     */
    public String getConfigures() {
        return SharePreferences.getInstance().getConfiguresSet();
    }

    /**
     * 设置是否自动静音
     */
    public void setMut(boolean mut) {
        SharePreferences.getInstance().setMut(mut);
    }

    /**
     * 获取是否自动静音
     */
    public boolean getMut() {
        return SharePreferences.getInstance().getMut();
    }

    /**
     * 设置是否自动应答
     */
    public void setAutoAnswer(boolean autoAnswer) {
        SharePreferences.getInstance().setAutoAnswer(autoAnswer);
    }

    /**
     * 获取是否自动应答
     */
    public boolean getAutoAnswer() {
        return SharePreferences.getInstance().getAutoAnswer();
    }

    /**
     * 设置是否自动屏蔽视频
     */
    public void setAutoblockVideo(boolean autoblockVideo) {
        SharePreferences.getInstance().setAutoblockVideo(autoblockVideo);
    }

    /**
     * 获取是否自动屏蔽视频
     */
    public boolean getAutoblockVideo() {
        return SharePreferences.getInstance().getAutoblockVideo();
    }

    /**
     * 设置静音
     */
    public void setMut() {
        setMut(!getMut());
    }

    /**
     * 设置自动应答
     */
    public void setAutoAnswer() {
        setAutoAnswer(!getAutoAnswer());
    }

    /**
     * 设置自动屏蔽视频
     */
    public void setAutoblockVideo() {
        setAutoblockVideo(!getAutoblockVideo());
    }

    /**
     * 设置呼叫速率
     */
    public void setCallRate(int callRate) {
        SharePreferences.getInstance().setCallRate(callRate);
    }

    /**
     * 获取呼叫速率
     */
    public int getCallRate() {
        return SharePreferences.getInstance().getCallRate();
    }


    /**
     * 拨号记录列表 拨打时间
     */
    public String getRecentTime(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault());
        return format.format(new Date(time));
    }

    /**
     * 拨号记录列表 通话时间
     */
    public String convertRecentDuration(long duration) {
        int hour = (int) (duration / 3600);
        int temp = (int) (duration % 3600);
        int min = temp / 60;
        int sec = temp % 60;
        String hourText = hour >= 10 ? String.valueOf(hour) : "0" + hour;
        String minText = min >= 10 ? String.valueOf(min) : "0" + min;
        String secText = sec >= 10 ? String.valueOf(sec) : "0" + sec;
        return hourText + ":" + minText + ":" + secText;
    }

    /**
     * 拨号记录列表 通话时间
     *
     * @param phone   接待人电话
     * @param message 发送的短信内容
     */
    public void sendMessage(Context context, String phone, String message) {
        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(message)) return;
        XXPermissionsUtils.getInstances().hasSendMessagePermission(new Function<Boolean>() {
            @Override
            public void action(Boolean var) {
                SmsManager smsManager = SmsManager.getDefault();
                if (smsManager != null) {
                    ArrayList<String> strings = smsManager.divideMessage(message);
                    for (String string : strings) {
                        smsManager.sendTextMessage(phone, null, string, null, null);
                    }
                }
            }
        }, context);
    }

}
