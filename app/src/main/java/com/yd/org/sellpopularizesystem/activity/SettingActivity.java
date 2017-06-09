package com.yd.org.sellpopularizesystem.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.application.BaseApplication;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.javaBean.CustomBean;
import com.yd.org.sellpopularizesystem.myView.BindAcountPopupWindow;
import com.yd.org.sellpopularizesystem.myView.CircleImageView;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;
import com.yd.org.sellpopularizesystem.utils.ToasShow;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 * 设置中心
 */
public class SettingActivity extends BaseActivity {
    private TextView  cancelLoginTv, versionTv, tvUserName;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {

                //注销登录
                case R.id.cancelLoginTv:
                    logOut();
                    break;

            }
        }
    };

    @Override
    protected int setContentView() {
        return R.layout.activity_setting;
    }

    @Override
    public void initView() {
        setTitle(getResources().getString(R.string.home_setting));
        hideRightImagview();
        //settiing_noticTv = getViewById(R.id.settiing_noticTv);
        cancelLoginTv = getViewById(R.id.cancelLoginTv);
        versionTv = getViewById(R.id.versionTv);
        versionTv.setText(getVersion());

    }


    @Override
    public void setListener() {

        cancelLoginTv.setOnClickListener(mOnClickListener);

    }

    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return "Ver: " + version;

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }


    private void logOut() {
        SharedPreferencesHelps.clearUserID();
        SharedPreferencesHelps.cleaAccount();
        SharedPreferencesHelps.clearUserName();
        SharedPreferencesHelps.clearUserPassword();
        ActivitySkip.forward(SettingActivity.this, LoginActivity.class);
        finish();
    }




}
