package com.yd.org.sellpopularizesystem.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.javaBean.ProductListBean;
import com.yd.org.sellpopularizesystem.javaBean.SelectConditionBean;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.model.CacheMode;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.HttpParams;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class SelectConditionActivity extends BaseActivity {
    //private RadioGroup rgType, rgPrice;
    private LinearLayout llHouseType, llType, llPrice,llBase;
    private TextView tvSelect;
    private ImageView ivSearch;
    private String str, strFlag;
    private String selectStr;
    private String selectStrTag;
    private StringBuilder sb = new StringBuilder();
    public static int temp = -1;
    private ArrayList<Object> productData;


    @Override
    protected int setContentView() {
        return R.layout.activity_select_condition;
    }

    @Override
    public void initView() {
        initViews();
        initStting();
        str = getIntent().getExtras().getString("fatosca");
        if (str.equals("area")) {
            setTitle(R.string.type);
            llHouseType.setVisibility(View.GONE);
            /*rgType.setVisibility(View.GONE);
            rgPrice.setVisibility(View.GONE);*/
            llType.setVisibility(View.GONE);
            llPrice.setVisibility(View.GONE);
        } else if (str.equals("housetype")) {
            setTitle(R.string.housetype);
            //rgArea.setVisibility(View.GONE);
            /*rgType.setVisibility(View.GONE);
            rgPrice.setVisibility(View.GONE);*/
            llType.setVisibility(View.GONE);
            llPrice.setVisibility(View.GONE);
            String string = getIntent().getExtras().getString("selectstatus");
            setHouseTypeStatus(string, llHouseType);
        } else if (str.equals("type")) {
            setTitle(R.string.type);
            //rgArea.setVisibility(View.GONE);
            llHouseType.setVisibility(View.GONE);
            //rgPrice.setVisibility(View.GONE);
            llPrice.setVisibility(View.GONE);
            String string = getIntent().getExtras().getString("selectstatus");
            setStatus(string, llType);
            setListener(llType);
        } else if (str.equals("price")) {
            setTitle(R.string.price);
            // rgArea.setVisibility(View.GONE);
            llHouseType.setVisibility(View.GONE);
            //rgType.setVisibility(View.GONE);
            llType.setVisibility(View.GONE);
            String string = getIntent().getExtras().getString("selectstatus");
            setStatus(string, llPrice);
            setListener(llPrice);
        }
        //getSelectConditionData();
    }

    private void getSelectConditionData() {
        HttpParams httpParams = new HttpParams();
        EasyHttp.get(Contants.PRODUCT_LIST)
                .cacheMode(CacheMode.DEFAULT)
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
                        jsonParse(json);
                    }
                });
    }

    private void jsonParse(String json) {
        productData = new ArrayList<>();
        Gson gson = new Gson();
        SelectConditionBean scb = gson.fromJson(json, SelectConditionBean.class);
        if (scb.getCode().equals("1")) {
            if (str.equals("price")){
                SelectConditionBean.ResultBean.ProductPriceBean priceBean=scb.getResult().getProduct_price();
               // Field[] field = priceBean.getClass().getDeclaredFields();

               // int[] priceId=new int[]{R.id.rbPrice1,R.id.rbPrice2,R.id.rbPrice3,R.id.rbPrice4,R.id.rbPrice5,R.id.rbPrice6};
                //String[] priceTags=new String[]{"0~650000","650000~800000","800000~950000","950000~1100000","1100000~300000"};
                String[] priceTexts=new String[]{priceBean.getValue1(),priceBean.getValue2(),priceBean.getValue3()
                ,priceBean.getValue4(),priceBean.getValue5()};

                int j=0;
                for (int i = 0; i <llPrice.getChildCount() ; i++) {
                       /*View view= LayoutInflater.from(SelectConditionActivity.this).inflate(R.layout.select_item_layout,null);
                        view.setId(priceId[i]);
                        view.setTag(priceTags[i]);*/
                       View view=llPrice.getChildAt(i);
                    if (view instanceof CheckBox){
                        ((CheckBox) view).setText(priceTexts[j]);
                        j++;
                    }

                      /*  llBase.addView(view);
                       String firstLetter = field[i].getName().substring(0, 1).toUpperCase();
                        String getMethodName = "get" + firstLetter + field[i].getName().substring(1);
                        Method getMethod = null;
                    try {
                        getMethod = priceBean.getClass().getMethod(getMethodName, new Class[] {});
                        try {
                            String str= (String) getMethod.invoke(priceBean,new Object[]{});
                            ((CheckBox)view).setText(priceBean.get_$_065000021());
                            llBase.addView(view);
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (InvocationTargetException e) {
                            e.printStackTrace();
                        }
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    }*/

                }

            }else if (str.equals("type")){

                SelectConditionBean.ResultBean.ProductCateBean cateBean=scb.getResult().getProduct_cate();
               // String[] typeTags=new String[]{"1","2","3"};
                String[] typeTexts=new String[]{cateBean.getValue1(),cateBean.getValue2(),cateBean.getValue3()};

                int j=0;
                for (int i = 0; i <llType.getChildCount() ; i++) {
                    View view=llType.getChildAt(i);
                    if (view instanceof CheckBox){
                        ((CheckBox) view).setText(typeTexts[j]);
                        j++;
                    }
                }

            }else {

                SelectConditionBean.ResultBean.ProductHouseBean houseBean=scb.getResult().getProduct_house();

                String[] houseTags=new String[]{"1","2","3","4","5","6"};
                String[] houseTexts=new String[]{houseBean.getValue1(),houseBean.getValue2(),houseBean.getValue3(),
                        houseBean.getValue4(),houseBean.getValue5(),houseBean.getValue6()};
                int j=0;
                for (int i = 0; i <llHouseType.getChildCount() ; i++) {
                    View view=llHouseType.getChildAt(i);
                    if (view instanceof CheckBox){
                        ((CheckBox) view).setText(houseTexts[j]);
                        j++;
                    }
                }
            }

        }


    }

    private void setHouseTypeStatus(String string, LinearLayout llHouseType) {
        for (int i = 0; i < llHouseType.getChildCount(); i++) {
            // CheckBox checkBox= (CheckBox) llHouseType.getChildAt(i);
            View view = llHouseType.getChildAt(i);
            if (view instanceof CheckBox) {
                if (string.contains(((CheckBox) view).getText().toString())) {
                    ((CheckBox) view).setChecked(true);
                    strFlag = string;
                }
            }

        }
    }

    private void setStatus(String string, final LinearLayout linearLayout) {
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            final View view = linearLayout.getChildAt(i);
            //RadioButton radioButton= (RadioButton) radioGroup.getChildAt(i);
            if (view instanceof CheckBox) {
                if (string.equals(((CheckBox) view).getText().toString())) {
                    ((CheckBox) view).setChecked(true);
                    strFlag = string;
                    break;
                }
            }

        }
    }

    //回显


    private void getHouseTypeData() {
        int count = llHouseType.getChildCount();
        StringBuffer sb = new StringBuffer();
        StringBuffer sbHouseTypeTag = new StringBuffer();
        for (int i = 0; i < count; i++) {
            View view = llHouseType.getChildAt(i);
            if (view instanceof CheckBox) {
                if (((CheckBox) view).isChecked()) {
                    sb.append(((CheckBox) view).getText().toString() + ",");
                    sbHouseTypeTag.append(((CheckBox) view).getTag() + ",");
                }
            }
        }
        selectStr = sb.toString();
        selectStrTag = sbHouseTypeTag.toString();
    }

    private void initStting() {
        /*tvSelect=getViewById(R.id.tvSelect);
        tvSelect.setText(getString(R.string.home_sure));
        tvSelect.setVisibility(View.VISIBLE);*/
        // setRightTitleBackground(new ColorDrawable(Color.WHITE), Color.parseColor("#ee781f"));
        if (getIntent().getExtras().getString("ss") != null) {
            setRightTitle(R.string.ok, getResources().getColor(R.color.scale_tab5), mOnClickListener);
        } else {
            getViewById(R.id.tvSelect).setVisibility(View.VISIBLE);
        }
        ivSearch = getViewById(R.id.rightSearchLinearLayout);
        ivSearch.setVisibility(View.GONE);
    }

    private void initViews() {
        //rgArea=getViewById(R.id.rgArea);
        llHouseType = getViewById(R.id.llHouseType);
        /*rgType = getViewById(R.id.rgType);
        rgPrice = getViewById(R.id.rgPrice);*/
        llType = getViewById(R.id.llType);
        llPrice = getViewById(R.id.llPrice);
        tvSelect = getViewById(R.id.tvSelect);
        llBase= getViewById(R.id.llBase);
    }

    private void setListener(final LinearLayout linearLayout) {
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            if (linearLayout.getChildAt(i) instanceof CheckBox) {
                CheckBox c = (CheckBox) linearLayout.getChildAt(i);
                c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (isChecked) {
                            if (temp != -1) {
                                CheckBox tempButton = (CheckBox) findViewById(temp);
                                if (tempButton != null) {
                                    tempButton.setChecked(false);
                                }
                            }
                            //得到当前的position
                            temp = buttonView.getId();
                            selectStr = buttonView.getText().toString();
                            selectStrTag = (String) buttonView.getTag();
                        } else {
                            temp = -1;
                            selectStr = "";
                            selectStrTag="";
                        }

                    }
                });

            }

        }

    }


    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            switch (v.getId()) {
                case R.id.backLinearLayout:
                    finish();
                    overridePendingTransition(0, R.anim.out_anim);
                    break;
                case R.id.rightTitle:
                    setData(intent);
                    break;
                case R.id.tvSelect:
                    setData(intent);
                    break;
            }
        }
    };

    private void setData(Intent intent) {
        if (!str.equals("housetype")) {
            Log.e("TAG", "setData: " + selectStr);
            intent.putExtra("selectextra", selectStr);
            intent.putExtra("selecttagextra", selectStrTag);
            setResult(RESULT_OK, intent);
            finish();
            overridePendingTransition(0, R.anim.out_anim);


        } else {
            getHouseTypeData();
            if (!TextUtils.isEmpty(selectStr)) {
                if (selectStr.endsWith(",")) {
                    selectStr = selectStr.substring(0, selectStr.length() - 1);
                }
                if (selectStrTag.endsWith(",")) {
                    selectStrTag = selectStrTag.substring(0, selectStrTag.length() - 1);
                }
                intent.putExtra("selectextra", selectStr);
                intent.putExtra("selecttagextra", selectStrTag);
                setResult(RESULT_OK, intent);
                finish();
                overridePendingTransition(0, R.anim.out_anim);
            } else {
                intent.putExtra("selectextra", selectStr);
                intent.putExtra("selecttagextra", selectStrTag);
                setResult(RESULT_OK, intent);
                finish();
                overridePendingTransition(0, R.anim.out_anim);
            }
        }

    }


    /*RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (group.getId()) {
                //区域
                *//*case R.id.rgArea:
                    RadioButton rbArea= (RadioButton) findViewById(group.getCheckedRadioButtonId());
                    selectStrTag = (String) rbArea.getTag();
                    selectStr=rbArea.getText().toString();

                    Log.e("选择***","selectStr:"+selectStr);
                    break;*//*
                //类型
                case R.id.rgType:
                    RadioButton rbType = (RadioButton) findViewById(group.getCheckedRadioButtonId());
                    rbType.setOnClickListener(mOnClickListener);
                    selectStrTag = (String) rbType.getTag();
                    selectStr = rbType.getText().toString();
                    break;
                //价格
                case R.id.rgPrice:
                    RadioButton rbPrice = (RadioButton) findViewById(group.getCheckedRadioButtonId());
                    selectStrTag = (String) rbPrice.getTag();
                    selectStr = rbPrice.getText().toString();
                    break;
            }
        }
    };*/

    @Override
    public void setListener() {
        changeLeftImageView(R.mipmap.close, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, R.anim.out_anim);
            }
        });

        //tvSelect.setOnClickListener(mOnClickListener);
        backLinearLayou.setOnClickListener(mOnClickListener);
        //rgArea.setOnCheckedChangeListener(mOnCheckedChangeListener);
        /*rgType.setOnCheckedChangeListener(mOnCheckedChangeListener);
        rgPrice.setOnCheckedChangeListener(mOnCheckedChangeListener);*/
        tvSelect.setOnClickListener(mOnClickListener);

    }
}
