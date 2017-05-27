package com.yd.org.sellpopularizesystem.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.igexin.sdk.PushManager;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.yd.org.sellpopularizesystem.R;
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
 * 登陆页面
 * Created by bai on 207/1/11.
 */

public class LoginActivity extends BaseActivity {


    private EditText useName, usePassword;
    private ImageView loginImageView, loginWechat;
    private Class userPushService = PushService.class;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                //登陆
                case R.id.loginImageView:
                    getUserInfo();
                    break;

                //第三方登陆
                case R.id.loginWechat:

                    if (UMShareAPI.get(LoginActivity.this).isInstall(LoginActivity.this, SHARE_MEDIA.WEIXIN)) {//判断是否安装微信
                        UMShareAPI.get(LoginActivity.this).doOauthVerify(LoginActivity.this, SHARE_MEDIA.WEIXIN, authListener);
                    } else {
                        ToasShow.showToastCenter(LoginActivity.this, "检查是否安装微信");
                        return;
                    }
                    break;
            }
        }
    };


    @Override
    protected int setContentView() {
        return R.layout.login_activty;
    }

    @Override
    public void initView() {
        hideBaseTab();
        useName = getViewById(R.id.useName);
        usePassword = getViewById(R.id.usePassword);
        loginImageView = getViewById(R.id.loginImageView);
        loginWechat = getViewById(R.id.loginWechat);

        //gettui
        startGeTui();
    }


    //启动个推服务
    private void startGeTui() {
        //cid= PushManager.getInstance().getClientid(this);
        // 注册 intentService 后 PushDemoReceiver 无效, sdk 会使用 IntentService 传递数据,
        // AndroidManifest 对应保留一个即可(如果注册 IntentService, 可以去掉 PushDemoReceiver, 如果注册了
        // IntentService, 必须在 AndroidManifest 中声明)
        PushManager.getInstance().initialize(this.getApplicationContext(), userPushService);
        PushManager.getInstance().registerPushIntentService(this.getApplicationContext(), IntentService.class);


    }


    @Override
    public void setListener() {
        loginImageView.setOnClickListener(mOnClickListener);
        loginWechat.setOnClickListener(mOnClickListener);

        /**
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

    private void getUserInfo() {
        String name = useName.getText().toString().trim();
        String password = usePassword.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password)) {
            showToast(this, getResources().getString(R.string.login_usename));
            return;

        } else {
            getLogin(name, password);
        }

    }

    private void getLogin(String name, String password) {
        showDialog();
        final FinalHttp fh = new FinalHttp();
        fh.addHeader("Content-Type", "application/x-www-form-urlencoded");
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("account", name);
        ajaxParams.put("password", password);
        //个推识别码
        ajaxParams.put("client_id", BaseApplication.mApp.cid);
        fh.post(Contants.HOME_LOGIN, ajaxParams, new AjaxCallBack<String>() {

            @Override
            public void onSuccess(String json) {
                closeDialog();
                if (!TextUtils.isEmpty(json)) {
                    Gson gson = new Gson();
                    UserBean userBean = gson.fromJson(json, UserBean.class);
                    if (userBean.getCode().equals("1")) {
                        showToast(LoginActivity.this, userBean.getMsg());
                        SharedPreferencesHelps.setUserID(userBean.getResult().getUser_id() + "");
                        SharedPreferencesHelps.setCompanyId(userBean.getResult().getCompany_id() + "");
                        SharedPreferencesHelps.setAccount(userBean.getResult().getAccount());
                        SharedPreferencesHelps.setUserName(userBean.getResult().getUser_nick());
                        SharedPreferencesHelps.setFirstName(userBean.getResult().getFirst_name());
                        SharedPreferencesHelps.setSurName(userBean.getResult().getSurname());
                        SharedPreferencesHelps.setUserPassword(userBean.getResult().getPassword());
                        ActivitySkip.forward(LoginActivity.this, HomeActiviyt.class);
                        finish();
                    } else {
                        ToasShow.showToast(LoginActivity.this, userBean.getMsg());
                    }


                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                closeDialog();
                ToasShow.showToast(LoginActivity.this, strMsg);
            }
        });
    }


    UMAuthListener authListener = new UMAuthListener() {
        @Override
        public void onStart(SHARE_MEDIA platform) {
            showDialog();
            Log.e("开始授权", "platform:" + platform);
        }

        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            closeDialog();
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
            ToasShow.showToastBottom(LoginActivity.this, t.getMessage());
        }

        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {

        }
    };

    private void third_login(String openid) {
        showDialog();
        FinalHttp finalHttp = new FinalHttp();
        finalHttp.addHeader("Content-Type", "application/x-www-form-urlencoded");
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("type", "wechat");
        ajaxParams.put("openid", openid);
        finalHttp.post(Contants.WEIXIN_LOGIN, ajaxParams, new AjaxCallBack<String>() {


            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                closeDialog();
                ToasShow.showToast(LoginActivity.this, strMsg);
            }

            @Override
            public void onSuccess(String json) {
                closeDialog();
                if (!TextUtils.isEmpty(json)) {
                    Gson gson = new Gson();
                    UserBean userBean = gson.fromJson(json, UserBean.class);
                    if (userBean.getCode().equals("1")) {
                        //先取消上次的授权
                        UMShareAPI.get(LoginActivity.this).deleteOauth(LoginActivity.this, SHARE_MEDIA.WEIXIN, null);
                        showToast(LoginActivity.this, userBean.getMsg());
                        SharedPreferencesHelps.setUserID(userBean.getResult().getUser_id() + "");
                        SharedPreferencesHelps.setCompanyId(userBean.getResult().getCompany_id() + "");
                        SharedPreferencesHelps.setAccount(userBean.getResult().getAccount());
                        SharedPreferencesHelps.setUserName(userBean.getResult().getUser_nick());
                        SharedPreferencesHelps.setFirstName(userBean.getResult().getFirst_name());
                        SharedPreferencesHelps.setSurName(userBean.getResult().getSurname());
                        SharedPreferencesHelps.setUserPassword(userBean.getResult().getPassword());
                        ActivitySkip.forward(LoginActivity.this, HomeActiviyt.class);
                        finish();
                    } else {
                        ToasShow.showToast(LoginActivity.this, userBean.getMsg());
                    }
                }
            }
        });


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(LoginActivity.this).onActivityResult(requestCode, resultCode, data);
    }
}
