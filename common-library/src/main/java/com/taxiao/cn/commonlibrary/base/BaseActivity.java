package com.taxiao.cn.commonlibrary.base;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.taxiao.cn.commonlibrary.R;
import com.taxiao.cn.commonlibrary.uitl.data.StringUtils;
import com.taxiao.cn.commonlibrary.uitl.ui.ActivityManager;
import com.taxiao.cn.commonlibrary.uitl.ui.StatusBarUtils;
import com.taxiao.cn.commonlibrary.uitl.ui.StatusbarColorUtils;
import com.taxiao.cn.commonlibrary.uitl.ui.UIUtil;


/**
 * BaseActivity 基类
 * Created by taxiao on 2020/6/28
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/taxiao213
 * 微信公众号:他晓
 */
public abstract class BaseActivity<P extends BasePresenter, CONTRACT> extends AppCompatActivity implements BaseViewActivityInterface, View.OnClickListener {
    protected static final String TAG = BaseActivity.class.getSimpleName();
    protected P p;
    protected BaseActivity mContext;
    private RelativeLayout rl_title_root;// 标题栏
    private ImageView iv_title_left;// 后退
    private TextView tv_title_close;// 关闭
    private TextView tv_title_name;// 标题
    private TextView tv_title_save;// 保存
    private final int ACTIVITY_LEVEL_NUM = 3;// 当 Activity 层级超过3层,显示关闭按钮

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.LibraryCommonAppTheme);
        setView();
        mContext = this;
        // 换状态栏字体颜色
        StatusbarColorUtils.setStatusBarDarkIcon(this, true);
        changeStatusBarBg(android.R.color.white);
        p = getPresenter();
        if (p != null) {
            p.bindView(this);
        }
        ActivityManager.getActivityManager().addActivity(this);
        init(savedInstanceState);
    }

    // 添加共用的的标题栏
    private void setView() {
        LinearLayout linearLayout = new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        if (hasTitle()) {
            View title = LayoutInflater.from(this).inflate(R.layout.library_include_layout_title, null);
            title.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, UIUtil.dip2px(48)));
            rl_title_root = title.findViewById(R.id.rl_title_root);
            iv_title_left = title.findViewById(R.id.iv_title_left);
            tv_title_close = title.findViewById(R.id.tv_title_close);
            tv_title_name = title.findViewById(R.id.tv_title_name);
            tv_title_save = title.findViewById(R.id.tv_title_save);
            linearLayout.addView(title);
            iv_title_left.setOnClickListener(this);
            tv_title_close.setOnClickListener(this);
            tv_title_save.setOnClickListener(this);
            showTitleClose(false);
            showTitleSave(false);
        }
        View content = LayoutInflater.from(this).inflate(getLayoutID(), null);
        content.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        linearLayout.addView(content);
        setContentView(linearLayout);
    }

    // 改变状态栏的背景色
    protected void changeStatusBarBg(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StatusBarUtils.forStatusBar(this, ContextCompat.getColor(this, color));
        }
    }

    // 是否有标题
    protected boolean hasTitle() {
        return true;
    }

    // 是否显示关闭按钮
    protected void showTitleClose(boolean isShow) {
        if (ActivityManager.getActivityManager().currentSize() > ACTIVITY_LEVEL_NUM) {
            isShow = true;
        }
        tv_title_close.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    // 是否显示保存按钮
    protected void showTitleSave(boolean isShow) {
        tv_title_save.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }

    // 设置保存名称
    protected void setSaveName(String name) {
        tv_title_save.setText(StringUtils.null2Length0(name));
    }

    // 设置标题名称
    protected void setTitleName(String name) {
        tv_title_name.setText(StringUtils.null2Length0(name));
    }

    // 设置标题名称颜色
    protected void setTitleNameColor(int color) {
        tv_title_name.setTextColor(getResources().getColor(color));
    }

    // 设置标题名称大小
    protected void setTitleNameSize(float size) {
        tv_title_name.setTextSize(size);
    }

    // 设置标题背景色
    protected void setTitleBackgroundColor(int color) {
        rl_title_root.setBackgroundColor(getResources().getColor(color)) ;
    }

    // 设置标题背景色
    protected void setTitleBackgroundDrawable(Drawable drawable) {
        rl_title_root.setBackground(drawable);
    }

    // 设置后退按钮颜色
    protected void setBackTint(int color) {
        iv_title_left.setBackgroundTintList(ColorStateList.valueOf(color));
    }

    // 关闭逻辑
    protected void clickClose() {
        ActivityManager.getActivityManager().removeAllActivity();
        // TODO: 2020/7/28 跳转首页
        // startActivity(new Intent(this, MainActivity.class));
    }

    // 标题栏右侧按钮逻辑,子类自己去实现
    protected void clickSave() {

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.iv_title_left) {// 后退
            finish();
        } else if (id == R.id.tv_title_close) {// 关闭
            clickClose();
        } else if (id == R.id.tv_title_save) {// 标题栏右侧按钮逻辑
            clickSave();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (p != null) {
            p.unBindView();
        }
        ActivityManager.getActivityManager().removeActivity(this);
    }

    // 获取资源id
    protected abstract int getLayoutID();

    // 初始化
    protected abstract void init(Bundle savedInstanceState);

    // 获取子类具体的 Presenter
    protected abstract P getPresenter();

    // 获取子类具体的契约
    protected abstract CONTRACT getContract();

    // 如果Presenter层出现了异常，需要告知View层
    public void error(Exception e) {
    }
}
