package com.yd.org.sellpopularizesystem.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.IdRes;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.myView.ElasticHorizontalScrollView;
import com.yd.org.sellpopularizesystem.utils.ToasShow;

public class SelectConditionActivity extends BaseActivity {
    //private RadioGroup rgType, rgPrice;
    private LinearLayout llHouseType, llType, llPrice;
    private TextView tvSelect;
    private ImageView ivSearch;
    private String str, strFlag;
    private String selectStr;
    private String selectStrTag;
    private StringBuilder sb = new StringBuilder();
    int position = 0;
    public static int temp = -1;

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

    }

    private void setListener(final LinearLayout linearLayout) {
        // final View[] view = new View[linearLayout.getChildCount()];
        for (int i = 0; i < linearLayout.getChildCount(); i++) {
            position = i;
            // view[i]=linearLayout.getChildAt(i);
            //RadioButton radioButton= (RadioButton) radioGroup.getChildAt(i);
            if (linearLayout.getChildAt(i) instanceof CheckBox) {


                CheckBox c = (CheckBox) linearLayout.getChildAt(i);
                c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        Log.e("TAG", "buttonView**: " + buttonView.getId());
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
