package com.yd.org.sellpopularizesystem.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.lib.WheelView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.hp.hpl.sparta.xpath.ThisNodeTest;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.myView.CommonPopuWindow;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.MyUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CusOprateRecordActivity extends BaseActivity {
    private String customeId;
    private Bundle bundle;
    private String flag;

    @Override
    protected int setContentView() {
        return R.layout.activity_cus_oprate_record;
    }

    @Override
    public void initView() {
        bundle = getIntent().getExtras();
        flag = bundle.getString("custocora");
        customeId = (String) bundle.get("customeId");
        Log.e("TAG", "initView: "+customeId);
        if (flag.equals("custovisit") || flag.equals("custoreser")) {
            if (flag.equals("custovisit")) {
                setTitle(getString(R.string.visit));
                getVisitData();
            } else if (flag.equals("custoreser")) {
                setTitle(getString(R.string.yuyue));
                getReservertData();
            }
            clickRightImageView(R.mipmap.addim, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flag.equals("custovisit")){
                        bundle.putString("visit","visit");
                        ActivitySkip.forward(CusOprateRecordActivity.this,DialogOptionActivity.class, bundle);
                    }else if (flag.equals("custoreser")){
                        bundle.putString("reserver","reserver");
                        ActivitySkip.forward(CusOprateRecordActivity.this,DialogOptionActivity.class, bundle);
                    }
                    //addOpratePopWin.showAtLocation(CusOprateRecordActivity.this.findViewById(R.id.flContent), Gravity.BOTTOM,0,0);
                }
            });
        } else if (flag.equals("custoexpand") || flag.equals("custopurchase")){
            hideRightImagview();
            if (flag.equals("custoexpand")) {
                setTitle(getString(R.string.expandre));
                getExpandReData();
            } else if (flag.equals("custopurchase")) {
                setTitle(getString(R.string.housepurchase));
                getHousePurData();
            }
        }else{
            setRightTitle(R.string.recharge, new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }
    }

    @Override
    public void setListener() {

    }

    private void submit() {

    }

    private void getVisitData() {

    }

    private void getReservertData() {

    }

    private void getExpandReData() {

    }

    private void getHousePurData() {

    }

}
