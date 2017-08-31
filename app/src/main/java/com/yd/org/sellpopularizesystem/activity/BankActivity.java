package com.yd.org.sellpopularizesystem.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.gson.Gson;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.javaBean.ErrorBean;
import com.yd.org.sellpopularizesystem.javaBean.MyUserInfo;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;
import com.yd.org.sellpopularizesystem.utils.ToasShow;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.model.CacheMode;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.HttpParams;



public class BankActivity extends BaseActivity {
    EditText accountNameEd;
    EditText accountNumberEd;
    EditText bankNameEd;
    EditText bsbEd;
    private MyUserInfo myUserInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int setContentView() {
        return R.layout.activity_bank;
    }

    @Override
    public void initView() {
        setTitle("银行卡信息");

        myUserInfo = (MyUserInfo) getIntent().getSerializableExtra("userkey");

         accountNameEd=getViewById(R.id.accountNameEd);
         accountNumberEd=getViewById(R.id.accountNumberEd);
         bankNameEd=getViewById(R.id.bankNameEd);
         bsbEd=getViewById(R.id.bsbEd);


        //持卡人
        accountNameEd.setText((null == myUserInfo.getResult().getAccount_name() || TextUtils.isEmpty(myUserInfo.getResult().getAccount_name())) ? "" : myUserInfo.getResult().getAccount_name());
        //卡号
        accountNumberEd.setText((null == myUserInfo.getResult().getAccount_number() || TextUtils.isEmpty(myUserInfo.getResult().getAccount_number())) ? "" : myUserInfo.getResult().getAccount_number());
        //开户行
        bankNameEd.setText((null == myUserInfo.getResult().getBank_name() || TextUtils.isEmpty(myUserInfo.getResult().getBank_name())) ? "" : myUserInfo.getResult().getBank_name());
        //bsb
        bsbEd.setText((null == myUserInfo.getResult().getBsb() || TextUtils.isEmpty(myUserInfo.getResult().getBsb())) ? "" : myUserInfo.getResult().getBsb());


    }

    @Override
    public void setListener() {
        setRightTitle(R.string.customdetaild_save, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAdress();
            }
        });
    }


    private void saveAdress() {

        String account_name, account_number, bank_name, bsb;
        //持卡人
        if (TextUtils.isEmpty(accountNameEd.getText().toString().trim())) {
            ToasShow.showToastCenter(BankActivity.this, "请填写持卡人");
            return;
        } else {
            account_name = accountNameEd.getText().toString().trim();
        }

        //卡号
        if (TextUtils.isEmpty(accountNumberEd.getText().toString().trim())) {
            ToasShow.showToastCenter(BankActivity.this, "请填写卡号");
            return;
        } else {
            account_number = accountNumberEd.getText().toString().trim();
        }
        //开户行
        if (TextUtils.isEmpty(bankNameEd.getText().toString().trim())) {
            ToasShow.showToastCenter(BankActivity.this, "请填写街道号");
            return;
        } else {
            bank_name = bankNameEd.getText().toString().trim();
        }
        //bsb
        if (TextUtils.isEmpty(bsbEd.getText().toString().trim())) {
            ToasShow.showToastCenter(BankActivity.this, "请填写BSB");
            return;
        } else {
            bsb = bsbEd.getText().toString().trim();
        }


        HttpParams httpParams = new HttpParams();
        httpParams.put("user_id", SharedPreferencesHelps.getUserID());
        httpParams.put("account_name", account_name);
        httpParams.put("account_number", account_number);
        httpParams.put("bank_name", bank_name);
        httpParams.put("bsb", bsb);


        EasyHttp.post(Contants.UPDATE_USER)
                .params(httpParams)
                .timeStamp(true)
                .cacheMode(CacheMode.NO_CACHE)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        showDialog();
                    }

                    @Override
                    public void onError(ApiException e) {
                        closeDialog();
                        ToasShow.showToastCenter(BankActivity.this, e.getMessage());
                        Log.e("onError***", "onError:" + e.getMessage());
                    }

                    @Override
                    public void onSuccess(String s) {
                        closeDialog();
                        Log.e("onSuccess***", "onSuccess:" + s);

                        Gson gson = new Gson();
                        ErrorBean errorBean = gson.fromJson(s, ErrorBean.class);
                        ToasShow.showToastCenter(BankActivity.this, errorBean.getMsg());
                        if (errorBean.getCode().equals("1")) {
                            //保存银行信息记录
                            SharedPreferencesHelps.setUserBank(1);
                            finish();
                        }

                    }
                });

    }


}
