package com.itbomb.space.permission.rom;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * @author A.D.Wilme
 * @date on 2019/1/3  11:24
 * @email A.D.Wilme@gmail.com
 * @describe
 */
public class LGPermission implements IPermission {

    @Override
    public void applyFloatWindowPermission(Context context) {
        applyCommonPermission(context);
    }

    @Override
    public void applyCommonPermission(Context context) {
        try {
            Intent intent = new Intent(context.getPackageName());
            ComponentName comp = new ComponentName("com.android.settings", "com.android.settings.Settings$AccessLockSummaryActivity");
            intent.setComponent(comp);
            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            CommonPermissionUtils.goSettingIntent(context);
        }
    }
}
