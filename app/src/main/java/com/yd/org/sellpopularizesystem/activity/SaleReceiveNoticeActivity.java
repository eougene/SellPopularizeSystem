package com.yd.org.sellpopularizesystem.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yd.org.sellpopularizesystem.R;

public class SaleReceiveNoticeActivity extends BaseActivity {


    @Override
    protected int setContentView() {
        return R.layout.activity_sale_receive_notice;
    }

    @Override
    public void initView() {
        setTitle(getString(R.string.sale_notice));
        hideRightImagview();
    }

    @Override
    public void setListener() {

    }
}
