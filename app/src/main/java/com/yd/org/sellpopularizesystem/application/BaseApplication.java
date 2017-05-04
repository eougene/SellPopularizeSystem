package com.yd.org.sellpopularizesystem.application;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.SDKInitializer;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.yd.org.sellpopularizesystem.javaBean.CustomBean;
import com.yd.org.sellpopularizesystem.javaBean.ProductDetailBean;
import com.yd.org.sellpopularizesystem.javaBean.ProductListBean;


/**
 * Created by bai on 2017/1/10.
 */

public class BaseApplication extends Application {
    private static BaseApplication mApp;
    private RequestQueue queue;
    private CustomBean.ResultBean resultBean;
    private ProductDetailBean.ResultBean prs;
    public BaseApplication() {
        mApp = this;
    }

    public RequestQueue getQueue() {
        return queue;
    }

    public void setQueue(RequestQueue queue) {
        this.queue = queue;
    }

    public CustomBean.ResultBean getResultBean() {
        return resultBean;
    }

    public void setResultBean(CustomBean.ResultBean resultBean) {
        this.resultBean = resultBean;
    }

    public ProductDetailBean.ResultBean getPrs() {
        return prs;
    }

    public void setPrs(ProductDetailBean.ResultBean prs) {
        this.prs = prs;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Config.DEBUG = true;
        UMShareAPI.get(this);
        queue= Volley.newRequestQueue(mApp);
        PlatformConfig.setWeixin(Contants.WEXIN_APP_ID, Contants.WEXIN_APP_SECRET);
        SDKInitializer.initialize(this);
        bindService(new Intent(this, com.baidu.location.f.class), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        }, this.BIND_AUTO_CREATE);
    }

    public static BaseApplication getInstance() {
        return mApp;
    }
    {
        PlatformConfig.setWeixin(Contants.WEXIN_APP_ID, Contants.WEXIN_APP_SECRET);
    }
}
