package com.yd.org.sellpopularizesystem.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.adapter.PromotionRecordAdapter;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.internal.PullToRefreshLayout;
import com.yd.org.sellpopularizesystem.internal.PullableListView;
import com.yd.org.sellpopularizesystem.javaBean.CustomBean;
import com.yd.org.sellpopularizesystem.javaBean.PromotionRecordBean;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.List;


/**
 * 推广记录
 */
public class PromotionRecordActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener {
    private Bundle bundle;
    private String flag;
    private PullableListView listView;
    private PullToRefreshLayout ptrl;
    private int page = 1;
    private CustomBean.ResultBean resultBean;
    private List<PromotionRecordBean.ResultBean> datas = new ArrayList<>();
    private PromotionRecordAdapter pradapter;

    @Override
    protected int setContentView() {
        return R.layout.activity_promotion_record;
    }

    @Override
    public void initView() {
        hideRightImagview();
        //接收数据
        bundle = getIntent().getExtras();
        flag = bundle.getString("custocora");
        resultBean = (CustomBean.ResultBean) bundle.getSerializable("customer_id");

        //下拉加载
        ptrl = getViewById(R.id.refresh_view);
        ptrl.setOnRefreshListener(this);
        listView = getViewById(R.id.content_view);


        //推广记录
        if (flag.equals("custoexpand")) {
            setTitle(getString(R.string.expandre));
            getExpandReData(page, true);

            //购房记录
        } else if (flag.equals("custopurchase")) {
            setTitle(getString(R.string.housepurchase));
            getHouseurchase(page, true);
        }


    }

    @Override
    public void setListener() {

    }


    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        page = 1;
        if (flag.equals("custoexpand")) {
            getExpandReData(page, true);
        } else {
            getHouseurchase(page, true);
        }


    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

        page++;
        if (flag.equals("custoexpand")) {
            getExpandReData(page, false);
        } else {

            //测试阶段没有数据才简单写成这样
            getViewById(R.id.noInfomation).setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);

            getHouseurchase(page, false);
        }


    }

    private void getExpandReData(int page, final boolean isRefresh) {
        showDialog();

        FinalHttp finalHttp = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        ajaxParams.put("company_id", SharedPreferencesHelps.getCompanyId());
        ajaxParams.put("customer_id", resultBean.getCustomer_id() + "");
        ajaxParams.put("page", String.valueOf(page));
        ajaxParams.put("number", "20");
        Log.e("参数", "ajaxParams:" + ajaxParams.toString());
        finalHttp.get(Contants.SALE_LOG_LIST, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                Log.e("s***", "s:" + s);
                closeDialog();
                if (null != s) {
                    paseJSON(s, isRefresh);
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                Log.e("s***", "s:" + errorNo);
                closeDialog();

            }
        });

    }

    private void paseJSON(String json, boolean isRefresh) {

        Gson gson = new Gson();
        PromotionRecordBean pb = gson.fromJson(json, PromotionRecordBean.class);

        if (pb.getCode() == 1) {
            datas = pb.getResult();
        }

        if (isRefresh) {
            if (datas.size() == 0) {
                getViewById(R.id.noInfomation).setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            } else {
                getViewById(R.id.noInfomation).setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }
            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
            pradapter = new PromotionRecordAdapter(PromotionRecordActivity.this);
            listView.setAdapter(pradapter);
        }


        ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
        pradapter.addMore(datas);

    }

    private void getHouseurchase(int page, boolean isRefresh) {
        showDialog();
        FinalHttp fin = new FinalHttp();
        AjaxParams a = new AjaxParams();
        a.put("user_id", SharedPreferencesHelps.getUserID());
        a.put("company_id", SharedPreferencesHelps.getCompanyId());
        a.put("client", resultBean.getCustomer_id() + "");
        a.put("status", resultBean.getStatus() + "");
        a.put("page", page + "");
        a.put("number", "20");

        fin.get(Contants.ORDER_LIST, a, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                closeDialog();
                Log.e("购房记录", "s:" + s);


            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                Log.e("购房记录", "s:" + errorNo);
                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
                ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
                closeDialog();
            }
        });


    }
}