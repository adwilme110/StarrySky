package com.itbomb.common.starrysky;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;

/**
 * @author A.D.Wilme
 * @date on 2019/4/11  10:40
 * @email A.D.Wilme@gmail.com
 * @describe
 */
public class SimpleApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.AROUTER_DEBUG) {           // These two lines must be written before init, otherwise these configurations will be invalid in the init process
            ARouter.openLog();     // Print log
            ARouter.openDebug();   // Turn on debugging mode (If you are running in InstantRun mode, you must turn on debug mode! Online version needs to be closed, otherwise there is a security risk)
        }
        ARouter.init(this);
    }
}
