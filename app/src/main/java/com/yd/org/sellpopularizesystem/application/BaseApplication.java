package com.yd.org.sellpopularizesystem.application;

import android.app.Application;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.baidu.mapapi.SDKInitializer;
import com.igexin.sdk.PushManager;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.yd.org.sellpopularizesystem.activity.HomeActiviyt;
import com.yd.org.sellpopularizesystem.fragment.HomeFragment;
import com.yd.org.sellpopularizesystem.getui.IntentService;
import com.yd.org.sellpopularizesystem.getui.PushService;
import com.yd.org.sellpopularizesystem.javaBean.CustomBean;
import com.yd.org.sellpopularizesystem.javaBean.ProductDetailBean;
import com.yd.org.sellpopularizesystem.utils.ACache;


/**
 * Created by bai on 2017/1/10.
 */

public class BaseApplication extends Application {
    public static BaseApplication mApp;
    private RequestQueue queue;
    private CustomBean.ResultBean resultBean;
    private ProductDetailBean.ResultBean prs;
    private ACache aCache;
    private Class userPushService = PushService.class;


    //个推开始
    // private static DemoHandler handler;
    /**
     * 应用未启动, 个推 service已经被唤醒,保存在该时间段内离线消息(此时 GetuiSdkDemoActivity.tLogView == null)
     */
    public String cid;
    private static MainHandler handler;

    //个推结束
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

    public ACache getaCache() {
        return aCache;
    }

    public void setaCache(ACache aCache) {
        this.aCache = aCache;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //腾讯bugly
        CrashReport.initCrashReport(getApplicationContext(),"ea61d4b40a",false);
        //获取设备的cid,用于绑定账号用
        cid = PushManager.getInstance().getClientid(this);

        Log.e("cid**", "cid:" + cid);


        //设置友盟
        Config.DEBUG = false;
        UMShareAPI.get(this);

        //初始化Volley
        queue = Volley.newRequestQueue(mApp);

        PlatformConfig.setWeixin(Contants.WEXIN_APP_ID, Contants.WEXIN_APP_SECRET);
        SDKInitializer.initialize(this);
        aCache=ACache.get(this);

        //百度sdk
        bindService(new Intent(this, com.baidu.location.f.class), new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        }, this.BIND_AUTO_CREATE);

        if (handler == null) {
            handler = new MainHandler();
        }


        startGeTui();

    }

    //启动个推服务
    private void startGeTui() {
        //cid= PushManager.getInstance().getClientid(this);
        // 注册 intentService 后 PushDemoReceiver 无效, sdk 会使用 IntentService 传递数据,
        // AndroidManifest 对应保留一个即可(如果注册 IntentService, 可以去掉 PushDemoReceiver, 如果注册了
        // IntentService, 必须在 AndroidManifest 中声明)
        PushManager.getInstance().initialize(this.getApplicationContext(), userPushService);
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), IntentService.class);


    }

    public static void sendMessage(Message msg) {
        handler.sendMessage(msg);
    }

    public static class MainHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    Log.e("返回消息数据**", "0:" + msg.obj);
                    //通知刷新消息
                    //主页面数据
                    Message message = new Message();
                    message.obj = msg.obj;
                    msg.what=1;
                    HomeActiviyt.homeActiviyt.showToasHandler.sendMessage(message);
                    //更新数据
                    HomeFragment.homeFragment.mHandler.sendEmptyMessage(1);

                    break;

                case 1:
                    Log.e("监听消息变化**", "1:" + msg.obj);
                    break;
            }
        }
    }


    public static BaseApplication getInstance() {
        return mApp;
    }


}
