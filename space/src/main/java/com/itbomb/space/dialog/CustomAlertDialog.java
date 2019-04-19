package com.itbomb.space.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.WindowManager;

import com.itbomb.space.R;
import com.itbomb.space.util.ScreenUtil;


/**
 * author: zz
 * date:  2017/5/12
 *
 * @description: 带确定的提示框
 */

public class CustomAlertDialog extends BaseFragmentDialog {

    public static final short TYPE_CANCEL = 11;
    public static final short TYPE_DEFINE = 12;

    public static final String EXTRA_TITLE = "title";
    public static final String EXTRA_CONTENT = "content";
    public static final String EXTRA_CONFIRM = "confirm";
    public static final String EXTRA_CANCEL = "cancel";

    private String title;
    private String content;
    private String cancel;
    private String confirm;

    private AlertDialog mDialog;

    @Override
    public void setArguments(Bundle args) {
        super.setArguments(args);
        if (args.containsKey(EXTRA_TITLE)) {
            title = args.getString(EXTRA_TITLE);
        }

        if (args.containsKey(EXTRA_CONTENT)) {
            content = args.getString(EXTRA_CONTENT);
        }

        if (args.containsKey(EXTRA_CONFIRM)) {
            confirm = args.getString(EXTRA_CONFIRM);
        }

        if (args.containsKey(EXTRA_CANCEL)) {
            cancel = args.getString(EXTRA_CANCEL);
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        mDialog = new AlertDialog.Builder(getActivity(), R.style.Custom_Translucent_Theme_Dialog)
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        CustomAlertDialog.this.dismiss();
                        if (null != mListener)
                            mListener.update(TYPE_CANCEL, null);
                    }
                })
                .setTitle(title)
                .setMessage(content)
                .setNegativeButton(TextUtils.isEmpty(cancel) ? getString(R.string.common_cancel) : cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (null != mListener)
                            mListener.update(TYPE_CANCEL, null);
                    }
                })
                .setPositiveButton(TextUtils.isEmpty(confirm) ? getString(R.string.common_define) : confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CustomAlertDialog.this.dismiss();
                        if (null != mListener)
                            mListener.update(TYPE_DEFINE, null);
                    }
                })
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
                        if (keyCode == KeyEvent.KEYCODE_BACK) {
                            if (null != mDialog && mDialog.isShowing())
                                CustomAlertDialog.this.dismiss();
                            if (null != mListener)
                                mListener.update(TYPE_CANCEL, null);
                        }
                        return false;
                    }
                })
                .create();
        return mDialog;
    }

    @Override
    public void onResume() {
        super.onResume();
        WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();//获取dialog信息
        params.width = ScreenUtil.getScreenWidth() - ScreenUtil.dip2px(80);
        mDialog.getWindow().setAttributes(params);//设置大小
    }
}
