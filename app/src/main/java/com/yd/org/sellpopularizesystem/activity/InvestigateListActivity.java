package com.yd.org.sellpopularizesystem.activity;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.internal.PullToRefreshLayout;
import com.yd.org.sellpopularizesystem.internal.PullableListView;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;
import com.yd.org.sellpopularizesystem.utils.ToasShow;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;


/**
 * 调研列表
 */
public class InvestigateListActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener {
    private PullableListView listView;
    private PullToRefreshLayout ptrl;
    private int page = 1;


    @Override
    protected int setContentView() {
        return R.layout.activity_investigate_list;
    }

    @Override
    public void initView() {
        hideRightImagview();
        setTitle(getString(R.string.investigate_list));
        ptrl = getViewById(R.id.refresh_view);
        ptrl.setOnRefreshListener(this);
        listView = getViewById(R.id.content_view);

    }


    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        page = 1;
        getInfo(page, true);

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {

        page++;
        getInfo(page, false);

    }

    private void getInfo(int page, final boolean isRefresh) {
        showDialog();
        FinalHttp finalHttp = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("product_id", SharedPreferencesHelps.getUserID());
        ajaxParams.put("number", "10");

        finalHttp.get(Contants.QUESTION_LIST, ajaxParams, new AjaxCallBack<String>() {

            @Override
            public void onSuccess(String s) {
                closeDialog();
                if (null != s) {

                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                closeDialog();
                ToasShow.showToastCenter(InvestigateListActivity.this, strMsg);
            }
        });


    }

    @Override
    public void setListener() {

    }
}
