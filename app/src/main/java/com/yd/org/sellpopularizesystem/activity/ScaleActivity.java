package com.yd.org.sellpopularizesystem.activity;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.adapter.CustomeListAdapter;
import com.yd.org.sellpopularizesystem.adapter.ScaleCityAdapter;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.application.ExtraName;
import com.yd.org.sellpopularizesystem.internal.PullToRefreshLayout;
import com.yd.org.sellpopularizesystem.internal.PullableListView;
import com.yd.org.sellpopularizesystem.javaBean.CityBean;
import com.yd.org.sellpopularizesystem.javaBean.ProductListBean;
import com.yd.org.sellpopularizesystem.javaBean.ProductSearchUrl;
import com.yd.org.sellpopularizesystem.utils.ACache;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.MyUtils;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;
import com.yd.org.sellpopularizesystem.utils.ToasShow;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * 销售推广
 */
public class ScaleActivity extends BaseActivity implements PullToRefreshLayout.OnRefreshListener {
    public static ScaleActivity scaleActivity;
    private LinearLayout scaleLL;
    private GridView scaleGridView;
    private Button sureBt;
    private TextView tvProjectNum;
    private EditText etSearch;
    private View scale_popup_dialog;
    private Button btLocation, btHouseType, btArea, btPriceRange, btScaleSearch;
    private ScaleCityAdapter rad_PopupWindowAdapter;
    private PullableListView listView;
    private PullToRefreshLayout ptrl;
    private Vector<String> product_area = new Vector<String>();
    private Vector<String> product_house = new Vector<String>();
    private Vector<String> product_space = new Vector<String>();
    private Vector<String> product_price = new Vector<String>();

