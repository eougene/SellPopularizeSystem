package com.yd.org.sellpopularizesystem.activity;

import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.myView.WebViewClientBase;
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
        setTitle("支付");
        return R.layout.activity_alipay_qr;
    }

    @Override
    public void initView() {
        strId = getIntent().getExtras().getString("payurlId");
        wvQr = getViewById(R.id.wvQr);
        getQrcodeUrl(strId);
    }

    private void getQrcodeUrl(String strId) {
        FinalHttp fh = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("trust_account_id", strId);
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

                            Log.e("支付***", "qrcodeUrl:" + qrcodeUrl);


                            //声明WebSettings子类
                            WebSettings webSettings = wvQr.getSettings();

                            //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
                            webSettings.setJavaScriptEnabled(true);

                            //设置自适应屏幕，两者合用
                            webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
                            webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

                            //缩放操作
                            webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
                            webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
                            webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

                            //其他细节操作
                            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
                            webSettings.setAllowFileAccess(true); //设置可以访问文件
                            webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
                            webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
                            webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
                            wvQr.loadUrl(qrcodeUrl);
                            wvQr.setWebViewClient(new WebViewClientBase(PaymentQrActivity.this));


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
