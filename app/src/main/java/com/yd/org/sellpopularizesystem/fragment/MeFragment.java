package com.yd.org.sellpopularizesystem.fragment;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
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
import com.yd.org.sellpopularizesystem.activity.ChangePassWordActivity;
import com.yd.org.sellpopularizesystem.activity.CommissionActivity;
import com.yd.org.sellpopularizesystem.activity.LoginActivity;
import com.yd.org.sellpopularizesystem.activity.MyTeamActivity;
import com.yd.org.sellpopularizesystem.activity.SaleRecordActivity;
import com.yd.org.sellpopularizesystem.activity.SettingActivity;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.javaBean.CustomBean;
import com.yd.org.sellpopularizesystem.myView.BindAcountPopupWindow;
import com.yd.org.sellpopularizesystem.myView.CircleImageView;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.ObjectSaveUtil;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;
import com.yd.org.sellpopularizesystem.utils.StatusBarUtil;
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

public class MeFragment extends BaseFragmentView {
    private RelativeLayout changePassWordRel, bindAccountRel, saleRecord, rlTeam, rlSetting;
    private TextView tvUserName;
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
                //销售记录
                case R.id.saleRecord:
                    ActivitySkip.forward(getActivity(), SaleRecordActivity.class);
                    break;
                //我的佣金
                case R.id.rlCommission:
                    ActivitySkip.forward(getActivity(), CommissionActivity.class);
                    break;
                //我的团队
                case R.id.rlTeam:
                    ActivitySkip.forward(getActivity(), MyTeamActivity.class);
                    break;
                //设置
                case R.id.rlSetting:
                    ActivitySkip.forward(getActivity(), SettingActivity.class);
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
                    if (UMShareAPI.get(getActivity()).isInstall(getActivity(), SHARE_MEDIA.WEIXIN)) {//判断是否安装微信
                        acountPopupWindow.dismiss();
                        UMShareAPI.get(getActivity()).doOauthVerify(getActivity(), SHARE_MEDIA.WEIXIN, authListener);
                    } else {
                        ToasShow.showToastCenter(getActivity(), getString(R.string.check_we));
                        return;
                    }

                    break;
                case R.id.cancelButton:
                    acountPopupWindow.dismiss();
                    break;
            }
        }
    };

    private void logOut() {
        //先取消上次的微信授权
        UMShareAPI.get(getActivity()).deleteOauth(getActivity(), SHARE_MEDIA.WEIXIN, null);

        //清除用户数据
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
            showLoadingDialog();
            Log.e("开始授权", "platform:" + platform);
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            dismissLoadingDialog();
            Log.e("授权成功", "platform:" + platform);
            String openId = data.get("openid");
            String access_token = data.get("access_token");
            SharedPreferencesHelps.setOpenId(openId);

            Log.e("data***", "openId:" + openId + "\naccess_token:" + access_token);


            getWeiXinInfo(access_token, openId);


        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            ToasShow.showToastBottom(getActivity(), t.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            // ToasShow.showToastBottom(getActivity(), "已取消授权");
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
                            ToasShow.showToastCenter(getContext(), getString(R.string.binding_f));
                            return;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                ToasShow.showToastCenter(getContext(), strMsg);
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
        http.post(Contants.BINDING_THIRD, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                dismissLoadingDialog();
                Log.e("TAG", "onSuccess: " + s);
                try {
                    JSONObject json = new JSONObject(s);
                    ToasShow.showToastCenter(getActivity(), json.getString("msg"));
                    if (json.getString("code").equals("1")) {
                        logOut();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                ToasShow.showToastCenter(getContext(), strMsg);
                dismissLoadingDialog();
            }
        });
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_me);
        StatusBarUtil.setTranslucentForImageViewInFragment(getActivity(), 0, null);
        initWidget();
    }

    private void initWidget() {
        saleRecord = getViewById(R.id.saleRecord);
        rlCommission = getViewById(R.id.rlCommission);
        rlCommission.setOnClickListener(mOnClickListener);
        rlTeam = getViewById(R.id.rlTeam);
        rlTeam.setOnClickListener(mOnClickListener);
        ivCustomePhoto = getViewById(R.id.ivCustomePhoto);
        rlSetting = getViewById(R.id.rlSetting);
        rlSetting.setOnClickListener(mOnClickListener);
        changePassWordRel = getViewById(R.id.changePassWordRel);
        bindAccountRel = getViewById(R.id.bindAccountRel);
        tvUserName = getViewById(R.id.tvCustomeName);
        tvUserName.setText(SharedPreferencesHelps.getSurName() + "  " + SharedPreferencesHelps.getFirstName());

        acountPopupWindow = new BindAcountPopupWindow(getActivity(), mItemClick);
        CustomBean.ResultBean resultBean = (CustomBean.ResultBean) ObjectSaveUtil.readObject(getActivity(),"custome");
        if (resultBean != null) {
            Picasso.with(getActivity()).load(Contants.DOMAIN + "/" + resultBean.getHead_img()).fit().centerCrop().
                    config(Bitmap.Config.RGB_565).error(R.mipmap.settingbt).into(ivCustomePhoto);

        }
    }

    @Override
    protected void setListener() {
        changePassWordRel.setOnClickListener(mOnClickListener);
        bindAccountRel.setOnClickListener(mOnClickListener);
        saleRecord.setOnClickListener(mOnClickListener);
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
