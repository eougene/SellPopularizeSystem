package com.yd.org.sellpopularizesystem.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
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
import com.yd.org.sellpopularizesystem.javaBean.SaleOrderBean;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.BitmapUtil;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;
import com.yd.org.sellpopularizesystem.utils.ToasShow;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.model.CacheMode;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.HttpParams;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * 订单列表
 */
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
        EasyHttp.get(Contants.INQUIRE_ORDER_LIST)
                .cacheKey(this.getClass().getSimpleName())//缓存key
                .timeStamp(true)
                .params("company_id", SharedPreferencesHelps.getCompanyId())
                .params("user_id", SharedPreferencesHelps.getUserID())
                .params("page", page + "")
                .params("number", String.valueOf(Integer.MAX_VALUE))
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        showDialog();
                    }

                    @Override
                    public void onError(ApiException e) {
                        closeDialog();
                        Log.e("onError", "onError:" + e.getCode() + ";;" + e.getMessage());
                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("onSuccess","onSuccess:"+json);

                        closeDialog();
                        parseJson(json, isRefresh);
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

            saleAdapter = new SaleRecordAdapter(this);
            lvSaleRecord.setAdapter(saleAdapter);
        }
       // ptrlSaleRecord.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        saleAdapter.addMore(sobRbData);

        locatedOrderIdPos();
    }

    private void locatedOrderIdPos() {
        if (getIntent().getExtras() != null && getIntent().getExtras().getString("orderid") != null) {
            for (int i = 0; i < sobRbData.size(); i++) {
                if (sobRbData.get(i).getProduct_orders_id() == Integer.parseInt(getIntent().getExtras().getString("orderid"))) {
                    lvSaleRecord.setSelection(i);
                    Log.e("TAG", "initView: " + i);
                    return;
                }
            }
        }
    }


    public void startPhotos(SaleOrderBean.ResultBean resultBeans, String type) {
        rBean = resultBeans;
        flag = type;
        if (Build.VERSION.SDK_INT < 23) {
            //BitmapUtil.startImageCapture(CusOprateRecordActivity.this, ExtraName.TAKE_PICTURE);
            BitmapUtil.startImageCapture(SaleRecordActivity.this, ExtraName.TAKE_PICTURE);
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA,
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    new PermissionListener() {
                        @Override
                        public void onGranted() {// 全部授权成功回调
                            // 执行具体业务
                            //BitmapUtil.startImageCapture(CusOprateRecordActivity.this, ExtraName.TAKE_PICTURE);
                            BitmapUtil.startImageCapture(SaleRecordActivity.this, ExtraName.TAKE_PICTURE);
                        }

                        @Override
                        public void onDenied(List<String> deniedPermissionList) {// 部分或全部未授权回调
                            for (String permission : deniedPermissionList) {
                                ToasShow.showToastCenter(SaleRecordActivity.this, permission.toString());
                            }
                        }
                    });
        }

    }

    /**
     * 取消订单
     *
     * @param orderId
     */
    public void canceOrder(int orderId) {

        EasyHttp.post(Contants.ORDER_CANCEL)
                .cacheMode(CacheMode.DEFAULT)
                .params("order_id", orderId + "")
                .params("user_id", SharedPreferencesHelps.getUserID())
                .timeStamp(true)
                .accessToken(true)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onStart() {
                        showDialog();
                    }

                    @Override
                    public void onError(ApiException e) {
                        closeDialog();
                        ToasShow.showToastCenter(SaleRecordActivity.this, e.getMessage());
                        Log.e("onError***", "onError:" + e.getCode() + ":" + e.getMessage());
                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("onSuccess***", "UserBean:" + json);
                        closeDialog();

                        Gson gson = new Gson();
                        ErrorBean errorBean = gson.fromJson(json, ErrorBean.class);
                        if (errorBean.getCode().equals("1")) {
                            ToasShow.showToastCenter(SaleRecordActivity.this, errorBean.getMsg());
                        } else {
                            ToasShow.showToastCenter(SaleRecordActivity.this, errorBean.getMsg());
                        }


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
                Bundle bundle = new Bundle();
                bundle.putSerializable("order", resultBean);
                ActivitySkip.forward(SaleRecordActivity.this, OrderDetailActivity.class, bundle);

            }
        });


    }


    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        ptrlSaleRecord.refreshFinish(PullToRefreshLayout.SUCCEED);
        page = 1;
        getSaleData(page, true);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        page ++;
        ptrlSaleRecord.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        //getSaleData(page, false);
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
                        if (photoUri != null) {
                            Log.e("TAG", "onActivityResult: " + photoUri.toString());
                            picPath = BitmapUtil.getImagePath(SaleRecordActivity.this, photoUri, null, null);
                            setPicPath(picPath, rBean);
                        }

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
        String strUrl = "";
        File picFile = null;

        HttpParams httpParams = new HttpParams();

        if (flag.equals("1")) {
            strUrl = Contants.UPLOAD_CONTRACT_PHOTO;
            httpParams.put("order_id", resultBean.getProduct_orders_id() + "");
            if (null != picPath && !picPath.equals("")) {
                picFile = new File(picPath);
                httpParams.put("file", picFile, null);
            }


        } else {
            //支付房款-上传凭证或在线支付
            strUrl = Contants.UPLOAD_FIRST_COMMISSION;
            httpParams.put("order_id", resultBean.getProduct_orders_id() + "");
            httpParams.put("customer_id", resultBean.getClient() + "");
            if (null != picPath && !picPath.equals("")) {
                picFile = new File(picPath);
                httpParams.put("image[]", picFile, null);
            }
        }


        EasyHttp.post(strUrl)
                .cacheMode(CacheMode.DEFAULT)

                .timeStamp(true)
                .accessToken(true)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onStart() {
                        showDialog();
                    }

                    @Override
                    public void onError(ApiException e) {
                        closeDialog();
                        ToasShow.showToastCenter(SaleRecordActivity.this, e.getMessage());
                        Log.e("onError***", "onError:" + e.getCode() + ":" + e.getMessage());
                    }

                    @Override
                    public void onSuccess(String json) {
                        Log.e("onSuccess***", "UserBean:" + json);
                        closeDialog();


                        Gson gson = new Gson();
                        ErrorBean errorBean = gson.fromJson(json, ErrorBean.class);
                        if (errorBean.getCode().equals("1")) {
                            ToasShow.showToastCenter(SaleRecordActivity.this, getResources().getString(R.string.su));
                        } else {
                            ToasShow.showToastCenter(SaleRecordActivity.this, errorBean.getMsg());
                        }


                    }
                });


    }

    public void askntractO(SaleOrderBean.ResultBean resultBeans, String type) {
        SaleRecordActivity.saleRecordActivity.startPhotos(resultBeans, type);
    }
}
