package com.yd.org.sellpopularizesystem.activity;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.adapter.CommonAdapter;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.application.ExtraName;
import com.yd.org.sellpopularizesystem.application.ViewHolder;
import com.yd.org.sellpopularizesystem.internal.PullToRefreshLayout;
import com.yd.org.sellpopularizesystem.internal.PullableListView;
import com.yd.org.sellpopularizesystem.javaBean.ProductListBean;
import com.yd.org.sellpopularizesystem.javaBean.ProductSearchUrl;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.MyUtils;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;
import com.yd.org.sellpopularizesystem.utils.ToasShow;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.model.CacheMode;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.HttpParams;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 销售推广
 */
public class ScaleActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener {
    private static final String LAND="Land";
    private static final String HOUSE_LAND="House&Land";
    private static final String APT="APT";
    private String strSearch, selectStrTag;
    public static ScaleActivity scaleActivity;
    public LinearLayout parent_container, llPrice, llType, llHouseType;
    private TextView tvProjectNum, tvPrice, tvType, tvHouseType;
    private PullableListView listView;
    private PullToRefreshLayout ptrl;
    //代表筛选的范围,0x001表示区域,0x002表示房型,0x003表示面积,0x004表示价格
    private List<ProductListBean.ResultBean> productData ;
    private int page = 1;
    private String space = "", search_key = "", price = "", house = "", area = "", cate_id = "";
    public String strSelect = "", hotsale = "", promote = "";
    public ProductSearchUrl psu = new ProductSearchUrl();
    private CommonAdapter mCommonAdapter;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
                //价格
                case R.id.llPrice:
                    Bundle bundle = new Bundle();
                    bundle.putString("fatosca", "price");
                    bundle.putString("ss", "ss");
                    bundle.putString("selectstatus", tvPrice.getText().toString());
                    ActivitySkip.forward(ScaleActivity.this, SelectConditionActivity.class, ExtraName.PRICE, bundle);
                    overridePendingTransition(R.anim.downtoup_in_anim, 0);

