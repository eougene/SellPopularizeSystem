package com.yd.org.sellpopularizesystem.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.igexin.sdk.PushManager;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.activity.HomeActiviyt;
import com.yd.org.sellpopularizesystem.application.BaseApplication;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.getui.IntentService;
import com.yd.org.sellpopularizesystem.getui.PushService;
import com.yd.org.sellpopularizesystem.javaBean.UserBean;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;
import com.yd.org.sellpopularizesystem.utils.ToasShow;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.Map;

import static com.yd.org.sellpopularizesystem.utils.ToasShow.showToast;

/**
 * Created by hejin on 2017/6/12.
 */

public class LoginFragment extends BaseFragmentView {
    private EditText useName, usePassword;
    private TextView tvLogin;
    private RelativeLayout rlloginWechat;
    private String client_id = "";
    private Class userPushService = PushService.class;
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_login);
        initWidget();
    }

    private void initWidget() {

        useName = getViewById(R.id.useName);
        usePassword = getViewById(R.id.usePassword);
        tvLogin = getViewById(R.id.tvLogin);
        rlloginWechat = getViewById(R.id.loginWechat);
        startGeTui();
    }

    private void startGeTui() {
        PushManager.getInstance().initialize(getActivity().getApplicationContext(), userPushService);
        PushManager.getInstance().registerPushIntentService(getActivity().getApplicationContext(), IntentService.class);
    }

    @Override
    protected void setListener() {
        tvLogin.setOnClickListener(mOnClickListener);
        rlloginWechat.setOnClickListener(mOnClickListener);
        /*
         * 键盘是完成按钮的功能,直接登陆
          */
                usePassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_DONE) {
                            getUserInfo();
                        }
                        return false;
                    }
                });
    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                //登陆
                case R.id.tvLogin:
                    getUserInfo();
                    break;

                //微信登陆
                case R.id.loginWechat:

                    if (UMShareAPI.get(getActivity()).isInstall(getActivity(), SHARE_MEDIA.WEIXIN)) {//判断是否安装微信
                        UMShareAPI.get(getActivity()).doOauthVerify(getActivity(), SHARE_MEDIA.WEIXIN, authListener);
                    } else {
                        ToasShow.showToastCenter(getActivity(), getString(R.string.check_wechat_install));
                        return;
                    }
                    break;
            }
        }
    };

    private void getUserInfo() {
        String name = useName.getText().toString().trim();
        String password = usePassword.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password)) {
            showToast(getActivity(), getResources().getString(R.string.login_usename));
            return;

        } else {
            if (TextUtils.isEmpty(BaseApplication.getInstance().cid) || BaseApplication.getInstance().cid == "") {
                client_id = PushManager.getInstance().getClientid(getActivity());
            } else {
                client_id = BaseApplication.getInstance().cid;

            }
            getLogin(name, password, client_id);
        }

    }

    private void getLogin(String name, String password, String client_id) {
       showLoadingDialog();
        final FinalHttp fh = new FinalHttp();
        fh.addHeader("Content-Type", "application/x-www-form-urlencoded");
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("account", name);
        ajaxParams.put("password", password);
        //个推识别码
        ajaxParams.put("client_id", client_id);
        fh.post(Contants.HOME_LOGIN, ajaxParams, new AjaxCallBack<String>() {

            @Override
            public void onSuccess(String json) {
                dismissLoadingDialog();
                if (!TextUtils.isEmpty(json)) {
                    Gson gson = new Gson();
                    UserBean userBean = gson.fromJson(json, UserBean.class);
                    if (userBean.getCode().equals("1")) {
                        showToast(getActivity(), userBean.getMsg());
                        SharedPreferencesHelps.setUserID(userBean.getResult().getUser_id() + "");
                        SharedPreferencesHelps.setCompanyId(userBean.getResult().getCompany_id() + "");
                        SharedPreferencesHelps.setAccount(userBean.getResult().getAccount());
                        SharedPreferencesHelps.setUserName(userBean.getResult().getUser_nick());
                        SharedPreferencesHelps.setFirstName(userBean.getResult().getFirst_name());
                        SharedPreferencesHelps.setSurName(userBean.getResult().getSurname());
                        SharedPreferencesHelps.setUserPassword(userBean.getResult().getPassword());
                        ActivitySkip.forward(getActivity(), HomeActiviyt.class);
                        getActivity().finish();
                    } else {
                        ToasShow.showToast(getActivity(), userBean.getMsg());
                    }


                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                dismissLoadingDialog();
                ToasShow.showToast(getActivity(), strMsg);
            }
        });
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
            SharedPreferencesHelps.setOpenId(openId);
            String temp = "";
         /*   for (String key : data.keySet()) {
                temp = temp + key + " : " + data.get(key) + "\n";
            }*/
            Log.e("data***", "openId:" + openId);


            third_login(openId);


        }

        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {
            ToasShow.showToastBottom(getActivity(), t.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {

        }
    };

    private void third_login(String openid) {
        showLoadingDialog();
        FinalHttp finalHttp = new FinalHttp();
        finalHttp.addHeader("Content-Type", "application/x-www-form-urlencoded");
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("type", "wechat");
        ajaxParams.put("openid", openid);
        finalHttp.post(Contants.WEIXIN_LOGIN, ajaxParams, new AjaxCallBack<String>() {

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                dismissLoadingDialog();
                ToasShow.showToast(getActivity(), strMsg);
            }

            @Override
            public void onSuccess(String json) {
                dismissLoadingDialog();
                if (!TextUtils.isEmpty(json)) {
                    Gson gson = new Gson();
                    UserBean userBean = gson.fromJson(json, UserBean.class);
                    if (userBean.getCode().equals("1")) {
                        //先取消上次的授权
                        UMShareAPI.get(getActivity()).deleteOauth(getActivity(), SHARE_MEDIA.WEIXIN, null);
                        showToast(getActivity(), userBean.getMsg());
                        SharedPreferencesHelps.setUserID(userBean.getResult().getUser_id() + "");
                        SharedPreferencesHelps.setCompanyId(userBean.getResult().getCompany_id() + "");
                        SharedPreferencesHelps.setAccount(userBean.getResult().getAccount());
                        SharedPreferencesHelps.setUserName(userBean.getResult().getUser_nick());
                        SharedPreferencesHelps.setFirstName(userBean.getResult().getFirst_name());
                        SharedPreferencesHelps.setSurName(userBean.getResult().getSurname());
                        SharedPreferencesHelps.setUserPassword(userBean.getResult().getPassword());
                        ActivitySkip.forward(getActivity(), HomeActiviyt.class);
                        getActivity().finish();
                    } else {
                        ToasShow.showToast(getActivity(), userBean.getMsg());
                    }
                }
            }
        });


    }
}
