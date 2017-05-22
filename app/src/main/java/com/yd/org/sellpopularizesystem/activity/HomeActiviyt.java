package com.yd.org.sellpopularizesystem.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.igexin.sdk.PushManager;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.fragment.HomeFragment;
import com.yd.org.sellpopularizesystem.fragment.NotificationFragment;
import com.yd.org.sellpopularizesystem.fragment.SettingFragment;
import com.yd.org.sellpopularizesystem.getui.IntentService;
import com.yd.org.sellpopularizesystem.getui.PushService;
import com.yd.org.sellpopularizesystem.javaBean.MessageCountBean;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;
import com.yd.org.sellpopularizesystem.utils.StatusBarUtil;
import com.yd.org.sellpopularizesystem.utils.ToasShow;

import java.util.Locale;

public class HomeActiviyt extends FragmentActivity implements View.OnClickListener {
    public static HomeActiviyt homeActiviyt;
    private long mExitTime = 0;
    private TextView tvHome, tvMessage, tvSetting, tvMessageCount;
    private HomeFragment homeFragment;
    private NotificationFragment notificationFragment;
    private SettingFragment settingFragment;
    private Class userPushService = PushService.class;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                MessageCountBean messageCountBean = (MessageCountBean) msg.obj;
                //有消息
                if (messageCountBean.state.equals("1")) {
                    tvMessageCount.setVisibility(View.VISIBLE);
                    tvMessageCount.setText(messageCountBean.count);

                    //没有有新的消息
                } else {
                    tvMessageCount.setVisibility(View.GONE);

                }
            }


        }
    };

    public Handler showToasHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String mess = (String) msg.obj;
            showToas(mess);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        homeActiviyt = this;
        //获取系统语言
        Locale locale = Locale.getDefault();
        String language = locale.getLanguage();
        //选择语言
        showLanguage(language);
        setContentView(R.layout.activity_home_activiyt);
        StatusBarUtil.setTranslucentForImageView(homeActiviyt,null);
        inintView();
        setSelect(0);


        // AndroidManifest 对应保留一个即可(如果注册 IntentService, 可以去掉 PushDemoReceiver, 如果注册了
        // IntentService, 必须在 AndroidManifest 中声明)
        PushManager.getInstance().initialize(this.getApplicationContext(), userPushService);
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), IntentService.class);


    }

    /**
     * 设置语言
     *
     * @param language
     */
    protected void showLanguage(String language) {
        //设置应用语言类型
        Resources resources = getResources();
        Configuration config = resources.getConfiguration();
        DisplayMetrics dm = resources.getDisplayMetrics();
        if (language.equals("zh")) {
            config.locale = Locale.SIMPLIFIED_CHINESE;
        } else {
            config.locale = Locale.ENGLISH;
        }
        resources.updateConfiguration(config, dm);
        //保存设置语言的类型
        SharedPreferencesHelps.setLanguage(language);
    }

    private void inintView() {
        tvHome = (TextView) findViewById(R.id.tvHome);
        tvMessage = (TextView) findViewById(R.id.tvMessage);
        tvSetting = (TextView) findViewById(R.id.tvSetting);
        tvMessageCount = (TextView) findViewById(R.id.tvMessageCount);
        tvMessageCount.setVisibility(View.GONE);
        tvHome.setOnClickListener(this);
        tvMessage.setOnClickListener(this);
        tvSetting.setOnClickListener(this);
    }

    public void setUnselected() {
        tvHome.setSelected(false);
        tvMessage.setSelected(false);
        tvSetting.setSelected(false);
    }


    @Override
    public void onClick(View v) {
        setUnselected();
        switch (v.getId()) {
            case R.id.tvHome:
                setSelect(0);
                break;
            case R.id.tvMessage:
                setSelect(1);
                break;
            case R.id.tvSetting:
                setSelect(2);
                break;

        }

    }

    private void setSelect(int i) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        switch (i) {
            case 0:
                tvHome.setSelected(true);
                if (homeFragment == null) {
                    homeFragment = new HomeFragment();
                    transaction.add(R.id.flContent, homeFragment);
                } else {
                    transaction.show(homeFragment);
                }
                break;
            case 1:
                tvMessage.setSelected(true);
                if (notificationFragment == null) {
                    notificationFragment = new NotificationFragment();
                    transaction.add(R.id.flContent, notificationFragment);
                } else {
                    transaction.show(notificationFragment);
                }
                break;
            case 2:
                tvSetting.setSelected(true);
                if (settingFragment == null) {
                    settingFragment = new SettingFragment();
                    transaction.add(R.id.flContent, settingFragment);
                } else {
                    transaction.show(settingFragment);
                }
                break;
        }
        transaction.commit();
    }

    private void hideAllFragment(FragmentTransaction transaction) {
        if (homeFragment != null) {
            transaction.hide(homeFragment);
        }
        if (notificationFragment != null) {
            transaction.hide(notificationFragment);
        }
        if (settingFragment != null) {
            transaction.hide(settingFragment);
        }
    }

    /**
     * 清楚个推消息
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onStop() {
        super.onStop();


    }

    @Override
    protected void onResume() {
        super.onResume();
        //根据语言判断是否重启HomeActivity
        String LAN = SharedPreferencesHelps.getLanguage();
        Locale locale = Locale.getDefault();
        String language = locale.getLanguage();
        Log.e("语言————————", language);
        if (!language.equals("")) {
            if (!LAN.equals(language)) {
                freshView();//重新启动HomeActivity

            }
        }
        showLanguage(language);
    }


    /**
     * 重新启动,更新系统语言
     */
    private void freshView() {
        Intent intent = new Intent(this, HomeActiviyt.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);

    }

    private void showToas(String message) {
        ToasShow.showToastCenter(this, message);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                showToas(getResources().getString(R.string.app_exit));
                mExitTime = System.currentTimeMillis();// 更新mExitTime
            } else {
                System.exit(0);// 否则退出程序
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }


}
