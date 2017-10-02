package com.yd.org.sellpopularizesystem.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.adapter.CommonAdapter;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.application.ViewHolder;
import com.yd.org.sellpopularizesystem.javaBean.ProductListBean;
import com.yd.org.sellpopularizesystem.myView.CustomProgressDialog;
import com.yd.org.sellpopularizesystem.myView.MyNestScrollView;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.MyUtils;
import com.yd.org.sellpopularizesystem.utils.NetUtil;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;
import com.yd.org.sellpopularizesystem.utils.StatusBarUtil;
import com.yd.org.sellpopularizesystem.utils.ToasShow;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.model.CacheMode;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.HttpParams;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ProjectPromotionActivity extends AppCompatActivity implements AppBarLayout.OnOffsetChangedListener {
    private LinearLayout llAll;
    private TextView tvHotSale, tvLookHouse, tvMore, tvTilte, tvBuildingNum;
    private GridView gvHouse;
    private Toolbar mToolbar;
    private CommonAdapter mCommonAdapter;
    private CustomProgressDialog loading_Dialog;
    private List<ProductListBean.ResultBean> productData = new ArrayList<>();
    private List<ProductListBean.ResultBean> promoteDatas=new ArrayList<>();
    protected float mTitlePercentage;
    private static final float DEFAULT_PERCENTAGE = 0.95f;
    private float percentageOfShowTitle = DEFAULT_PERCENTAGE;
    private String space = "", price = "", house = "", area = "";
    private AppBarLayout mAppBarLayout;
    private ImageView backImageView;
    private MyNestScrollView myNsv;
    private boolean isSvToBottom = false;

    private float mLastX;
    private float mLastY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_promotion);
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        mToolbar = (Toolbar) findViewById(R.id.tb);
        mAppBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        mAppBarLayout.addOnOffsetChangedListener(this);

        initView();
       // NetBroadcastReceiver.mListeners.add(this);
    }

    private void initView() {
        myNsv= (MyNestScrollView) findViewById(R.id.myNsv);
        backImageView = (ImageView) findViewById(R.id.backImageView);
        tvTilte = (TextView) findViewById(R.id.tvTilte);
        llAll = (LinearLayout) findViewById(R.id.llAll);
        tvBuildingNum = (TextView) findViewById(R.id.tvBuildingNum);
        tvHotSale = (TextView) findViewById(R.id.tvHotSale);
        tvLookHouse = (TextView) findViewById(R.id.tvLookHouse);
        tvMore = (TextView) findViewById(R.id.tvMore);
        gvHouse = (GridView) findViewById(R.id.gvHouse);
        loading_Dialog = new CustomProgressDialog(this, R.style.customLoadDialog);
        getProductListData(true, space, price, house, area);
        setListeners();
        fixSlideConflict();
    }

    private void fixSlideConflict() {
        gvHouse.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int action = event.getAction();

                if(action == MotionEvent.ACTION_DOWN) {
                    mLastY = event.getY();
                }
                if(action == MotionEvent.ACTION_MOVE) {
                    int top = gvHouse.getChildAt(0).getTop();
                    float nowY = event.getY();
                    if(!isSvToBottom) {
                        // 允许scrollview拦截点击事件, scrollView滑动
                        myNsv.requestDisallowInterceptTouchEvent(false);
                    } else if(top == 0 && nowY - mLastY > 20) {
                        // 允许scrollview拦截点击事件, scrollView滑动
                        myNsv.requestDisallowInterceptTouchEvent(false);
                    } else {
                        // 不允许scrollview拦截点击事件， listView滑动
                        myNsv.requestDisallowInterceptTouchEvent(true);
                    }
                }
                return false;
            }
        });
    }

    private void setListeners() {
        llAll.setOnClickListener(mOnClickListener);
        tvHotSale.setOnClickListener(mOnClickListener);
        tvLookHouse.setOnClickListener(mOnClickListener);
        tvMore.setOnClickListener(mOnClickListener);

        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAlertDialog();
            }
        });
        gvHouse.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ProductListBean.ResultBean item = productData.get(position);
                Bundle bundle = new Bundle();
                bundle.putSerializable("bean", item);
                bundle.putString("productName", item.getProduct_name());
                bundle.putString("productId", item.getProduct_id() + "");
                ActivitySkip.forward(ProjectPromotionActivity.this, ProductItemDetailActivity.class, bundle);
            }
        });
        myNsv.setScrollToBottomListener(new MyNestScrollView.OnScrollToBottomListener() {
            @Override
            public void onScrollToBottom() {
                isSvToBottom=true;
            }

            @Override
            public void onNotScrollToBottom() {
                isSvToBottom=false;
            }
        });
    }

    private void getProductListData(final boolean boool, String space, String price, String house, String area) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("user_id", SharedPreferencesHelps.getUserID());
        httpParams.put("page", String.valueOf(1));
        httpParams.put("number", "100");
        httpParams.put("cate_id", area);
        httpParams.put("search_key", "");
        httpParams.put("area", "");
        httpParams.put("house", house);
        httpParams.put("space", space);
        httpParams.put("price", price);
        httpParams.put("is_hot_sale", "");
        httpParams.put("is_promote", "");
        httpParams.put("is_old_product","1");

        Log.e("参数***", "params:" + httpParams.toString());


        EasyHttp.get(Contants.PRODUCT_LIST)
                .cacheMode(CacheMode.NO_CACHE)
                .cacheKey(this.getClass().getSimpleName())
                .timeStamp(true)
                .params(httpParams)
                .execute(new SimpleCallBack<String>() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        loading_Dialog.show();
                    }

                    @Override
                    public void onError(ApiException e) {
                        loading_Dialog.dismiss();
                        Log.e("onError", "onError:" + e.getCode() + ";;" + e.getMessage());
                    }

                    @Override
                    public void onSuccess(String json) {
                        loading_Dialog.dismiss();
                        jsonParse(json, boool);
                    }
                });


    }

    private void jsonParse(String json, boolean isRefresh) {
        Gson gson = new Gson();
        ProductListBean product = gson.fromJson(json, ProductListBean.class);
        if (product.getCode().equals("1")) {
            productData = product.getResult();
            tvBuildingNum.setText(product.getTotal_number() + "");
            for (int i = 0; i <productData.size() ; i++) {
                if (productData.get(i).getIs_promote()==1){
                    promoteDatas.add(productData.get(i));
                }
                if (promoteDatas.size()==6){
                    break;
                }
            }
            setAdapter();

        }
    }

    private void setAdapter() {
        mCommonAdapter = new CommonAdapter<ProductListBean.ResultBean>(ProjectPromotionActivity.this, promoteDatas, R.layout.gv_item_house_suggest) {
            @Override
            public void convert(ViewHolder holder, ProductListBean.ResultBean item) {
                holder.setImageByUrl(R.id.ivHousePic, Contants.DOMAIN + "/" + item.getThumb());
                holder.setText(R.id.tvName, item.getProduct_name());
                holder.setText(R.id.tvLocation, item.getState() + "-" + item.getAddress_suburb());
                holder.setText(R.id.tvHousePrice, getString(R.string.totalprice) + "$" +
                        MyUtils.addComma(String.valueOf(Math.ceil(Double.parseDouble(item.getChilds().get(0).getMin_price())) / 1000).split("\\.")[0]) + "k" +
                        getString(R.string.perset));
            }

        };

        gvHouse.setAdapter(mCommonAdapter);
    }


    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.llAll:
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "all");
                    bundle.putSerializable("data", (Serializable) productData);
                    ActivitySkip.forward(ProjectPromotionActivity.this, ScaleActivity.class, bundle);
                    break;
                case R.id.tvHotSale:
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("type", "hot");
                    bundle1.putSerializable("data", (Serializable) productData);
                    ActivitySkip.forward(ProjectPromotionActivity.this, ScaleActivity.class, bundle1);
                    break;
                case R.id.tvLookHouse:
                    Bundle bundle2 = new Bundle();
                    bundle2.putSerializable("data", (Serializable) productData);
                    ActivitySkip.forward(ProjectPromotionActivity.this, MapActivity.class, bundle2);
                    break;
                case R.id.tvMore:
                    Bundle bundle3 = new Bundle();
                    bundle3.putString("type", "promote");
                    bundle3.putSerializable("data", (Serializable) productData);
                    ActivitySkip.forward(ProjectPromotionActivity.this, ScaleActivity.class, bundle3);
                    break;
            }
        }
    };


    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        int maxScroll = appBarLayout.getTotalScrollRange();
        mTitlePercentage = (float) Math.abs(verticalOffset) / (float) maxScroll;
        if (!(mTitlePercentage < percentageOfShowTitle)) {
            tvTilte.setVisibility(View.VISIBLE);

        } else {
            tvTilte.setVisibility(View.GONE);
        }

    }

    private void showAlertDialog() {
        new AlertDialog.Builder(ProjectPromotionActivity.this)
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
