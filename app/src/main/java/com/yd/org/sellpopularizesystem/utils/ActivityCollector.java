package com.yd.org.sellpopularizesystem.utils;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by BYJC-LiuXu on 2016/12/27.
 * 活动管理器
 */
public class ActivityCollector {

    /**
     * 栈中活动集合
     */
    public static List<Activity> activityList = new ArrayList<>();

    /**
     * 向栈中活动集合中添加元素
     * @param activity 添加的活动
     */
    public static void addActivity(Activity activity) {
        activityList.add(activity);
    }

    /**
     * 从栈中活动集合中移除元素
     * @param activity 移除的活动
     */
    public static void removeActivity(Activity activity) {
        activityList.remove(activity);
    }

    /**
     * 获得当前栈顶活动
     * @return 当前栈顶Activity
     */
    public static Activity getTopActivity() {
        if (activityList.isEmpty()) {
            return null;
        } else {
            return activityList.get(activityList.size() - 1);
        }
    }
}
