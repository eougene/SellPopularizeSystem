package com.yd.org.sellpopularizesystem.activity;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.gson.Gson;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.adapter.ScaleListItemAdapter;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.internal.PullToRefreshLayout;
import com.yd.org.sellpopularizesystem.internal.PullableListView;
import com.yd.org.sellpopularizesystem.javaBean.ProductListBean;
import com.yd.org.sellpopularizesystem.javaBean.ScaleListBean;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.List;

public class ScalListActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener {
    private ProductListBean.ResultBean resultBean;
    private PullableListView listView;
    private PullToRefreshLayout ptrl;
    private int page = 1;
    private List<ScaleListBean.ResultBean> baseData = new ArrayList<>();
    private ScaleListItemAdapter scaleListItemAdapter;

    @Override
    protected int setContentView() {
        return R.layout.activity_scal_list;
    }

    @Override
    public void initView() {
        hideRightImagview();

        Bundle bundle = getIntent().getExtras();
        resultBean = (ProductListBean.ResultBean) bundle.getSerializable("scale");

        Log.e("resultBean**", "resultBean:" + resultBean.getAddress());
        setTitle(resultBean.getProduct_name());


        //下拉加载
        ptrl = getViewById(R.id.refresh_view);
        ptrl.setOnRefreshListener(this);
        listView = getViewById(R.id.content_view);
        listView.setDividerHeight(0);

        getProductListData(true, page);

    }

    private void getProductListData(final boolean boool, int page) {
        showDialog();
        final FinalHttp fh = new FinalHttp();
        final AjaxParams ajaxParams = new AjaxParams();

       // String url = "http://www.wingaid.com/index.php/app/product/product_childs_list?user_id=100014&page=1&number=10&product_name&provice&city&town&address&bedroom&bathroom&car_space&has_study&ensuite&level&price=0~100000000&internal=0~100000000&external=0~100000000&building_area=0~100000000";
        ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        ajaxParams.put("product_name", resultBean.getProduct_name());
        ajaxParams.put("provice", resultBean.getProvice());
        ajaxParams.put("city", resultBean.getCity());
        ajaxParams.put("town", resultBean.getTown());
        ajaxParams.put("address", resultBean.getAddress());
        ajaxParams.put("bedroom", "");
        ajaxParams.put("bathroom", "");

        ajaxParams.put("price", "0-100000000");
        ajaxParams.put("car_space", "");
        ajaxParams.put("has_study", "");
        ajaxParams.put("ensuite", "");

        ajaxParams.put("internal", "0-100000000");
        ajaxParams.put("external", "0-100000000");
        ajaxParams.put("building_area", "0-100000000");


        ajaxParams.put("page", page + "");
        ajaxParams.put("number", "10");

        fh.get(Contants.SCALE_LIST,ajaxParams, new AjaxCallBack<String>() {

            @Override
            public void onSuccess(String s) {

                //Log.e("sss", "ajax:" + ajaxParams.toString());

                Log.e("获取推广List数据", "s:" + s);
                super.onSuccess(s);
                closeDialog();
                if (null != s) {
                    jsonParse(s, boool);
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                Log.e("strMsg", "strMsg:" + strMsg);
                closeDialog();
            }
        });


    }

    private void jsonParse(String json, boolean isRefresh) {

        Gson gson = new Gson();
        ScaleListBean product = gson.fromJson(json, ScaleListBean.class);
        if (product.getCode().equals("1")) {
            baseData = product.getResult();
        }
        if (isRefresh) {
            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
            scaleListItemAdapter = new ScaleListItemAdapter(ScalListActivity.this);
            listView.setAdapter(scaleListItemAdapter);
        }
        ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        scaleListItemAdapter.addData(baseData);

    }

    @Override
    public void setListener() {

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ActivitySkip.forward(ScalListActivity.this, ScaleDeltaileActivity.class);
            }
        });
    }

    /**
     * 上下拉相关
     *
     * @param pullToRefreshLayout
     */
    @Override
    public void onRefresh(PullToRefreshLayout pullToRefreshLayout) {
        // 千万别忘了告诉控件刷新完毕了哦！

        page = 1;
        getProductListData(true, page);


    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        // 千万别忘了告诉控件刷新完毕了哦！

        page++;
        getProductListData(false, page);

    }

}
