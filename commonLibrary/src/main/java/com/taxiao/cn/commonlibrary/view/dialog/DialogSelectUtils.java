package com.taxiao.cn.commonlibrary.view.dialog;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;


import com.taxiao.cn.commonlibrary.R;
import com.taxiao.cn.commonlibrary.model.CallEnity;
import com.taxiao.cn.commonlibrary.uitl.data.StringUtils;
import com.taxiao.cn.commonlibrary.uitl.down.Function;
import com.taxiao.cn.commonlibrary.uitl.ui.UIUtil;

import java.util.ArrayList;


/**
 * 弹框选项
 * Created by Han on 2019/3/13
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */
public class DialogSelectUtils {

    private AlertDialog mAlertDialog;
    private Context mActivity;
    private Function<Integer> function;
    private ArrayList<CallEnity> mList;
    private String titleName;
    private int BASE_HEIGHT = UIUtil.dip2px(48);

    /**
     * @param mActivity 上下文
     * @param titleName 标题名称
     * @param function  Function<Integer>回调
     */
    public DialogSelectUtils(Context mActivity, String titleName, ArrayList<CallEnity> list, Function<Integer> function) {
        this.mAlertDialog = new AlertDialog
                .Builder(mActivity)
                .setCancelable(true)
                .create();
        mAlertDialog.show();
        this.titleName = titleName;
        this.mActivity = mActivity;
        if (list == null) {
            list = new ArrayList<>();
        }
        this.mList = list;
        this.function = function;
        init();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void init() {
        Window window = mAlertDialog.getWindow();
        if (window == null) return;
        window.setBackgroundDrawable(new BitmapDrawable());
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        window.setContentView(R.layout.library_dialog_select);
        RecyclerView ry = window.findViewById(R.id.ry);
        if (mList != null) {
            ViewGroup.LayoutParams layoutParams = ry.getLayoutParams();
            if (mList.size() >= 5) {
                layoutParams.height = BASE_HEIGHT * 5;
            } else {
                layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            }
            ry.requestLayout();
        }
        ((AppCompatTextView) window.findViewById(R.id.tv_title)).setText(StringUtils.null2Length0(titleName));
        window.findViewById(R.id.iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        ry.setLayoutManager(new LinearLayoutManager(mActivity, LinearLayoutManager.VERTICAL, false));
        DialogSelectAdapter adapter = new DialogSelectAdapter(mActivity, mList, new Function<Integer>() {
            @Override
            public void action(Integer var) {
                if (function != null) {
                    function.action(var);
                }
                dismiss();
            }
        });
        ry.setAdapter(adapter);
    }

    /**
     * Dialog消失
     */
    private void dismiss() {
        if (mAlertDialog != null)
            mAlertDialog.dismiss();
    }

    public AlertDialog getAlertDialog() {
        return mAlertDialog;
    }
}
