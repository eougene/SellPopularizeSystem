package com.yd.org.sellpopularizesystem.activity;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.adapter.CommissionAdapter;
import com.yd.org.sellpopularizesystem.adapter.FragAdapter;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.fragment.CommissionFragment;
import com.yd.org.sellpopularizesystem.internal.PullToRefreshLayout;
import com.yd.org.sellpopularizesystem.internal.PullableListView;
import com.yd.org.sellpopularizesystem.javaBean.CommissionBean;
import com.yd.org.sellpopularizesystem.myView.CustomProgressDialog;
import com.yd.org.sellpopularizesystem.utils.MyUtils;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;
import com.yd.org.sellpopularizesystem.utils.StatusBarUtil;
import com.yd.org.sellpopularizesystem.utils.ToasShow;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.model.CacheMode;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;


/**
 * 我的佣金
 */
public class CommissionActivity extends FragmentActivity {
    private int page = 1;
    private List<CommissionBean.ResultBean> datas = new ArrayList<>();
    public static CommissionActivity commissionActivity;
    private TextView tvPersonalSum, tvLeaderSum;
    private ImageView ivBack;
    private RadioButton rbSaleCommission, rbLeaderAward;
    private ViewPager vpCommission;
    private FragAdapter adapter;
    private CommissionFragment saleFragment, leaderFragment;
    private CustomProgressDialog loading_Dialog;

    /*protected int setContentView() {
        return R.layout.activity_commission;
    }*/

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commission);
        //setImmerseLayout(findViewById(R.id.base_header_main));
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        initView();
    }

    public void initView() {
        commissionActivity = this;
        setTitle(getString(R.string.commission));
        //注册事件
        EventBus.getDefault().register(this);
        loading_Dialog = new CustomProgressDialog(this, R.style.customLoadDialog);
        initWedget();
        initViewPage();
        getInfo(1, true);

    }

    private void initWedget() {
        //下拉加载
        /*ptrl = (PullToRefreshLayout) findViewById(R.id.refresh_view);
        ptrl.setOnRefreshListener(this);*/
        ivBack= (ImageView) findViewById(R.id.ivBack);
        rbSaleCommission = (RadioButton) findViewById(R.id.rbPersonalCom);
        tvPersonalSum = (TextView) findViewById(R.id.tvPersonalSum);
        rbLeaderAward = (RadioButton) findViewById(R.id.rbLeaderAward);
        tvLeaderSum = (TextView) findViewById(R.id.tvLeaderAwardSum);
        vpCommission = (ViewPager) findViewById(R.id.vpCommission);
        ivBack.setOnClickListener(mOnClickListener);
        rbSaleCommission.setOnClickListener(mOnClickListener);
        rbLeaderAward.setOnClickListener(mOnClickListener);

    }

    private void initViewPage() {
        List<Fragment> fragments = new ArrayList<Fragment>();
        saleFragment = CommissionFragment.getInstnce(0);
        leaderFragment = CommissionFragment.getInstnce(1);
        fragments.add(saleFragment);
        fragments.add(leaderFragment);
        adapter = new FragAdapter(getSupportFragmentManager(), fragments);
        vpCommission.setAdapter(adapter);
        vpCommission.setCurrentItem(0);
        vpCommission.addOnPageChangeListener(new MyVPageChangeListener());
    }

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                //返回
                case R.id.ivBack:
                    finish();
                    break;

                //园地
                case R.id.rbPersonalCom:
                    selectRadio(0);
                    vpCommission.setCurrentItem(0);
                    break;

                //考核
                case R.id.rbLeaderAward:
                    selectRadio(1);
                    vpCommission.setCurrentItem(1);
                    break;

                default:
                    break;

            }
        }
    };

    private class MyVPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageSelected(int location) {
            changeTextColor(location);
        }

    }

    private void changeTextColor(int location) {
        switch (location) {
            case 0:
                selectRadio(0);
                break;

            case 1:
                selectRadio(1);
                break;

            default:
                break;
        }
    }

    private void selectRadio(int type) {

        if (type == 0) {
            rbSaleCommission.setChecked(true);
            rbLeaderAward.setChecked(false);

        } else {
            rbSaleCommission.setChecked(false);
            rbLeaderAward.setChecked(true);
        }

    }

    public void setListener() {


    }

    private void getInfo(int page, final boolean isRefresh) {
        EasyHttp.get(Contants.COMMOSSION_LIST)
                .cacheMode(CacheMode.DEFAULT)
                .headers("Cache-Control", "max-age=0")
                .timeStamp(true).params("user_id", SharedPreferencesHelps.getUserID())
                .params("customer_id", "").params("page", page + "")
                .params("number", "100").execute(new SimpleCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                showDialog();
            }

            @Override
            public void onError(ApiException e) {

                Log.e("onError", "onError:" + e.getCode() + ";;" + e.getMessage());
                ToasShow.showToastCenter(CommissionActivity.this, e.getMessage());
            }

            @Override
            public void onSuccess(String json) {

                closeDialog();

            }
        });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册事件
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(String message) {
        Log.e("TAG", "onMoonEvent: " + message);
        if (message.equals("ok")) {
            getInfo(1, true);
        }

    }

    /**
     * 显示数据加载对话框
     */
    public void showDialog() {
        loading_Dialog.show();
    }

    /**
     * 关闭数据加载对话框
     */
    public void closeDialog() {
        if (loading_Dialog != null && loading_Dialog.isShowing()) {
            loading_Dialog.dismiss();
        }


    }

    protected void setImmerseLayout(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            //window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            int statusBarHeight = MyUtils.getStatusBarHeight(this.getBaseContext());
            view.setPadding(0, statusBarHeight, 0, 0);
        }
    }
}
