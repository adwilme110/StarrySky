package com.itbomb.space.permission.rom;

import android.content.Context;

/**
 * @author A.D.Wilme
 * @date on 2019/1/3  10:11
 * @email A.D.Wilme@gmail.com
 * @describe
 */
public interface IPermission {

    void applyFloatWindowPermission(Context context);

    void applyCommonPermission(Context context);
}
