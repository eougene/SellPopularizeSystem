package com.yd.org.sellpopularizesystem.activity;

import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.adapter.ImagePagerAdapter;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.javaBean.ImageContent;
import com.yd.org.sellpopularizesystem.myView.imageshow.ImageShowViewPager;

import java.util.ArrayList;
import java.util.List;

public class ImageShowActivity extends AppCompatActivity {
    /** 图片展示 */
    private ImageShowViewPager image_pager;
    private TextView page_number;
    /** 图片下载按钮 */
    private ImageView download;
    /** 图片列表 */
    private List<ImageContent> imgContent;
    private List<String> imgUrls=new ArrayList<>();
    /** PagerAdapter */
    private ImagePagerAdapter mAdapter;
    private ImageView ivBack;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_image_show);
        initViews();
    }

    private void initViews() {
        image_pager= (ImageShowViewPager) findViewById(R.id.image_pager);
        page_number= (TextView) findViewById(R.id.page_number);
        ivBack= (ImageView) findViewById(R.id.ivBack);
        //download= (ImageView) findViewById(R.id.download);
        initData();
        initViewPager();
        setListener();
    }


    private void initViewPager() {
        if (imgUrls.size()>0){
            mAdapter=new ImagePagerAdapter(getApplicationContext(), imgUrls);
            image_pager.setAdapter(mAdapter);
        }
    }

    private void initData() {
        imgContent = (List<ImageContent>) getIntent().getSerializableExtra("img_content");
        if (imgContent!=null && imgContent.size()>0){
            Log.e("imgContent", "initData: "+imgContent.size());
            for (int i = 0; i < imgContent.size(); i++) {
                imgUrls.add(Contants.DOMAIN +"/"+imgContent.get(i).getUrl());
            }
            page_number.setText("1" + "/" + imgUrls.size());
        }

    }

    public void setListener() {
        image_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                page_number.setText((position + 1) + "/" + imgUrls.size());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

}