                    break;
                //类型
                case R.id.llType:
                    Bundle bundle1 = new Bundle();
                    bundle1.putString("fatosca", "type");
                    bundle1.putString("ss", "ss");
                    bundle1.putString("selectstatus", tvType.getText().toString());
                    ActivitySkip.forward(ScaleActivity.this, SelectConditionActivity.class, ExtraName.TYPE, bundle1);
                    overridePendingTransition(R.anim.downtoup_in_anim, 0);
                    break;
                //房型
                case R.id.llHouseType:
                    Bundle bundle2 = new Bundle();
                    bundle2.putString("fatosca", "housetype");
                    bundle2.putString("ss", "ss");
                    bundle2.putString("selectstatus", tvHouseType.getText().toString());
                    ActivitySkip.forward(ScaleActivity.this, SelectConditionActivity.class, ExtraName.HOURSE, bundle2);
                    overridePendingTransition(R.anim.downtoup_in_anim, 0);
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
                getProductListData(true, page, space, price, house, area);

            }
        }
    };
    private String type;

    @Override
    protected int setContentView() {
        scaleActivity = this;
        return R.layout.activity_scale;
    }

    @Override
    public void initView() {
        Bundle bundle = getIntent().getExtras();
        type = bundle.getString("type");
        setLayout();
        etSearch = getViewById(R.id.etSearch);
        parent_container = getViewById(R.id.parent_container);
        tvProjectNum = getViewById(R.id.tvProjectNum);
        ptrl = getViewById(R.id.refresh_view);
        ptrl.setOnRefreshListener(this);
        listView = getViewById(R.id.content_view);
        llPrice = getViewById(R.id.llPrice);
        llHouseType = getViewById(R.id.llHouseType);
        llType = getViewById(R.id.llType);
        tvPrice = getViewById(R.id.tvPrice);
        tvType = getViewById(R.id.tvType);
        tvHouseType = getViewById(R.id.tvHouseType);

        if (type.equals("hot")) {
            hotsale = "1";
            promote = "0";
            setTitle(getString(R.string.hot_sale));
        } else if (type.equals("promote")) {
            hotsale = "0";
            promote = "1";
            setTitle(getString(R.string.good_house));
        }else {
            hotsale = "";
            promote = "";
        }
        getProductListData(true, 1, space, price, house, area);

    }

    private void setLayout() {
        if (type.equals("all")){
            setCenterInput();
        }
        hideBaseView();
        setRightTitle(R.string.search, getResources().getColor(R.color.scale_tab5), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(etSearch.getText().toString())) {
                    search_key = etSearch.getText().toString();
                    getProductListData(true, 1, space, price, house, area);
                }else {
                    if (TextUtils.equals(getString(R.string.price),tvPrice.getText().toString()) &&
                            TextUtils.equals(getString(R.string.type),tvType.getText().toString()) &&
                            TextUtils.equals(getString(R.string.housetype),tvHouseType.getText().toString())){
                        ToasShow.showToastCenter(ScaleActivity.this,getString(R.string.select_ele));
                    }else {
                        Log.e("TAG", "onClick: "+cate_id);
                        getProductListData(true, 1, space, price, house, area);
                    }
                }
            }
        });
    }


    @Override
    public void setListener() {
        llPrice.setOnClickListener(mOnClickListener);
        llType.setOnClickListener(mOnClickListener);
        llHouseType.setOnClickListener(mOnClickListener);
        //地图

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    strSearch = (!TextUtils.isEmpty(etSearch.getText().toString() + "")) ? etSearch.getText().toString() : "";
                    Log.e("strSearch**", "strSearch:" + strSearch);
                    Log.e("TAG", "onEditorAction: "+cate_id+promote+hotsale);
                    getProductListData(true, 1, space, price, house, area);
                }
                return false;
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                ProductListBean.ResultBean item = (ProductListBean.ResultBean) mCommonAdapter.getItem(position);

                // is_study  是否关联学习  0：未关联   1：已关联
                //is_can _sale  是否可推广  0：可推广  1：不可推广

                if (item.getIs_can_sale().equals("1")) {

                    //对应的学习项目
                    ToasShow.showToastCenter(ScaleActivity.this, getString(R.string.sale_toas) + item.getProduct_name());
                    Bundle bundle = new Bundle();
                    bundle.putString("type_id", String.valueOf(item.getStudy_type_id()));
                    ActivitySkip.forward(ScaleActivity.this, StudySubitemActivity.class, bundle);

                } else {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("bean", item);
                    bundle.putString("productName", item.getProduct_name());
                    bundle.putString("productId", item.getProduct_id() + "");
                    ActivitySkip.forward(ScaleActivity.this, ProductItemDetailActivity.class, bundle);
                }
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
        ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
        // 千万别忘了告诉控件刷新完毕了哦！
        page = 1;
        getProductListData(true, page, space, price, house, area);

    }

    @Override
    public void onLoadMore(PullToRefreshLayout pullToRefreshLayout) {
        // 千万别忘了告诉控件刷新完毕了哦！
        page++;
        ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        getProductListData(false, page, space, price, house, area);

    }


    private void getProductListData(final boolean boool, int page, String space, String price, String house, String area) {
        HttpParams httpParams = new HttpParams();
        httpParams.put("user_id", SharedPreferencesHelps.getUserID());
        httpParams.put("page", String.valueOf(page));
        httpParams.put("number", "100");
        httpParams.put("cate_id", cate_id);
        httpParams.put("search_key", "");
        httpParams.put("area", "");
        httpParams.put("house", house);
        httpParams.put("space", space);
        httpParams.put("price", price);
        httpParams.put("hot_sale", hotsale);
        httpParams.put("promote", promote);

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
                        showDialog();
                    }

                    @Override
                    public void onError(ApiException e) {
                        closeDialog();
                        Log.e("onError", "onError:" + e.getCode() + ";;" + e.getMessage());
                    }

                    @Override
                    public void onSuccess(String json) {
                        closeDialog();
                        jsonParse(json, boool);
                    }
                });


    }

    private void jsonParse(String json, boolean isRefresh) {
        productData = new ArrayList<>();
        Gson gson = new Gson();
        ProductListBean product = gson.fromJson(json, ProductListBean.class);
        if (product.getCode().equals("1")) {
            Log.e("TAG", "jsonParse: "+product.getTotal_number() );
            productData=product.getResult();
            tvProjectNum.setText(getString(R.string.sum) + product.getTotal_number() + getString(R.string.individuaproject) + getString(R.string.single_blank_space) + strSelect);
        }
        if (isRefresh) {
            setAdapter(productData);
            listView.setAdapter(mCommonAdapter);
        } else {
            mCommonAdapter.addMore(productData);
        }

    }

    private void setAdapter(List<ProductListBean.ResultBean> list) {

        mCommonAdapter = new CommonAdapter<ProductListBean.ResultBean>(ScaleActivity.this, list, R.layout.lv_item_project_promotion) {
            @Override
            public void convert(ViewHolder holder, ProductListBean.ResultBean item) {
                if (item.getCate_id() == 1) {
                    holder.setText(R.id.tvBuildType, HOUSE_LAND);
                } else if (item.getCate_id() == 2) {
                    holder.setText(R.id.tvBuildType, LAND);
                } else if (item.getCate_id() == 3){
                    holder.setText(R.id.tvBuildType, getString(R.string.villa));
                }



                if (item.getAttr_1() == 1) {
                    holder.getView(R.id.tvHot).setVisibility(View.VISIBLE);
                } else {
                    holder.getView(R.id.tvHot).setVisibility(View.GONE);
                }


                if (null!=item.getIs_can_sale()&&item.getIs_can_sale().equals("1")) {
                    holder.getView(R.id.ivIslock).setVisibility(View.VISIBLE);
                } else {
                    holder.getView(R.id.ivIslock).setVisibility(View.GONE);
                }



                if (item.getAttr_2() == 1) {
                    holder.getView(R.id.tvCollection).setVisibility(View.VISIBLE);
                } else {
                    holder.getView(R.id.tvCollection).setVisibility(View.GONE);
                }




                if (item.getAttr_3() == 1) {
                    holder.getView(R.id.tvDiscount).setVisibility(View.VISIBLE);
                } else {
                    holder.getView(R.id.tvDiscount).setVisibility(View.INVISIBLE);
                }


                holder.setImageByUrl(R.id.ivHousePic, Contants.DOMAIN + "/" + item.getThumb());
                holder.setText(R.id.tvName, item.getProduct_name());
                holder.setText(R.id.tvLocation, item.getState() + "-" + item.getAddress_suburb());
                holder.setText(R.id.tvHousePrice, getString(R.string.totalprice) + "$" +
                        MyUtils.addComma(String.valueOf(Math.ceil(Double.parseDouble(item.getChilds().get(0).getMin_price())) / 1000)
                                .split("\\.")[0]) + "k" + getString(R.string.perset));

            }
        };
        listView.setAdapter(mCommonAdapter);
    }

    public void goTo(Object bean, Class<?> cls, String str1, String str2) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", (Serializable) bean);
        bundle.putString("productName", str1 == null ? "" : str1);
        bundle.putString("productId", str2 == null ? "" : str2);
        ActivitySkip.forward(ScaleActivity.this, cls, bundle);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            strSelect="";
            strSelect=data.getStringExtra("selectextra")== null ? "" : data.getStringExtra("selectextra");
            switch (requestCode) {
                //类型
                case ExtraName.TYPE:
                    if (!TextUtils.isEmpty(data.getStringExtra("selectextra"))) {
                        Log.e("TAG", "onActivityResult: " + data.getStringExtra("selectextra"));
                        tvType.setText(data.getStringExtra("selectextra") == null ? "" : data.getStringExtra("selectextra"));
                        if (data.getStringExtra("selectextra").equals(LAND)) {
                            Log.e("TAG", "land: "+ data.getStringExtra("selectextra"));
                            cate_id = "2";
                        } else if (data.getStringExtra("selectextra").equals(HOUSE_LAND)) {
                            Log.e("TAG", "house&land: "+ data.getStringExtra("selectextra"));
                            cate_id = "1";
                        } else if (data.getStringExtra("selectextra").equals(APT)){
                            Log.e("TAG", "APT: "+ data.getStringExtra("selectextra"));
                            cate_id = "3";
                        }
                    } else {
                        tvType.setText(getString(R.string.nolimit));
                        cate_id="";
                    }
                    selectStrTag = TextUtils.isEmpty(data.getStringExtra("selecttagextra")) ? "" : data.getStringExtra("selecttagextra");
                    break;
                //房型
                case ExtraName.HOURSE:
                    if (!TextUtils.isEmpty(data.getStringExtra("selectextra"))) {
                        if (data.getStringExtra("selectextra").length() > 2) {
                            tvHouseType.setGravity(Gravity.CENTER_VERTICAL | Gravity.START);
                        }
                        //area=data.getStringExtra("selectextra");
                        tvHouseType.setText(data.getStringExtra("selectextra"));
                    } else {
                        tvHouseType.setText(getString(R.string.nolimit));
                    }
                    selectStrTag = TextUtils.isEmpty(data.getStringExtra("selecttagextra")) ? "" : data.getStringExtra("selecttagextra");
                    area = selectStrTag;
                    break;
                //价格
                case ExtraName.PRICE:
                    if (!TextUtils.isEmpty(data.getStringExtra("selectextra"))) {
                        tvPrice.setText(data.getStringExtra("selectextra"));
                        //price=data.getStringExtra("selectextra");
                    } else {
                        tvPrice.setText(getString(R.string.nolimit));
                    }
                    selectStrTag = TextUtils.isEmpty(data.getStringExtra("selecttagextra")) ? "" : data.getStringExtra("selecttagextra");
                    price = selectStrTag;
                    break;
            }
            //getProductListData(true, page, space, price, house, area);
        }
    }

}
