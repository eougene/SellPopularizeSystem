package com.yd.org.sellpopularizesystem.activity;

import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.gson.Gson;
import com.yd.org.sellpopularizesystem.R;
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
 * 我的佣金
 */
public class CommissionActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener {
    private PullableListView listView;
    private PullToRefreshLayout ptrl;
    private int page = 1;
    private List<CommissionBean.ResultBean> datas = new ArrayList<>();
    private CommissionAdapter commissionAdapter;
    public static CommissionActivity commissionActivity;

    @Override
    protected int setContentView() {
        return R.layout.activity_commission;
    }

    @Override
    public void initView() {
        commissionActivity = this;
        hideRightImagview();
        setTitle(getString(R.string.commission));
        //下拉加载
        ptrl = getViewById(R.id.refresh_view);
        ptrl.setOnRefreshListener(this);
        listView = getViewById(R.id.content_view);

        listView.setDividerHeight(30);

        getInfo(1, true);

    }

    @Override
    public void setListener() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                CommissionAdapter.ViewHoler viewHoler = (CommissionAdapter.ViewHoler) view.getTag();
                CommissionBean.ResultBean resultBean = viewHoler.resultBean;


            }
        });

    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
        page = 1;
        getInfo(page, true);

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

        page++;
        ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        // getInfo(page, false);

    }

    private void getInfo(int page, final boolean isRefresh) {
        EasyHttp.get(Contants.COMMOSSION_LIST)
                .cacheMode(CacheMode.DEFAULT)
                .cacheKey(this.getClass().getSimpleName())
                .timeStamp(true)
                .params("user_id", SharedPreferencesHelps.getUserID())
                .params("customer_id", "")
                .params("page", String.valueOf(page))
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
                        ToasShow.showToastCenter(CommissionActivity.this, e.getMessage());
                    }

                    @Override
                    public void onSuccess(String json) {

                        closeDialog();
                        paserJSON(json, isRefresh);
                    }
                });


    }

    private void paserJSON(String data, boolean isRefresh) {

        Gson s = new Gson();
        CommissionBean commission = s.fromJson(data, CommissionBean.class);
        datas = commission.getResult();

        if (isRefresh) {

            if (datas.size() == 0) {
                getViewById(R.id.noInfomation).setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            } else {
                getViewById(R.id.noInfomation).setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }
            commissionAdapter = new CommissionAdapter(CommissionActivity.this);
            listView.setAdapter(commissionAdapter);
        }
        //ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        commissionAdapter.addMore(datas);


    }
}
