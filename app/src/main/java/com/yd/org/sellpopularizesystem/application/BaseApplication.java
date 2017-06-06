package com.yd.org.sellpopularizesystem.application;

import android.app.Application;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.igexin.sdk.PushManager;
import com.tencent.bugly.crashreport.CrashReport;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.yd.org.sellpopularizesystem.activity.HomeActiviyt;
import com.yd.org.sellpopularizesystem.fragment.HomeFragment;
import com.yd.org.sellpopularizesystem.javaBean.CustomBean;
import com.yd.org.sellpopularizesystem.javaBean.ProductDetailBean;
import com.yd.org.sellpopularizesystem.utils.ACache;


/**
 * Created by bai on 2017/1/10.
 */

public class BaseApplication extends Application {
    public static BaseApplication mApp;
    private CustomBean.ResultBean resultBean;
    private ProductDetailBean.ResultBean prs;
    private ACache aCache;
    public String cid="";
    private static MainHandler handler;


    //个推结束
    public BaseApplication() {
        mApp = this;
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


    @Override
    public void onCreate() {
        super.onCreate();
        //腾讯bugly
        CrashReport.initCrashReport(getApplicationContext(), "ea61d4b40a", false);
        //获取设备的cid,用于绑定账号用
        cid = PushManager.getInstance().getClientid(this);
        Log.e("cid**", "cid:" + cid);
        //设置友盟
        Config.DEBUG = false;
        UMShareAPI.get(this);
        //微信
        PlatformConfig.setWeixin(Contants.WEXIN_APP_ID, Contants.WEXIN_APP_SECRET);
        //缓存
        aCache = ACache.get(this);

        if (handler == null) {
            handler = new MainHandler();
        }


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
                    msg.what = 1;
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
