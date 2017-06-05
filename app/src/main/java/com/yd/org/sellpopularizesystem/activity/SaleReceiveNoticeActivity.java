package com.yd.org.sellpopularizesystem.activity;

import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.utils.MyUtils;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

public class SaleReceiveNoticeActivity extends BaseActivity {

    private WebView pdfView;
    private String sale_advice_url, orderId;


    @Override
    protected int setContentView() {
        return R.layout.activity_sale_receive_notice;
    }

    @Override
    public void initView() {
        setTitle(getString(R.string.sale_notice));
        hideRightImagview();
        Bundle bundle = getIntent().getExtras();
        orderId = bundle.getString("orderId");
        sale_advice_url = bundle.getString("sale_advice_url");
        pdfView = getViewById(R.id.pdfView);
        Log.e("合同**", "sale_rl:" + sale_advice_url);
        MyUtils.getInstance().showWebView(SaleReceiveNoticeActivity.this, pdfView, "http://dcsapi.com?k=140337680&url=" + sale_advice_url);

    }


    @Override
    public void setListener() {

    }

    /**
     * 请求合同
     */
    private void getAskOntract() {
        showDialog();
        FinalHttp finalHttp = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("order_id", "");
        ajaxParams.put("remark", "");
        ajaxParams.put("sales_advice_is_true", "");


        finalHttp.post(Contants.ASK_ONTRACT, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                closeDialog();

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                closeDialog();

            }
        });
    }
}