    //代表筛选的范围,0x001表示区域,0x002表示房型,0x003表示面积,0x004表示价格
    private int tag = ExtraName.AREA;
    private List<ProductListBean.ResultBean> productData = new ArrayList<>();
    private CustomeListAdapter adapter;
    private int page = 1;
    private String space = "", product_name = "", price = "", house = "", area = "", cate_id = "";
    public String strSelect = "";
    private boolean isChecked = true;//是否多选
    public ProductSearchUrl psu = new ProductSearchUrl();
    public static ACache aCache;

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            switch (view.getId()) {
              /*  //区域
                case R.id.btLocation:
                    btLocation.setEnabled(false);
                    resetText();
                    btHouseType.setEnabled(true);
                    btArea.setEnabled(true);
                    btPriceRange.setEnabled(true);
                    //设置为空
                    space = price = house = area = "";
                    tag = ExtraName.AREA;
                    isChecked = true;
                    scale_popup_dialog.setVisibility(View.VISIBLE);
                    //scaleLL.setBackgroundColor(getResources().getColor(R.color.scale_tab4));
                    setCityAdapter(isChecked, product_area);
                    break;
                //房型
                case R.id.btHouseType:
                    btHouseType.setEnabled(false);
                    resetText();
                    btLocation.setEnabled(true);
                    btArea.setEnabled(true);
                    btPriceRange.setEnabled(true);
                    //设置为空
                    space = price = house = area = "";
                    tag = ExtraName.HOURSE;
                    isChecked = true;
                    scale_popup_dialog.setVisibility(View.VISIBLE);
                    //scaleLL.setBackgroundColor(getResources().getColor(R.color.scale_tab3));
                    setCityAdapter(isChecked, product_house);
                    break;
                //面积
                case R.id.btArea:
                    btArea.setEnabled(false);
                    resetText();
                    btHouseType.setEnabled(true);
                    btLocation.setEnabled(true);
                    btPriceRange.setEnabled(true);
                    //设置为空
                    space = price = house = area = "";
                    tag = ExtraName.SPACE;
                    isChecked = false;
                    scale_popup_dialog.setVisibility(View.VISIBLE);
                    //scaleLL.setBackgroundColor(getResources().getColor(R.color.scale_tab2));
                    setCityAdapter(isChecked, product_space);
                    break;
                //价格
                case R.id.btPriceRange:
                    btPriceRange.setEnabled(false);
                    resetText();
                    btArea.setEnabled(true);
                    btHouseType.setEnabled(true);
                    btLocation.setEnabled(true);
                    //设置为空
                    space = price = house = area = "";
                    tag = ExtraName.PRICE;
                    isChecked = false;
                    scale_popup_dialog.setVisibility(View.VISIBLE);
                    //scaleLL.setBackgroundColor(getResources().getColor(R.color.scale_tab1));
                    setCityAdapter(isChecked, product_price);
                    break;
                //确定筛选的数据,重新加载数据
                case R.id.sureBt:
                    page = 1;
                    scale_popup_dialog.setVisibility(View.GONE);
                    //获取产品数据
                    getProductListData(true, page, space, price, house, area);
                    break;*/
                case R.id.btScaleSearch:
                    ActivitySkip.forward(ScaleActivity.this, FilterActivity.class);
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
                    Log.e("TAG", "retrun: ");
                    return;
                } else {
                    Log.e("TAG", "retrun**: ");
                    if (!MyUtils.getInstance().isNetworkConnected(ScaleActivity.this)) {
                        String jsonStr = aCache.getAsString("product_list_json");
                        Log.e("tag1", "handleMessage: " + jsonStr);
                        jsonParse(jsonStr, true);
                    } else {
                        getProductListData(true, page, null, null, null, null);
                    }
                }
            } else {
                area = psu.getArea();
                house = psu.getHouse();
                //space = psu.getSpace();
                price = psu.getPrice();
                Log.e("tag", "handleMessage: " + area + house + space + space);
                if (!MyUtils.getInstance().isNetworkConnected(ScaleActivity.this)) {
                    ToasShow.showToastCenter(ScaleActivity.this, getString(R.string.network_error));
                } else {
                    getProductListData(true, page, space, price, house, area);
                }

            }
        }
    };
    private String strSearch;

    private void resetText() {
        btLocation.setText(R.string.scale_tab_area);
        btHouseType.setText(R.string.scale_tab_hourse);
        btArea.setText(R.string.scale_tab_prop);
        btPriceRange.setText(R.string.scale_tab_price);
    }


    @Override
    protected int setContentView() {
        scaleActivity = this;
        return R.layout.activity_scale;
    }

    @Override
    public void initView() {
        etSearch = getViewById(R.id.etSearch);
        btScaleSearch = getViewById(R.id.btScaleSearch);
        tvProjectNum = getViewById(R.id.tvProjectNum);
        ptrl = getViewById(R.id.refresh_view);
        ptrl.setOnRefreshListener(this);
        aCache = ACache.get(this);
        listView = getViewById(R.id.content_view);
        //加载筛选数据
        //getProductSearch();
        //获取产品数据
        if (!MyUtils.getInstance().isNetworkConnected(this)) {
            String jsonStr = aCache.getAsString("product_list_json");
            if (jsonStr != null) {
                Log.e("tag1", "initView: " + jsonStr);
                jsonParse(jsonStr, true);
            } else {
                ToasShow.showToastCenter(this, getString(R.string.nonetwork));
            }

        } else {
            getProductListData(true, page, space, price, house, area);
        }
        //初始化view
        scale_popup_dialog = getViewById(R.id.scale_popup_dialog);
        setBackImageView(R.mipmap.backbt);
        setTitle(getResources().getString(R.string.home_scale));
        setColor(Color.BLACK);
        setBaseLayoutBackground(Color.WHITE);
        /*btLocation = getViewById(R.id.btLocation);
        btHouseType = getViewById(R.id.btHouseType);
        btArea = getViewById(R.id.btArea);
        btPriceRange = getViewById(R.id.btPriceRange);*/
        //下拉加载
        setTab();

    }

    private void setCityAdapter(Boolean bool, Vector<String> data) {

        if (bool) {
            //多选
            rad_PopupWindowAdapter = new ScaleCityAdapter(this, data, bool);
            scaleGridView.setAdapter(rad_PopupWindowAdapter);
        } else {
            //单选
            rad_PopupWindowAdapter = new ScaleCityAdapter(this, data, bool);
            scaleGridView.setAdapter(rad_PopupWindowAdapter);
        }
    }

    //初始化筛选tab
    private void setTab() {
        scaleLL = getViewById(R.id.scaleLL);
        scaleGridView = getViewById(R.id.scaleGridView);
        sureBt = getViewById(R.id.sureBt);
    }


    @Override
    public void setListener() {
        /*btLocation.setOnClickListener(mOnClickListener);
        btHouseType.setOnClickListener(mOnClickListener);
        btArea.setOnClickListener(mOnClickListener);
        btPriceRange.setOnClickListener(mOnClickListener);
        sureBt.setOnClickListener(mOnClickListener);*/
        btScaleSearch.setOnClickListener(mOnClickListener);
        //筛选点击事件
        /*scaleGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

                rad_PopupWindowAdapter.changeState(position);

                if (tag == ExtraName.AREA) {
                    area = getSelectData(product_area);
                    btLocation.setText(product_area.get(position));
                } else if (tag == ExtraName.HOURSE) {
                    house = getSelectData(product_house);
                    btHouseType.setText(product_house.get(position));
                } else if (tag == ExtraName.SPACE) {
                    space = getSelectData(product_space);
                    btArea.setText(product_space.get(position));
                } else if (tag == ExtraName.PRICE) {
                    price = getSelectData(product_price);
                    btPriceRange.setText(product_price.get(position));
                }
            }
        });*/


        //产品列表点击事件
        /*listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position%2==0){
                    StorePopHousingAdapter.Holder holder = (StorePopHousingAdapter.Holder) view.getTag();
                    ProductListBean.ResultBean productListBean = holder.productListBean;
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("scale", productListBean);
                    ActivitySkip.forward(ScaleActivity.this, ScalListActivity.class, bundle);
                }else{

                }
            }
        });*/


    /*    clickRightImageView(R.mipmap.searchgraybt, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivitySkip.forward(ScaleActivity.this, SearchActivity.class);
            }
        });*/
        /*clickRightImageView(R.mipmap.areablack, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("data", (Serializable) productData);
                ActivitySkip.forward(ScaleActivity.this, MapActivity.class, bundle);
                overridePendingTransition(R.anim.reverse_anim, 0);
            }
        });*/
        hideRightImagview();
    }

    /**
     * 获取要筛选的内容
     *
     * @param dtat
     * @return
     */

    /*private String getSelectData(Vector<String> dtat) {
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < dtat.size(); i++) {
            if (rad_PopupWindowAdapter.getSelect(i)) {

                if (stringBuffer.length() > 0) {
                    stringBuffer.append(",").append(dtat.get(i));
                } else {

                    stringBuffer.append(dtat.get(i));
                }

            }

        }

        return ((getResources().getString(R.string.unlimited)).equals(stringBuffer.toString())) ? "" : stringBuffer.toString();

    }*/


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

    //加载筛选数据
    private void getProductSearch() {
        final FinalHttp fh = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        fh.get(Contants.SCALE_SEARCH, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                if (null != s) {
                    Gson gson = new Gson();
                    CityBean cityBean = gson.fromJson(s, CityBean.class);
                    if (cityBean.getCode().equals("1")) {
                        // addSearchData(cityBean);

                    }

                }

            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });

    }

    private void addSearchData(CityBean cityBean) {
        //区域
        product_area.add(getResources().getString(R.string.unlimited));
        product_area.add(cityBean.getResult().getProduct_area().get_$1());
        product_area.add(cityBean.getResult().getProduct_area().get_$2());
        product_area.add(cityBean.getResult().getProduct_area().get_$3());
        product_area.add(cityBean.getResult().getProduct_area().get_$4());
        product_area.add(cityBean.getResult().getProduct_area().get_$4());

        //房型
        product_house.add(getResources().getString(R.string.unlimited));
        product_house.add(cityBean.getResult().getProduct_house().get_$1());
        product_house.add(cityBean.getResult().getProduct_house().get_$2());
        product_house.add(cityBean.getResult().getProduct_house().get_$3());
        product_house.add(cityBean.getResult().getProduct_house().get_$4());
        product_house.add(cityBean.getResult().getProduct_house().get_$5());
        product_house.add(cityBean.getResult().getProduct_house().get_$6());
        //面积
        product_space.add(getResources().getString(R.string.unlimited));
        product_space.add(cityBean.getResult().getProduct_space().get_$1());
        product_space.add(cityBean.getResult().getProduct_space().get_$2());

        //价格
        product_price.add(getResources().getString(R.string.unlimited));
        product_price.add(cityBean.getResult().getProduct_price().get_$_0300000291());
        product_price.add(cityBean.getResult().getProduct_price().get_$_300000400000162());
        product_price.add(cityBean.getResult().getProduct_price().get_$_4000006000003());
        product_price.add(cityBean.getResult().getProduct_price().get_$_600000800000242());
        product_price.add(cityBean.getResult().getProduct_price().get_$_8000001000000226());
        product_price.add(cityBean.getResult().getProduct_price().get_$_10000001200000247());
        product_price.add(cityBean.getResult().getProduct_price().get_$_1200000140000067());
        product_price.add(cityBean.getResult().getProduct_price().get_$_14000002000000210());
        product_price.add(cityBean.getResult().getProduct_price().get_$_2000000500000093());
        product_price.add(cityBean.getResult().getProduct_price().get_$_50000001000000094());
        product_price.add(cityBean.getResult().getProduct_price().get_$_1000000020000000298());
    }

    private void getProductListData(final boolean boool, int page, String space, String price, String house, String area) {

        Log.e("筛选内容**", "space:" + space + "price:" + price + " house:" + house + "area:" + area);
        showDialog();


        final FinalHttp fh = new FinalHttp();
        final AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        ajaxParams.put("page", page + "");
        ajaxParams.put("number", "10");
        ajaxParams.put("cate_id", cate_id);
        strSearch = !TextUtils.isEmpty(etSearch.getText().toString() + "") ? etSearch.getText().toString() : "";
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
                    if (aCache != null) {
                        if (boool) {
                            aCache.put("product_list_json", s, ACache.TIME_HOUR);
                        }
                    }
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
            tvProjectNum.setText(getString(R.string.sum) + productData.size() + getString(R.string.individuaproject) + getString(R.string.single_blank_space) + strSelect);
        }
        if (isRefresh) {
            if (MyUtils.getInstance().isNetworkConnected(ScaleActivity.this)) {
                ptrl.refreshFinish(PullToRefreshLayout.SUCCEED);
            }
            adapter = new CustomeListAdapter(ScaleActivity.this, isRefresh);
            listView.setAdapter(adapter);
        }
        if (MyUtils.getInstance().isNetworkConnected(ScaleActivity.this)) {
            ptrl.loadmoreFinish(PullToRefreshLayout.SUCCEED);
        }
        adapter.addData(productData);

    }

    public void goTo(Object bean, Class<?> cls, String str1, String str2) {
        Bundle bundle = new Bundle();
        bundle.putSerializable("bean", (Serializable) bean);
        bundle.putString("productName", str1 == null ? "" : str1);
        bundle.putString("productId", str2 == null ? "" : str2);
        ActivitySkip.forward(ScaleActivity.this, cls, bundle);
        ;
    }
}
