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
    private String sale_advice_url, type = "";

    @Override
    protected int setContentView() {
        return R.layout.activity_pdf;
    }

    @Override
    public void initView() {

        hideRightImagview();
        pdfView = getViewById(R.id.pdfView);
        Bundle bundle = getIntent().getExtras();
        sale_advice_url = bundle.getString("introduce");
        type = bundle.getString("type");

        if (type.equals("1")) {
            setTitle(R.string.floorphoto);

        } else {
            setTitle(R.string.jieshao);

        }


        //正式域名
        if (Contants.DOMAIN.equals("https://www.wingaid.com")) {
            MyUtils.getInstance().showWebView(IntroduceActivity.this, pdfView, Contants.PDF + Contants.DOMAIN + "/" + sale_advice_url);


            //测试域名
        } else {
            MyUtils.getInstance().showWebView(IntroduceActivity.this, pdfView, Contants.PDF_TEST + Contants.DOMAIN + "/" + sale_advice_url);

        }


    }

    @Override
    public void setListener() {

    }
}
