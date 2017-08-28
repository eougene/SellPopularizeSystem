package com.yd.org.sellpopularizesystem.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.adapter.CommonAdapter;
import com.yd.org.sellpopularizesystem.application.BaseApplication;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.application.ViewHolder;
import com.yd.org.sellpopularizesystem.javaBean.CustomBean;
import com.yd.org.sellpopularizesystem.javaBean.EoilistBean;
import com.yd.org.sellpopularizesystem.javaBean.ErrorBean;
import com.yd.org.sellpopularizesystem.javaBean.ImageContent;
import com.yd.org.sellpopularizesystem.javaBean.ProSubunitListBean;
import com.yd.org.sellpopularizesystem.javaBean.ProductDetailBean;
import com.yd.org.sellpopularizesystem.javaBean.ProductListBean;
import com.yd.org.sellpopularizesystem.myView.CommonPopuWindow;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.MyUtils;
import com.yd.org.sellpopularizesystem.utils.ObjectSaveUtil;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;
import com.yd.org.sellpopularizesystem.utils.StringUtils;
import com.yd.org.sellpopularizesystem.utils.ToasShow;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.model.CacheMode;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.igexin.push.core.g.s;

public class ProductSubunitListActivity extends BaseActivity {
    private Button btViewDetail, btRemain, btLineup, btCancel;
    private int pos;
    private RelativeLayout rlPop;
    private TextView tvSelect, tvNoInfo,
            tvIntroduce, tvVideo, tvFloor, tvContract, tvFile;
    private ListView lvHouseDetail;
    private ImageView ivSearch;
    private View mView;
    private List<ProSubunitListBean.ResultBean.PropertyBean> data = new ArrayList<>();
    private CommonAdapter adapter;
    private Object bean;
    private ProductListBean.ResultBean.ChildsBean childBean;
    private CustomePopuWindow mCustomePopuWindow;
    private String bedRoomNum;
    private String bathRoomNum = "";
    private String carSpace = "";
    private String product_id;
    private String string = "";
    private String page = "1";
    //从产品中点击预订跳转标志
    private String flag = "";
    private ProductDetailBean.ResultBean prs;
    private Bundle bund = new Bundle();
    //筛选
    private OptionsPickerView optionsPickerView;
    private List houseTypes = new ArrayList<>();
    private List numbers = new ArrayList<>();
    private String strHouseType;
    private String strNum;
    private List<ImageContent> imgContents = new ArrayList<ImageContent>();
    private List<EoilistBean.ResultBean> eoiList = new ArrayList<>();

    @Override
    protected int setContentView() {
        return R.layout.activity_view_more;

    }

    @Override
    public void initView() {
        getViews();
        Bundle bundle = getIntent().getExtras();
        bean = bundle.get("bean");
        product_id = (String) bundle.get("productId");
        flag = (String) bundle.get("pidatopsla");
        prs = (ProductDetailBean.ResultBean) bundle.getSerializable("prs");
        if (flag != null && flag.equals("maptopsla")) {
            string = (String) bundle.get("title");
            setTitle(string);
            getListData();
        }
        if (prs == null) {
            //获取产品详情
            getItemProductDetail();
        } else {
            controlColor();
        }
        if (flag != null && flag.equals("pidatopsla")) {
            string = (String) bundle.get("title");
            setTitle(string);
            getListData();
        }
        if (bean != null) {
            if (bean instanceof List) {//点击查看所有传递过来的集合对象
            /*data.clear();
            data.addAll((List)bean);
            Log.e("TAG", "initView: "+"集合");*/
                String proName = bundle.getString("productName");
                setTitle(proName);
                getListData();
            } else {//点击单个item传递过来的对象
                childBean = (ProductListBean.ResultBean.ChildsBean) bean;
            /*data.clear();
            data.add(childBean);*/
                bedRoomNum = childBean.getBedroom();
                product_id = (String) bundle.get("productId");
                string = bundle.getString("productName");
                setTitle(string);
                getListData();
            }
        }
    }

