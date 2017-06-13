package com.yd.org.sellpopularizesystem.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.adapter.SaleRecordAdapter;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.application.ExtraName;
import com.yd.org.sellpopularizesystem.internal.PullToRefreshLayout;
import com.yd.org.sellpopularizesystem.internal.PullableListView;
import com.yd.org.sellpopularizesystem.javaBean.ErrorBean;
import com.yd.org.sellpopularizesystem.javaBean.SaleOrderBean;
import com.yd.org.sellpopularizesystem.myView.CommonPopuWindow;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.BitmapUtil;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;
import com.yd.org.sellpopularizesystem.utils.ToasShow;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;


public class SaleRecordActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener {
    private PullableListView lvSaleRecord;
    private PullToRefreshLayout ptrlSaleRecord;
    private int page = 1;
    private View mSalePopView;
    private Button btApplyContract, btOrderUpdate, btPayIntention, btOrderCancel, btSaleCancel;
    private LinearLayout llSalePop;
    private List<SaleOrderBean.ResultBean> sobRbData = new ArrayList<>();
    private SaleRecordAdapter saleAdapter;
    private CommonPopuWindow mSalePopuwindow;
    public static SaleRecordActivity sra;
    private String picPath;
    private SaleOrderBean.ResultBean resultBean;

