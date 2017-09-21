package com.yd.org.sellpopularizesystem.activity;

import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yd.org.sellpopularizesystem.R;


/**
 * 发票
 */

public class InvoiceActivity extends BaseActivity {
    private TextView consentTextView, resoluteTextView, subButton;
    private EditText moneyEdit, memoEdit;
    private RelativeLayout rlFirb;
    private View firbPwView;
    private PopupWindow firbSelectPopWindow;


    private View.OnClickListener mOnClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                //同意
                case R.id.consentTextView:
                    firbSelectPopWindow.showAtLocation(InvoiceActivity.this.findViewById(R.id.primaryLinear), Gravity.BOTTOM, 0, 0);
                    break;

                //拒绝
                case R.id.resoluteTextView:

                    break;

            }
        }
    };


    @Override
    protected int setContentView() {
        return R.layout.activity_invoice;
    }

    @Override
    public void initView() {
        hideRightImagview();
        setTitle(getString(R.string.invoice));

        consentTextView = getViewById(R.id.consentTextView);
        resoluteTextView = getViewById(R.id.resoluteTextView);


        showReasonInfo();

    }

    @Override
    public void setListener() {
        consentTextView.setOnClickListener(mOnClick);
        resoluteTextView.setOnClickListener(mOnClick);
    }

    private void showReasonInfo() {
        firbPwView = LayoutInflater.from(this).inflate(R.layout.invoce_layout, null);
        rlFirb = (RelativeLayout) firbPwView.findViewById(R.id.mainRelat);

        //金额
        moneyEdit = (EditText) firbPwView.findViewById(R.id.moneyEdit);
        //备注
        memoEdit = (EditText) firbPwView.findViewById(R.id.memoEdit);
        subButton = (TextView) firbPwView.findViewById(R.id.subButton);

        firbSelectPopWindow = new PopupWindow(firbPwView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        //设置SelectPicPopupWindow弹出窗体动画效果
        firbSelectPopWindow.setAnimationStyle(R.style.Animation);
        //实例化一个ColorDrawable颜色为半透明
        ColorDrawable firb = new ColorDrawable(0xb0000000);
        //设置SelectPicPopupWindow弹出窗体的背景
        firbSelectPopWindow.setBackgroundDrawable(firb);


        //提交数据
        subButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firbSelectPopWindow.dismiss();
                String moneyString = moneyEdit.getText().toString().trim();
                String memoString = memoEdit.getText().toString().trim();


            }
        });

        //点击外面取消
        rlFirb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firbSelectPopWindow.dismiss();
            }
        });


    }


}
