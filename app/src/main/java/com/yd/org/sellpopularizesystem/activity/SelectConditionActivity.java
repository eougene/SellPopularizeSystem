package com.yd.org.sellpopularizesystem.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IdRes;
import android.text.TextUtils;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.utils.ToasShow;

public class SelectConditionActivity extends BaseActivity {
    private RadioGroup rgType,rgPrice;
    private LinearLayout llHouseType;
    private TextView tvSelect;
    private ImageView ivSearch;
    private String str,strFlag;
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
            setTitle(R.string.type);
            llHouseType.setVisibility(View.GONE);
            rgType.setVisibility(View.GONE);
            rgPrice.setVisibility(View.GONE);
        }else if (str.equals("housetype")){
            setTitle(R.string.housetype);
            //rgArea.setVisibility(View.GONE);
            rgType.setVisibility(View.GONE);
            rgPrice.setVisibility(View.GONE);
            String string=getIntent().getExtras().getString("selectstatus");
            setHouseTypeStatus(string,llHouseType);
        }else if (str.equals("type")){
            setTitle(R.string.type);
            //rgArea.setVisibility(View.GONE);
            llHouseType.setVisibility(View.GONE);
            rgPrice.setVisibility(View.GONE);
            String string=getIntent().getExtras().getString("selectstatus");
            setStatus(string,rgType);
        }else if (str.equals("price")){
            setTitle(R.string.price);
           // rgArea.setVisibility(View.GONE);
            llHouseType.setVisibility(View.GONE);
            rgType.setVisibility(View.GONE);
            String string=getIntent().getExtras().getString("selectstatus");
            setStatus(string,rgPrice);
        }
    }

    private void setHouseTypeStatus(String string, LinearLayout llHouseType) {
        for (int i = 0; i < llHouseType.getChildCount(); i++) {
           // CheckBox checkBox= (CheckBox) llHouseType.getChildAt(i);
            View view=llHouseType.getChildAt(i);
            if (view instanceof CheckBox){
                if (string.contains(((CheckBox)view).getText().toString())){
                    ((CheckBox)view).setChecked(true);
                    strFlag=string;
                }
            }

        }
    }

    private void setStatus(String string, RadioGroup radioGroup) {
        for (int i = 0; i < radioGroup.getChildCount(); i++) {
            View view= radioGroup.getChildAt(i);
            //RadioButton radioButton= (RadioButton) radioGroup.getChildAt(i);
            if (view instanceof RadioButton){
                if (string.equals(((RadioButton) view).getText().toString())){
                    ((RadioButton) view).setChecked(true);
                    strFlag=string;
                    break;
                }
            }

        }
    }

    //回显


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
        /*tvSelect=getViewById(R.id.tvSelect);
        tvSelect.setText(getString(R.string.home_sure));
        tvSelect.setVisibility(View.VISIBLE);*/
        setRightTitleBackground(new ColorDrawable(Color.WHITE), Color.RED);
        if (getIntent().getExtras().getString("ss")==null){
            setRightTitle(R.string.ok, mOnClickListener);
        }else {
            getViewById(R.id.tvSelect).setVisibility(View.VISIBLE);
        }
        ivSearch=getViewById(R.id.rightSearchLinearLayout);
        ivSearch.setVisibility(View.GONE);
    }

    private void initViews() {
        //rgArea=getViewById(R.id.rgArea);
        llHouseType=getViewById(R.id.llHouseType);
        rgType=getViewById(R.id.rgType);
        rgPrice=getViewById(R.id.rgPrice);
        tvSelect=getViewById(R.id.tvSelect);
    }

    View.OnClickListener mOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            switch (v.getId()){
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
        if (!str.equals("housetype")){
            if (!TextUtils.isEmpty(selectStr)){
                intent.putExtra("selectextra",selectStr);
                intent.putExtra("selecttagextra",selectStrTag);
                setResult(RESULT_OK, intent);
                finish();
                overridePendingTransition(0, R.anim.out_anim);
            }else {
                if (TextUtils.isEmpty(strFlag)){
                    ToasShow.showToastCenter(SelectConditionActivity.this,getString(R.string.select_ele));
                }else {
                    finish();
                    overridePendingTransition(0, R.anim.out_anim);
                }

            }

        }else{
            getHouseTypeData();
            if (!TextUtils.isEmpty(selectStr)){
                if (selectStr.endsWith(",")){
                    selectStr=selectStr.substring(0,selectStr.length()-1);
                }
                if (selectStrTag.endsWith(",")){
                    selectStrTag=selectStrTag.substring(0,selectStrTag.length()-1);
                }
                intent.putExtra("selectextra",selectStr);
                intent.putExtra("selecttagextra",selectStrTag);
                setResult(RESULT_OK, intent);
                finish();
                overridePendingTransition(0, R.anim.out_anim);
            }else {
                if (TextUtils.isEmpty(strFlag)){
                    ToasShow.showToastCenter(SelectConditionActivity.this,getString(R.string.select_ele));
                }else {
                    finish();
                    overridePendingTransition(0, R.anim.out_anim);
                }

            }
        }

    }

    RadioGroup.OnCheckedChangeListener mOnCheckedChangeListener=new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
            switch (group.getId()){
                //区域
                /*case R.id.rgArea:
                    RadioButton rbArea= (RadioButton) findViewById(group.getCheckedRadioButtonId());
                    selectStrTag = (String) rbArea.getTag();
                    selectStr=rbArea.getText().toString();

                    Log.e("选择***","selectStr:"+selectStr);
                    break;*/
                //类型
                case R.id.rgType:
                    RadioButton rbType = (RadioButton) findViewById(group.getCheckedRadioButtonId());
                    selectStrTag = (String) rbType.getTag();
                    selectStr=rbType.getText().toString();
                    break;
                //价格
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
        rgType.setOnCheckedChangeListener(mOnCheckedChangeListener);
        rgPrice.setOnCheckedChangeListener(mOnCheckedChangeListener);
        tvSelect.setOnClickListener(mOnClickListener);
    }
}
