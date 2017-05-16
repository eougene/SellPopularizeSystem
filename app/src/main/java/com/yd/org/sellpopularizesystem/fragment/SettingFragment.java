package com.yd.org.sellpopularizesystem.fragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.activity.ChangePassWordActivity;
import com.yd.org.sellpopularizesystem.activity.CommissionActivity;
import com.yd.org.sellpopularizesystem.activity.LoginActivity;
import com.yd.org.sellpopularizesystem.activity.SaleRecordActivity;
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
 * Created by hejin on 2017/4/10.
 */

public class SettingFragment extends BaseFragmentView {
    private RelativeLayout changePassWordRel, bindAccountRel, rlSaleRecord;
    private TextView cancelLoginTv, versionTv, tvUserName;
    private BindAcountPopupWindow acountPopupWindow;
    private CircleImageView ivCustomePhoto;
    private RelativeLayout rlCommission;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                //修改密码
                case R.id.changePassWordRel:
                    ActivitySkip.forward(getActivity(), ChangePassWordActivity.class);
                    break;
                //绑定账号
                case R.id.bindAccountRel:
                    bindAccountRel();
                    break;
                //通知
                case R.id.saleRecord:
                    ActivitySkip.forward(getActivity(), SaleRecordActivity.class);
                    break;
                //注销登录
                case R.id.cancelLoginTv:
                    logOut();
                    break;
                //我的佣金
                case R.id.rlCommission:
                    ActivitySkip.forward(getActivity(), CommissionActivity.class);
                    break;
            }
        }
    };

    private void bindAccountRel() {
        if (acountPopupWindow != null) {
            acountPopupWindow = new BindAcountPopupWindow(getActivity(), mItemClick);
        }
        acountPopupWindow.showAtLocation(getActivity().findViewById(R.id.bindAccountRel), Gravity.BOTTOM, 50, 50);
    }

    View.OnClickListener mItemClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.bindAcount:
//                    if (OauthHelper.isAuthenticated(SettingActivity.this, SHARE_MEDIA.WEIXIN)) {
//                        mController.deleteOauth(SettingActivity.this, SHARE_MEDIA.WEIXIN, null);
//                        Log.e("log", "--- SHARE_MEDIA.WEIXIN----已授权------");
//                    } else {
//                        Log.e("log", "--- SHARE_MEDIA.WEIXIN-----未授权------");
//                    }

                    UMShareAPI.get(getActivity()).doOauthVerify(getActivity(), SHARE_MEDIA.WEIXIN, authListener);

                    break;
                case R.id.cancelButton:
                    acountPopupWindow.dismiss();
                    break;
            }
        }
    };

    private void logOut() {
        SharedPreferencesHelps.clearUserID();
        SharedPreferencesHelps.cleaAccount();
        SharedPreferencesHelps.clearUserName();
        SharedPreferencesHelps.clearUserPassword();
        ActivitySkip.forward(getActivity(), LoginActivity.class);
        getActivity().finish();
    }

    UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            Log.e("开始授权", "platform:" + platform);
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Log.e("授权成功", "platform:" + platform);
            String openId = data.get("openid");
            SharedPreferencesHelps.setOpenId(openId);
            String temp = "";
         /*   for (String key : data.keySet()) {
                temp = temp + key + " : " + data.get(key) + "\n";
            }*/
            Log.e("data***", "openId:" + openId);


            getWeiXinInfo(data.get("access_token"), data.get("openId"));


        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {

            Toast.makeText(getActivity(), "失败：" + t.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {

            Toast.makeText(getActivity(), "取消了", Toast.LENGTH_LONG).show();
        }
    };


    /**
     * 获取微信用户信息  unionid
     *
     * @param access_token
     * @param opendid
     */
    public void getWeiXinInfo(String access_token, final String opendid) {

        final FinalHttp fh = new FinalHttp();
        fh.get("https://api.weixin.qq.com/sns/userinfo?" + "access_token=" + access_token + "&openid=" + opendid, new AjaxCallBack<String>() {

            @Override
            public void onSuccess(String content) {
                Log.e("获取微信数据*******", "new String(arg2):" + content);

                if (content != null) {

                    try {
                        JSONObject UseJson = new JSONObject(content);
                        if (!TextUtils.isEmpty(UseJson.optString("unionid"))) {
                            sendBindAccount(opendid, UseJson.optString("unionid"));
                        } else {
                            ToasShow.showToastCenter(getContext(), "绑定失败");
                            return;
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                ToasShow.showToastCenter(getContext(), "网络异常");
            }
        });


    }

    private void sendBindAccount(String openId, String unionid) {
        showLoadingDialog();
        FinalHttp http = new FinalHttp();
        http.addHeader("Content-Type", "application/x-www-form-urlencoded");
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        ajaxParams.put("type", "wechat");
        ajaxParams.put("openid", openId);
        ajaxParams.put("unionid", unionid);
        http.post(Contants.WEXIN_URL_STRING, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                acountPopupWindow.dismiss();
                dismissLoadingDialog();
                Log.e("TAG", "onSuccess: " + s);
                try {
                    JSONObject json = new JSONObject(s);
                    if (json.get("code").equals("1")) {
                        acountPopupWindow.dismiss();
                    } else {
                        String str = json.getString("msg");
                        ToasShow.showToastBottom(getActivity(), json.getString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToasShow.showToastCenter(getContext(), "网络异常");
                dismissLoadingDialog();
            }
        });
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.activity_setting);
        initWidget();
    }

    private void initWidget() {

        rlCommission = getViewById(R.id.rlCommission);
        rlCommission.setOnClickListener(mOnClickListener);
        ivCustomePhoto = getViewById(R.id.ivCustomePhoto);
        changePassWordRel = getViewById(R.id.changePassWordRel);
        bindAccountRel = getViewById(R.id.bindAccountRel);
        rlSaleRecord = getViewById(R.id.saleRecord);
        tvUserName = getViewById(R.id.tvCustomeName);
        tvUserName.setText(SharedPreferencesHelps.getSurName() + SharedPreferencesHelps.getFirstName());
        //settiing_noticTv = getViewById(R.id.settiing_noticTv);
        cancelLoginTv = getViewById(R.id.cancelLoginTv);
        versionTv = getViewById(R.id.versionTv);
        versionTv.setText(getVersion());
        acountPopupWindow = new BindAcountPopupWindow(getActivity(), mItemClick);
        CustomBean.ResultBean resultBean = BaseApplication.getInstance().getResultBean();
        if (resultBean != null) {
            Picasso.with(getActivity()).load(Contants.DOMAIN + "/" + resultBean.getHead_img())
                    .error(R.mipmap.settingbt).into(ivCustomePhoto);
        }
    }

    @Override
    protected void setListener() {
        changePassWordRel.setOnClickListener(mOnClickListener);
        bindAccountRel.setOnClickListener(mOnClickListener);
        cancelLoginTv.setOnClickListener(mOnClickListener);
        rlSaleRecord.setOnClickListener(mOnClickListener);
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(getActivity()).onActivityResult(requestCode, resultCode, data);
    }

    public String getVersion() {
        try {
            PackageManager manager = getContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo(getContext().getPackageName(), 0);
            String version = info.versionName;
            return "Ver: " + version;

        } catch (Exception e) {
            e.printStackTrace();
            return null;

        }
    }
}
