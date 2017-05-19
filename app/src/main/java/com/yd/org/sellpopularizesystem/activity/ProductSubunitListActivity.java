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
import com.yd.org.sellpopularizesystem.javaBean.ProductChildBean;
import com.yd.org.sellpopularizesystem.javaBean.ProductDetailBean;
import com.yd.org.sellpopularizesystem.javaBean.ProductSubunitListBean;
import com.yd.org.sellpopularizesystem.myView.CommonPopuWindow;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.MyUtils;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.io.Serializable;
import java.util.ArrayList;
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
    private List<ProductChildBean> data = new ArrayList<ProductChildBean>();
    private CommonAdapter adapter;
    private Object bean;
    private ProSubUnitClassifyBean childBean;
    private CustomePopuWindow mCustomePopuWindow;
    private String bedRoomNum;
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
        houseTypes.add(getString(R.string.bedroom));
        houseTypes.add(getString(R.string.bathroom));
        houseTypes.add(getString(R.string.carport));
        numbers.add(getString(R.string.nolimit));
        numbers.add("1");numbers.add("1.5");numbers.add("2");numbers.add("2.5");
    }

    private void initOptionsPickerView() {
        OptionsPickerView.Builder builder=new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {

            }
        }).setTitleColor(R.color.black)
                .setCyclic(true, true, true).setSelectOptions(houseTypes.indexOf(getString(R.string.bedroom)),numbers.indexOf(getString(R.string.nolimit)));
    }

    private void getListData() {
        showDialog();
        FinalHttp fh = new FinalHttp();
        AjaxParams ajaxParams = new AjaxParams();
        ajaxParams.put("user_id", SharedPreferencesHelps.getUserID());
        ajaxParams.put("product_id", product_id == null ? "" : product_id);
        ajaxParams.put("page", page);
        ajaxParams.put("number", 10 + "");
        ajaxParams.put("provice", "");
        ajaxParams.put("city", "");
        ajaxParams.put("town", "");
        ajaxParams.put("address", "");
        ajaxParams.put("bedroom", bedRoomNum == null ? "" : bedRoomNum);
        ajaxParams.put("bathroom", "");
        ajaxParams.put("car_space", "");
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
        ProductSubunitListBean pslb = gson.fromJson(s, ProductSubunitListBean.class);
        data = pslb.getResult();
        if (data.size() > 0) {
            setAdapter();
        } else {
            tvNoInfo.setVisibility(View.VISIBLE);
        }

    }

    private void setAdapter() {
        adapter = new CommonAdapter<ProductChildBean>(this, data, R.layout.productdetaill_activity_listview_item) {
            @Override
            public void convert(ViewHolder holder, ProductChildBean item) {
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
                holder.setText(R.id.tvHouseSquare, item.getBuilding_area());
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

                    break;
                case R.id.ivHousePic:
                    if (bund == null) {
                        bund = new Bundle();
                    }
                  /*  if (prs!=null){
                        if (prs.getImg_content()==null||prs.getImg_content().size()==0){
                            Log.e("ivHousePic", "onClick: "+"该项目没有图片");

                        }
                    }*/

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
