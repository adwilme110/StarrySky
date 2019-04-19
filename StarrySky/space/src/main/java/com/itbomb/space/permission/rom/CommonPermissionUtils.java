package com.itbomb.space.permission.rom;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;

import com.itbomb.space.BuildConfig;
import com.itbomb.space.util.LogUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author A.D.Wilme
 * @date on 2019/1/3  10:44
 * @email A.D.Wilme@gmail.com
 * @describe 通用权限设置
 */
public class CommonPermissionUtils {

    /**
     * 一般的手机跳入设置页面
     *
     * @param mContext
     */
    public static void goSettingIntent(Context mContext) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", mContext.getPackageName(), null);
        intent.setData(uri);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);
    }

    /**
     * 检测指定权限
     *
     * @param context
     * @param op
     * @return
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static boolean checkOp(Context context, int op) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= Build.VERSION_CODES.KITKAT) {
            AppOpsManager manager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            try {
                Class clazz = AppOpsManager.class;
                Method method = clazz.getDeclaredMethod("checkOp", int.class, int.class, String.class);
                return AppOpsManager.MODE_ALLOWED == (int) method.invoke(manager, op, Binder.getCallingUid(), context.getPackageName());
            } catch (Exception e) {
                if (BuildConfig.IS_DEBUG) {
                    LogUtil.e(Log.getStackTraceString(e));
                }
            }
        } else {
            if (BuildConfig.IS_DEBUG) {
                LogUtil.e("Below API 19 cannot invoke!");
            }
        }
        return false;
    }

    /**
     * 检测悬浮窗权限
     *
     * @param context
     * @return
     */
    public static boolean checkFloatWindowPermission(Context context) {
        final int version = Build.VERSION.SDK_INT;
        if (version >= Build.VERSION_CODES.M) {
            return commonROMPermissionCheck(context);
        } else if (version >= Build.VERSION_CODES.KITKAT) {
            return checkOp(context, 24); //OP_SYSTEM_ALERT_WINDOW = 24;
        } else {
            return true;
        }
    }

    /**
     * 6.0 版本之后由于 google 增加了对悬浮窗权限的管理，所以方式就统一了
     *
     * @param context
     * @return
     */
    private static boolean commonROMPermissionCheck(Context context) {
        //最新发现魅族6.0的系统这种方式不好用，天杀的，只有你是奇葩，没办法，单独适配一下
        if (RomUtils.checkIsMeizuRom()) {
            return checkOp(context, 24);
        } else {
            Boolean result = true;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    Class clazz = Settings.class;
                    Method canDrawOverlays = clazz.getDeclaredMethod("canDrawOverlays", Context.class);
                    result = (Boolean) canDrawOverlays.invoke(null, context);
                } catch (Exception e) {
                    LogUtil.e(Log.getStackTraceString(e));
                }
            }
            return result;
        }
    }

    /**
     * 通过包名跳入指定的设置页面
     *
     * @param mContext
     * @param systemPackageName
     */
    public static void doStartApplicationWithPackageName(Context mContext, String systemPackageName) {
        // 通过包名获取此APP详细信息，包括Activities、services、versioncode、name等等
        PackageInfo packageinfo = null;
        try {
            packageinfo = mContext.getPackageManager().getPackageInfo(systemPackageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageinfo == null) {
            return;
        }
        // 创建一个类别为CATEGORY_LAUNCHER的该包名的Intent
        Intent resolveIntent = new Intent(Intent.ACTION_MAIN, null);
        resolveIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        resolveIntent.setPackage(packageinfo.packageName);
        // 通过getPackageManager()的queryIntentActivities方法遍历
        List<ResolveInfo> resolveinfoList = mContext.getPackageManager()
                .queryIntentActivities(resolveIntent, 0);
        ResolveInfo resolveinfo = resolveinfoList.iterator().next();
        if (resolveinfo != null) {
            // packageName参数2 = 参数 packname
            String packageName = resolveinfo.activityInfo.packageName;
            // 这个就是我们要找的该APP的LAUNCHER的Activity[组织形式：packageName参数2.mainActivityname]
            String className = resolveinfo.activityInfo.name;
            // LAUNCHER Intent
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            // 设置ComponentName参数1:packageName参数2:MainActivity路径
            ComponentName cn = new ComponentName(packageName, className);
            intent.setComponent(cn);
            try {
                mContext.startActivity(intent);
            } catch (Exception e) {
                goSettingIntent(mContext);
                e.printStackTrace();
            }
        }
    }

    /**
     * 申请悬浮窗的权限
     *
     * @param context
     */
    public static void applyFloatWindowPermission(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            if (RomUtils.checkIsMiuiRom()) {
                PermissionFactory.createPermissionUtil(MiuiPermission.class).applyFloatWindowPermission(context);
                return;
            } else if (RomUtils.checkIsMeizuRom()) {
                PermissionFactory.createPermissionUtil(MeizuPermission.class).applyFloatWindowPermission(context);
                return;
            } else if (RomUtils.checkIsHuaweiRom()) {
                PermissionFactory.createPermissionUtil(HuaweiPermission.class).applyFloatWindowPermission(context);
                return;
            } else if (RomUtils.checkIs360Rom()) {
                PermissionFactory.createPermissionUtil(QikuPermission.class).applyFloatWindowPermission(context);
                return;
            }
        }
        //通用申请权限方法
        if (RomUtils.checkIsMeizuRom()) {
            PermissionFactory.createPermissionUtil(MeizuPermission.class).applyFloatWindowPermission(context);
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                try {
                    Class clazz = Settings.class;
                    Field field = clazz.getDeclaredField("ACTION_MANAGE_OVERLAY_PERMISSION");

                    Intent intent = new Intent(field.get(null).toString());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setData(Uri.parse("package:" + context.getPackageName()));
                    context.startActivity(intent);
                } catch (Exception e) {
                    LogUtil.e(Log.getStackTraceString(e));
                }
            }
        }
    }

    /**
     * 申请一般的权限
     *
     * @param context
     */
    public static void applyCommonPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (RomUtils.checkIsMiuiRom()) {
                PermissionFactory.createPermissionUtil(MiuiPermission.class).applyCommonPermission(context);
            } else if (RomUtils.checkIsMeizuRom()) {
                PermissionFactory.createPermissionUtil(MeizuPermission.class).applyCommonPermission(context);
            } else if (RomUtils.checkIsHuaweiRom()) {
                PermissionFactory.createPermissionUtil(HuaweiPermission.class).applyCommonPermission(context);
            } else if (RomUtils.checkIs360Rom()) {
                PermissionFactory.createPermissionUtil(QikuPermission.class).applyCommonPermission(context);
            } else if (RomUtils.checkIsLGRom()) {
                PermissionFactory.createPermissionUtil(LGPermission.class).applyCommonPermission(context);
            } else if (RomUtils.checkIsOppoRom()) {
                PermissionFactory.createPermissionUtil(OppoPermission.class).applyCommonPermission(context);
            } else if (RomUtils.checkIsSonyRom()) {
                PermissionFactory.createPermissionUtil(SonyPermission.class).applyCommonPermission(context);
            } else if (RomUtils.checkIsCoolpadRom()) {
                PermissionFactory.createPermissionUtil(CoolpadPermission.class).applyCommonPermission(context);
            } else if (RomUtils.checkIsVivoRom()) {
                PermissionFactory.createPermissionUtil(VivoPermission.class).applyCommonPermission(context);
            } else {
                CommonPermissionUtils.goSettingIntent(context);
            }
        }
    }
}
