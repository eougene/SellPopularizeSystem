package com.yd.org.sellpopularizesystem.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.javaBean.ErrorBean;
import com.yd.org.sellpopularizesystem.utils.StringUtils;
import com.yd.org.sellpopularizesystem.utils.ToasShow;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.model.CacheMode;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.HttpParams;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by hejin on 2017/6/12.
 * 注册
 */

public class RegisterFragment extends BaseFragmentView {
    @BindView(R.id.recommendIdEdit)
    EditText recommendIdEdit;
    @BindView(R.id.familyNameEdit)
    EditText familyNameEdit;
    @BindView(R.id.lastNameEdit)
    EditText lastNameEdit;
    @BindView(R.id.phoneEdit)
    EditText phoneEdit;
    @BindView(R.id.regsterEmailEdit)
    EditText regsterEmailEdit;
    @BindView(R.id.passwordEdit)
    EditText passwordEdit;
    @BindView(R.id.showPassword)
    ImageView showPassword;
    @BindView(R.id.regsterText)
    TextView regsterText;
    Unbinder unbinder;
    private int temp = 0;


    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_register);
    }


    private void getInfo() {
        String recommendIdString, familyNameString, lastNameString, phoneString, regsterEmailString, passwordString;


        //推荐码

        if (TextUtils.isEmpty(recommendIdEdit.getText().toString().trim())) {
            ToasShow.showToastCenter(getActivity(), "推荐码不能为空");
            return;
        } else {
            recommendIdString = recommendIdEdit.getText().toString().trim();
        }

        //姓氏
        if (TextUtils.isEmpty(familyNameEdit.getText().toString().trim())) {
            ToasShow.showToastCenter(getActivity(), "请填写真实的姓氏");
            return;
        } else {
            familyNameString = familyNameEdit.getText().toString().trim();
        }
        //名字
        if (TextUtils.isEmpty(lastNameEdit.getText().toString().trim())) {
            ToasShow.showToastCenter(getActivity(), "请填写真实的名字");
            return;
        } else {
            lastNameString = lastNameEdit.getText().toString().trim();
        }


        //手机号码
        phoneString = phoneEdit.getText().toString().trim();


        //邮箱

        if (TextUtils.isEmpty(regsterEmailEdit.getText().toString().trim())) {
            ToasShow.showToastCenter(getActivity(), getString(R.string.email_hint));
            return;
        } else {
            regsterEmailString = regsterEmailEdit.getText().toString().trim();
        }


        //密码

        if (TextUtils.isEmpty(passwordEdit.getText().toString().trim())) {
            ToasShow.showToastCenter(getActivity(), getString(R.string.hintpass));
            return;
        } else {

            if (!StringUtils.isLetterDigit(passwordEdit.getText().toString().trim())) {
                ToasShow.showToastCenter(getActivity(), getString(R.string.hintpass));
                return;
            } else {
                passwordString = passwordEdit.getText().toString().trim();
            }
        }


        HttpParams httpParams = new HttpParams();
        //推荐码
        httpParams.put("refer_code", recommendIdString);
        httpParams.put("first_name", familyNameString);
        httpParams.put("surname", lastNameString);
        httpParams.put("mobile", phoneString);
        httpParams.put("e_mail", regsterEmailString);
        httpParams.put("password", passwordString);

        EasyHttp.post(Contants.REGISTER_REFER)
                .cacheMode(CacheMode.NO_CACHE)
                .params(httpParams)
                .timeStamp(true)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onStart() {
                        showLoadingDialog();
                    }

                    @Override
                    public void onError(ApiException e) {
                        dismissLoadingDialog();
                        ToasShow.showToastCenter(getActivity(), e.getMessage());

                    }

                    @Override
                    public void onSuccess(String s) {
                        dismissLoadingDialog();
                        Log.e("注册推荐人", "s:" + s);

                        Gson gson = new Gson();
                        ErrorBean errorBean = gson.fromJson(s, ErrorBean.class);
                        if (errorBean.getCode().equals("1")) {
                            ToasShow.showToastCenter(getActivity(), errorBean.getMsg());
                        } else {
                            ToasShow.showToastCenter(getActivity(), errorBean.getMsg());
                        }

                    }
                });

    }


    @Override
    protected void setListener() {


    }


    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }


    @OnClick({R.id.showPassword, R.id.regsterText})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            //是否显示密码
            case R.id.showPassword:

                //隐藏密码
                if (temp == 0) {
                    passwordEdit.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    temp = 1;
                } else {
                    //显示密码
                    passwordEdit.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    temp = 0;
                }
                break;

            //注册
            case R.id.regsterText:
                getInfo();
                break;
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
