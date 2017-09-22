package com.yd.org.sellpopularizesystem.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;

import java.util.ArrayList;

/**
 * Created by hejin on 2017/9/22.
 */

public class NetBroadcastReceiver extends BroadcastReceiver {

    public static final String CONNECTIVITY_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";
    public static ArrayList<netEventHandler> mListeners = new ArrayList<netEventHandler>();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(CONNECTIVITY_CHANGE_ACTION)) {
           // Application.mNetWorkState = NetUtil.getNetworkState(context);
            if (mListeners.size() > 0)// 通知接口完成加载
                for (netEventHandler handler : mListeners) {
                    handler.onNetChange();
                }
        }
    }

    public static abstract interface netEventHandler {

        public abstract void onNetChange();
    }
}
