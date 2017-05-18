package com.yd.org.sellpopularizesystem.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.application.ExtraName;
import com.yd.org.sellpopularizesystem.utils.ToasShow;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentQrActivity extends BaseActivity {
    private WebView wvQr;
    private String strId;
    private String qrcodeUrl;

    @Override
    protected int setContentView() {
        hideRightImagview();
        return R.layout.activity_alipay_qr;
    }

    @Override
    public void initView() {
        strId = getIntent().getExtras().getString("payurlId");
        wvQr=getViewById(R.id.wvQr);
        getQrcodeUrl(strId);
        WebSettings ws=wvQr.getSettings();
        ws.setJavaScriptEnabled(true);
    }

    private void getQrcodeUrl(String strId) {
        FinalHttp fh=new FinalHttp();
        AjaxParams ajaxParams=new AjaxParams();
        ajaxParams.put("trust_account_id",strId);
        fh.post(Contants.ALIPAY_INTERFACE, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                try {
                    JSONObject json = new JSONObject(s);
                    if (json.getString("code").equals("1")) {
                        ToasShow.showToastCenter(PaymentQrActivity.this, json.getString("msg"));
                        if (json.getString("msg").equals("预下单成功")) {
                            qrcodeUrl = json.getString("qrcode");
                            wvQr.setWebViewClient(new WebViewClient(){
                                @Override
                                public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                                    view.loadUrl(qrcodeUrl);
                                    return true;
                                }
                            });
                        }
                    } else {
                        ToasShow.showToastCenter(PaymentQrActivity.this, json.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    @Override
    public void setListener() {

    }
}
