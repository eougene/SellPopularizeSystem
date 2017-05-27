package com.yd.org.sellpopularizesystem.myView;

import android.app.Activity;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.webkit.SslErrorHandler;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.yd.org.sellpopularizesystem.R;

/**
 * Created by e-dot on 2017/5/15.
 */

public class WebViewClientBase extends WebViewClient {
    private Activity activitys;
    private CustomProgressDialog loading_Dialog;

    public WebViewClientBase(Activity activity) {
        this.activitys = activity;
        loading_Dialog = new CustomProgressDialog(activitys, R.style.customLoadDialog);

    }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
        //handler.cancel(); 默认的处理方式，WebView变成空白页
        handler.proceed();//接受证书
        //handleMessage(Message msg); 其他处理
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        // 重写此方法表明点击网页里面的链接还是在当前的webview里跳转，不跳到浏览器那边
        view.loadUrl(url);
        return true;
    }

    @Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        super.onPageStarted(view, url, favicon);
        loading_Dialog.show();
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        loading_Dialog.dismiss();

    }


}
