package com.itbomb.space.permission.rom;

import android.content.Context;

/**
 * @author A.D.Wilme
 * @date on 2019/1/3  11:04
 * @email A.D.Wilme@gmail.com
 * @describe
 */
public class CoolpadPermission implements IPermission {

    @Override
    public void applyFloatWindowPermission(Context context) {
        applyCommonPermission(context);
    }

    @Override
    public void applyCommonPermission(Context context) {
        CommonPermissionUtils.doStartApplicationWithPackageName(context,"com.yulong.android.security:remote");
    }
}
