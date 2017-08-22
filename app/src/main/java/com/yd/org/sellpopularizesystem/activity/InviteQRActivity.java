package com.yd.org.sellpopularizesystem.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.widget.TextView;

import com.yd.org.sellpopularizesystem.R;

public class InviteQRActivity extends BaseActivity {
    private TextView tvDes;

    @Override
    protected int setContentView() {
        setTitle(getString(R.string.invite_code));
        hideRightImagview();
        return R.layout.activity_invite;
    }

    @Override
    public void initView() {
        tvDes=getViewById(R.id.tvDes);
        String msg=getString(R.string.qrdes);
        setTextColor(msg);
    }

    private void setTextColor(String msg) {
        if(msg.contains("5%")){
            int a=msg.indexOf("5");
            int b=msg.indexOf("%")+1;
            SpannableStringBuilder builder = new SpannableStringBuilder(msg);
            builder.setSpan(new ForegroundColorSpan(Color.RED), a, b, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvDes.setText(builder);
        }
    }

    @Override
    public void setListener() {

    }
}
