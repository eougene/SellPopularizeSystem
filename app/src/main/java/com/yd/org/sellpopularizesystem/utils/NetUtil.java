package com.yd.org.sellpopularizesystem.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.usage.UsageEvents;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;

import java.util.List;

/**
 * Created by hejin on 2017/9/22.
 */

public class NetUtil {
    public static final int NETWORN_NONE = 0;
    public static final int NETWORN_WIFI = 1;
    public static final int NETWORN_MOBILE = 2;

    public static int getNetworkState(final Context context) {

        ConnectivityManager connManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        // 判断有没有网
        NetworkInfo activeNetworkInfo = connManager.getActiveNetworkInfo();
        //若没有网则弹出对话框
        if (activeNetworkInfo == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("设置网络");
            builder.setMessage("网络错误，请设置网络");
            builder.setPositiveButton("设置网络", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (android.os.Build.VERSION.SDK_INT > 10) {
                        //3.0以上打开设置界面
                        // 打开按钮调出系统自带的管理网络的activity
                        Intent intent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                        context.startActivity(intent);
                    }
                }
            });

            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                    return;
                }
            });

            if (context != null) {
                if (context instanceof Activity) {
                    if (isForeground(context)){
                        builder.create().show();
                    }

                }
            }

        }


        // Wifi
        NetworkInfo.State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
            return NETWORN_WIFI;
        }

        // 3G
        state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState();
        if (state == NetworkInfo.State.CONNECTED || state == NetworkInfo.State.CONNECTING) {
            return NETWORN_MOBILE;
        }
        return NETWORN_NONE;
    }

    public static boolean isForeground(Context context) {
        if (context == null || TextUtils.isEmpty(context.getClass().getName())){
            return false;
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> appTasks = am.getRunningTasks(1);
            if (null != appTasks && !appTasks.isEmpty()) {
                ComponentName cpn = appTasks.get(0).topActivity;
                if (context.getClass().getName().equals(cpn.getClassName())){
                    return true;
                    //appTasks.get(0).topActivity.getPackageName();
                }
            }
            return false;
        }else {

           /* UsageStatsManager sUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
            if (sUsageStatsManager!=null){
                long endTime = System.currentTimeMillis();
                long beginTime = endTime - 10000;
                String result="";
                UsageEvents.Event event = new UsageEvents.Event();
                UsageEvents usageEvents = sUsageStatsManager.queryEvents(beginTime, endTime);
                while (usageEvents.hasNextEvent()) {
                    usageEvents.getNextEvent(event);
                    if (event.getEventType() == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                        result = event.getPackageName();
                        Log.e("TAG", "isForeground: "+result );
                    }
                }
                if (TextUtils.isEmpty(result)){
                    return false;
                }else {
                    return true;
                }
            }*/

            //此处要判断用户的安全权限有没有打开，如果打开了就进行获取栈顶Activity的名字的方法
            //当然，我们的要求是如果没打开就不获取了，要不然跳转会影响用户的体验
            if (isSecurityPermissionOpen(context)) {
                UsageStatsManager mUsageStatsManager = (UsageStatsManager) context.getSystemService(Context.USAGE_STATS_SERVICE);
                long endTime = System.currentTimeMillis();
                long beginTime = endTime - 1000 * 60 * 2;
                UsageStats recentStats = null;

                List<UsageStats> queryUsageStats = mUsageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, beginTime, endTime);
                if (queryUsageStats == null || queryUsageStats.isEmpty()) {
                    return false;
                }

                for (UsageStats usageStats : queryUsageStats) {
                    if (recentStats == null || recentStats.getLastTimeUsed() < usageStats.getLastTimeUsed()) {
                        recentStats = usageStats;
                    }
                }
              String   topActivityPackageName = recentStats.getClass().getName();
                if (context.getClass().getName().equals(topActivityPackageName)){
                    return true;
                }else {
                    return false;
                }

            } else {
                return false;
            }

        }
    }

    //判断用户对应的安全权限有没有打开
    private static boolean isSecurityPermissionOpen(Context context) {
        long endTime = System.currentTimeMillis();
        UsageStatsManager usageStatsManager = (UsageStatsManager) context.getApplicationContext().getSystemService("usagestats");
        List<UsageStats> queryUsageStats = usageStatsManager.queryUsageStats(UsageStatsManager.INTERVAL_BEST, 0, endTime);
        if (queryUsageStats == null || queryUsageStats.isEmpty()) {
            return false;
        }
        return true;
    }

}
