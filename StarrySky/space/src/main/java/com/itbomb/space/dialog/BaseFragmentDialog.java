package com.itbomb.space.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;

import com.itbomb.space.listener.IUIEventListener;
import com.itbomb.space.util.LogUtil;


/**
 * @author A.D.Wilme
 * @date on 2018/3/27  18:33
 * @email A.D.Wilme@gmail.com
 * @describe 修改基础Dialog引起的异常
 */

public class BaseFragmentDialog extends DialogFragment implements DialogInterface.OnKeyListener {

    public static final int FAIL_BACK_STACK_ID = -1;

    protected IUIEventListener mListener;

    @Override
    public final void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initCreate();
    }

    protected void initCreate() {
        setStyle(STYLE_NORMAL, android.R.style.Theme_Translucent_NoTitleBar);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.setOnKeyListener(this);
        return dialog;
    }

    public int show(FragmentTransaction transaction) {
        return show(transaction, this.getClass().getSimpleName());
    }

    public void show(FragmentManager manager) {
        show(manager, this.getClass().getSimpleName());
    }

    @Override
    public void show(FragmentManager manager, String tag) {
        if (!manager.isDestroyed() && !isStateSaved()) {
            try {
                super.show(manager, tag);
            } catch (IllegalStateException ignore) {
                LogUtil.e(ignore.getMessage());
            }
        }
    }

    @Override
    public int show(FragmentTransaction transaction, String tag) {
        if (isStateSaved()) {
            return FAIL_BACK_STACK_ID;
        } else {
            try {
                return super.show(transaction, tag);
            } catch (IllegalStateException ignore) {
                LogUtil.e(ignore.getMessage());
                return FAIL_BACK_STACK_ID;
            }
        }
    }

    @Override
    public void dismiss() {
        if (getDialog() != null && getDialog().isShowing()) {
            if (!isStateSaved()) {
                try {
                    super.dismiss();
                } catch (IllegalStateException ignore) {
                    LogUtil.e(ignore.getMessage());
                }
            } else {
                super.dismissAllowingStateLoss();
            }
        }
    }

    @Override
    public boolean onKey(DialogInterface dialogInterface, int keyCode, KeyEvent keyEvent) {
        if (keyCode == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
            dismiss();
            return true;
        } else {
            return false;
        }
    }

    public void setListener(IUIEventListener mListener) {
        this.mListener = mListener;
    }

}
