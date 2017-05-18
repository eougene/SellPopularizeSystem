package com.yd.org.sellpopularizesystem.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yd.org.sellpopularizesystem.R;

public class SelectConditionActivity extends BaseActivity {
    private RadioGroup rgArea,rgType,rgPrice;
    private LinearLayout llHouseType;
    private TextView tvSelect;
    private ImageView ivSearch;
    private String str;
    private String selectStr;
    private String selectStrTag;

    @Override
    protected int setContentView() {
        return R.layout.activity_select_condition;
    }

    @Override
    public void initView() {
        initViews();
        initStting();
        str = getIntent().getExtras().getString("fatosca");
        if (str.equals("area")){
            setTitle(R.string.area);
            llHouseType.setVisibility(View.GONE);
            rgType.setVisibility(View.GONE);
            rgPrice.setVisibility(View.GONE);
        }else if (str.equals("housetype")){
            setTitle(R.string.housetype);
            rgArea.setVisibility(View.GONE);
            rgType.setVisibility(View.GONE);
            rgPrice.setVisibility(View.GONE);
        }else if (str.equals("type")){
            setTitle(R.string.type);
            rgArea.setVisibility(View.GONE);
            llHouseType.setVisibility(View.GONE);
            rgPrice.setVisibility(View.GONE);
        }else if (str.equals("price")){
            setTitle(R.string.price);
            rgArea.setVisibility(View.GONE);
            llHouseType.setVisibility(View.GONE);
            rgType.setVisibility(View.GONE);
        }
    }

    private void getHouseTypeData() {
        int count=llHouseType.getChildCount();
        StringBuffer sb = new StringBuffer();
        StringBuffer sbHouseTypeTag = new StringBuffer();
        for (int i = 0; i <count; i++) {
            View view=llHouseType.getChildAt(i);
            if (view instanceof CheckBox){
                if (((CheckBox) view).isChecked()){
                    sb.append(((CheckBox) view).getText().toString()+",");
                    sbHouseTypeTag.append(((CheckBox) view).getTag()+",");
                }
            }
        }
        selectStr=sb.toString();
        selectStrTag=sbHouseTypeTag.toString();
    }

    private void initStting() {
        tvSelect=getViewById(R.id.tvSelect);
        tvSelect.setText(getString(R.string.home_sure));
        tvSelect.setVisibility(View.VISIBLE);
        ivSearch=getViewById(R.id.rightSearchLinearLayout);
        ivSearch.setVisibility(View.GONE);
    }

    private void initViews() {
        rgArea=getViewById(R.id.rgArea);
        llHouseType=getViewById(R.id.llHouseType);
        rgType=getViewById(R.id.rgType);
        rgPrice=getViewById(R.id.rgPrice);
    }

    View.OnClickListener mOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            switch (v.getId()){
                case R.id.backLinearLayout:
                    setData(intent);
                    break;
                case R.id.tvSelect:
                    setData(intent);
                    break;
            }
        }
    };

    private void setData(Intent intent) {
        if (!str.equals("housetype")){
            intent.putExtra("selectextra",selectStr);
            intent.putExtra("selecttagextra",selectStrTag);
            setResult(RESULT_OK, intent);
        }else{
            getHouseTypeData();
            if (selectStr.endsWith(",")){
                selectStr=selectStr.substring(0,selectStr.length()-1);
            }
            if (selectStrTag.endsWith(",")){
                selectStrTag=selectStrTag.substring(0,selectStrTag.length()-1);
            }
            intent.putExtra("selectextra",selectStr);
            intent.putExtra("selecttagextra",selectStrTag);
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener=new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (group.getId()){
                case R.id.rgArea:
                    RadioButton rbArea= (RadioButton) findViewById(group.getCheckedRadioButtonId());
                    selectStrTag = (String) rbArea.getTag();
                    selectStr=rbArea.getText().toString();
                    break;
                case R.id.rgType:
                    RadioButton rbType = (RadioButton) findViewById(group.getCheckedRadioButtonId());
                    selectStrTag = (String) rbType.getTag();
                    selectStr=rbType.getText().toString();
                    break;
                case R.id.rgPrice:
                    RadioButton rbPrice= (RadioButton) findViewById(group.getCheckedRadioButtonId());
                    selectStrTag = (String) rbPrice.getTag();
                    selectStr=rbPrice.getText().toString();
                    break;
            }
        }
    };

    @Override
    public void setListener() {
        tvSelect.setOnClickListener(mOnClickListener);
        backLinearLayou.setOnClickListener(mOnClickListener);
        rgArea.setOnCheckedChangeListener(mOnCheckedChangeListener);
        rgType.setOnCheckedChangeListener(mOnCheckedChangeListener);
        rgPrice.setOnCheckedChangeListener(mOnCheckedChangeListener);
    }
}
