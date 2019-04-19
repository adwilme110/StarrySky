package com.itbomb.space.permission;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.PermissionChecker;

import com.itbomb.space.permission.rom.CommonPermissionUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * author: zz
 * date:  2017/5/8
 *
 * @description: 权限管理
 */

/**
 * group:android.permission-group.CONTACTS 通讯录
 * permission:android.permission.WRITE_CONTACTS
 * permission:android.permission.GET_ACCOUNTS
 * permission:android.permission.READ_CONTACTS
 * <p>
 * group:android.permission-group.PHONE 电话
 * permission:android.permission.READ_CALL_LOG
 * permission:android.permission.READ_PHONE_STATE
 * permission:android.permission.CALL_PHONE
 * permission:android.permission.WRITE_CALL_LOG
 * permission:android.permission.USE_SIP
 * permission:android.permission.PROCESS_OUTGOING_CALLS
 * permission:com.android.voicemail.permission.ADD_VOICEMAIL
 * <p>
 * group:android.permission-group.CALENDAR 日历
 * permission:android.permission.READ_CALENDAR
 * permission:android.permission.WRITE_CALENDAR
 * <p>
 * group:android.permission-group.CAMERA  相机
 * permission:android.permission.CAMERA
 * <p>
 * group:android.permission-group.SENSORS 传感器
 * permission:android.permission.BODY_SENSORS
 * <p>
 * group:android.permission-group.LOCATION 位置
 * permission:android.permission.ACCESS_FINE_LOCATION
 * permission:android.permission.ACCESS_COARSE_LOCATION
 * <p>
 * group:android.permission-group.STORAGE 存储卡
 * permission:android.permission.READ_EXTERNAL_STORAGE
 * permission:android.permission.WRITE_EXTERNAL_STORAGE
 * <p>
 * group:android.permission-group.MICROPHONE 麦克风
 * permission:android.permission.RECORD_AUDIO
 * <p>
 * group:android.permission-group.SMS 短信
 * permission:android.permission.READ_SMS
 * permission:android.permission.RECEIVE_WAP_PUSH
 * permission:android.permission.RECEIVE_MMS
 * permission:android.permission.RECEIVE_SMS
 * permission:android.permission.SEND_SMS
 * permission:android.permission.READ_CELL_BROADCASTS
 * <p>
 * Manifest.permission.SYSTEM_ALERT_WINDOW 悬浮窗
 */

public class PermissionCheckUtils {
    private final int MY_PERMISSIONS_REQUEST = 10;

    private Activity mActivity;
    private OnRequestResultListener resultListener;
    private String[] mPermissions;

    private PermissionCheckUtils(Builder builder) {
        this.mActivity = builder.mActivity;
        this.mPermissions = builder.mPermissions;
        this.resultListener = builder.resultListener;
    }

    /**
     * 申请权限
     */
    public final void requestPermissions() {
        if (mPermissions[0].equals(Manifest.permission.SYSTEM_ALERT_WINDOW)) {
            if (CommonPermissionUtils.checkFloatWindowPermission(mActivity)) {
                if (resultListener != null)
                    resultListener.onSuccessRequestPermission();
            } else {
                if (resultListener != null)
                    resultListener.onFailedRequestPermission();
            }
        } else {
            if (isAllowAllPermission()) {
                if (resultListener != null)
                    resultListener.onSuccessRequestPermission();
            } else {
                if (shouldShowRequestPermissionRationale(mActivity, mPermissions)) {//用户禁用了授权
                    if (resultListener != null)
                        resultListener.onFailedRequestPermission();
                }else {
                    ActivityCompat.requestPermissions(mActivity, mPermissions, MY_PERMISSIONS_REQUEST);
                }
            }
        }
    }

    private boolean shouldShowRequestPermissionRationale(Activity activity, String... permissions) {
        for (String permission : permissions) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 结果处理
     *
     * @param grantResults
     */
    private void resultResponse(int[] grantResults) {
        boolean isAllowAll = true;

        for (int i : grantResults) {
            if (i != PackageManager.PERMISSION_GRANTED) {
                isAllowAll = false;
                break;
            }
        }

        if (isAllowAll) {
            if (null != resultListener)
                resultListener.onSuccessRequestPermission();
        } else {
            if (resultListener != null)
                resultListener.onFailedRequestPermission();
        }
    }

    /**
     * @return 是否允许所有权限(过滤掉已允许的权限)
     */
    public final boolean isAllowAllPermission() {
        boolean isAllowAll = true;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            List<String> mList = new ArrayList<>();

            for (String permission : mPermissions) {
                if (permission.equals(Manifest.permission.SYSTEM_ALERT_WINDOW)){
                   if(!CommonPermissionUtils.checkFloatWindowPermission(mActivity)){
                       isAllowAll = false;
                       mList.add(permission);
                   }
                }else{
                    if (PermissionChecker.checkSelfPermission(mActivity, permission) != PackageManager.PERMISSION_GRANTED ) {
                        isAllowAll = false;
                        mList.add(permission);
                    }
                }
            }

            String[] mps = new String[mList.size()];
            for (int i = 0, length = mList.size(); i < length; i++) {
                mps[i] = mList.get(i);
            }
            mPermissions = mps;
        }
        return isAllowAll;
    }

    /**
     * 处理请求回调结果
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_PERMISSIONS_REQUEST) {
            resultResponse(grantResults);
        }
    }

    public interface OnRequestResultListener {

        void onSuccessRequestPermission();

        void onFailedRequestPermission();
    }

    public static class Builder {
        private Activity mActivity;
        private OnRequestResultListener resultListener;
        private String[] mPermissions;
        private FragmentManager fragmentManager;

        public Builder(FragmentActivity activity) {
            this.mActivity = activity;
            fragmentManager = activity.getSupportFragmentManager();
        }

        public Builder permissions(String... permissions) {
            this.mPermissions = permissions;
            return this;
        }

        public Builder setOnRequestResultListener(OnRequestResultListener listener) {
            this.resultListener = listener;
            return this;
        }

        public PermissionCheckUtils build() {
            PermissionCheckUtils mPermissionResult = new PermissionCheckUtils(this);
            if (mActivity != null && fragmentManager != null && mPermissions != null && mPermissions.length > 0)
                mPermissionResult.requestPermissions();
            return mPermissionResult;
        }

    }

}
