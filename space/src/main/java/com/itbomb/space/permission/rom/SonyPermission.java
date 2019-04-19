package com.itbomb.space.permission.rom;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * @author A.D.Wilme
 * @date on 2019/1/3  11:25
 * @email A.D.Wilme@gmail.com
 * @describe
 */
public class SonyPermission implements IPermission {

    @Override
    public void applyFloatWindowPermission(Context context) {
        applyCommonPermission(context);
    }

    @Override
    public void applyCommonPermission(Context mContext) {
        try {
            Intent intent = new Intent(mContext.getPackageName());
            ComponentName comp = new ComponentName("com.sonymobile.cta", "com.sonymobile.cta.SomcCTAMainActivity");
            intent.setComponent(comp);
            mContext.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
            CommonPermissionUtils.goSettingIntent(mContext);
        }
    }
}
