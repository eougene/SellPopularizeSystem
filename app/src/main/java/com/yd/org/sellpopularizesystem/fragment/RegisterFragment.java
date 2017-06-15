package com.yd.org.sellpopularizesystem.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.yd.org.sellpopularizesystem.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.yd.org.sellpopularizesystem.utils.ToasShow.showToast;

/**
 * Created by hejin on 2017/6/12.
 */

public class RegisterFragment extends BaseFragmentView {
    private EditText etUser,etPassword,etPassAgain;
    private TextView tvRegister;
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_register);
        initWidget();
    }

    private void initWidget() {
        etUser=getViewById(R.id.etUser);
        etPassword=getViewById(R.id.etPassword);
        etPassAgain=getViewById(R.id.etPassAgain);
        tvRegister=getViewById(R.id.tvRegister);
    }

    @Override
    protected void setListener() {
        /*tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getRegisterInfo();
            }
        });*/
    }

    private void getRegisterInfo() {
        String userName = etUser.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String passAgain= etPassAgain.getText().toString().trim();
        if (TextUtils.isEmpty(userName) || TextUtils.isEmpty(password) || TextUtils.isEmpty(passAgain)) {
            showToast(getActivity(), getResources().getString(R.string.login_usename));
            return;

        }else if (TextUtils.isDigitsOnly(userName)){
            if (!isPhoneNumberValid(userName)){

            }
        }
    }

    private boolean isPhoneNumberValid(String userName) {
        Matcher m = null;
        if(userName.length()>0){
            Pattern p = Pattern.compile("^((13[0-9])|(15[0-3])|(15[7-9])|(18[0,5-9]))\\d{8}$");
            m= p.matcher(userName);
        } else{

            return false;
        }
        return m.matches();
    }


    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
