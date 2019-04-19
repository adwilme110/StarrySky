package com.itbomb.space.permission.rom;

/**
 * @author A.D.Wilme
 * @date on 2019/1/3  11:46
 * @email A.D.Wilme@gmail.com
 * @describe
 */
public class PermissionFactory {

    public static IPermission createPermissionUtil(Class clazz) {
        try {
            return (IPermission) clazz.newInstance();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        } catch (InstantiationException e) {
            e.printStackTrace();
            return null;
        }
    }
}
