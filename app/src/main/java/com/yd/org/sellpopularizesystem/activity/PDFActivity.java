package com.yd.org.sellpopularizesystem.activity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.javaBean.FileContent;
import com.yd.org.sellpopularizesystem.utils.MyUtils;

public class PDFActivity extends BaseActivity {
    private WebView pdfView;
    private FileContent fileContent;
    private String sale_advice_url, orderId;

    @Override
    protected int setContentView() {
        return R.layout.activity_pdf;
    }

    @Override
    public void initView() {
        hideRightImagview();
        pdfView = getViewById(R.id.pdfView);
        Bundle bundle = getIntent().getExtras();
        orderId = bundle.getString("orderId");
        sale_advice_url = bundle.getString("sale_advice_url");

        Log.e("","");
        if (orderId == null || sale_advice_url == null) {
            fileContent = (FileContent) getIntent().getSerializableExtra("pdf");
            setTitle(fileContent.getDetail_name());
            MyUtils.getInstance().showWebView(PDFActivity.this, pdfView, Contants.PDF_TEST  + Contants.DOMAIN + "/" + fileContent.getUrl());

        } else {

            setTitle(getString(R.string.sale_notice));
            MyUtils.getInstance().showWebView(PDFActivity.this, pdfView, Contants.PDF_TEST  + sale_advice_url);

        }


    }


    @Override
    public void setListener() {

    }


}
