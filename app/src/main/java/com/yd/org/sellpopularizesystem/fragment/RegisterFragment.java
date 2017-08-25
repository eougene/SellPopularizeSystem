package com.yd.org.sellpopularizesystem.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.utils.StringUtils;
import com.yd.org.sellpopularizesystem.utils.ToasShow;

/**
 * Created by hejin on 2017/6/12.
 * 注册
 */

public class RegisterFragment extends BaseFragmentView {
    private EditText regsterID, regsterPassword;
    private ImageView showPassword;
    private TextView tvRegster;
    private int temp = 0;


    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_register);
        initWeight();

    }

    private void initWeight() {
        regsterID = getViewById(R.id.regsterID);
        regsterPassword = getViewById(R.id.regsterPassword);
        showPassword = getViewById(R.id.showPassword);
        tvRegster = getViewById(R.id.tvRegster);

    }


    private void getInfo() {

        String rId, pass;


        //邮箱

        if (TextUtils.isEmpty(regsterID.getText().toString().trim()) || StringUtils.isEmail(regsterID.getText().toString().trim())) {
            ToasShow.showToastCenter(getActivity(), getString(R.string.email_hint));
            return;
        } else {
            rId = regsterID.getText().toString().trim();
        }


        //密码

        if (TextUtils.isEmpty(regsterPassword.getText().toString().trim())) {
            ToasShow.showToastCenter(getActivity(), getString(R.string.hintpass));
            return;
        } else {

            if (!StringUtils.isLetterDigit(regsterPassword.getText().toString().trim())) {
                ToasShow.showToastCenter(getActivity(), getString(R.string.hintpass));
                return;
            } else {
                pass = regsterPassword.getText().toString().trim();
            }
        }


        commintInfo();

    }




    @Override
    protected void setListener() {
        //注册
        tvRegster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getInfo();
            }
        });

        //密码显示或不显示
        showPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                //隐藏密码
                if (temp == 0) {
                    regsterPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    temp = 1;
                } else {
                    //显示密码
                    regsterPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    temp = 0;
                }

            }
        });
    }


    private void commintInfo(){
      //  showLoadingDialog();






    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }
}
