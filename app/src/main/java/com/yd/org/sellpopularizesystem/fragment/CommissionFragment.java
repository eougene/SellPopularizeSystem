package com.yd.org.sellpopularizesystem.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.activity.CommissionActivity;
import com.yd.org.sellpopularizesystem.adapter.CommissionAdapter;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.internal.PullToRefreshLayout;
import com.yd.org.sellpopularizesystem.internal.PullableListView;
import com.yd.org.sellpopularizesystem.javaBean.CommissionBean;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;
import com.yd.org.sellpopularizesystem.utils.ToasShow;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.model.CacheMode;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hejin on 2017/11/10.
 */

public class CommissionFragment extends BaseFragmentView implements PullToRefreshLayout.OnRefreshListener{
    private int cate_id, type = 0,page = 1;
    private List<CommissionBean.ResultBean> datas = new ArrayList<>();
    private PullToRefreshLayout ptrl;
    private PullableListView listView;
    private CommissionAdapter commissionAdapter;
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_notific);
        initViews();
    }

    private void initViews() {
        cate_id = getArguments().getInt("cate_id");
        ptrl = getViewById(R.id.refresh_view);
        ptrl.setOnRefreshListener(this);
        listView = getViewById(R.id.content_view);
        getInfo(1, true, cate_id);
    }

    private void getInfo(int i,final boolean isRefresh, int cate_id) {
        EasyHttp.get(Contants.COMMOSSION_LIST)
                .cacheMode(CacheMode.DEFAULT)
                .headers("Cache-Control", "max-age=0")
                .timeStamp(true).params("user_id", SharedPreferencesHelps.getUserID())
                .params("customer_id", "").params("page", page + "")
                .params("number", "100")
                .execute(new SimpleCallBack<String>() {
            @Override
            public void onStart() {
                super.onStart();
                showLoadingDialog();
            }

            @Override
            public void onError(ApiException e) {

                Log.e("onError", "onError:" + e.getCode() + ";;" + e.getMessage());
                ToasShow.showToastCenter(getActivity(), e.getMessage());
            }

            @Override
            public void onSuccess(String json) {

                dismissLoadingDialog();
                paserJSON(json, isRefresh);
            }
        });
    }

    private void paserJSON(String json, boolean isRefresh) {
        Gson s = new Gson();
        CommissionBean commission = s.fromJson(json, CommissionBean.class);
        datas = commission.getResult();

        if (isRefresh) {

            if (datas.size() == 0) {
                getViewById(R.id.noInfomation).setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            } else {
                getViewById(R.id.noInfomation).setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }
            commissionAdapter = new CommissionAdapter(getActivity());
            listView.setAdapter(commissionAdapter);
        }
        if (commissionAdapter != null) {
            commissionAdapter.addMore(datas);
        }
    }

    @Override
    protected void setListener() {

    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    /**
     * 创建实体类
     *
     * @param cate_id
     * @return
     */
    public static CommissionFragment getInstnce(int cate_id) {
        CommissionFragment notificFragmen = new CommissionFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("cate_id", cate_id);
        notificFragmen.setArguments(bundle);
        return notificFragmen;
    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
        page = 1;
        getInfo(page, true,cate_id);
    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        page++;
        ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        getInfo(page, false,cate_id);

    }
}