    private void getViews() {
        /*tvSelect = getViewById(R.id.tvSelect);
        tvSelect.setVisibility(View.VISIBLE);*/
        rightRtitle.setTextColor(ContextCompat.getColor(this, R.color.redyellow));
        setRightTitle(R.string.select, mOnClickListener);
        ivSearch = getViewById(R.id.rightSearchLinearLayout);
        ivSearch.setVisibility(View.GONE);
        //tvSelect.setOnClickListener(mOnClickListener);
        //tvRightDes.setBackgroundColor(Color.parseColor("#e14143"));
        //tvRightDes.setBackground(ContextCompat.getDrawable(this,R.drawable.button_bac));
        lvHouseDetail = getViewById(R.id.lvHouseDetail);
        tvIntroduce = getViewById(R.id.tvIntroduce);
        tvIntroduce.setOnClickListener(mOnClickListener);
        tvNoInfo = getViewById(R.id.tvNoInfo);
        tvVideo = getViewById(R.id.tvVideo);
        tvFloor = getViewById(R.id.tvFloor);
        tvFloor.setOnClickListener(mOnClickListener);
        tvContract = getViewById(R.id.tvContract);
        tvContract.setOnClickListener(mOnClickListener);
        tvFile = getViewById(R.id.tvFile);
        tvFile.setOnClickListener(mOnClickListener);

        mCustomePopuWindow = new CustomePopuWindow(ProductSubunitListActivity.this);
        mView = mCustomePopuWindow.getContentView();
        rlPop = (RelativeLayout) mView.findViewById(R.id.rlPop);
        rlPop.setOnClickListener(mOnClickListener);
        btViewDetail = (Button) mView.findViewById(R.id.btViewDetail);
        btRemain = (Button) mView.findViewById(R.id.btRemain);
        btLineup = (Button) mView.findViewById(R.id.btLineup);
        btCancel = (Button) mView.findViewById(R.id.btCancel);
        initOptionData();
        initOptionsPickerView();

    }

    private void initOptionData() {
        //筛选卧室,浴室,车库
        houseTypes.add(getString(R.string.bedroom));
        houseTypes.add(getString(R.string.bathroom));
        houseTypes.add(getString(R.string.carport));
        numbers.add(getString(R.string.nolimit));
        //筛选数据
        for (int i = 1; i < 10; i++) {
            numbers.add(String.valueOf(i));
            if (i != 9) {
                numbers.add(String.valueOf(i + 0.5));
            }
        }
    }

