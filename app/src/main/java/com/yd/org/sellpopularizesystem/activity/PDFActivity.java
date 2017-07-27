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


        Log.e("", "");
        if (orderId == null || sale_advice_url == null) {
            fileContent = (FileContent) getIntent().getSerializableExtra("pdf");
            setTitle(fileContent.getDetail_name());


            //正式域名
            if (Contants.DOMAIN.equals("https://www.wingaid.com")) {
                MyUtils.getInstance().showWebView(PDFActivity.this, pdfView, Contants.PDF + Contants.DOMAIN + "/" + fileContent.getUrl());

            } else {
                MyUtils.getInstance().showWebView(PDFActivity.this, pdfView, Contants.PDF_TEST + Contants.DOMAIN + "/" + fileContent.getUrl());

            }

        } else {

            setTitle(getString(R.string.sale_notice));


            //正式域名
            if (Contants.DOMAIN.equals("https://www.wingaid.com")) {
                MyUtils.getInstance().showWebView(PDFActivity.this, pdfView, Contants.PDF + Contants.DOMAIN + "/" + sale_advice_url);

            } else {
                //测试域名
                MyUtils.getInstance().showWebView(PDFActivity.this, pdfView, Contants.PDF_TEST + Contants.DOMAIN + "/" + sale_advice_url);

            }


        }

       // FinalDownFile finalDownFile = new FinalDownFile(this, Contants.DOMAIN + "/" + fileContent.getUrl(), fileContent.getAdd_time() + ".pdf", pdfView);


    }


    @Override
    public void setListener() {

    }


}
