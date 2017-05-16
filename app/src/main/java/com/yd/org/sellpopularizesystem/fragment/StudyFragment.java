package com.yd.org.sellpopularizesystem.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.activity.StudyDetailaActivity;
import com.yd.org.sellpopularizesystem.adapter.StyudyAdapter;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.internal.PullToRefreshLayout;
import com.yd.org.sellpopularizesystem.internal.PullableListView;
import com.yd.org.sellpopularizesystem.javaBean.StudyBean;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.List;


/**
 * 学习园地
 */
public class StudyFragment extends BaseFragmentView implements PullToRefreshLayout.OnRefreshListener {
    private PullableListView listView;
    private PullToRefreshLayout ptrl;
    private int page = 1;
    private List<StudyBean.ResultBean> productData = new ArrayList<>();
    private StyudyAdapter adapter;
    private TextView tvNoMessage;
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_study);
        //下拉加载
        ptrl = getViewById(R.id.refresh_view);
        ptrl.setOnRefreshListener(this);
        listView = getViewById(R.id.content_view);
        tvNoMessage=getViewById(R.id.noInfomation);
        getStudyListData(true, page);

    }

    private void getStudyListData(final boolean b, int page) {
        showLoadingDialog();
        final FinalHttp fh = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        ajaxParams.put("page", String.valueOf(page));
        ajaxParams.put("number", "10");

        fh.get(Contants.STUDY_LIST, ajaxParams, new AjaxCallBack<String>() {

            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                dismissLoadingDialog();
                Log.e("获取学习的内容", "s:" + s);
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
        StudyBean product = gson.fromJson(json, StudyBean.class);
        if (product.getCode().equals("1")) {
            if (product.getMsg().equals("暂无数据")){
                tvNoMessage.setVisibility(View.VISIBLE);
            }else {
                productData = product.getResult();
            }
        }
        if (isRefresh) {
            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);

            adapter = new StyudyAdapter(getActivity());
            listView.setAdapter(adapter);
        }
        ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        adapter.addData(productData);

    }


    @Override
    protected void setListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                StyudyAdapter.HolderView holderView = (StyudyAdapter.HolderView) view.getTag();
                StudyBean.ResultBean resultBean = holderView.productListBean;

                if (resultBean.getCan_study() == 1) {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("study", resultBean);
                    ActivitySkip.forward(getActivity(), StudyDetailaActivity.class, bundle);
                }

            }
        });

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
