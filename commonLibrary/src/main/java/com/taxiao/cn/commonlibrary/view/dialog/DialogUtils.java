package com.taxiao.cn.commonlibrary.view.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.taxiao.cn.commonlibrary.R;
import com.taxiao.cn.commonlibrary.constant.LibraryConstant;
import com.taxiao.cn.commonlibrary.uitl.IMUtils;
import com.taxiao.cn.commonlibrary.uitl.down.Function;
import com.taxiao.cn.commonlibrary.uitl.ui.ToastUtils;
import com.taxiao.cn.commonlibrary.uitl.ui.UIUtil;
import com.taxiao.cn.commonlibrary.view.edittext.ClearEditText;


/**
 * type 1 修改昵称  2 选择相片
 * Created by Han on 2018/8/11
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class DialogUtils {

    private AlertDialog mAlertDialog;
    private ClearEditText clearEditText;
    private TextView tipTitle;
    private TextView cancel_tv;
    private TextView save_tv;
    private Context context;
    private int type;
    private View take_photo;
    private View select_photo;
    private TextView tv_notice_content;
    private EditText et_modify;

    public DialogUtils(Context context, int type) {
        this.mAlertDialog = new AlertDialog
                .Builder(context)
                .setCancelable(false)
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                            if (mAlertDialog != null) {
                                mAlertDialog.dismiss();
                            }
                        }
                        return false;
                    }
                }).create();
        this.context = context;
        this.type = type;
        mAlertDialog.show();
        init();
    }

    public DialogUtils setOKColor(int color) {
        if (type == LibraryConstant.HANDLER_RESULT1) {
            save_tv.setTextColor(color);
        }
        return this;
    }

    public DialogUtils setCancleColor(int color) {
        if (type == LibraryConstant.HANDLER_RESULT1) {
            cancel_tv.setTextColor(color);
        }
        return this;
    }

    public DialogUtils setTipTitle(String str) {
        if (type == LibraryConstant.HANDLER_RESULT1) {
            tipTitle.setText(str);
        }
        return this;
    }

    private void init() {
        Window window = mAlertDialog.getWindow();
        window.setBackgroundDrawable(new BitmapDrawable());
        if (type == LibraryConstant.HANDLER_RESULT1) {
            window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            window.setContentView(R.layout.library_dialog_modify_account_name);
            tipTitle = window.findViewById(R.id.tipTitle);
            cancel_tv = window.findViewById(R.id.cancel_tv);
            save_tv = window.findViewById(R.id.save_tv);
            clearEditText = window.findViewById(R.id.et_modify);
            clearEditText.setText(getName());
            clearEditText.setSelection(getName().length());
            window.setGravity(Gravity.CENTER);

            cancel_tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAlertDialog.dismiss();
                }
            });
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    UIUtil.openBroad(context, clearEditText);
                }
            }, 150);
        } else if (type == LibraryConstant.HANDLER_RESULT2) {
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.setContentView(R.layout.library_dialog_modify_person_photo);
            window.setGravity(Gravity.BOTTOM);
            window.setWindowAnimations(R.style.LibraryBottomDialogAnimation);
            window.findViewById(R.id.cancel_tv).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAlertDialog.dismiss();
                }
            });
            take_photo = window.findViewById(R.id.take_photo);
            select_photo = window.findViewById(R.id.select_photo);
        } else if (type == LibraryConstant.HANDLER_RESULT3) {
            window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
            window.setContentView(R.layout.library_dialog_remind);
            window.setGravity(Gravity.CENTER);
            TextView tv_notice_title = window.findViewById(R.id.tv_notice_title);
            tv_notice_content = window.findViewById(R.id.tv_notice_content);
            TextView tv_notice_see = window.findViewById(R.id.tv_notice_see);
            tv_notice_see.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAlertDialog.dismiss();

                }
            });
        }
    }

    public DialogUtils setTakePhoto(final Function<Integer> action) {
        if (type == 2) {
            if (take_photo != null)
                take_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        action.action(0);
                        mAlertDialog.dismiss();
                    }
                });
        }
        return this;
    }

    public DialogUtils setSelectPhoto(final Function<Integer> action) {
        if (type == 2) {
            if (select_photo != null)
                select_photo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        action.action(0);
                        mAlertDialog.dismiss();
                    }
                });
        }
        return this;
    }

    public DialogUtils setSureOnCilck(final Function<String> action) {
        if (type == 1) {
            if (save_tv != null)
                save_tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String nickname = getName();
                        String name = clearEditText.getText().toString().trim();
                        if (TextUtils.isEmpty(name)) {
                            ToastUtils.show(context, context.getResources().getString(R.string.library_modify_name_empty));
                        } else if (TextUtils.equals(name, nickname)) {
                            ToastUtils.show(context, context.getResources().getString(R.string.library_modify_no_name));
                        } else {
                            UIUtil.closeBroad(context, clearEditText);
                            action.action(name);
                            mAlertDialog.dismiss();
                        }
                    }
                });
        }

        return this;
    }

    private String getName() {
        return IMUtils.getInstance().getName();
    }

    /**
     * 设置提示框信息
     *
     * @param string
     * @return
     */
    public DialogUtils setNoticeContent(String string) {
        if (type == 3) {
            if (tv_notice_content != null)
                tv_notice_content.setText(string);
        }
        return this;
    }

    /**
     * 设置点击周边可取消
     *
     * @param cancelable
     * @return
     */
    public DialogUtils setCancelable(boolean cancelable) {
        if (mAlertDialog != null) {
            mAlertDialog.setCancelable(cancelable);
        }
        return this;
    }

    public void onDismiss() {
        if (mAlertDialog != null) {
            mAlertDialog.dismiss();
        }
    }
}
