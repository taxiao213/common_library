package com.taxiao.cn.commonlibrary.uitl.data;

import android.content.Context;
import android.content.SharedPreferences;

import com.taxiao.cn.commonlibrary.base.BaseApplication;
import com.taxiao.cn.commonlibrary.constant.LibraryConstant;
import com.taxiao.cn.commonlibrary.uitl.IMUtils;


/**
 * 文件存储
 * Created by hanqq on 2020/7/1
 */
public class SharePreferences {
    private static volatile SharePreferences sharePreferences;
    public SharedPreferences prefs;
    // 名称
    private final String PREFERENCE_NAME = "preference_name";
    // 电话
    private final String PREFERENCE_PHONE = "preference_phone";
    // 头像
    private final String PREFERENCE_AVATAR = "preference_avatar";
    // 服务器设置
    private final String PREFERENCE_CALL_SERVER = "preference_call_server";
    // 预配置
    private final String PREFERENCE_CONFIGURES = "preference_configures";
    // 自动静音
    private final String PREFERENCE_CONFIGURES_MUT = "preference_configures_mut";
    // 自动应答
    private final String PREFERENCE_CONFIGURES_AUTO_ANSWER = "preference_configures_auto_answer";
    // 自动屏蔽视频
    private final String PREFERENCE_CONFIGURES_AUTO_BLOCK_VIDEO = "preference_configures_auto_block_video";
    // 呼叫速率
    private final String PREFERENCE_CONFIGURES_CALL_RATE = "preference_configures_call_rate";


    public static SharePreferences getInstance() {
        if (sharePreferences == null) {
            synchronized (IMUtils.class) {
                if (sharePreferences == null) {
                    sharePreferences = new SharePreferences(BaseApplication.getApplication());
                }
            }
        }
        return sharePreferences;
    }

    private SharePreferences(Context context) {
        prefs = context.getSharedPreferences(LibraryConstant.SHARE_PREFERENCE_NAME, Context.MODE_PRIVATE);
    }

    // 存储姓名
    public void setName(String value) {
        putString(PREFERENCE_NAME, value);
    }

    // 获取姓名
    public String getName() {
        return getString(PREFERENCE_NAME);
    }

    // 存储电话
    public void setPhone(String value) {
        putString(PREFERENCE_PHONE, value);
    }

    // 获取电话
    public String getPhone() {
        return getString(PREFERENCE_PHONE);
    }

    // 存储头像
    public void setAvatarUrl(String value) {
        putString(PREFERENCE_AVATAR, value);
    }

    // 获取头像
    public String getAvatarUrl() {
        return getString(PREFERENCE_AVATAR);
    }

    // 存储服务器设置
    public void setCallServerSet(int value) {
        putInt(PREFERENCE_CALL_SERVER, value);
    }

    // 获取服务器设置
    public int getCallServerSet() {
        return getInt(PREFERENCE_CALL_SERVER);
    }

    // 存储预配置
    public void setConfiguresSet(String value) {
        putString(PREFERENCE_CONFIGURES, value);
    }

    // 获取预配置
    public String getConfiguresSet() {
        return getString(PREFERENCE_CONFIGURES);
    }

    // 设置是否自动静音
    public void setMut(boolean mut) {
        putBoolean(PREFERENCE_CONFIGURES_MUT, mut);
    }

    // 获取是否自动静音
    public boolean getMut() {
        return getBoolean(PREFERENCE_CONFIGURES_MUT);
    }

    // 设置是否自动应答
    public void setAutoAnswer(boolean autoAnswer) {
        putBoolean(PREFERENCE_CONFIGURES_AUTO_ANSWER, autoAnswer);
    }

    // 获取是否自动应答
    public boolean getAutoAnswer() {
        return getBoolean(PREFERENCE_CONFIGURES_AUTO_ANSWER);
    }

    // 设置是否自动屏蔽视频
    public void setAutoblockVideo(boolean autoblockVideo) {
        putBoolean(PREFERENCE_CONFIGURES_AUTO_BLOCK_VIDEO, autoblockVideo);
    }

    // 获取是否自动屏蔽视频
    public boolean getAutoblockVideo() {
        return getBoolean(PREFERENCE_CONFIGURES_AUTO_BLOCK_VIDEO);
    }

    // 设置呼叫速率
    public void setCallRate(int callRate) {
        putInt(PREFERENCE_CONFIGURES_CALL_RATE, callRate);
    }

    // 获取呼叫速率
    public int getCallRate() {
        return getInt(PREFERENCE_CONFIGURES_CALL_RATE);
    }

    /**
     * 存放普通数据的方法
     *
     * @param key  存储数据的键
     * @param data 存储数据的值
     */
    private void putString(String key, String data) {
        prefs.edit().putString(key, data).apply();
    }

    /**
     * 读取普通数据的方法
     *
     * @param key 要读取数据的key
     * @return 要读取的数据
     */
    private String getString(String key) {
        return prefs.getString(key, "");
    }

    /**
     * 存放普通数据的方法
     *
     * @param key  存储数据的键
     * @param data 存储数据的值
     */
    protected void putInt(String key, int data) {
        prefs.edit().putInt(key, data).apply();
    }

    /**
     * 读取普通数据的方法
     *
     * @param key 要读取数据的key
     * @return 要读取的数据
     */
    protected int getInt(String key) {
        return prefs.getInt(key, 0);
    }

    /**
     * 移除相关key对应的item
     *
     * @param key 需要移除的key
     */
    protected void remove(String key) {
        prefs.edit().remove(key).apply();
    }


    protected void putLong(String key, long data) {
        prefs.edit().putLong(key, data).apply();
    }

    protected Long getLong(String key) {
        return prefs.getLong(key, 0);
    }

    protected void putBoolean(String key, Boolean value) {
        prefs.edit().putBoolean(key, value).apply();
    }

    protected Boolean getBoolean(String value) {
        return prefs.getBoolean(value, false);
    }

    /**
     * 清除相关prefs数据
     */
    public void clear() {
        prefs.edit().clear().apply();
    }

}
