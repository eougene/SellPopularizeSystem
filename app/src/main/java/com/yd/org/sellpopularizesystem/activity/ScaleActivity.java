package com.yd.org.sellpopularizesystem.activity;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.adapter.CustomeListAdapter;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.internal.PullToRefreshLayout;
import com.yd.org.sellpopularizesystem.internal.PullableListView;
import com.yd.org.sellpopularizesystem.javaBean.ProductListBean;
import com.yd.org.sellpopularizesystem.javaBean.ProductSearchUrl;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 销售推广
 */
public class ScaleActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener {
    private String strSearch;
    protected ImageView backLinearLayou;
    public static ScaleActivity scaleActivity;
    public LinearLayout parent_container;
    private TextView tvProjectNum;
    private EditText etSearch;
    private Button btScaleSearch;
    private PullableListView listView;
    private PullToRefreshLayout ptrl;
    //代表筛选的范围,0x001表示区域,0x002表示房型,0x003表示面积,0x004表示价格
    private List<ProductListBean.ResultBean> productData = new ArrayList<>();
    private CustomeListAdapter adapter;
    private int page = 1;
    private String space = "", price = "", house = "", area = "", cate_id = "";
    public String strSelect = "";
    public ProductSearchUrl psu = new ProductSearchUrl();

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                //搜索
                case R.id.btScaleSearch:
                    ActivitySkip.forward(ScaleActivity.this, FilterActivity.class);
                    overridePendingTransition(R.anim.downtoup_in_anim, 0);
                    break;

                //返回按钮
                case R.id.backLinearLayout:
                    showAlertDialog();
                    break;
            }
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ProductSearchUrl psu = (ProductSearchUrl) msg.obj;
            if (psu.getArea().equals("") && psu.getHouse().equals("") && psu.getPrice().equals("")) {
                if (space.equals("") && price.equals("") && house.equals("") && area.equals("")) {
                    return;
                } else {
                    getProductListData(true, page, null, null, null, null);
                }
            } else {
                area = psu.getArea();
                house = psu.getHouse();
                price = psu.getPrice();
                Log.e("area:::", "area::" + area);
                Log.e("house:::", "house::" + house);
                Log.e("price:::", "price::" + price);
                getProductListData(true, page, space, price, house, area);

            }
        }
    };

    @Override
    protected int setContentView() {
        scaleActivity = this;
        return R.layout.activity_scale;
    }

    @Override
    public void initView() {
        setTitle(R.string.home_scale);
        backLinearLayou = getViewById(R.id.backLinearLayout);
        backLinearLayou.setOnClickListener(mOnClickListener);
        etSearch = getViewById(R.id.etSearch);
        parent_container = getViewById(R.id.parent_container);
        btScaleSearch = getViewById(R.id.btScaleSearch);
        tvProjectNum = getViewById(R.id.tvProjectNum);
        ptrl = getViewById(R.id.refresh_view);
        ptrl.setOnRefreshListener(this);
        listView = getViewById(R.id.content_view);
        setTitle(getResources().getString(R.string.home_scale));


        getProductListData(true, page, space, price, house, area);

    }


    @Override
    public void setListener() {

        //搜索事件
        btScaleSearch.setOnClickListener(mOnClickListener);

        //地图
        clickRightImageView(R.mipmap.map1, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", (Serializable) productData);
                ActivitySkip.forward(ScaleActivity.this, MapActivity.class, bundle);
            }
        });


        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    strSearch = (!TextUtils.isEmpty(etSearch.getText().toString() + "")) ? etSearch.getText().toString() : "";
                    Log.e("strSearch**", "strSearch:" + strSearch);
                    getProductListData(true, 1, space, price, house, area);
                }
                return false;
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
        getProductListData(true, page, space, price, house, area);

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        // 千万别忘了告诉控件刷新完毕了哦！
        page++;
        getProductListData(false, page, space, price, house, area);

    }


    private void getProductListData(final boolean boool, int page, String space, String price, String house, String area) {
        showDialog();
        final FinalHttp fh = new FinalHttp();
        final AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        ajaxParams.put("page", page + "");
        ajaxParams.put("number", "20");
        ajaxParams.put("cate_id", cate_id);
        ajaxParams.put("search_key", strSearch);
        ajaxParams.put("area", area);
        ajaxParams.put("house", house);
        ajaxParams.put("space", space);
        ajaxParams.put("price", price);
        fh.get(Contants.PRODUCT_LIST, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                closeDialog();
                if (null != s) {
                    jsonParse(s, boool);
                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
                closeDialog();
            }
        });


    }

    private void jsonParse(String json, boolean isRefresh) {
        Gson gson = new Gson();
        ProductListBean product = gson.fromJson(json, ProductListBean.class);
        if (product.getCode().equals("1")) {
            productData = product.getResult();
            tvProjectNum.setText(getString(R.string.sum) + product.getTotal_number() + getString(R.string.individuaproject) + getString(R.string.single_blank_space) + strSelect);
        }
        if (isRefresh) {
            ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
            adapter = new CustomeListAdapter(ScaleActivity.this, isRefresh);
            listView.setAdapter(adapter);
        }
        ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        adapter.addData(productData);

    }

    public void goTo(Object bean, Class<?> cls, String str1, String str2) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", (Serializable) bean);
        bundle.putString("productName", str1 == null ? "" : str1);
        bundle.putString("productId", str2 == null ? "" : str2);
        ActivitySkip.forward(ScaleActivity.this, cls, bundle);
    }

    private void showAlertDialog() {
        new AlertDialog.Builder(ScaleActivity.this)
                .setMessage(R.string.exit_scale)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //删除推广记录数据
                        SharedPreferencesHelps.clearTime();
                        SharedPreferencesHelps.clearData();

                        finish();
                    }
                }).setNegativeButton(R.string.cancel, null).create().show();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            showAlertDialog();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }


}
