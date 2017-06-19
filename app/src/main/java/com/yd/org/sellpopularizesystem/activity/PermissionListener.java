package com.yd.org.sellpopularizesystem.activity;

import java.util.List;

/**
 * Created by BYJC-LiuXu on 2016/12/27.
 * 运行时权限封装回调接口
 */
public interface PermissionListener {


    /**
     * 用户授权时的回调方法
     */
    public void onGranted();

    /**
     * 用户拒绝授权时的回调方法
     * @param deniedPermissionList 被拒绝的权限
     */
    public void onDenied(List<String> deniedPermissionList);

}
