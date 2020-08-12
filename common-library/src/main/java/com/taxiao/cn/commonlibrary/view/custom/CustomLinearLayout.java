package com.taxiao.cn.commonlibrary.view.custom;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.os.Build;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.taxiao.cn.commonlibrary.R;


/**
 * 自定义View
 * Created by Han on 2018/6/9
 * Email:yin13753884368@163.com
 */
public class CustomLinearLayout extends LinearLayout {

    private AppCompatTextView tv_left;
    private AppCompatTextView tv_right;
    private AppCompatImageView iv_arrow;
    private View vie;
    private String left_text;
    private int left_text_color;
    private int left_text_drawable;
    private int left_text_color_tint;
    private String right_text;
    private int right_text_color;
    private int right_text_drawable;
    private int right_arrow_color_tint;
    private int arrowVisible;
    private int viewVisible;

    public CustomLinearLayout(Context context) {
        this(context, null);
    }

    public CustomLinearLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.library_custom_layout);
        left_text = typedArray.getString(R.styleable.library_custom_layout_left_text);
        left_text_color = typedArray.getInteger(R.styleable.library_custom_layout_left_text_color, R.color.grey_33);
        left_text_drawable = typedArray.getResourceId(R.styleable.library_custom_layout_left_text_drawable, 0);
        left_text_color_tint = typedArray.getInteger(R.styleable.library_custom_layout_left_text_color_tint, R.color.grey_33);

        right_text = typedArray.getString(R.styleable.library_custom_layout_right_text);
        right_text_color = typedArray.getInteger(R.styleable.library_custom_layout_right_text_color, R.color.grey_33);
//        right_text_drawable = typedArray.getResourceId(R.styleable.custom_layout_right_text_drawable, 0);
        right_arrow_color_tint = typedArray.getInteger(R.styleable.library_custom_layout_right_arrow_color_tint, R.color.grey_33);

        arrowVisible = typedArray.getInteger(R.styleable.library_custom_layout_isvisible_arrow, View.VISIBLE);
        viewVisible = typedArray.getInteger(R.styleable.library_custom_layout_isvisible_view, View.VISIBLE);
        typedArray.recycle();
        View view = LayoutInflater.from(context).inflate(R.layout.library_custom_layout, this);
        tv_left = view.findViewById(R.id.tv_left);
        tv_right = view.findViewById(R.id.tv_right);
        iv_arrow = view.findViewById(R.id.iv_arrow);
        vie = view.findViewById(R.id.view);
        setTextAttr(tv_left, left_text, left_text_color);
        setTextAttr(tv_right, right_text, right_text_color);
        setLeftTextDrawable(left_text_drawable);
//        setRightTextDrawable(right_text_drawable);
        setTextDrawableTint(tv_left, left_text_color_tint);
        setArrowDrawableTint(right_arrow_color_tint);
        setViewVisible(viewVisible);
        setArrowVisible(arrowVisible);
    }

    private void setArrowDrawableTint(int tintColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            iv_arrow.setImageTintList(ColorStateList.valueOf(tintColor));
        }
    }

    /**
     * 着色器
     *
     * @param textView
     * @param tintColor
     */
    private void setTextDrawableTint(AppCompatTextView textView, int tintColor) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setCompoundDrawableTintList(ColorStateList.valueOf(tintColor));
        }
    }

    /**
     * drawable
     */
    private void setLeftTextDrawable(int drawable) {
        tv_left.setCompoundDrawablesWithIntrinsicBounds(drawable, 0, 0, 0);
    }

    /**
     * drawable
     *
     * @param drawable
     */
    private void setRightTextDrawable(int drawable) {
        tv_right.setCompoundDrawablesWithIntrinsicBounds(0, 0, drawable, 0);
    }

    /**
     * 文本和颜色
     *
     * @param textView
     * @param text
     * @param color
     */
    private void setTextAttr(TextView textView, String text, int color) {
        textView.setText(text);
        textView.setTextColor(color);
    }

    private void setViewVisible(int visible) {
        vie.setVisibility(visible);
    }

    private void setArrowVisible(int visible) {
        iv_arrow.setVisibility(visible);
    }

    public void setLeftText(String text) {
        tv_left.setText(text);
    }

    public void setRightText(String text) {
        tv_right.setText(text);
    }

    public AppCompatTextView getTvLeft() {
        return tv_left;
    }

    public AppCompatTextView getTvRight() {
        return tv_right;
    }

}
