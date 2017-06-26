package com.yd.org.sellpopularizesystem.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.adapter.SaleRecordAdapter;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.application.ExtraName;
import com.yd.org.sellpopularizesystem.internal.PullToRefreshLayout;
import com.yd.org.sellpopularizesystem.internal.PullableListView;
import com.yd.org.sellpopularizesystem.javaBean.ErrorBean;
import com.yd.org.sellpopularizesystem.javaBean.ProductListBean;
import com.yd.org.sellpopularizesystem.javaBean.SaleOrderBean;
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
    private List<SaleOrderBean.ResultBean> sobRbData = new ArrayList<>();
    private SaleRecordAdapter saleAdapter;
    public static SaleRecordActivity saleRecordActivity;
    //上传合同首页还是首付款标志
    private String flag, picPath;
    private SaleOrderBean.ResultBean rBean;


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
        saleRecordActivity = this;
        hideRightImagview();
        setTitle(getString(R.string.sale_list));
        lvSaleRecord = getViewById(R.id.content_view);
        ptrlSaleRecord = getViewById(R.id.refresh_view);
        ptrlSaleRecord.setOnRefreshListener(this);
        //加载数据
        getSaleData(page, true);
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
            if (sobRbData.size() == 0) {
                getViewById(R.id.noInfomation).setVisibility(View.VISIBLE);
                lvSaleRecord.setVisibility(View.GONE);
            } else {
                getViewById(R.id.noInfomation).setVisibility(View.GONE);
                lvSaleRecord.setVisibility(View.VISIBLE);
            }
            ptrlSaleRecord.refreshFinish(PullToRefreshLayout.SUCCEED);
            saleAdapter = new SaleRecordAdapter(this);
            lvSaleRecord.setAdapter(saleAdapter);
        }
        saleAdapter.addMore(sobRbData);
        ptrlSaleRecord.loadmoreFinish(PullToRefreshLayout.SUCCEED);
    }


    public void startPhotos(String type, SaleOrderBean.ResultBean resultBeans) {
        rBean = resultBeans;
        flag = type;
        BitmapUtil.startImageCapture(SaleRecordActivity.this, ExtraName.TAKE_PICTURE);
    }

    /**
     * 取消订单
     *
     * @param orderId
     */
    public void canceOrder(int orderId) {
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
                SaleOrderBean.ResultBean resultBean = viewHolder.resultBean;
                ProductListBean.ResultBean pp = new ProductListBean.ResultBean();
                pp.setProduct_id(Integer.parseInt(resultBean.getProduct_id()));
                pp.setProduct_name(resultBean.getProduct_name().getProduct_name());
                pp.setThumb(resultBean.getProduct_info().getThumb());
                pp.setType(1);


                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", pp);
                bundle.putString("productName", resultBean.getProduct_name().getProduct_name());
                bundle.putString("productId", resultBean.getProduct_id());
                ActivitySkip.forward(SaleRecordActivity.this, ProductItemDetailActivity.class, bundle);

            }
        });

        backLinearLayou.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("saletoorder", "saletoorder");
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {


        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent intent = new Intent();
            intent.putExtra("saletoorder", "saletoorder");
            setResult(Activity.RESULT_OK, intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
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

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                //拍照
                case ExtraName.TAKE_PICTURE:
                    if (resultCode == RESULT_OK) {
                        Uri photoUri = BitmapUtil.imgUri;
                        picPath = BitmapUtil.getImagePath(SaleRecordActivity.this, photoUri, null, null);
                        Log.e("picPath**", "picPath:" + picPath);
                        setPicPath(picPath, rBean);

                    }
                    break;


            }

        }

    }


    /**
     * 支付意向金
     *
     * @param resultBean
     */
    public void payMoney(SaleOrderBean.ResultBean resultBean) {
        Bundle bun = new Bundle();
        bun.putString("payurlId", resultBean.getOrder_money_url());
        bun.putString("payment_method", resultBean.getPayment_method() + "");
        ActivitySkip.forward(SaleRecordActivity.this, PaymentQrActivity.class, bun);

    }

    /**
     * 上传支付凭证
     *
     * @param picPath
     */
    private void setPicPath(String picPath, SaleOrderBean.ResultBean resultBean) {
        Log.e("picPath", "picPath:" + picPath);
        try {
            showDialog();
            FinalHttp finalHttp = new FinalHttp();
            AjaxParams ajaxParams = new AjaxParams();
            String strUrl = "";
            ajaxParams.put("order_id", resultBean.getProduct_orders_id() + "");

            //上次合同图片
            if (flag.equals("1")) {
                strUrl = Contants.UPLOAD_CONTRACT_PHOTO;
                if (null != picPath && !picPath.equals("")) {
                    File picFile = new File(picPath);
                    ajaxParams.put("file", picFile);
                }
            } else {
                //支付房款-上传凭证或在线支付
                strUrl = Contants.UPLOAD_FIRST_COMMISSION;
                ajaxParams.put("money_where", "1");
                ajaxParams.put("pay_method", resultBean.getPayment_method() + "");
                ajaxParams.put("pay_time", resultBean.getPay_time() + "");
                ajaxParams.put("amount", resultBean.getPrice() + "");
                ajaxParams.put("remark", resultBean.getRemark() + "");

                if (null != picPath && !picPath.equals("")) {
                    File picFile = new File(picPath);
                    ajaxParams.put("image[]", picFile);

                }

            }

            finalHttp.post(strUrl, ajaxParams, new AjaxCallBack<String>() {
                @Override
                public void onSuccess(String s) {
                    Log.e("s**", "s:" + s);
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
                    Log.e("s**", "s:" + errorNo);
                    closeDialog();

                }
            });

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }
}
