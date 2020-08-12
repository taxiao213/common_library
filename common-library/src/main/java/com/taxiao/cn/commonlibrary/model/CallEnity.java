package com.taxiao.cn.commonlibrary.model;

import java.util.ArrayList;

/**
 * 服务器配置
 * Created by hanqq on 2020/7/2
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/taxiao213
 */
public class CallEnity extends BaseEnity {
    // SIP
    // 通信协议 0
    // 传输协议 1
    // 启用SIP注册  2
    // 代理服务器    3
    // 域    4
    // 登陆地址 5
    // 身份验证用户名  6
    // 密码   7

    // H.323
    // 通信协议 0
    // 启用H.323注册    1
    // 网闸地址 2
    // H.323名称  3
    // H.323分机(E.164)   4

    private String leftName; // 左侧名称
    private int parentType; // 父类型
    private int currentType; // 当前类型
    private String rightName; // 右侧名称
    private String rightHintName; // 右侧hint名称
    private int isSelect; // 是否选中 1选择 0未选择，默认未选择
    private int layoutType;// 0 选择类型 1 输入类型
    private ArrayList<CallEnity> childList;// 子类集合

    public int getParentType() {
        return parentType;
    }

    public String getRightName() {
        return rightName;
    }

    public void setRightName(String rightName) {
        this.rightName = rightName;
    }

    public ArrayList<CallEnity> getChildList() {
        return childList;
    }

    public int getLayoutType() {
        return layoutType;
    }

    public String getLeftName() {
        return leftName;
    }

    public String getRightHintName() {
        return rightHintName;
    }

    public int getCurrentType() {
        return currentType;
    }

    public int getIsSelect() {
        return isSelect;
    }

    public void setCurrentType(int currentType) {
        this.currentType = currentType;
    }

    public void setIsSelect(int isSelect) {
        this.isSelect = isSelect;
    }
}
