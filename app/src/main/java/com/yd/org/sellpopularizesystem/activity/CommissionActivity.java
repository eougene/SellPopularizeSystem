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
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;
import com.yd.org.sellpopularizesystem.utils.ToasShow;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.model.CacheMode;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.HttpParams;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;
import org.json.JSONObject;

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
        //注册事件
        EventBus.getDefault().register(this);
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
                //CommissionBean.ResultBean resultBean = viewHoler.resultBean;
                //CommissionBean.ResultBean resultBean=datas.get(position);
                //getDepositDetails(resultBean);
              //  ActivitySkip.forward(CommissionActivity.this,InvoiceActivity.class);

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
        getInfo(page, false);

    }

    private void getInfo(int page, final boolean isRefresh) {
        EasyHttp.get(Contants.COMMOSSION_LIST)
                .cacheMode(CacheMode.NO_CACHE)
                .cacheKey(this.getClass().getSimpleName())
                .timeStamp(true)
                .params("user_id", SharedPreferencesHelps.getUserID())
                .params("customer_id", "")
                .params("page", page+"")
                .params("number", "100")
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
        if (commissionAdapter!=null){
            commissionAdapter.addMore(datas);
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //取消注册事件
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMoonEvent(String message){
        Log.e("TAG", "onMoonEvent: "+message);
        if (message.equals("ok")){
            getInfo(1, true);
        }

    }
}
