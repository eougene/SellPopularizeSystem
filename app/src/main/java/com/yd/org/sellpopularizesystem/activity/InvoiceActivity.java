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
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.javaBean.InvoiceDetailBean;
import com.yd.org.sellpopularizesystem.utils.MyUtils;
import com.yd.org.sellpopularizesystem.utils.ToasShow;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.HttpParams;


/**
 * 发票
 */

public class InvoiceActivity extends BaseActivity {
    private TextView consentTextView, resoluteTextView, subButton,tvInvoiceId,tvAbn,tvPhone,tvAddress,
    tvDate,tvBillTO,tvAcn,tvTel,tvFor,tvDescription,tvAmountIncl,tvSubtotal,tvGst,tvAbnRegisterd,tvOther;
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
                    HttpParams httpParams=new HttpParams();
                    httpParams.put("invoice_id","");//发票编号
                    httpParams.put("status","2");//2：同意  3：拒绝
                    httpParams.put("sales_submit_amount","2");//销售提交金额
                    httpParams.put("reject_reason","2");//拒绝原因
                    EasyHttp.post(Contants.APPROVE_OR_REFUSE_INVOICE)
                            .timeStamp(true)//是否需要追加时间戳
                            .params(httpParams)
                            .execute(new SimpleCallBack<String>() {
                                @Override
                                public void onError(ApiException e) {
                                    ToasShow.showToast(InvoiceActivity.this, getResources().getString(R.string.network_error));
                                }

                                @Override
                                public void onSuccess(String s) {

                                }
                            });
                    break;

            }
        }
    };
    private InvoiceDetailBean.ResultBean rb;


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
        tvInvoiceId= getViewById(R.id.tvInvoiceId);
        tvAbn= getViewById(R.id.tvAbn);
        tvPhone= getViewById(R.id.tvPhone);
        tvAddress= getViewById(R.id.tvAddress);
        tvDate= getViewById(R.id.tvDate);
        tvBillTO= getViewById(R.id.tvBillTO);
        tvAcn= getViewById(R.id.tvAcn);
        tvTel= getViewById(R.id.tvTel);
        tvFor= getViewById(R.id.tvFor);
        tvDescription= getViewById(R.id.tvDescription);
        tvAmountIncl= getViewById(R.id.tvAmountIncl);
        tvSubtotal= getViewById(R.id.tvSubtotal);
        tvGst= getViewById(R.id.tvGst);
        tvAbnRegisterd= getViewById(R.id.tvAbnRegisterd);
        tvOther= getViewById(R.id.tvOther);
        rb = (InvoiceDetailBean.ResultBean) getIntent().getExtras().getSerializable("bean");
        setInfo();
        showReasonInfo();

    }

    private void setInfo() {
        tvInvoiceId.setText(rb.getInvoice_no());
        tvAbn.setText(rb.getAbn());
        tvPhone.setText(rb.getPhone());
        tvAddress.setText(rb.getAddress());
        tvDate.setText(MyUtils.getInstance().date2String("yyyy/MM/dd", Long.parseLong(String.valueOf(rb.getDate())+"000")));
        tvFor.setText(rb.getForX());
        tvDescription.setText(rb.getDescription());
        tvAmountIncl.setText(rb.getAmount());
        tvSubtotal.setText(rb.getSubtotal());
        tvAbnRegisterd.setText(rb.getIs_gst()==1?"Yes":"No");
        tvOther.setText(rb.getOther());
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
