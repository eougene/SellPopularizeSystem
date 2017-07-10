package com.yd.org.sellpopularizesystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.fragment.HomeFragment;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

public class InformationContentActivity extends BaseActivity {
    private TextView tvInformContent;
    @Override
    protected int setContentView() {
        return R.layout.activity_information_content;
    }

    @Override
    public void initView() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String str = bundle.getString("data", getString(R.string.novalue));
        String title = bundle.getString("title");
        String notice_id = bundle.getString("notice_id", "null");
        tvInformContent = (TextView) findViewById(R.id.tvInformContent);
        tvInformContent.setText(title+"\n"+str);
        setTitle(getString(R.string.notic_info));
        hideRightImagview();
        commitNotice(notice_id);

    }

    private void commitNotice(String str) {
        FinalHttp http = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        ajaxParams.put("notice_logs_id", str);
        http.get(Contants.SUBMIT_READED, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                //通知首页加载消息数量
                HomeFragment.homeFragment.mHandler.sendEmptyMessage(1);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
            }
        });
    }

    @Override
    public void setListener() {

    }
}
