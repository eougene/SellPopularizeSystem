package com.yd.org.sellpopularizesystem.activity;

import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.application.BaseApplication;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.javaBean.CustomBean;
import com.yd.org.sellpopularizesystem.myView.CircleImageView;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxParams;

/**
 * 开始调研页面
 */
public class InvestigationActivity extends BaseActivity {
    private Button btSubmit, btUnsure;
    private TextView tvCustomeName, edtTime;
    private EditText etContact, etAddress;
    private CircleImageView ivCustomePhoto;
    private CustomBean.ResultBean resultBean;

    @Override
    protected int setContentView() {
        return R.layout.activity_investigation;

    }

    @Override
    public void initView() {
        setTitle(getString(R.string.inverstigation));
        hideRightImagview();
        btSubmit = getViewById(R.id.btnSubmit);
        btUnsure = getViewById(R.id.btnUnsure);
        tvCustomeName = getViewById(R.id.tvCustomeName);
        etContact = getViewById(R.id.etContact);
        etAddress = getViewById(R.id.etAddress);
        ivCustomePhoto = getViewById(R.id.ivHeadPhoto);
        edtTime = getViewById(R.id.edtTime);
        resultBean = BaseApplication.getInstance().getResultBean();


        if (!TextUtils.isEmpty(resultBean.getSurname()) || !TextUtils.isEmpty(resultBean.getFirst_name())) {

            if (!TextUtils.isEmpty(resultBean.getSurname()) && !TextUtils.isEmpty(resultBean.getFirst_name())) {
                tvCustomeName.setText(resultBean.getSurname() + " " + resultBean.getFirst_name());
            } else if (!TextUtils.isEmpty(resultBean.getSurname())) {
                tvCustomeName.setText(resultBean.getSurname() + "");
            } else if (!TextUtils.isEmpty(resultBean.getFirst_name())) {
                tvCustomeName.setText(resultBean.getFirst_name() + "");
            }

        }

        if (!TextUtils.isEmpty(resultBean.getMobile())) {
            etContact.setText(resultBean.getMobile() + "");
        }

        if (!TextUtils.isEmpty(resultBean.getAddress() + "")) {
            etAddress.setText(resultBean.getAddress() + "");
        }

        if (!TextUtils.isEmpty(resultBean.getHead_img())) {
            Picasso.with(this).load(Contants.DOMAIN + "/" + resultBean.getHead_img()).into(ivCustomePhoto);
        } else {
            ivCustomePhoto.setImageResource(R.mipmap.settingbt);
        }

    }

    @Override
    public void setListener() {
        btSubmit.setOnClickListener(mClickListener);
        btUnsure.setOnClickListener(mClickListener);
        edtTime.setOnClickListener(mClickListener);
    }

    View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btnSubmit:
                    ActivitySkip.forward(InvestigationActivity.this, InvestigateListActivity.class);
                    break;
                case R.id.btnUnsure:
                    ActivitySkip.forward(InvestigationActivity.this, HomeActiviyt.class);
                    break;

                case R.id.edtTime:
                    break;
            }
        }
    };

    private void setResult() {
        FinalHttp finalHttp = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        // ajaxParams.put("product_id", );
        ajaxParams.put("answer", "");
        ajaxParams.put("add_ip", "");
    }
}
