package com.yd.org.sellpopularizesystem.activity;

import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.application.BaseApplication;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.javaBean.CustomBean;
import com.yd.org.sellpopularizesystem.javaBean.OrderDetailBean;
import com.yd.org.sellpopularizesystem.javaBean.SaleOrderBean;
import com.yd.org.sellpopularizesystem.utils.MyUtils;
import com.yd.org.sellpopularizesystem.utils.ObjectSaveUtil;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

public class OrderDetailActivity extends BaseActivity {
    TextView tvOrderDes, tvOrderNum, tvtype, tvFirb, tvSale, tvCus, tvCusAdd, tvLawyer, tvGoal, tvPrice, tvComplete;
    private CustomBean.ResultBean custome;

    @Override
    protected int setContentView() {
        return R.layout.activity_order_detail;
    }

    @Override
    public void initView() {
        hideRightImagview();
        setTitle(getString(R.string.order_detail));
        SaleOrderBean.ResultBean saleOrderBean = (SaleOrderBean.ResultBean) getIntent().getExtras().get("order");
        initWidgets();
        if (saleOrderBean != null) {
            getOrderDetail(saleOrderBean.getProduct_orders_id());
        }
    }

    private void getOrderDetail(int product_orders_id) {
        showDialog();
        FinalHttp ftth = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("order_id", product_orders_id + "");
        ftth.get(Contants.ORDER_DETAIL, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                closeDialog();
                super.onSuccess(s);
                Gson gson = new Gson();
                OrderDetailBean orderDetailBean = gson.fromJson(s, OrderDetailBean.class);
                if (orderDetailBean.getCode().equals("1") && orderDetailBean.getMsg().equals(getString(R.string.order_success_info))) {
                    initData(orderDetailBean.getResult());
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });
    }

    private void initData(OrderDetailBean.ResultBean result) {
        tvOrderNum.setText(result.getProduct_orders_id() + "");
        if (result.getProduct_info().getCate_id() != 0) {
            if (result.getProduct_info().getCate_id() == 1) {
                tvtype.setText(getString(R.string.house_land));
                tvOrderDes.setText(getString(R.string.house_land) + "-" + result.getProduct_name() + "-" + result.getProduct_info().getProduct_childs_unit_number());
            } else if (result.getProduct_info().getCate_id() == 2) {
                tvtype.setText(getString(R.string.land));
                tvOrderDes.setText(getString(R.string.land) + "-" + result.getProduct_name() + "-" + result.getProduct_info().getProduct_childs_unit_number());
            } else {
                tvtype.setText(getString(R.string.apt));
                tvOrderDes.setText(getString(R.string.apt) + "-" + result.getProduct_name() + "-" + result.getProduct_info().getProduct_childs_unit_number());
            }

        }
        tvFirb.setText(result.getIs_firb() == 0 ? getString(R.string.yes) : getString(R.string.bushi));
        tvSale.setText(result.getSales_id() + "");
        tvCus.setText(result.getCustomer_surname());
        custome = ((CustomBean.ResultBean) ObjectSaveUtil.readObject(OrderDetailActivity.this, "custome"));
        tvCusAdd.setText(custome != null ? custome.getAddress() : "");
        tvLawyer.setText(result.getLawyer_name());
        tvGoal.setText(result.getPurchaseReason());
        tvPrice.setText("$" + MyUtils.addComma(result.getPrice().split("\\.")[0]));
    }

    private void initWidgets() {
        tvOrderDes = getViewById(R.id.tvOrderDes);
        tvOrderNum = getViewById(R.id.tvOrderNum);
        tvtype = getViewById(R.id.tvtype);
        tvFirb = getViewById(R.id.tvFirb);
        tvSale = getViewById(R.id.tvSale);
        tvCus = getViewById(R.id.tvCus);
        tvCusAdd = getViewById(R.id.tvCusAdd);
        tvLawyer = getViewById(R.id.tvLawyer);
        tvGoal = getViewById(R.id.tvGoal);
        tvPrice = getViewById(R.id.tvPrice);
        tvComplete = getViewById(R.id.tvComplete);
    }

    @Override
    public void setListener() {
        tvComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
