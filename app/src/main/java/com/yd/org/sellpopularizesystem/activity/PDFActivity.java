package com.yd.org.sellpopularizesystem.activity;

import android.webkit.WebSettings;
import android.webkit.WebView;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.javaBean.FileContent;
import com.yd.org.sellpopularizesystem.myView.WebViewClientBase;

public class PDFActivity extends BaseActivity {
    private WebView mWebview;
    private FileContent fileContent;



    @Override
    protected int setContentView() {
        return R.layout.activity_pdf;
    }

    @Override
    public void initView() {
        hideRightImagview();
        fileContent = (FileContent) getIntent().getSerializableExtra("pdf");
        setTitle(fileContent.getDetail_name());
        mWebview = (WebView) findViewById(R.id.pdfView);

        WebSettings ws = mWebview.getSettings();
        ws.setJavaScriptEnabled(true);
        ws.setAllowFileAccessFromFileURLs(true);


        mWebview.clearHistory();
        mWebview.clearFormData();
        mWebview.loadUrl("file：///android_asset/pdf-website/index.html?pdf=" + Contants.DOMAIN + "/" + fileContent.getUrl());
        mWebview.requestFocus();
        mWebview.requestFocusFromTouch();
        mWebview.setWebViewClient(new WebViewClientBase(PDFActivity.this));

    }

    @Override
    public void setListener() {

    }


}
