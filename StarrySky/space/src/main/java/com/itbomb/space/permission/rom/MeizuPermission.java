/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.itbomb.space.permission.rom;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;

public class MeizuPermission implements IPermission {

    @Override
    public void applyCommonPermission(Context context) {
        try {
            Intent intent = new Intent("com.meizu.safe.security.SHOW_APPSEC");
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.putExtra("packageName", context.getPackageName());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (ActivityNotFoundException localActivityNotFoundException) {
            localActivityNotFoundException.printStackTrace();
            CommonPermissionUtils.goSettingIntent(context);
        }
    }

    /**
     * 去魅族权限申请页面
     */
    public void applyFloatWindowPermission(Context context) {
        applyCommonPermission(context);
    }

}
