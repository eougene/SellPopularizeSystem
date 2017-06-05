package com.yd.org.sellpopularizesystem.activity;

import android.util.Log;
import android.webkit.WebView;
import android.widget.TextView;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.utils.MyUtils;
import com.yd.org.sellpopularizesystem.utils.ToasShow;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

public class PaymentQrActivity extends BaseActivity {
    private WebView wvQr;
    private String strId;
    private String qrcodeUrl, payment_method;
    private TextView tvPaymentMethod;
    private String url = "";


    @Override
    protected int setContentView() {
        hideRightImagview();
        setTitle(getString(R.string.payment));
        return R.layout.activity_alipay_qr;
    }

    @Override
    public void initView() {
        strId = getIntent().getExtras().getString("payurlId");
        payment_method = getIntent().getExtras().getString("payment_method");

        Log.e("strId**","strId:"+strId+"payment_method:"+payment_method);
        wvQr = getViewById(R.id.wvQr);
        tvPaymentMethod = getViewById(R.id.tvPaymentMethod);

        if (payment_method.equals("6")) {
            tvPaymentMethod.setText(R.string.scanalipayqr);
            url = Contants.ALIPAY_INTERFACE;
        } else if (payment_method.equals("7")) {
            tvPaymentMethod.setText(R.string.scanalipayqrwx);
            url = Contants.WEICHAT_INTERFACE;
        }
        getQrcodeUrl(strId);
    }

    private void getQrcodeUrl(String strId) {
        showDialog();
        FinalHttp fh = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("trust_account_id", strId);
        fh.post(url, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                closeDialog();
                try {
                    JSONObject json = new JSONObject(s);
                    if (json.getString("code").equals("1")) {
                        qrcodeUrl = json.getString("qrcode");
                        MyUtils.getInstance().showWebView(PaymentQrActivity.this, wvQr, qrcodeUrl);
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
                closeDialog();
            }
        });
    }

    @Override
    public void setListener() {

    }
}
