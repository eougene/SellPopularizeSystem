package com.yd.org.sellpopularizesystem.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.application.BaseApplication;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.javaBean.CustomBean;
import com.yd.org.sellpopularizesystem.javaBean.ProvinceInfoModel;
import com.yd.org.sellpopularizesystem.myView.CircleImageView;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.MyUtils;

public class InvestigationActivity extends BaseActivity {
    private Button btSubmit,btUnsure;
    private TextView tvCustomeName;
    private EditText etContact,etAddress;
    private CircleImageView ivCustomePhoto;
    @Override
    protected int setContentView() {
        return R.layout.activity_investigation;

    }

    @Override
    public void initView() {
        setTitle(getString(R.string.inverstigation));
        hideRightImagview();
        btSubmit= (Button) findViewById(R.id.btnSubmit);
        btUnsure= (Button) findViewById(R.id.btnUnsure);
        tvCustomeName= (TextView) findViewById(R.id.tvCustomeName);
        etContact = (EditText) findViewById(R.id.etContact);
        etAddress= (EditText) findViewById(R.id.etAddress);
        ivCustomePhoto= (CircleImageView) findViewById(R.id.ivHeadPhoto);
        CustomBean.ResultBean resultBean = BaseApplication.getInstance().getResultBean();
        tvCustomeName.setText(resultBean.getTrue_name().equals("")?"":resultBean.getTrue_name());
        etContact.setText(resultBean.getMobile());
        etAddress.setText((String) resultBean.getAddress());
         int size=MyUtils.px2dip(this,115f);
        Log.i("size", "initView: "+size);
        Picasso.with(this).load(Contants.DOMAIN + "/"+resultBean.getHead_img())
                .error(R.mipmap.settingbt).into(ivCustomePhoto);
    }

    @Override
    public void setListener() {
            btSubmit.setOnClickListener(mClickListener);
            btUnsure.setOnClickListener(mClickListener);
    }

    View.OnClickListener mClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btnSubmit:
                    ActivitySkip.forward(InvestigationActivity.this,ScaleDeltaileActivity.class);
                    break;
                case  R.id.btnUnsure:
                    ActivitySkip.forward(InvestigationActivity.this,HomeActiviyt.class);
                    break;
            }
        }
    };

}
