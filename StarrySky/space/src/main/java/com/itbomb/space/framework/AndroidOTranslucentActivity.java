package com.itbomb.space.framework;

import android.content.pm.ApplicationInfo;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;


/**
 * @author A.D.Wilme
 * @date on 2019/4/1  14:37
 * @email A.D.Wilme@gmail.com
 * @describe 适配Android O 版本透明的Activity
 */
public class AndroidOTranslucentActivity extends FragmentActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O || Build.VERSION.SDK_INT == Build.VERSION_CODES.O_MR1) {
            ApplicationInfo applicationInfo = getApplicationInfo();
            applicationInfo.targetSdkVersion = -1;
        }
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O || Build.VERSION.SDK_INT == Build.VERSION_CODES.O_MR1) {
            ApplicationInfo applicationInfo = getApplicationInfo();
            applicationInfo.targetSdkVersion = 28;
        }
    }
}
