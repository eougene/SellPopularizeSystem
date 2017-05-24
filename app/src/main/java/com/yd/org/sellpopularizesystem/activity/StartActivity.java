package com.yd.org.sellpopularizesystem.activity;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;

public class StartActivity extends BaseActivity {
    private static final int GO_HOME = 1000;
    private static final int GO_GUIDE = 1001;
    // 延迟3秒
    private static final long SPLASH_DELAY_MILLIS = 2000;
    private ImageView iv_launcher;
    private Animation mAnimation = null;
    // IWXAPI 是第三方app和微信通信的openapi接口
    //private IWXAPI api;
    /**
     * Handler:跳转到不同界面
     */
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case GO_HOME:
                    goHome();
                    break;
                case GO_GUIDE:
                    goGuide();
                    break;
            }
            super.handleMessage(msg);
        }
    };


    @Override
    protected int setContentView() {
        return R.layout.activity_start;
    }

    @Override
    public void initView() {
        hideBaseTab();
        // 判断程序与第几次运行，如果是第一次运行则跳转到引导界面，否则跳转到主界面
        iv_launcher = (ImageView) findViewById(R.id.iv_launcher);
        /** 加载透明动画 **/
        mAnimation = AnimationUtils.loadAnimation(this, R.anim.alpha_launch_ui);

        /** 播放透明动画 **/
        iv_launcher.startAnimation(mAnimation);
        if (SharedPreferencesHelps.getIsFirstLauncher()) {
            iv_launcher.setVisibility(View.GONE);
            mHandler.sendEmptyMessageDelayed(GO_GUIDE, 0);
            SharedPreferencesHelps.setIsFirstLauncher(false);
        } else {
            // 使用Handler的postDelayed方法，3秒后执行跳转到MainActivity
            mHandler.sendEmptyMessageDelayed(GO_HOME, SPLASH_DELAY_MILLIS);
        }
    }


    private boolean isLogining() {
        if (TextUtils.isEmpty(SharedPreferencesHelps.getUserID()) || "null".equals(SharedPreferencesHelps.getUserID())) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void setListener() {

    }


    private void goHome() {
        if (isLogining()) {
            Log.e("start11*****", "Guide");
            ActivitySkip.forward(StartActivity.this, HomeActiviyt.class);
        } else {
            Log.e("start22*****", "Guide");
            ActivitySkip.forward(StartActivity.this, LoginActivity.class);
        }
        finish();
    }

    private void goGuide() {
        Log.e("start33*****", "Guide");
        ActivitySkip.forward(StartActivity.this, GuideActivity.class);
        finish();
    }
}
