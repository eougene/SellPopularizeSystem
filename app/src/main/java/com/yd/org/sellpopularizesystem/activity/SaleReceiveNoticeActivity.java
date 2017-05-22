package com.yd.org.sellpopularizesystem.activity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.lidong.pdf.PDFView;
import com.lidong.pdf.listener.OnLoadCompleteListener;
import com.lidong.pdf.listener.OnPageChangeListener;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.myView.WebViewClientBase;
import com.yd.org.sellpopularizesystem.utils.ToasShow;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

public class SaleReceiveNoticeActivity extends BaseActivity implements OnPageChangeListener
        , OnLoadCompleteListener {

    private PDFView pdfView;
    private String sale_advice_url, orderId;
    private WebView wvQr;


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
        wvQr = getViewById(R.id.wvQr);
//        pdfView = (PDFView) findViewById(R.id.pdfView);
//        displayFromFile1(sale_advice_url, orderId + "crm.pdf");

        setWebView();


    }
    private void setWebView(){
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
        wvQr.loadUrl(sale_advice_url);
        wvQr.setWebViewClient(new WebViewClientBase(SaleReceiveNoticeActivity.this));
    }


    @Override
    public void setListener() {

    }

    /**
     * 获取打开网络的pdf文件
     *
     * @param
     */
    private void displayFromFile1(String fileUrl, String fileNamee) {
        showDialog();
        pdfView.fileFromLocalStorage(this, this, fileUrl, fileNamee);   //设置pdf文件地址
    }

    /**
     * 翻页回调
     *
     * @param page
     * @param pageCount
     */
    @Override
    public void onPageChanged(int page, int pageCount) {
        ToasShow.showToastBottom(SaleReceiveNoticeActivity.this, page +
                "/" + pageCount);
    }

    /**
     * 加载完成回调
     *
     * @param nbPages 总共的页数
     */
    @Override
    public void loadComplete(int nbPages) {
        closeDialog();
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
