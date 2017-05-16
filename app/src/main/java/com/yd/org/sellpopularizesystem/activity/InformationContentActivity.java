package com.yd.org.sellpopularizesystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

public class InformationContentActivity extends BaseActivity {
    private TextView tvInformContent;
    private String strContent;

    @Override
    protected int setContentView() {
        return R.layout.activity_information_content;
    }

    @Override
    public void initView() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String str = bundle.getString("data", "没取到值");
        String title = bundle.getString("title");
        String userId = bundle.getString("notice_id", "null");
        tvInformContent = (TextView) findViewById(R.id.tvInformContent);
        tvInformContent.setText(str);
        setTitle(title);
        hideRightImagview();
        commitNotice(userId);

    }

    private void commitNotice(String str) {
        showDialog();
        FinalHttp http = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        ajaxParams.put("notice_id", str);
        http.get(Contants.SUBMIT_READED, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                closeDialog();
                try {
                    JSONObject json = new JSONObject(s);
                    String str = json.getString("msg");
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
