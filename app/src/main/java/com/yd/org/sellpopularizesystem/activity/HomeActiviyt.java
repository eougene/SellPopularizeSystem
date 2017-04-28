package com.yd.org.sellpopularizesystem.activity;

import android.os.Bundle;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.fragment.HomeFragment;
import com.yd.org.sellpopularizesystem.fragment.NotificationFragment;
import com.yd.org.sellpopularizesystem.fragment.SettingFragment;

public class HomeActiviyt extends FragmentActivity implements View.OnClickListener{
    private long mExitTime = 0;
    private FrameLayout flContent;
    private TextView tvHome,tvMessage,tvSetting;
    private HomeFragment homeFragment;
    private NotificationFragment notificationFragment;
    private SettingFragment settingFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //无title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_home_activiyt_1);
        inintView();

       setSelect(0);

    }


    private void inintView() {
        flContent= (FrameLayout) findViewById(R.id.flContent);
        tvHome = (TextView) findViewById(R.id.tvHome);
        tvMessage = (TextView) findViewById(R.id.tvMessage);
        tvSetting = (TextView) findViewById(R.id.tvSetting);
        tvHome.setOnClickListener(this);
        tvMessage.setOnClickListener(this);
        tvSetting.setOnClickListener(this);
    }

    public void setUnselected(){
        tvHome.setSelected(false);
        tvMessage.setSelected(false);
        tvSetting.setSelected(false);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                // 如果两次按键时间间隔大于2000毫秒，则不退出
                Toast.makeText(this, getResources().getString(R.string.app_exit), Toast.LENGTH_SHORT).show();
                mExitTime = System.currentTimeMillis();// 更新mExitTime
            } else {
                System.exit(0);// 否则退出程序
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }


    @Override
    public void onClick(View v) {
        setUnselected();
        switch (v.getId()){
            case R.id.tvHome:
                setSelect(0);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                break;
            case R.id.tvMessage:
                setSelect(1);
                this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                break;
            case R.id.tvSetting:
               setSelect(2);
                break;

        }

    }

    private void setSelect(int i) {
        FragmentTransaction transaction= getSupportFragmentManager().beginTransaction();
        hideAllFragment(transaction);
        switch (i){
            case 0:
                tvHome.setSelected(true);
                if(homeFragment==null){
                    homeFragment=new HomeFragment();
                    transaction.add(R.id.flContent,homeFragment);
                }else {
                    transaction.show(homeFragment);
                }
                break;
            case 1:
                tvMessage.setSelected(true);
                if (notificationFragment==null){
                    notificationFragment=new NotificationFragment();
                    transaction.add(R.id.flContent, notificationFragment);
                }else{
                    transaction.show(notificationFragment);
                }
                break;
            case 2:
                tvSetting.setSelected(true);
                if (settingFragment==null){
                    settingFragment=new SettingFragment();
                    transaction.add(R.id.flContent,settingFragment);
                }else{
                    transaction.show(settingFragment);
                }
                break;
        }
        transaction.commit();
    }

    private void hideAllFragment(FragmentTransaction transaction) {
            if (homeFragment!=null){
                transaction.hide(homeFragment);
            }
            if(notificationFragment!=null){
                transaction.hide(notificationFragment);
            }
            if (settingFragment!=null){
                transaction.hide(settingFragment);
            }
    }
}
