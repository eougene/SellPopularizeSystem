package com.yd.org.sellpopularizesystem.activity;

import android.app.Activity;
import android.os.Bundle;
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
import com.yd.org.sellpopularizesystem.javaBean.ProSubUnitClassifyBean;
import com.yd.org.sellpopularizesystem.javaBean.ProSubunitListBean;
import com.yd.org.sellpopularizesystem.javaBean.ProductChildBean;
import com.yd.org.sellpopularizesystem.javaBean.ProductDetailBean;
import com.yd.org.sellpopularizesystem.javaBean.ProductSubunitListBean;
import com.yd.org.sellpopularizesystem.myView.CommonPopuWindow;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.MyUtils;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;
import com.yd.org.sellpopularizesystem.utils.ToasShow;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProductSubunitListActivity extends BaseActivity {
    private Button btViewDetail, btRemain, btCancel;
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
    private ProSubUnitClassifyBean childBean;
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
    private Bundle bund;
    //筛选
    private OptionsPickerView optionsPickerView;
    private List houseTypes = new ArrayList<String>();
    private List numbers = new ArrayList<String>();
    private String strHouseType;
    private String strNum;

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
        if (prs == null) {
            getItemProductDetail();
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
                childBean = (ProSubUnitClassifyBean) bean;
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
        tvSelect = getViewById(R.id.tvSelect);
        tvSelect.setVisibility(View.VISIBLE);
        ivSearch = getViewById(R.id.rightSearchLinearLayout);
        ivSearch.setVisibility(View.GONE);
        tvSelect.setOnClickListener(mOnClickListener);
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

        mCustomePopuWindow = new CustomePopuWindow(ProductSubunitListActivity.this, mOnClickListener);
        mView = mCustomePopuWindow.getContentView();
        rlPop = (RelativeLayout) mView.findViewById(R.id.rlPop);
        rlPop.setOnClickListener(mOnClickListener);
        btViewDetail = (Button) mView.findViewById(R.id.btViewDetail);
        btRemain = (Button) mView.findViewById(R.id.btRemain);
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
                Log.e("filterList", "onOptionsSelect: "+filterList.size());
                if (filterList.size()>0){
                    adapter.setmDatas(filterList);
                    adapter.notifyDataSetChanged();
                }else {
                    ToasShow.showToastCenter(ProductSubunitListActivity.this,getString(R.string.nomatchdata));
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
        }else {
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
        showDialog();
        FinalHttp fh = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        ajaxParams.put("product_id", product_id == null ? "" : product_id);
        ajaxParams.put("page", page);
        ajaxParams.put("number", 50 + "");
        ajaxParams.put("provice", "");
        ajaxParams.put("city", "");
        ajaxParams.put("town", "");
        ajaxParams.put("address", "");
        ajaxParams.put("bedroom", bedRoomNum == null ? "" : bedRoomNum);
        ajaxParams.put("bathroom", bathRoomNum);
        ajaxParams.put("car_space", carSpace);
        ajaxParams.put("has_study", "");
        ajaxParams.put("ensuite", "");
        ajaxParams.put("level", "");
        ajaxParams.put("price", "0~100000000");
        ajaxParams.put("internal", "0~100000000");
        ajaxParams.put("external", "0~100000000");
        ajaxParams.put("building_area", "0~100000000");
        ajaxParams.put("is_lock", "");
        fh.get(Contants.PRODUCT_SUBUNIT_LIST, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                Log.e("s***", "s:" + s);
                closeDialog();
                parseJson(s);
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);

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
                if (Integer.parseInt(o1.getBedroom()) > Integer.parseInt(o2.getBedroom())) {
                    return 1;
                }
                if (Integer.parseInt(o1.getBedroom()) == Integer.parseInt(o2.getBedroom())) {
                    return 0;
                }
                return -1;
            }
        });
        if (data.size() > 0) {
            if (tvNoInfo.getVisibility() == View.VISIBLE) {
                tvNoInfo.setVisibility(View.GONE);
            }
            setAdapter();
        } else {
            Log.e("data", "parseJson: " + adapter.getmDatas().size());
            adapter.CleaDates(data);
            Log.e("data**", "parseJson: " + adapter.getmDatas().size());
            tvNoInfo.setVisibility(View.VISIBLE);
        }

    }

    private void setAdapter() {
        adapter = new CommonAdapter<ProSubunitListBean.ResultBean.PropertyBean>(this, data, R.layout.productdetaill_activity_listview_item) {
            @Override
            public void convert(ViewHolder holder, ProSubunitListBean.ResultBean.PropertyBean item) {
                Log.e("tag", "convert: " + item.getIs_lock());
                if (item.getIs_lock() == 0) {
                    holder.setImageResource(R.id.ivHouse, R.mipmap.greenhome);
                } else if (item.getIs_lock() == 1) {
                    holder.setImageResource(R.id.ivHouse, R.mipmap.redhome);
                } else {
                    holder.setImageResource(R.id.ivHouse, R.mipmap.yellowhome);
                }
                if (!item.getThumb().equals("")) {
                    //holder.setImageByUrl(R.id.ivHousePhoto,item.getThumb(),mOnClickListener);
                    holder.getView(R.id.tvNoPhoto).setVisibility(View.GONE);
                    holder.getView(R.id.ivHousePic).setVisibility(View.VISIBLE);
                    holder.getView(R.id.ivHousePic).setOnClickListener(mOnClickListener);
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
            bund = new Bundle();
            switch (v.getId()) {
                case R.id.rightTitle:

                    break;
                case R.id.tvSelect:
                    optionsPickerView.show();
                    break;
                case R.id.ivHousePic:
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
                    break;
                case R.id.rlPop:
                    if (mCustomePopuWindow != null) {
                        mCustomePopuWindow.dismiss();
                    }
                    break;
                case R.id.btViewDetail:
                    if (bund != null) {
                        bund.putSerializable("item", data.get(pos));
                        ActivitySkip.forward(ProductSubunitListActivity.this, ProductSubItemDetailActivity.class, bund);
                        mCustomePopuWindow.dismiss();
                    }
                    break;
                case R.id.btRemain:
                    if (bund != null) {
                        bund.putSerializable("item", data.get(pos));
                        ActivitySkip.forward(ProductSubunitListActivity.this, ReserveActivity.class, bund);
                        mCustomePopuWindow.dismiss();
                    }
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
                    break;
                //平面图
                case R.id.tvFloor:
                    if (tvFloor.getAlpha() == 1.0f) {
                        bund.putSerializable("floorListData", (Serializable) prs.getImg_content());
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

    private void getItemProductDetail() {
        showDialog();
        FinalHttp fh = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("product_id", product_id);
        ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        fh.get(Contants.PRODUCT_DETAIL, ajaxParams, new AjaxCallBack<String>() {
            @Override
            public void onSuccess(String s) {
                super.onSuccess(s);
                closeDialog();
                Gson gson = new Gson();
                ProductDetailBean pdb = gson.fromJson(s, ProductDetailBean.class);
                if (pdb.getCode().equals("1")) {
                    prs = pdb.getResult();
                    BaseApplication.getInstance().setPrs(prs);
                    controlColor();
                }
            }

            @Override
            public void onFailure(Throwable t, int errorNo, String strMsg) {
                super.onFailure(t, errorNo, strMsg);
            }
        });

    }

    private void controlColor() {
        if (prs.getDescription_url() != null) {
            tvIntroduce.setAlpha(1.0f);
        }
        if (prs.getVideo_url() != null) {
            tvVideo.setAlpha(1.0f);
        }
        if (prs.getImg_content() != null && prs.getImg_content().size() > 0) {
            tvFloor.setAlpha(1.0f);
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
        btCancel.setOnClickListener(mOnClickListener);
        lvHouseDetail.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mCustomePopuWindow.showAtLocation(ProductSubunitListActivity.this.findViewById(R.id.llProdet), Gravity.BOTTOM, 0, 0);
                pos = position;
                if (data.get(pos).getIs_lock() == 1) {
                    btRemain.setVisibility(View.GONE);
                } else if (data.get(pos).getIs_lock() == 0) {
                    btRemain.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    class CustomePopuWindow extends CommonPopuWindow {

        public CustomePopuWindow(Activity context, View.OnClickListener itemsOnClick) {
            super(context, itemsOnClick);
        }

        @Override
        protected int getLayoutId() {
            return R.layout.prodet_lvitem_popwindow;
        }
    }
}
