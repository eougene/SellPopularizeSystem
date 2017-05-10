package com.yd.org.sellpopularizesystem.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.activity.ExaminationActivity;
import com.yd.org.sellpopularizesystem.adapter.ExamineAdapter;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.internal.PullToRefreshLayout;
import com.yd.org.sellpopularizesystem.internal.PullableListView;
import com.yd.org.sellpopularizesystem.javaBean.ExamlineBean;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bai on 2017/1/19.
 */

public class ExamineFragment extends BaseFragmentView implements PullToRefreshLayout.OnRefreshListener {
    public static ExamineFragment examineFragment;
    private PullableListView listView;
    private PullToRefreshLayout ptrl;
    private int page = 1;
    private List<ExamlineBean.ResultBean> productData = new ArrayList<>();
    private ExamineAdapter adapter;
    private TextView tvNoMessage;
    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (msg.what == 1) {
                getStudyListData(true, page);
            }
        }
    };


    @Override
    protected void initView(Bundle savedInstanceState) {
        examineFragment = this;
        setContentView(R.layout.fragment_study);
        initView();
        getStudyListData(true, page);

    }

    private void initView() {
        //下拉加载
        ptrl = getViewById(R.id.refresh_view);
        ptrl.setOnRefreshListener(this);
        tvNoMessage=getViewById(R.id.noInfomation);
        listView = getViewById(R.id.content_view);
        listView.setDividerHeight(10);

    }

    private void getStudyListData(final boolean b, int page) {
        //String url = Contants.CHECK_LIST + "user_id=" + SharedPreferencesHelps.getUserID() + "&page=" + page + "&number=10";
        showLoadingDialog();
        final FinalHttp fh = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        ajaxParams.put("page", String.valueOf(page));
        ajaxParams.put("number", "10");

        fh.get(Contants.CHECK_LIST, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                dismissLoadingDialog();

                Log.e("获取考核的内容", "s:" + s);
                if (null != s) {
                    jsonParse(s, b);
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);

                dismissLoadingDialog();
            }
        });


    }

    private void jsonParse(String json, boolean isRefresh) {

        Gson gson = new Gson();
        ExamlineBean product = gson.fromJson(json, ExamlineBean.class);
        if (product.getCode().equals("1")) {
            if (product.getMsg().equals("暂无数据")){
                tvNoMessage.setVisibility(View.VISIBLE);
            }else {
                productData = product.getResult();
            }
        }
        if (isRefresh) {
            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
            adapter = new ExamineAdapter(getActivity());
            listView.setAdapter(adapter);
        }
        ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        adapter.addData(productData);

    }


    @Override
    protected void setListener() {


    }

    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {

        page = 1;
        getStudyListData(true, page);

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        page++;
        getStudyListData(false, page);

    }
}
