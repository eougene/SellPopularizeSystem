package com.yd.org.sellpopularizesystem.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.adapter.ViewPagerAdapter;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends BaseActivity implements ViewPager.OnPageChangeListener {
    private ViewPager vp;
    private ViewPagerAdapter vpAdapter;
    private List<View> views;
    // 引导图片资源
    private static final int[] pics = {R.mipmap.guild1, R.mipmap.guild2, R.mipmap.guild3};
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            if (msg.what == 0x001) {
                Log.e("Guide*****", "Guide");
                ActivitySkip.forward(GuideActivity.this, LoginActivity.class);
                finish();
            }
        }
    };


    @Override
    protected int setContentView() {
        return R.layout.activity_guide;
    }


    @Override
    public void initView() {
        hideBaseTab();
        views = new ArrayList<View>();
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
        // 初始化引导图片列表
        for (int i = 0; i < pics.length; i++) {
            ImageView iv = new ImageView(this);
            iv.setLayoutParams(mParams);
            iv.setBackgroundResource(pics[i]);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            views.add(iv);
        }
        vp = (ViewPager) findViewById(R.id.viewpager);
        // 初始化Adapter
        vpAdapter = new ViewPagerAdapter(GuideActivity.this,views);
        vp.setAdapter(vpAdapter);
        // 绑定回调
        vp.setOnPageChangeListener(this);
    }

    @Override
    public void setListener() {

    }


    // 当滑动状态改变时调用
    @Override
    public void onPageScrollStateChanged(int arg0) {

        Log.e("arz***1", "argo:" + arg0);

    }

    // 当当前页面被滑动时调用
    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {
        Log.e("arz***2", "argo:" + arg0);
        //判断当时最后一张图片的时候就可使跳转
        if (arg0 == 2) {
            handler.sendEmptyMessageDelayed(0x001, 1000);

        }
    }

    // 当新的页面被选中时调用
    @Override
    public void onPageSelected(int arg0) {
        // 设置底部小点选中状态
        Log.e("arz***3", "argo:" + arg0);
    }


}