    private void initOptionsPickerView() {
        OptionsPickerView.Builder builder = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                strHouseType = (String) houseTypes.get(options1);
                strNum = numbers.get(options2).equals(getString(R.string.nolimit)) ? "" : (String) numbers.get(options2);
                if (strHouseType.equals(getString(R.string.bedroom))) {
                    bedRoomNum = strNum;
                    bathRoomNum = "";
                    carSpace = "";
                } else if (houseTypes.get(options1).equals(getString(R.string.bathroom))) {
                    bedRoomNum = "";
                    bathRoomNum = strNum;
                    carSpace = "";
                } else if (houseTypes.get(options1).equals(getString(R.string.carport))) {
                    carSpace = strNum;
                    bedRoomNum = "";
                    bathRoomNum = "";
                }
                //getListData();
                List<ProSubunitListBean.ResultBean.PropertyBean> filterList = findAllByPro(data, strHouseType, strNum);
                if (filterList.size() > 0) {
                    adapter.setmDatas(filterList);
                    adapter.notifyDataSetChanged();
                } else {
                    ToasShow.showToastCenter(ProductSubunitListActivity.this, getString(R.string.nomatchdata));
                }
            }
        }).setTitleColor(R.color.black)
                .setCyclic(false, false, true).setSelectOptions(houseTypes.indexOf(getString(R.string.bedroom)), numbers.indexOf(getString(R.string.nolimit)));
        optionsPickerView = new OptionsPickerView(builder);
        optionsPickerView.setNPicker(houseTypes, numbers, null);
    }


    private List<ProSubunitListBean.ResultBean.PropertyBean> findAllByPro(List<ProSubunitListBean.ResultBean.PropertyBean> data, String strHouseType, String strNum) {
        List<ProSubunitListBean.ResultBean.PropertyBean> filterList = new ArrayList<>();
        if (strNum.equals("")) {
            filterList.clear();
            filterList.addAll(data);
        } else {
            for (int i = 0; i < data.size(); i++) {
                if (strHouseType.equals(getString(R.string.bedroom))) {
                    if (data.get(i).getBedroom().equals(strNum)) {
                        filterList.add(data.get(i));
                    }
                }
                if (strHouseType.equals(getString(R.string.bathroom))) {
                    if (data.get(i).getBathroom().equals(strNum)) {
                        filterList.add(data.get(i));
                    }
                }
                if (strHouseType.equals(getString(R.string.carport))) {
                    if (data.get(i).getCar_space().equals(strNum)) {
                        filterList.add(data.get(i));
                    }
                }
            }
        }
        return filterList;
    }

    //获取子单元列表数据
    private void getListData() {
        EasyHttp.get(Contants.PRODUCT_SUBUNIT_LIST)
                .cacheMode(CacheMode.NO_CACHE)
                .cacheKey(this.getClass().getSimpleName())
                .timeStamp(true)
                .params("user_id", SharedPreferencesHelps.getUserID())
                .params("product_id", product_id == null ? "" : product_id)
                .params("provice", "")
                .params("city", "")
                .params("town", "")
                .params("address", "")
                .params("bedroom", bedRoomNum == null ? "" : bedRoomNum)
                .params("bathroom", bathRoomNum)
                .params("car_space", carSpace)
                .params("has_study", "")
                .params("ensuite", "")
                .params("level", "")
                .params("price", "0~100000000")
                .params("internal", "0~100000000")
                .params("external", "0~100000000")
                .params("building_area", "0~100000000")
                .params("is_lock", "")
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
                    }

                    @Override
                    public void onSuccess(String json) {

                        Log.e("json***","json:"+json);

                        closeDialog();
                        parseJson(json);
                    }
                });


    }

    private void parseJson(String s) {
        Gson gson = new Gson();
        ProSubunitListBean pslb = gson.fromJson(s, ProSubunitListBean.class);
        data = pslb.getResult().getProperty();
        //按照集合中元素属性进行排序
        Collections.sort(data, new Comparator<ProSubunitListBean.ResultBean.PropertyBean>() {
            @Override
            public int compare(ProSubunitListBean.ResultBean.PropertyBean o1, ProSubunitListBean.ResultBean.PropertyBean o2) {
                if (TextUtils.isDigitsOnly(o1.getProduct_childs_unit_number()) && TextUtils.isDigitsOnly(o2.getProduct_childs_unit_number())) {
                    if (Integer.parseInt(o1.getProduct_childs_unit_number()) >
                            Integer.parseInt(o2.getProduct_childs_unit_number())) {
                        return 1;
                    }
                    if (Integer.parseInt(o1.getProduct_childs_unit_number()) ==
                            Integer.parseInt(o2.getProduct_childs_unit_number())) {
                        return 0;
                    }
                } else {
                    if (StringUtils.containLeter(o1.getProduct_childs_unit_number()) && StringUtils.containLeter(o2.getProduct_childs_unit_number())) {
                        if (StringUtils.getLetterFromString(o1.getProduct_childs_unit_number())
                                .equals(StringUtils.getLetterFromString(o2.getProduct_childs_unit_number()))) {
                            if (Integer.parseInt(StringUtils.getDigtalFromString(o1.getProduct_childs_unit_number())) >
                                    Integer.parseInt(StringUtils.getDigtalFromString(o2.getProduct_childs_unit_number()))) {
                                return 1;
                            }
                            if (Integer.parseInt(StringUtils.getDigtalFromString(o1.getProduct_childs_unit_number())) ==
                                    Integer.parseInt(StringUtils.getDigtalFromString(o2.getProduct_childs_unit_number()))) {
                                return 0;
                            }
                        } else {
                            return StringUtils.getLetterFromString(o1.getProduct_childs_unit_number())
                                    .compareTo(StringUtils.getLetterFromString(o2.getProduct_childs_unit_number()));
                        }
                    }
                    return o1.getProduct_childs_unit_number().compareTo(o2.getProduct_childs_unit_number());
                }

                return -1;
            }
        });

        //找出eoi元素
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getIf_eoi() == 1) {
                ProSubunitListBean.ResultBean.PropertyBean p = data.get(i);
                data.remove(i);
                //把eoi元素放在第一位
                data.add(0, p);
            }
        }

        if (data.size() > 0) {
            if (tvNoInfo.getVisibility() == View.VISIBLE) {
                tvNoInfo.setVisibility(View.GONE);
            }
            setAdapter();
        } else {
            //adapter.CleaDates(data);
            tvNoInfo.setVisibility(View.VISIBLE);
        }

    }

    private void setAdapter() {
        adapter = new CommonAdapter<ProSubunitListBean.ResultBean.PropertyBean>(this, data, R.layout.productdetaill_activity_listview_item) {
            @Override
            public void convert(ViewHolder holder, final ProSubunitListBean.ResultBean.PropertyBean item) {
                //没有预订时颜色
                if (item.getIs_lock() == 0) {
                    holder.setImageResource(R.id.ivHouse, R.mipmap.greenhome);
                } else if (item.getIs_lock() == 1) {
                    //预定过的颜色
                    holder.setImageResource(R.id.ivHouse, R.mipmap.redhome);
                    //为eoi时的颜色
                } else {
                    holder.setImageResource(R.id.ivHouse, R.mipmap.yellowhome);
                }
                if (item.getIf_eoi() == 1) {
                    Log.e("item", "item:" + item.getProduct_childs_id());
                    holder.setImageResource(R.id.ivHouse, R.mipmap.eoi);
                }


                //房型图
                if (!item.getThumb().equals("")) {
                    holder.getView(R.id.tvNoPhoto).setVisibility(View.GONE);
                    holder.getView(R.id.ivHousePic).setVisibility(View.VISIBLE);

                    if (item.getThumb().endsWith(".pdf") || item.getThumb().endsWith(".PDF")) {
                        holder.getView(R.id.ivHousePic).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ImageContent imageContent = new ImageContent();
                                imageContent.setUrl(item.getThumb());
                                imgContents.clear();
                                imgContents.add(imageContent);
                                bund.putString("introduce", item.getThumb() + "");
                                bund.putString("type", "1");
                                ActivitySkip.forward(ProductSubunitListActivity.this, IntroduceActivity.class, bund);

                            }
                        });
                    } else {

                        holder.getView(R.id.ivHousePic).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ImageContent imageContent = new ImageContent();
                                imageContent.setUrl(item.getThumb());
                                imgContents.clear();
                                imgContents.add(imageContent);
                                bund.putSerializable("img_content", (Serializable) imgContents);
                                ActivitySkip.forward(ProductSubunitListActivity.this, ImageShowActivity.class, bund);
                            }
                        });
                    }


                } else {
                    holder.getView(R.id.tvNoPhoto).setVisibility(View.VISIBLE);
                    holder.getView(R.id.ivHousePic).setVisibility(View.GONE);
                }
                holder.setText(R.id.tvDetailLoc, item.getProduct_childs_unit_number());
                holder.setText(R.id.tvBedRoom, item.getBedroom());
                holder.setText(R.id.tvBathroom, item.getBathroom());
                holder.setText(R.id.tvCarSquare, item.getCar_space());
                holder.setText(R.id.tvHouseSquare, MyUtils.addComma(item.getBuilding_area().split("\\.")[0]));
                holder.setText(R.id.tvDetailPrice, "$" + getString(R.string.single_blank_space) + MyUtils.addComma(item.getPrice().split("\\.")[0]));
            }
        };
        lvHouseDetail.setAdapter(adapter);
    }

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //筛选
                case R.id.rightTitle:
                    optionsPickerView.show();
                    break;

                /*case R.id.ivHousePic:
                    if (bund == null) {
                        bund = new Bundle();
                    }
                    if (prs != null && prs.getImg_content().size() > 0) {
                        bund.putSerializable("img_content", (Serializable) prs.getImg_content());
                        ActivitySkip.forward(ProductSubunitListActivity.this, ImageShowActivity.class, bund);
                    } else {
                        if (BaseApplication.getInstance().getPrs() != null && BaseApplication.getInstance().getPrs().getProduct_id() == Integer.parseInt(product_id)) {
                            prs = BaseApplication.getInstance().getPrs();
                            if (prs.getImg_content().size() > 0) {
                                bund.putSerializable("img_content", (Serializable) prs.getImg_content());
                                ActivitySkip.forward(ProductSubunitListActivity.this, ImageShowActivity.class, bund);
                            }
                        } else {
                            getItemProductDetail();
                        }
                    }
                    break;*/
                case R.id.rlPop:
                    if (mCustomePopuWindow != null) {
                        mCustomePopuWindow.dismiss();
                    }
                    break;
                //查看详情
                case R.id.btViewDetail:
                    if (bund != null) {
                        bund.putSerializable("item", data.get(pos));
                        ActivitySkip.forward(ProductSubunitListActivity.this, ProductSubItemDetailActivity.class, bund);
                        mCustomePopuWindow.dismiss();
                    }
                    break;
                //预定
                case R.id.btRemain:
                    if (bund != null) {
                        bund.putSerializable("item", data.get(pos));
                        ActivitySkip.forward(ProductSubunitListActivity.this, ReserveActivity.class, bund);
                        mCustomePopuWindow.dismiss();
                    }
                    break;
                //排队
                case R.id.btLineup:
                    getEoiData(data.get(pos));
                    break;
                case R.id.btCancel:
                    if (mCustomePopuWindow != null) {
                        mCustomePopuWindow.dismiss();
                    }
                    break;
                //视频
                case R.id.tvVideo:
                    if (bund != null) {
                        if (tvVideo.getAlpha() == 1.0f) {
                            bund.putSerializable("prs", prs);
                            ActivitySkip.forward(ProductSubunitListActivity.this, VideoActivity.class, bund);
                        }
                    }
                    break;
                //介绍
                case R.id.tvIntroduce:

                    if (tvIntroduce.getAlpha() == 1.0f) {
                        bund.putString("type", "0");
                        bund.putString("introduce", prs.getDescription_url() + "");
                        ActivitySkip.forward(ProductSubunitListActivity.this, IntroduceActivity.class, bund);
                    }
                    break;
                //平面图
                case R.id.tvFloor:
                    if (tvFloor.getAlpha() == 1.0f) {
                        bund.putSerializable("floorListData", (Serializable) prs.getFile_content());
                        ActivitySkip.forward(ProductSubunitListActivity.this, BuildingPlanActivity.class, bund);
                    }
                    break;
                //合同
                case R.id.tvContract:
                    if (tvContract.getAlpha() == 1.0f) {
                        bund.putSerializable("file", prs);
                        bund.putString("key", "Contract");
                        ActivitySkip.forward(ProductSubunitListActivity.this, FileActivity.class, bund);
                    }
                    break;
                //文件
                case R.id.tvFile:
                    if (tvFile.getAlpha() == 1.0f) {
                        bund.putString("key", "File");
                        bund.putSerializable("file", prs);
                        ActivitySkip.forward(ProductSubunitListActivity.this, FileActivity.class, bund);
                    }
                    break;
            }
        }
    };

    /**
     * 获取充值列表
     */
    private void getEoiData(final ProSubunitListBean.ResultBean.PropertyBean propertyBean) {
        EasyHttp.get(Contants.EOI_LIST)
                .cacheMode(CacheMode.NO_CACHE)
                .cacheKey(this.getClass().getSimpleName())
                .timeStamp(true)
                .params("user_id", SharedPreferencesHelps.getUserID())
                .params("page", "1")
                .params("number", "1")
                .params("company_id", ((CustomBean.ResultBean) ObjectSaveUtil.readObject(ProductSubunitListActivity.this, "custome")).getCompany_id() + "")
                .params("client", ((CustomBean.ResultBean) ObjectSaveUtil.readObject(ProductSubunitListActivity.this, "custome")).getCustomer_id() + "")
                .params("property_id", "")
                .params("is_use", "0")
                .params("house", "")
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
                        Gson gson = new Gson();

                        EoilistBean eoilistBean = gson.fromJson(s, EoilistBean.class);
                        if (eoilistBean.getCode() == 1) {
                            eoiList = eoilistBean.getResult();
                            if (eoilistBean.getMsg().equals("暂无数据")) {
                                ToasShow.showToastCenter(ProductSubunitListActivity.this, "暂无可用EOI,请充值");
                            } else {
                                if (eoiList.size() > 0) {
                                    eoiLineUp(propertyBean, eoiList.get(0).getProduct_eois_id() + "");
                                }
                            }
                        }
                    }
                });


    }


    //eoi排队请求
    private void eoiLineUp(ProSubunitListBean.ResultBean.PropertyBean propertyBean, String eoi) {
        EasyHttp.get(Contants.EOI_USE)
                .cacheMode(CacheMode.NO_CACHE)
                .cacheKey(this.getClass().getSimpleName())
                .timeStamp(true)
                .params("eoi_id", eoi)
                .params("user_id", SharedPreferencesHelps.getUserID())
                .params("product_id", propertyBean.getProduct_id() + "")
                .params("product_child_id", propertyBean.getProduct_childs_id() + "")
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
                        Gson gson = new Gson();

                        Gson gs = new Gson();
                        ErrorBean e = gs.fromJson(s, ErrorBean.class);
                        if (e.getCode().equals("1")) {
                            ToasShow.showToastCenter(ProductSubunitListActivity.this, e.getMsg());
                        } else {
                            ToasShow.showToastCenter(ProductSubunitListActivity.this, e.getMsg());
                        }
                    }
                });


    }

    private void getItemProductDetail() {
        EasyHttp.get(Contants.PRODUCT_DETAIL)
                .cacheMode(CacheMode.NO_CACHE)
                .cacheKey(this.getClass().getSimpleName())
                .timeStamp(true)
                .params("product_id", product_id)
                .params("user_id", SharedPreferencesHelps.getUserID())
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
                        Log.e("onSuccess", "json:" + json);
                        closeDialog();
                        Gson gson = new Gson();
                        ProductDetailBean pdb = gson.fromJson(json, ProductDetailBean.class);
                        if (pdb.getCode().equals("1")) {
                            prs = pdb.getResult();
                            BaseApplication.getInstance().setPrs(prs);
                            controlColor();


                        }
                    }
                });


    }


    //控制控件颜色
    private void controlColor() {
        if (prs.getDescription_url() != null) {
            tvIntroduce.setAlpha(1.0f);
        }
        if (prs.getVideo_url() != null) {
            tvVideo.setAlpha(1.0f);
        }


        if (prs.getFile_content() != null && prs.getFile_content().size() > 0) {

            for (int i = 0; i < prs.getFile_content().size(); i++) {
                if (prs.getFile_content().get(i).getFile_type() == 1) {
                    tvFloor.setAlpha(1.0f);
                }
            }

        }


        if (prs.getContract_url() != null && !prs.getContract_url().equals("")) {
            tvContract.setAlpha(1.0f);
        }
        if (prs.getFile_content() != null && prs.getFile_content().size() > 0) {
            tvFile.setAlpha(1.0f);
        }
    }

    @Override
    public void setListener() {
        btViewDetail.setOnClickListener(mOnClickListener);
        btRemain.setOnClickListener(mOnClickListener);
        btLineup.setOnClickListener(mOnClickListener);
        btCancel.setOnClickListener(mOnClickListener);
        lvHouseDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCustomePopuWindow.showAtLocation(ProductSubunitListActivity.this.findViewById(R.id.llProdet), Gravity.BOTTOM, 0, 0);
                pos = position;


                //销售可预订
                if (SharedPreferencesHelps.getType() == 1) {
                    btRemain.setVisibility(View.VISIBLE);


                    if (data.get(pos).getIs_lock() == 1) {
                        btRemain.setVisibility(View.GONE);
                        btLineup.setVisibility(View.GONE);
                    } else if (data.get(pos).getIs_lock() == 0) {
                        btRemain.setVisibility(View.VISIBLE);
                        btLineup.setVisibility(View.GONE);
                    }
                    if (data.get(pos).getIf_eoi() == 1) {
                        btRemain.setVisibility(View.GONE);
                        btLineup.setVisibility(View.VISIBLE);
                    }

                    //推荐人不可预订
                } else if (SharedPreferencesHelps.getType() == 2) {
                    btRemain.setVisibility(View.GONE);


                    if (data.get(pos).getIs_lock() == 1) {
                        btRemain.setVisibility(View.GONE);
                        btLineup.setVisibility(View.GONE);
                    } else if (data.get(pos).getIs_lock() == 0) {
                        btRemain.setVisibility(View.GONE);
                        btLineup.setVisibility(View.GONE);
                    }
                    if (data.get(pos).getIf_eoi() == 1) {
                        btRemain.setVisibility(View.GONE);
                        btLineup.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
    }

    class CustomePopuWindow extends CommonPopuWindow {

        public CustomePopuWindow(Activity context) {
            super(context);
        }

        @Override
        protected int getLayoutId() {
            return R.layout.prodet_lvitem_popwindow;
        }
    }


}
