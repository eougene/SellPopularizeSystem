package com.yd.org.sellpopularizesystem.activity;

import android.os.Bundle;
import android.webkit.WebView;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.utils.MyUtils;

/**
 * Created by e-dot on 2017/6/16.
 */

public class IntroduceActivity extends BaseActivity {
    private WebView pdfView;
    private String sale_advice_url;

    @Override
    protected int setContentView() {
        return R.layout.activity_pdf;
    }

    @Override
    public void initView() {

        hideRightImagview();
        setTitle(R.string.jieshao);
        pdfView = getViewById(R.id.pdfView);
        Bundle bundle = getIntent().getExtras();
        sale_advice_url = bundle.getString("introduce");

        MyUtils.getInstance().showWebView(IntroduceActivity.this, pdfView, "http://dcsapi.com?k=140337680&url=" + Contants.DOMAIN + "/" + sale_advice_url);


    }

    @Override
    public void setListener() {

    }
}
