package com.yd.org.sellpopularizesystem.activity;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.Gson;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.javaBean.ProductListBean;
import com.yd.org.sellpopularizesystem.javaBean.SelectConditionBean;
import com.yd.org.sellpopularizesystem.utils.StringUtils;
import com.zhouyou.http.EasyHttp;
import com.zhouyou.http.cache.model.CacheMode;
import com.zhouyou.http.callback.SimpleCallBack;
import com.zhouyou.http.exception.ApiException;
import com.zhouyou.http.model.HttpParams;


import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

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
            //llHouseType.setVisibility(View.GONE);
            //*rgType.setVisibility(View.GONE);
            //rgPrice.setVisibility(View.GONE);
            /*llType.setVisibility(View.GONE);
            llPrice.setVisibility(View.GONE);*/
        } else if (str.equals("housetype")) {
            setTitle(R.string.housetype);
            //rgArea.setVisibility(View.GONE);
            //*rgType.setVisibility(View.GONE);
            //rgPrice.setVisibility(View.GONE);
            /*llType.setVisibility(View.GONE);
            llPrice.setVisibility(View.GONE);
            String string = getIntent().getExtras().getString("selectstatus");
            setHouseTypeStatus(string, llHouseType);*/
        } else if (str.equals("type")) {
            setTitle(R.string.type);
            //rgArea.setVisibility(View.GONE);
            //llHouseType.setVisibility(View.GONE);
            //rgPrice.setVisibility(View.GONE);
           /* llPrice.setVisibility(View.GONE);
            String string = getIntent().getExtras().getString("selectstatus");
            setStatus(string, llType);
            setListener(llType);*/
        } else if (str.equals("price")) {
            setTitle(R.string.price);
            // rgArea.setVisibility(View.GONE);
            //llHouseType.setVisibility(View.GONE);
            //rgType.setVisibility(View.GONE);
            //llType.setVisibility(View.GONE);
            /*String string = getIntent().getExtras().getString("selectstatus");
            setStatus(string, llPrice);
            setListener(llPrice);*/
        }
        getSelectConditionData();
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
        //SelectConditionBean scb = gson.fromJson(json, SelectConditionBean.class);

        JSONObject object = JSON.parseObject(json);
        if (object.getString("code").equals("1")) {

            for (Map.Entry<String, Object> entry : object.getJSONObject("result").entrySet()) {
                String keyString = entry.getKey();//提取一级key
                String valueString = entry.getValue().toString();//提取一级value
                Log.e("TAG", "jsonParse: " + keyString + "---" + valueString);

                if (str.equals("price")) {
                    if (keyString.equals("product_price")) {

                        JSONObject object2 = JSON.parseObject(valueString);
                        // int sizes = object2.getJSONObject(keyString).entrySet().size();
                        int[] priceId = new int[]{R.id.rbPrice1, R.id.rbPrice2, R.id.rbPrice3, R.id.rbPrice4, R.id.rbPrice5,
                                R.id.rbPrice6};
                        // String[] priceTags=new String[sizes];
                        //String[] priceTexts = new String[sizes];
                        // List<String> priceIds=new ArrayList<>();
                        //priceIds= Arrays.asList(getResources().getStringArray(R.array.price_id_list));

                        List<String> priceTags = new ArrayList<>();
                        List<String> priceTexts = new ArrayList<>();
                        // View baseView= LayoutInflater.from(SelectConditionActivity.this).inflate(R.layout.select_item_layout,null);
                        for (Map.Entry<String, Object> entry2 : object2.entrySet()) {
                            String key = entry2.getKey();//提取key
                            String value = entry2.getValue().toString();//提取value
                            priceTags.add(key);
                            priceTexts.add(value);
                        }
                        sort(priceTags);
                        sort(priceTexts);
                        Log.e("TAG", "priceTag: "+priceTags.toString());
                        Log.e("TAG", "priceTexts: "+priceTexts.toString());
                        for (int i = 0; i < priceTags.size(); i++) {
                            ViewGroup baseView = (ViewGroup) LayoutInflater.from(SelectConditionActivity.this).inflate(R.layout.select_item_layout, null);
                            CheckBox checkBox = (CheckBox) baseView.getChildAt(0);
                            checkBox.setId(priceId[i]);
                            checkBox.setTag(priceTags.get(i));
                            checkBox.setText(priceTexts.get(i));
                            //View view = llPrice.getChildAt(i);
                            llBase.addView(baseView);

                        }
                        String string = getIntent().getExtras().getString("selectstatus");
                        setStatus(string, llBase);
                        setListener(llBase);
                        break;
                    }

                }

                if (str.equals("type")) {
                    if (keyString.equals("product_cate")) {
                        Log.e("TAG", "jsonParse: "+ valueString);
                        JSONObject object2 = JSON.parseObject(valueString);
                        //int sizes = object2.getJSONObject(keyString).entrySet().size();
                        int[] priceId = new int[]{R.id.cbType1, R.id.cbType2, R.id.cbType3};
                        // String[] priceTags=new String[sizes];
                        //String[] priceTexts = new String[sizes];
                        // List<String> priceIds=new ArrayList<>();
                        //priceIds= Arrays.asList(getResources().getStringArray(R.array.price_id_list));

                        List<String> typeTags = new ArrayList<>();
                        List<String> typeTexts = new ArrayList<>();
                        // View baseView= LayoutInflater.from(SelectConditionActivity.this).inflate(R.layout.select_item_layout,null);
                        for (Map.Entry<String, Object> entry2 : object2.entrySet()) {
                            String key = entry2.getKey();//提取key
                            String value = entry2.getValue().toString();//提取value
                            typeTags.add(key);
                            typeTexts.add(value);
                        }

                        for (int i = 0; i < typeTags.size(); i++) {
                            ViewGroup baseView = (ViewGroup) LayoutInflater.from(SelectConditionActivity.this).inflate(R.layout.select_item_layout, null);
                            CheckBox checkBox = (CheckBox) baseView.getChildAt(0);
                            checkBox.setId(priceId[i]);
                            checkBox.setTag(typeTags.get(i));
                            checkBox.setText(typeTexts.get(i));
                            //View view = llPrice.getChildAt(i);
                            llBase.addView(baseView);

                        }
                        String string = getIntent().getExtras().getString("selectstatus");
                        setStatus(string, llBase);
                        setListener(llBase);
                        break;
                    }

                }

                if (str.equals("housetype")) {
                    if (keyString.equals("product_house")) {

                        JSONObject object2 = JSON.parseObject(valueString);
                        int[] priceId = new int[]{R.id.cbHouseType1, R.id.cbHouseType2, R.id.cbHouseType3,
                                R.id.cbHouseType4, R.id.cbHouseType5, R.id.cbHouseType6};
                        // String[] priceTags=new String[sizes];
                        //String[] priceTexts = new String[sizes];
                        // List<String> priceIds=new ArrayList<>();
                        //priceIds= Arrays.asList(getResources().getStringArray(R.array.price_id_list));

                        List<String> houseTags = new ArrayList<>();
                        List<String> houseTexts = new ArrayList<>();
                        // View baseView= LayoutInflater.from(SelectConditionActivity.this).inflate(R.layout.select_item_layout,null);
                        for (Map.Entry<String, Object> entry2 : object2.entrySet()) {
                            String key = entry2.getKey();//提取key
                            String value = entry2.getValue().toString();//提取value
                            houseTags.add(key);
                            houseTexts.add(value);
                        }
                        sort(houseTags);
                        sort(houseTexts);
                        Log.e("TAG", "houseTag: "+houseTags.toString());
                        Log.e("TAG", "houseText: "+houseTexts.toString());
                        for (int i = 0; i < houseTags.size(); i++) {
                            ViewGroup baseView = (ViewGroup) LayoutInflater.from(SelectConditionActivity.this).inflate(R.layout.select_item_layout, null);
                            CheckBox checkBox = (CheckBox) baseView.getChildAt(0);
                            checkBox.setId(priceId[i]);
                            checkBox.setTag(houseTags.get(i));
                            checkBox.setText(houseTexts.get(i));
                            //View view = llPrice.getChildAt(i);
                            llBase.addView(baseView);
                        }
                        String string = getIntent().getExtras().getString("selectstatus");
                        Log.e("TAG", "jsonParse: "+string );
                        setHouseTypeStatus(string, llBase);
                        break;
                    }

                }

            }

        }

    }

    private void sort(List<String> typeTags) {
        Collections.sort(typeTags, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (o1.length()>1 && o2.length()>1){//长度大于1时
                    if (Integer.parseInt(StringUtils.getDigtalFromString(o1.split("\\~")[0]))>
                            Integer.parseInt(StringUtils.getDigtalFromString(o2.split("\\~")[0]))){
                        return 1;
                    }
                    if (Integer.parseInt(StringUtils.getDigtalFromString(o1.split("\\~")[0]))==
                            Integer.parseInt(StringUtils.getDigtalFromString(o2.split("\\~")[0]))){
                        return o1.compareTo(o2);

                    }
                }else {//长度相等时
                    if (TextUtils.isDigitsOnly(o1) && TextUtils.isDigitsOnly(o2)){
                        if (Integer.parseInt(o1)>Integer.parseInt(o2)){
                            return 1;
                        }
                        if (Integer.parseInt(o1)==Integer.parseInt(o2)){

                            return 0;
                        }
                    }
                }

                //return o1.compareTo(o2);
                return -1;
            }
        });
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
            if (view instanceof LinearLayout) {
                if (string.contains(((CheckBox) (((LinearLayout) view).getChildAt(0))).getText().toString())) {
                    ((CheckBox)((LinearLayout) view).getChildAt(0)).setChecked(true);
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

            if (view instanceof LinearLayout) {
                if (string.equals(((CheckBox) (((LinearLayout) view).getChildAt(0))).getText().toString())) {
                    ((CheckBox)((LinearLayout) view).getChildAt(0)).setChecked(true);
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

            if (view instanceof LinearLayout){
                CheckBox cb=(CheckBox) ((LinearLayout)view).getChildAt(0);
                if (cb.isChecked()){
                    sb.append(cb.getText().toString() + ",");
                    sbHouseTypeTag.append(cb.getTag() + ",");
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
        //llHouseType = getViewById(R.id.llHouseType);
        /*rgType = getViewById(R.id.rgType);
        rgPrice = getViewById(R.id.rgPrice);*/
        /*llType = getViewById(R.id.llType);
        llPrice = getViewById(R.id.llPrice);
        tvSelect = getViewById(R.id.tvSelect);*/
        llBase = getViewById(R.id.llBase);
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
                            buttonView.setChecked(true);
                            temp = -1;
                            selectStr = "";
                            selectStrTag="";
                        }

                    }
                });

            }

            if (linearLayout.getChildAt(i) instanceof LinearLayout) {
                CheckBox c = (CheckBox) ((LinearLayout) linearLayout.getChildAt(i)).getChildAt(0);
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
                            selectStrTag = "";
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
                   // setData(intent);
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
