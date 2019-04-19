package com.itbomb.space.permission;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.Window;

import com.alibaba.android.arouter.facade.annotation.Autowired;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.launcher.ARouter;
import com.itbomb.space.R;
import com.itbomb.space.dialog.CustomAlertDialog;
import com.itbomb.space.framework.AndroidOTranslucentActivity;
import com.itbomb.space.globle.RoutePath;
import com.itbomb.space.listener.IUIEventListener;
import java.util.Locale;

/**
 * @author A.D.Wilme
 * @date on 2018/8/31  17:43
 * @email A.D.Wilme@gmail.com
 * @describe 权限管理
 */

@Route(path = RoutePath.PATH_ACT_PERMISSION_CHECK)
public class PermissionCheckActivity extends AndroidOTranslucentActivity implements IUIEventListener {

    public static final int REQUEST_CODE = 8888;
    public static final int RESULT_CODE_SUCCESS = 6666;
    public static final int RESULT_CODE_FAIL = 7777;

    private static final String KEY_PERMISSIONS = "permissions";
    private final String COMMA = ",";

    @Autowired(name = KEY_PERMISSIONS)
    String[] mPermissions;

    private CustomAlertDialog mDialog;

    private PermissionCheckUtils permissionResult;

    private boolean isStartSettingIntent;


    public static void checkPermission(Activity activity, int requestCode, String... permissions) {
        Bundle bundle = new Bundle();
        bundle.putStringArray(KEY_PERMISSIONS, permissions);
        ARouter.getInstance().build(RoutePath.PATH_ACT_PERMISSION_CHECK).with(bundle).navigation(activity, requestCode);
    }

    public static void checkPermission(Activity activity, String... permissions) {
        checkPermission(activity, REQUEST_CODE, permissions);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //设置无标题
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        ARouter.getInstance().inject(this);

        permissionResult = new PermissionCheckUtils.Builder(this)
                .permissions(mPermissions)
                .setOnRequestResultListener(new PermissionCheckUtils.OnRequestResultListener() {
                    @Override
                    public void onSuccessRequestPermission() {
                        setResult(RESULT_CODE_SUCCESS);
                        finish();
                        overridePendingTransition(0, 0);
                    }

                    @Override
                    public void onFailedRequestPermission() {
                        showPermissionDialog();
                    }
                })
                .build();

        isStartSettingIntent = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isStartSettingIntent) {
            isStartSettingIntent = false;
            if (permissionResult.isAllowAllPermission()) {
                setResult(RESULT_CODE_SUCCESS);
            } else {
                setResult(RESULT_CODE_FAIL);
            }
            finish();
            overridePendingTransition(0, 0);
        }
    }

    /**
     * 显示申请权限
     */
    private final void showPermissionDialog() {
        Resources resources = getResources();
        StringBuilder stringBuilder = new StringBuilder();
        for (String p : mPermissions) {
            if (p.equals(Manifest.permission.CAMERA)) {//照相机
                stringBuilder.append(resources.getString(R.string.permission_camera));
            } else if (p.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {//存储空间
                stringBuilder.append(resources.getString(R.string.permission_storage));
            } else if (p.equals(Manifest.permission.CALL_PHONE)) {//电话
                stringBuilder.append(resources.getString(R.string.permission_phone));
            } else if (p.equals(Manifest.permission.READ_CALENDAR)) {//日历
                stringBuilder.append(resources.getString(R.string.permission_calendar));
            } else if (p.equals(Manifest.permission.READ_CONTACTS)) {//通讯录
                stringBuilder.append(resources.getString(R.string.permission_contacts));
            } else if (p.equals(Manifest.permission.READ_SMS)) {//短信
                stringBuilder.append(resources.getString(R.string.permission_sms));
            } else if (p.equals(Manifest.permission.BODY_SENSORS)) {//传感器
                stringBuilder.append(resources.getString(R.string.permission_sensors));
            } else if (p.equals(Manifest.permission.RECORD_AUDIO)) {//录音
                stringBuilder.append(resources.getString(R.string.permission_audio));
            } else if (p.equals(Manifest.permission.ACCESS_FINE_LOCATION)) {//位置
                stringBuilder.append(resources.getString(R.string.permission_location));
            } else if (p.equals(Manifest.permission.SYSTEM_ALERT_WINDOW)) {//悬浮窗
                stringBuilder.append(resources.getString(R.string.permission_floating));
            }
            stringBuilder.append(COMMA);
        }

        String permissionContent = stringBuilder.toString();
        if (TextUtils.isEmpty(permissionContent)) {
            return;
        } else {
            permissionContent = permissionContent.substring(0, permissionContent.length() - 1);
        }

        String content = String.format(Locale.CHINESE, resources.getString(R.string.permission_dialog_info), getApplicationName(this), permissionContent);

        mDialog = new CustomAlertDialog();
        Bundle bundle = new Bundle();
        bundle.putString(CustomAlertDialog.EXTRA_TITLE, resources.getString(R.string.permission_dialog_title));
        bundle.putString(CustomAlertDialog.EXTRA_CONTENT, content);
        bundle.putString(CustomAlertDialog.EXTRA_CONFIRM, resources.getString(R.string.permission_dialog_btn_positive));
        bundle.putString(CustomAlertDialog.EXTRA_CANCEL, resources.getString(R.string.permission_dialog_btn_negative));
        mDialog.setArguments(bundle);
        mDialog.setListener(this);
        mDialog.show(getSupportFragmentManager());
    }

    @Override
    public void update(short which, Object obj) {
        if (which == CustomAlertDialog.TYPE_CANCEL) {
            setResult(RESULT_CODE_FAIL);
            finish();
            overridePendingTransition(0, 0);
        } else if (which == CustomAlertDialog.TYPE_DEFINE) {
            isStartSettingIntent = true;
            PermissionSettingActivity.startMe(this, mPermissions);
        }
    }

    /**
     * 获取应用名
     *
     * @param mContext
     * @return
     */
    private String getApplicationName(Context mContext) {
        try {
            PackageManager packageManager = mContext.getApplicationContext().getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(mContext.getPackageName(), 0);
            String applicationName =
                    (String) packageManager.getApplicationLabel(applicationInfo);
            return applicationName;
        } catch (PackageManager.NameNotFoundException e) {
            return "";
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissionResult != null) {
            permissionResult.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    @Override
    public void onBackPressed() {
        //禁止返回键
    }

}
