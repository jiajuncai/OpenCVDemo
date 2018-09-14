package com.junecai.opencvdemo.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.pm.PackageManager;

/**
 * @author Cai, Jiajun
 * @classame PermissionUtil
 * @description 权限判断
 * @date 2018/9/14
 */
public class PermissionUtil {

    @TargetApi(23)
    public static boolean hasPermission(Context mContext, String[] permissionStr) {
        for (String p : permissionStr) {
            if (mContext.checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }
}
