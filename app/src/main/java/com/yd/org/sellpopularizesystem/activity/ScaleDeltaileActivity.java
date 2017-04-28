package com.yd.org.sellpopularizesystem.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.adapter.ViewPagerAdapter;
import com.yd.org.sellpopularizesystem.fragment.LastFragmentView;
import com.yd.org.sellpopularizesystem.javaBean.ProductListBean;
import com.yd.org.sellpopularizesystem.myView.ExpandView;
import com.yd.org.sellpopularizesystem.myView.ScaleAlphaPageTransformer;
import com.yd.org.sellpopularizesystem.viewpage.HackyViewPager;
import com.yd.org.sellpopularizesystem.viewpage.PhotoViewFragment;
import com.yd.org.sellpopularizesystem.viewpage.ViewpagerAdapter;

import java.util.ArrayList;
import java.util.List;

import static com.yd.org.sellpopularizesystem.R.id.viewPager;

public class ScaleDeltaileActivity extends FragmentActivity {
    private HackyViewPager hackyViewPager;
    private ViewPager smallViewPager;
    private ViewGroup lastPagerView;
    private ArrayList<String> picList;
    private ProductListBean.ResultBean productListBean;
    private List<Fragment> fragmentList = new ArrayList<Fragment>();
    private ViewpagerAdapter vpAdapter;
    private LinearLayout llClick;
    private ExpandView mExpandView;
    private List<View> viewList;
    private ImageView backImageView,mTrangle;
    private View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.backImageView:
                    finish();
                    break;
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scale_deltaile);
        //Bundle bundle = getIntent().getExtras();
        //productListBean = (ProductListBean.ResultBean) bundle.getSerializable("scale");
       // Log.e("获取数据***", "productListBean:" + productListBean.getProduct_id());
        lastPagerView= (ViewGroup) getLayoutInflater().inflate(R.layout.last_pager_layout,null);
        picList = new ArrayList<>();
        picList.add("http://img.ivsky.com/img/bizhi/pre/201601/27/february_2016-001.jpg");
        picList.add("http://img.ivsky.com/img/bizhi/pre/201601/27/february_2016-002.jpg");
        picList.add("http://img.ivsky.com/img/bizhi/pre/201601/27/february_2016-003.jpg");
        picList.add("http://img.ivsky.com/img/bizhi/pre/201601/27/february_2016-004.jpg");
        picList.add("http://img.ivsky.com/img/tupian/pre/201511/16/chongwugou.jpg");
        initData();
        setSmallViewPage();

    }


    /**
     * 设置小图viewpager
     */
    private void setSmallViewPage() {
        mExpandView = (ExpandView) findViewById(R.id.vp_layout);
        mExpandView.setContentView();
        //vpLayout= (LinearLayout) findViewById(R.id.vp_layout);
        llClick= (LinearLayout) findViewById(R.id.llClick);
        mTrangle= (ImageView) findViewById(R.id.ivTrangle);
        smallViewPager = (ViewPager) findViewById(R.id.viewpagers);

        viewList = new ArrayList<View>();
        for (int i = 0; i < picList.size()+1; i++) {
            if(i==picList.size()){
                viewList.add(i,lastPagerView);
            }else {
                ImageView imageView = new ImageView(this);
                imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                Picasso.with(this).load(picList.get(i)).into(imageView);
                viewList.add(i, imageView);
            }
        }
        smallViewPager.setAdapter(new ViewPagerAdapter(ScaleDeltaileActivity.this,viewList));
        smallViewPager.setOffscreenPageLimit(4);
        ScaleAlphaPageTransformer mScaleAlphaPageTransformer = new ScaleAlphaPageTransformer();
        mScaleAlphaPageTransformer.setType(true, true);
        smallViewPager.setPageTransformer(true, mScaleAlphaPageTransformer);
        smallViewPager.setCurrentItem(0);
        smallViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int arg0) {
                hackyViewPager.setCurrentItem(arg0);
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                if (mExpandView != null) {
                    mExpandView.invalidate();
                }
            }

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }
        });

        mExpandView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.performClick();
                return smallViewPager.dispatchTouchEvent(event);
            }
        });

        llClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ScaleDeltaileActivity.this,"您点击了imageview",Toast.LENGTH_SHORT).show();
                if (mExpandView.isExpand()) {
                    mExpandView.collapse();
                    mTrangle.setImageResource(R.mipmap.up_arrow);
                }else{
                    mExpandView.expand();
                    mTrangle.setImageResource(R.mipmap.down_arrow);
                }
            }
        });
    }

    /**
     * 设置大图veiwpager
     */
    private void initData() {
        backImageView = (ImageView) findViewById(R.id.backImageView);
        hackyViewPager = (HackyViewPager) findViewById(viewPager);
        for (int i = 0; i < picList.size()+1; i++) {
                if(i== picList.size()){
                    fragmentList.add(i, LastFragmentView.getInstnce("1",0));
                }else {
                    fragmentList.add(PhotoViewFragment.getInstnce(picList.get(i)));
                }
        }
        backImageView.setOnClickListener(onClickListener);
        vpAdapter = new ViewpagerAdapter(getSupportFragmentManager(), fragmentList, null);
        hackyViewPager.setAdapter(vpAdapter);
        hackyViewPager.setCurrentItem(0);
        hackyViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                smallViewPager.setCurrentItem(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


}
