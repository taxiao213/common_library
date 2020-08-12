package com.taxiao.cn.commonlibrary.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.taxiao.cn.commonlibrary.R;


/**
 * 加载进度条
 * Created by hanqq on 2018/11/06
 */
public class LoadingDialog extends Dialog {
    // 有背景色
    public LoadingDialog(Context context) {
        super(context, R.style.LibraryCalendarDialogTheme);
        initView();
    }

    // 没有背景色
    public LoadingDialog(Context context, boolean notBg) {
        super(context, R.style.LibraryCalendarDialogThemeNobg);
        initView();
    }

    private void initView() {
        setContentView(R.layout.library_dialog_custom_layout);
        setCanceledOnTouchOutside(true);
        WindowManager.LayoutParams attributes = getWindow().getAttributes();
        attributes.alpha = 1f;
        getWindow().setAttributes(attributes);
        setCancelable(false);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (LoadingDialog.this.isShowing())
                    LoadingDialog.this.dismiss();
                break;
        }
        return true;
    }

    // 设置加载文字
    public void setText(String text) {
        TextView textView = findViewById(R.id.tvcontent);
        textView.setVisibility(View.VISIBLE);
        if (textView != null) {
            textView.setText(text);
        }
    }
}