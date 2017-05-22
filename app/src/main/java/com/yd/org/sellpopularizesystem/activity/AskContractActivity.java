package com.yd.org.sellpopularizesystem.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.ToasShow;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

public class AskContractActivity extends BaseActivity {
    private Button btConSubmit;
    private TextView tvSaleAskBill;
    private RelativeLayout rlSure;
    private CheckBox cbSure;
    private String orderId,sale_advice_url;

    @Override
    protected int setContentView() {
        return R.layout.activity_ask_contract;
    }

    @Override
    public void initView() {
        setTitle(getString(R.string.ask_contract));
        hideRightImagview();
        Bundle bundle=getIntent().getExtras();
        orderId = bundle.getString("orderId");
        sale_advice_url=bundle.getString("sale_advice_url");
        tvSaleAskBill=getViewById(R.id.tvSaleAskBill);
        rlSure=getViewById(R.id.rlSure);
        cbSure=getViewById(R.id.tvDot);
        btConSubmit=getViewById(R.id.btConSubmit);
    }

    @Override
    public void setListener() {
        tvSaleAskBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putString("orderId", orderId + "");
                bundle.putString("sale_advice_url", sale_advice_url);
                ActivitySkip.forward(AskContractActivity.this,SaleReceiveNoticeActivity.class,bundle);
            }
        });

        btConSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyContract(orderId);
            }
        });
        rlSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbSure.isChecked()){
                    cbSure.setChecked(false);
                }else{
                    cbSure.setChecked(true);
                }
            }
        });
    }

    private void applyContract(String orderId) {
        FinalHttp http = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("order_id", orderId + "");
        http.post(Contants.APPLY_CONTRACT, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                try {
                    JSONObject json=new JSONObject(s);
                    if(json.getString("code").equals("1")){
                        SaleRecordActivity.sra.handler.sendEmptyMessage(0x00);
                        ToasShow.showToastBottom(AskContractActivity.this,json.getString("msg"));
                    }else{
                        ToasShow.showToastBottom(AskContractActivity.this,json.getString("msg"));
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
}
