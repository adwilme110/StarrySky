/*
 * Copyright (C) 2016 Facishare Technology Co., Ltd. All Rights Reserved.
 */
package com.itbomb.space.permission.rom;

import android.os.Build;
import android.text.TextUtils;

import com.itbomb.space.BuildConfig;
import com.itbomb.space.util.LogUtil;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Description:
 *
 * @author zhaozp
 * @since 2016-05-23
 */
public class RomUtils {

    /**
     * 获取 emui 版本号
     *
     * @return
     */
    public static double getEmuiVersion() {
        try {
            String emuiVersion = getSystemProperty("ro.build.version.emui");
            String version = emuiVersion.substring(emuiVersion.indexOf("_") + 1);
            return Double.parseDouble(version);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 4.0;
    }

    public static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            if (BuildConfig.IS_DEBUG) {
                LogUtil.e("Unable to read sysprop " + propName, ex);
            }
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    if (BuildConfig.IS_DEBUG) {
                        LogUtil.e("Exception while closing InputStream", e);
                    }
                }
            }
        }
        return line;
    }

    public static boolean checkIsHuaweiRom() {
        return Build.MANUFACTURER.contains("HUAWEI");
    }

    /**
     * check if is miui ROM
     */
    public static boolean checkIsMiuiRom() {
        return !TextUtils.isEmpty(getSystemProperty("ro.miui.ui.version.name"));
    }

    public static boolean checkIsMeizuRom() {
        //return Build.MANUFACTURER.contains("Meizu");
        String meizuFlymeOSFlag = getSystemProperty("ro.build.display.id");
        if (TextUtils.isEmpty(meizuFlymeOSFlag)) {
            return false;
        } else if (meizuFlymeOSFlag.contains("flyme") || meizuFlymeOSFlag.toLowerCase().contains("flyme")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean checkIs360Rom() {
        //fix issue https://github.com/zhaozepeng/FloatWindowPermission/issues/9
        return Build.MANUFACTURER.contains("QiKU")
                || Build.MANUFACTURER.contains("360");
    }


    public static boolean checkIsOppoRom() {
        return Build.MANUFACTURER.contains("OPPO");
    }

    public static boolean checkIsVivoRom() {
        return Build.MANUFACTURER.contains("vivo");
    }

    public static boolean checkIsCoolpadRom() {
        return Build.MANUFACTURER.contains("Coolpad");
    }

    public static boolean checkIsLGRom() {
        return Build.MANUFACTURER.contains("LG");
    }

    public static boolean checkIsSonyRom() {
        return Build.MANUFACTURER.contains("Sony");
    }

}
