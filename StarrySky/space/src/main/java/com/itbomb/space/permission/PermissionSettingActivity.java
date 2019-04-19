package com.itbomb.space.permission;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Window;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.itbomb.space.framework.AndroidOTranslucentActivity;
import com.itbomb.space.globle.RoutePath;
import com.itbomb.space.permission.rom.CommonPermissionUtils;

/**
 * @author A.D.Wilme
 * @date on 2018/8/31  17:43
 * @email A.D.Wilme@gmail.com
 * @describe 权限管理
 */
@Route(path = RoutePath.PATH_ACT_PERMISSION_SETTING)
public class PermissionSettingActivity extends AndroidOTranslucentActivity {

    private static final String KEY_PERMISSIONS = "permissions";

    @Autowired(name = KEY_PERMISSIONS)
    String[] mPermissions;

    public static void startMe(Context context, String... permissions) {
        Bundle bundle = new Bundle();
        bundle.putStringArray(KEY_PERMISSIONS, permissions);
        ARouter.getInstance().build(RoutePath.PATH_ACT_PERMISSION_SETTING).with(bundle).navigation(context);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        ARouter.getInstance().inject(this);

        if (mPermissions[0].equals(Manifest.permission.SYSTEM_ALERT_WINDOW)) {
            CommonPermissionUtils.applyFloatWindowPermission(this);
        } else {
            CommonPermissionUtils.goSettingIntent(this);
        }
        finish();
        overridePendingTransition(0, 0);
    }

}
