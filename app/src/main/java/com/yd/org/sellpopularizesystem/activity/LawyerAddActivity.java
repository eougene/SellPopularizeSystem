package com.yd.org.sellpopularizesystem.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.application.ExtraName;
import com.yd.org.sellpopularizesystem.utils.ToasShow;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.json.JSONException;
import org.json.JSONObject;

public class LawyerAddActivity extends BaseActivity {
    private EditText etLawyerName, etAddress, etFirstName, etLastName, etTel, etEmail;
    private String strName;
    private String strAdress;
    private String strFirst;
    private String strLast;
    private String strTel;
    private String strEmail;

    @Override
    protected int setContentView() {

        return R.layout.activity_lawyer_add;
    }

    @Override
    public void initView() {
        setTitle(getString(R.string.lawyerinformation));
        setRightTitle(R.string.customdetaild_save, mSaveOnClickListener);
        etLawyerName = getViewById(R.id.etLawyerName);
        etAddress = getViewById(R.id.etAddress);
        etFirstName = getViewById(R.id.etFirstName);
        etLastName = getViewById(R.id.etLastName);
        etTel = getViewById(R.id.etTel);
        etEmail = getViewById(R.id.etEmail);
    }

    View.OnClickListener mSaveOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            strName = etLawyerName.getText().toString();
            strAdress = etAddress.getText().toString();
            strFirst = etAddress.getText().toString();
            strLast = etAddress.getText().toString();
            strTel = etAddress.getText().toString();
            strEmail = etAddress.getText().toString();
            if (!TextUtils.isEmpty(strName) && !TextUtils.isEmpty(strAdress) && !TextUtils.isEmpty(strFirst)
                    && !TextUtils.isEmpty(strFirst) && !TextUtils.isEmpty(strLast) && !TextUtils.isEmpty(strTel) && !TextUtils.isEmpty(strEmail)) {
                save();
            }else {
                ToasShow.showToastCenter(LawyerAddActivity.this,getString(R.string.complete_hint));
            }
        }
    };

    private void save() {
        FinalHttp finalHttp = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("law_firm", strName);
        ajaxParams.put("street_address_1", strAdress);
        ajaxParams.put("first_name", strFirst);
        ajaxParams.put("surname", strLast);
        ajaxParams.put("lawyer_tel", strTel);
        ajaxParams.put("lawyer_email", strEmail);
        finalHttp.post(Contants.NEW_LAWYER, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                try {
                    JSONObject json = new JSONObject(s);
                    if (json.getString("code").equals("1")) {
                        ToasShow.showToastCenter(LawyerAddActivity.this, json.getString("msg"));
                        LawyerActivity.lawyerActivity.handler.sendEmptyMessage(ExtraName.UPDATE);
                        finish();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    @Override
    public void setListener() {

    }
}