    //上传合同首页还是首付款标志
    private String flag;


    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            getSaleData(page, true);
        }
    };

    @Override
    protected int setContentView() {
        return R.layout.activity_sale_record;
    }

    @Override
    public void initView() {
        sra = this;
        hideRightImagview();
        setTitle(getString(R.string.sale_list));
        lvSaleRecord = getViewById(R.id.content_view);
        ptrlSaleRecord = getViewById(R.id.refresh_view);
        ptrlSaleRecord.setOnRefreshListener(this);
        //加载数据
        getSaleData(page, true);
    }

    private void showDialogWindow(SaleOrderBean.ResultBean resultBean) {
        mSalePopuwindow = new CommonPopuWindow(SaleRecordActivity.this) {
            @Override
            protected int getLayoutId() {
                return R.layout.saleoperate_popuwindow;
            }
        };
        mSalePopView = mSalePopuwindow.getContentView();
        llSalePop = (LinearLayout) mSalePopView.findViewById(R.id.llSalePop);
        btApplyContract = (Button) mSalePopView.findViewById(R.id.btApplyContract);
        btPayIntention = (Button) mSalePopView.findViewById(R.id.btPayIntention);
        btOrderUpdate = (Button) mSalePopView.findViewById(R.id.btOrderUpdate);
        btOrderCancel = (Button) mSalePopView.findViewById(R.id.btOrderCancel);
        btSaleCancel = (Button) mSalePopView.findViewById(R.id.btSaleCancel);

        mSalePopuwindow.showAtLocation(SaleRecordActivity.this.findViewById(R.id.flSale), Gravity.CENTER, 0, 0);


        //事件
        llSalePop.setOnClickListener(new MOnClickListener(resultBean));
        btApplyContract.setOnClickListener(new MOnClickListener(resultBean));
        btOrderCancel.setOnClickListener(new MOnClickListener(resultBean));
        btSaleCancel.setOnClickListener(new MOnClickListener(resultBean));
        btPayIntention.setOnClickListener(new MOnClickListener(resultBean));
        btOrderUpdate.setOnClickListener(new MOnClickListener(resultBean));


        //意向金凭证已上传,,,请申请合同
        if (resultBean.getOrder_money_status() == 1
                && resultBean.getCancel_apply_status() == 0
                && resultBean.getSale_advice_status() == 0
                && resultBean.getStatus() == 0) {

            //测试用
            btOrderUpdate.setVisibility(View.GONE);
            if (resultBean.getPayment_method() == 1 || resultBean.getPayment_method() == 4) {
                //意向金已支付
                btPayIntention.setVisibility(View.GONE);


            } else {
                //意向金未支付
                btApplyContract.setVisibility(View.GONE);
            }
        }


    }


    public class MOnClickListener implements View.OnClickListener {

        private SaleOrderBean.ResultBean resultBeans;

        public MOnClickListener(SaleOrderBean.ResultBean resultBeans) {
            this.resultBeans = resultBeans;

        }


        @Override
        public void onClick(View v) {
            Bundle bundle = new Bundle();
            bundle.putString("orderId", resultBeans.getProduct_orders_id() + "");
            bundle.putString("sale_advice_url", resultBeans.getSale_advice_url());
            bundle.putString("price", resultBeans.getPrice());
            bundle.putString("surname", resultBeans.getCustomer_surname() + " " + resultBeans.getCustomer_first_name());
            switch (v.getId()) {
                //点击pop内容区之外pop消失
                case R.id.llSalePop:
                    mSalePopuwindow.dismiss();
                    break;
                //点击申请合同
                case R.id.btApplyContract:
                    if (btApplyContract.getText().equals(getString(R.string.saler_15))) {
                        mSalePopuwindow.dismiss();
                        flag = ExtraName.UPLOAD_CONTRACT;
                        BitmapUtil.startImageCapture(SaleRecordActivity.this, ExtraName.TAKE_PICTURE);
                    } else {
                        mSalePopuwindow.dismiss();
                        ActivitySkip.forward(SaleRecordActivity.this, AskContractActivity.class, bundle);
                    }
                    break;
                //点击意向金
                case R.id.btPayIntention:
                    mSalePopuwindow.dismiss();
                    Bundle bun = new Bundle();
                    bun.putString("payurlId", resultBeans.getOrder_money_url());
                    bun.putString("payment_method", resultBeans.getPayment_method() + "");
                    ActivitySkip.forward(SaleRecordActivity.this, PaymentQrActivity.class, bun);

                    break;
                //上传凭证
                case R.id.btOrderUpdate:
                    mSalePopuwindow.dismiss();
                    flag = ExtraName.UPLOAD_FIRST_COMMISSION;
                    BitmapUtil.startImageCapture(SaleRecordActivity.this, ExtraName.TAKE_PICTURE);
                    break;
                //点击放弃订单
                case R.id.btOrderCancel:
                    mSalePopuwindow.dismiss();
                    canceOrder(resultBeans.getProduct_orders_id());

                    break;
                //点击取消
                case R.id.btSaleCancel:
                    mSalePopuwindow.dismiss();
                    break;
            }
        }
    }


    private void getSaleData(int page, final boolean isRefresh) {
        showDialog();
        FinalHttp http = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("company_id", SharedPreferencesHelps.getCompanyId());
        ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        ajaxParams.put("page", page + "");
        ajaxParams.put("number", "50");
        http.get(Contants.INQUIRE_ORDER_LIST, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                closeDialog();
                Log.e("TAG", "onSuccess: " + s);
                if (null != s) {
                    parseJson(s, isRefresh);
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                closeDialog();
            }
        });
    }


    /**
     * 获取数据
     *
     * @param json
     * @param isRefresh
     */
    private void parseJson(String json, boolean isRefresh) {

        Gson gson = new Gson();
        SaleOrderBean saleOrderBean = gson.fromJson(json, SaleOrderBean.class);

        if (saleOrderBean.getCode() == 1) {
            sobRbData = saleOrderBean.getResult();

        }


        if (isRefresh) {
            ptrlSaleRecord.refreshFinish(PullToRefreshLayout.SUCCEED);
            saleAdapter = new SaleRecordAdapter(this);
            lvSaleRecord.setAdapter(saleAdapter);
        }
        saleAdapter.addMore(sobRbData);
        ptrlSaleRecord.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }

    /**
     * 取消订单
     *
     * @param orderId
     */
    private void canceOrder(int orderId) {
        showDialog();
        FinalHttp http = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("order_id", orderId + "");
        ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        http.post(Contants.ORDER_CANCEL, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                closeDialog();
                Gson gson = new Gson();
                ErrorBean errorBean = gson.fromJson(s, ErrorBean.class);
                if (errorBean.getCode().equals("1")) {
                    ToasShow.showToastCenter(SaleRecordActivity.this, errorBean.getMsg());
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                closeDialog();
            }
        });
    }

    @Override
    public void setListener() {
        lvSaleRecord.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                SaleRecordAdapter.ViewHolder viewHolder = (SaleRecordAdapter.ViewHolder) view.getTag();
                resultBean = viewHolder.resultBean;
                saleAdapter.setCurrentItem(resultBean.getProduct_orders_id());
                saleAdapter.notifyDataSetChanged();

                //正在申请合同,等待管理员审核
                if (resultBean.getOrder_money_status() == 1
                        && resultBean.getSales_advice_status() == 0
                        && resultBean.getContract_apply_status() == 1
                        && resultBean.getStatus() == 0
                        && resultBean.getCancel_apply_status() == 0) {


                }
                //意向金凭证已上传,,,请申请合同
                else if (resultBean.getOrder_money_status() == 1
                        && resultBean.getCancel_apply_status() == 0
                        && resultBean.getSale_advice_status() == 0
                        && resultBean.getStatus() == 0) {

                    showDialogWindow(resultBean);


                    //订单放弃中
                } else if ((resultBean.getOrder_money_status() == 1 || resultBean.getOrder_money_status() == 2)
                        && resultBean.getCancel_apply_status() == 1
                        && resultBean.getStatus() == 0) {

                    //订单已取消
                } else if (resultBean.getOrder_money_status() == 1
                        && resultBean.getCancel_apply_status() == 2
                        && resultBean.getStatus() == 10) {


                    //准备交换合同
                } else if (resultBean.getOrder_money_status() == 2
                        && resultBean.getContract_apply_status() == 2
                        && resultBean.getBuy_money_status() == 2
                        && resultBean.getCancel_apply_status() == 0
                        && resultBean.getUpload_contract_status() == 2
                        && resultBean.getStatus() == 0) {


                    //合同首页已审核 , 首付款凭证审核中
                } else if (resultBean.getUpload_contract_status() == 2
                        && resultBean.getBuy_money_status() == 1
                        && resultBean.getCancel_apply_status() == 0) {


                    //请上传合同首页 ,请上传首付款凭证
                } else if (resultBean.getContract_apply_status() == 2
                        && resultBean.getUpload_contract_status() == 0
                        && resultBean.getBuy_money_status() == 0
                        && resultBean.getCancel_apply_status() == 0) {
                    showDialogWindow(resultBean);
                    //合同首页审核中,请上传首付款凭证
                } else if (resultBean.getContract_apply_status() == 2
                        && resultBean.getUpload_contract_status() == 1
                        && resultBean.getBuy_money_status() == 0
                        && resultBean.getCancel_apply_status() == 0) {
                    showDialogWindow(resultBean);
                    // 合同首页已审核,请上传首付款凭证
                } else if (resultBean.getContract_apply_status() == 2
                        && resultBean.getUpload_contract_status() == 2
                        && resultBean.getBuy_money_status() == 0
                        && resultBean.getCancel_apply_status() == 0) {
                    showDialogWindow(resultBean);
                    //合同首页已审核,首付款凭证审核中
                } else if (resultBean.getContract_apply_status() == 2
                        && resultBean.getUpload_contract_status() == 2
                        && resultBean.getBuy_money_status() == 1
                        && resultBean.getCancel_apply_status() == 0) {

                }


            }
        });


    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        page = 1;
        getSaleData(page, true);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        page++;
        getSaleData(page, false);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                //拍照上传
                case ExtraName.TAKE_PICTURE:
                    if (null != data && null != data.getData()) {
                        picPath = BitmapUtil.getImagePath(SaleRecordActivity.this, data.getData(), null, null);
                        setPicPath(picPath);
                    }
            }
        }
    }


    /**
     * 上传支付凭证
     *
     * @param picPath
     */
    private void setPicPath(String picPath) {
        try {
            showDialog();
            FinalHttp finalHttp = new FinalHttp();
            AjaxParams ajaxParams = new AjaxParams();
            String strUrl = "";
            ajaxParams.put("order_id", resultBean.getProduct_orders_id() + "");

            //支付定金
            if (flag.equals(ExtraName.UPLOAD_CONTRACT)) {
                strUrl = Contants.UPLOAD_CONTRACT_PHOTO;
                if (null != picPath && !picPath.equals("")) {
                    File picFile = new File(picPath);
                    ajaxParams.put("file", picFile);
                }
            } else {
                //支付房款-上传凭证或在线支付
                strUrl = Contants.UPLOAD_FIRST_COMMISSION;
                ajaxParams.put("money_where", "");
                ajaxParams.put("pay_method", "");
                ajaxParams.put("pay_time", "");
                ajaxParams.put("amount", "");
                ajaxParams.put("remark", "");
                ajaxParams.put("image", "");
            }

            finalHttp.post(strUrl, ajaxParams, new AjaxCallBack<String>() {
                @Override
                public void onSuccess(String s) {
                    closeDialog();
                    Gson gson = new Gson();
                    ErrorBean errorBean = gson.fromJson(s, ErrorBean.class);
                    if (errorBean.getCode().equals("1")) {
                        ToasShow.showToastCenter(SaleRecordActivity.this, errorBean.getMsg());
                    } else {
                        ToasShow.showToastCenter(SaleRecordActivity.this, errorBean.getMsg());
                    }


                }

                @Override
                public void onFailure(Throwable t, int errorNo, String strMsg) {
                    closeDialog();

                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
