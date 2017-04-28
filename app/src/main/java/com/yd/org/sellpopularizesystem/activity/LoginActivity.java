package com.yd.org.sellpopularizesystem.activity;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.javaBean.UserBean;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;
import com.yd.org.sellpopularizesystem.utils.ToasShow;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import static com.yd.org.sellpopularizesystem.utils.ToasShow.showToast;

/**
 * 登陆页面
 * Created by bai on 207/1/11.
 */

public class LoginActivity extends BaseActivity {
    private EditText useName, usePassword;
    private ImageView loginImageView, loginWechat;
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
                    loginByWeChat();
                    break;
            }
        }
    };

    private void loginByWeChat() {
        FinalHttp fht = new FinalHttp();
        AjaxParams ajaxParams=new AjaxParams();
        ajaxParams.put("type","wechat");
        ajaxParams.put("openid",SharedPreferencesHelps.getOpenId());
        fht.post(Contants.WEIXIN_LOGIN, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }


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
                        SharedPreferencesHelps.setCompanyId(userBean.getResult().getCompany_id()+"");
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


                } else {
                    return;
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                closeDialog();
                ToasShow.showToast(LoginActivity.this, getResources().getString(R.string.network_error));
            }
        });
    }

}
