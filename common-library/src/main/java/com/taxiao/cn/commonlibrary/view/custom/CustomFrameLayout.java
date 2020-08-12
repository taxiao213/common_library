package com.taxiao.cn.commonlibrary.view.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.taxiao.cn.commonlibrary.R;


/**
 * 需要用到网络重载 和 数据为空的framelayout
 * Created by Han on 2018/9/3
 * Email:yin13753884368@163.com
 */
public class CustomFrameLayout extends FrameLayout {

    private TextView iv_data_empty;
    private View iv_net_error;

    public CustomFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public CustomFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.library_custom_frame_layout);
        boolean data_empty = typedArray.getBoolean(R.styleable.library_custom_frame_layout_data_empty, false);
        boolean net_error = typedArray.getBoolean(R.styleable.library_custom_frame_layout_net_error, false);
        typedArray.recycle();

        View view = LayoutInflater.from(context).inflate(R.layout.library_custom_loading_framelayout, this);
        iv_data_empty = view.findViewById(R.id.iv_data_empty);
        iv_net_error = view.findViewById(R.id.iv_net_error);
        if (iv_data_empty != null) {
            iv_data_empty.setVisibility(data_empty ? VISIBLE : GONE);
        }
        if (iv_net_error != null) {
            iv_net_error.setVisibility(net_error ? VISIBLE : GONE);
        }
    }

    /**
     * 设置是否显示数据为空的状态
     *
     * @param isVisible
     */
    public void setDataEmptyVisible(boolean isVisible) {
        if (iv_data_empty != null) {
            iv_data_empty.setVisibility(isVisible ? VISIBLE : GONE);
        }
        if (iv_net_error != null) {
            iv_net_error.setVisibility(GONE);
        }
    }

    /**
     * 设置是否显示网络异常的状态
     *
     * @param isVisible
     */
    public void setNetErrorVisible(boolean isVisible) {
        if (iv_net_error != null) {
            iv_net_error.setVisibility(isVisible ? VISIBLE : GONE);
        }
        if (iv_data_empty != null) {
            iv_data_empty.setVisibility(GONE);
        }
    }

    /**
     * 点击网络重载
     *
     * @param onClickListener
     */
    public void setNetErrorOnClickListener(OnClickListener onClickListener) {
        if (iv_net_error != null) {
            iv_net_error.setOnClickListener(onClickListener);
        }
    }

    /**
     * 设置占位图
     *
     * @param drawable Drawable
     */
    public void setReplaceDrawable(Drawable drawable) {
        if (iv_data_empty != null) {
            iv_data_empty.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        }
    }

    /**
     * 设置占位图显示文字
     *
     * @param text String
     */
    public void setReplaceText(String text) {
        if (iv_data_empty != null) {
            iv_data_empty.setText(text);
        }
    }
}
