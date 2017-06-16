package com.yd.org.sellpopularizesystem.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

import com.igexin.sdk.PushManager;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.fragment.LoginFragment;
import com.yd.org.sellpopularizesystem.fragment.RegisterFragment;
import com.yd.org.sellpopularizesystem.getui.IntentService;
import com.yd.org.sellpopularizesystem.getui.PushService;
import com.yd.org.sellpopularizesystem.myView.ViewPagerIndicator;
import com.yd.org.sellpopularizesystem.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 登陆页面
 * Created by bai on 207/1/11.
 */

public class LoginActivity extends FragmentActivity {
    private Class userPushService = PushService.class;
    private String client_id = "";

    private ViewPagerIndicator vpi;
    private ViewPager mViewPager;
    private FragmentPagerAdapter mAdapter;// ViewPager适配器
    private String login;
    private String register;
    private List<String> mTitles;
    private List<Fragment> fragments = new ArrayList<>();


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activty);
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        login = getResources().getString(R.string.login);
        register = getResources().getString(R.string.register);
        mTitles = Arrays.asList(login);
        initView();
        initData();
        //动态设置tab
        vpi.setVisibleTabCount(2);
        vpi.setTabItemTitles(mTitles);
        mViewPager.setAdapter(mAdapter);
        vpi.setViewPager(mViewPager, 0);
    }

    private void initView() {
        vpi = (ViewPagerIndicator) findViewById(R.id.indicator);
        mViewPager = (ViewPager) findViewById(R.id.lrViewPager);
        startGeTui();
    }

    private void initData() {
        fragments.add(new LoginFragment());
        //fragments.add(new RegisterFragment());
        mAdapter = new FragmentPagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments.get(position);
            }

            @Override
            public int getCount() {
                return fragments.size();
            }
        };
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
}
