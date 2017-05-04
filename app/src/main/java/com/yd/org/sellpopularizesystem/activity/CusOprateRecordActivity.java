package com.yd.org.sellpopularizesystem.activity;

import android.app.Dialog;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bigkoo.pickerview.lib.WheelView;
import com.bigkoo.pickerview.listener.CustomListener;
import com.hp.hpl.sparta.xpath.ThisNodeTest;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.javaBean.GradeBean;
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
    private TextView tvMoneyNum,tvPayMethod,tvEoiSubmit;
    private Button btDoacash,btDoaTransfer,btDoaCancel;
    private CommonPopuWindow payMethodPop;
    private View mView;
    @Override
    protected int setContentView() {
        setBaseLayoutBackground(Color.WHITE);
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
                        bundle.putString("cora","visit");
                        ActivitySkip.forward(CusOprateRecordActivity.this,DialogOptionActivity.class, bundle);
                    }else if (flag.equals("custoreser")){
                        bundle.putString("cora","reserver");
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
            setTitle("EOI");
            getEoiData();
            setRightTitle(R.string.recharge, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Dialog dialog=new Dialog(CusOprateRecordActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.eoi_operate_view);
                    Window dialogWindow = dialog.getWindow();
                    WindowManager.LayoutParams lp = dialogWindow.getAttributes();
                    lp.x= MyUtils.getStatusBarHeight(CusOprateRecordActivity.this);
                    dialogWindow.setAttributes(lp);
                    dialogWindow.setGravity(Gravity.CENTER|Gravity.TOP);
                    dialog.show();
                    initDialogViews(dialog);
                }
            });

        }
    }

    private void initDialogViews(Dialog dialog) {
        tvMoneyNum= (TextView) dialog.findViewById(R.id.tvMoneyNum);
        tvPayMethod= (TextView) dialog.findViewById(R.id.tvPayMethod);
        tvEoiSubmit= (TextView) dialog.findViewById(R.id.tvEoiSubmit);
        payMethodPop=new CommonPopuWindow(CusOprateRecordActivity.this,mOnClickListener) {
            @Override
            protected int getLayoutId() {
                return R.layout.doa_paymetod_pop;
            }
        };
        mView=payMethodPop.getContentView();
        btDoacash= (Button) mView.findViewById(R.id.btDoaCash);
        btDoaTransfer= (Button) mView.findViewById(R.id.btDoaTransfer);
        btDoaCancel= (Button) mView.findViewById(R.id.btDoaCancel);
        tvPayMethod.setOnClickListener(mOnClickListener);
        tvEoiSubmit.setOnClickListener(mOnClickListener);
        btDoacash.setOnClickListener(mOnClickListener);
        btDoaTransfer.setOnClickListener(mOnClickListener);
        btDoaCancel.setOnClickListener(mOnClickListener);
        tvPayMethod.setOnClickListener(mOnClickListener);
    }

    View.OnClickListener mOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tvPayMethod:
                    payMethodPop.showAtLocation(CusOprateRecordActivity.this.findViewById(R.id.llEoi),Gravity.BOTTOM, 0,0);
                    break;
                case R.id.tvEoiSubmit:
                    submitEoi();
                    break;
                case R.id.btDoaCash:
                    tvMoneyNum.setText(btDoacash.getText());
                    payMethodPop.dismiss();
                    break;
                case R.id.btDoaTransfer:
                    tvMoneyNum.setText(btDoaTransfer.getText());
                    payMethodPop.dismiss();
                    break;
                case R.id.btDoaCancel:
                    payMethodPop.dismiss();
                    break;
            }
        }
    };

    private void submitEoi() {

    }

    private void getEoiData() {

    }

    @Override
    public void setListener() {

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
