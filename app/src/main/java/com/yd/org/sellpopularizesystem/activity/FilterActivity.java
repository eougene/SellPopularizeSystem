package com.yd.org.sellpopularizesystem.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.application.ExtraName;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;

public class FilterActivity extends BaseActivity {
    private TextView tvSelect,tvAreaDes,tvHouseType,tvSelectType,tvSelectPrice,tvProductSearch;
    private ImageView ivSearch;
    //获取到筛选条件内容对应代号如1,2,3等
    private String selectStrTag;
    //获取到筛选条件内容
    private String selectContent;
    private RelativeLayout rlArea,rlHouseType,rlType,rlPrice;
    @Override
    protected int setContentView() {
        return R.layout.activity_filter;
    }

    @Override
    public void initView() {
        initSetting();
        initViews();
    }

    private void initViews() {
        rlArea=getViewById(R.id.rlArea);
        tvAreaDes=getViewById(R.id.tvAreaDes);
        rlHouseType=getViewById(R.id.rlHouseType);
        tvHouseType=getViewById(R.id.tvHouseType);
        /*rlType=getViewById(R.id.rlType);
        tvSelectType=getViewById(R.id.tvSelectType);*/
        rlPrice=getViewById(R.id.rlPrice);
        tvSelectPrice=getViewById(R.id.tvSelectPrice);
        tvProductSearch=getViewById(R.id.tvProductSearch);
    }

    private void initSetting() {
        setTitle(R.string.select);
        setRightTitleBackground(new ColorDrawable(Color.WHITE), Color.RED);
        setRightTitle(R.string.reset, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    resetText();
            }
        });
        changeLeftImageView(R.mipmap.close, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(0, R.anim.out_anim);
            }
        });
        /*tvSelect=getViewById(R.id.tvSelect);
        tvSelect.setText(getString(R.string.reset));
        tvSelect.setVisibility(View.VISIBLE);*/
        ivSearch=getViewById(R.id.rightSearchLinearLayout);
        ivSearch.setVisibility(View.GONE);
    }

    @Override
    public void setListener() {
        //tvSelect.setOnClickListener(mOnClickListener);
        rlArea.setOnClickListener(mOnClickListener);
       rlHouseType.setOnClickListener(mOnClickListener);
        //rlType.setOnClickListener(mOnClickListener);
        rlPrice.setOnClickListener(mOnClickListener);
        tvProductSearch.setOnClickListener(mOnClickListener);
    }
    View.OnClickListener mOnClickListener=new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Bundle bun=new Bundle();
            switch (v.getId()){
                /*case R.id.tvSelect:
                    resetText();
                    break;*/
                case R.id.rlArea:
                    bun.putString("fatosca","area");
                    ActivitySkip.forward(FilterActivity.this,SelectConditionActivity.class, ExtraName.AREA,bun);
                    break;
                case R.id.rlHouseType:
                    bun.putString("fatosca","housetype");
                    ActivitySkip.forward(FilterActivity.this,SelectConditionActivity.class,ExtraName.HOURSE,bun);
                    break;
                /*case R.id.rlType:
                    bun.putString("fatosca","type");
                    ActivitySkip.forward(FilterActivity.this,SelectConditionActivity.class,ExtraName.SPACE,bun);
                    break;*/
                case R.id.rlPrice:
                    bun.putString("fatosca","price");
                    ActivitySkip.forward(FilterActivity.this,SelectConditionActivity.class,ExtraName.PRICE,bun);
                    break;
                case R.id.tvProductSearch:
                    Message message=ScaleActivity.scaleActivity.handler.obtainMessage();
                    ScaleActivity.scaleActivity.psu.setArea(tvAreaDes.getText().toString().equals(getString(R.string.unlimited_))?"":selectStrTag);
                    ScaleActivity.scaleActivity.psu.setHouse(tvHouseType.getText().toString().equals(getString(R.string.unlimited_))?"":selectStrTag);
                    //ScaleActivity.scaleActivity.psu.setSpace(tvSelectType.getText().toString().equals("不限")?"":selectStrTag);
                    ScaleActivity.scaleActivity.psu.setPrice(tvSelectPrice.getText().toString().equals(getString(R.string.unlimited_))?"":selectStrTag);
                    message.obj=ScaleActivity.scaleActivity.psu;
                    ScaleActivity.scaleActivity.handler.handleMessage(message);
                    String strArea=tvAreaDes.getText().toString().equals(getString(R.string.unlimited_))?"":tvAreaDes.getText().toString();
                    String strHt=tvHouseType.getText().toString().equals(getString(R.string.unlimited_))?"":tvHouseType.getText().toString();
                    String strPrice=tvSelectPrice.getText().toString().equals(getString(R.string.unlimited_))?"":tvSelectPrice.getText().toString();
                    selectContent=strArea+strHt+strPrice;
                    ScaleActivity.scaleActivity.strSelect=selectContent;
                    finish();
                    break;
            }
        }
    };

    private void resetText() {
        if (!tvAreaDes.getText().equals(getString(R.string.unlimited_))){
            tvAreaDes.setText(getString(R.string.unlimited_));
        }
        if (!tvHouseType.getText().equals(getString(R.string.unlimited_))){
            tvHouseType.setText(getString(R.string.unlimited_));
        }
        /*if (!tvSelectType.getText().equals("不限")){
            tvSelectType.setText("不限");
        }*/
        if (!tvSelectPrice.getText().equals(getString(R.string.unlimited_))){
            tvSelectPrice.setText(getString(R.string.unlimited_));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            switch (requestCode){
                case ExtraName.AREA:
                    tvAreaDes.setText(TextUtils.isEmpty(data.getStringExtra("selectextra"))?getString(R.string.unlimited_):data.getStringExtra("selectextra"));
                    selectStrTag=TextUtils.isEmpty(data.getStringExtra("selecttagextra"))?"":data.getStringExtra("selecttagextra");
                    break;
                case ExtraName.HOURSE:
                    tvHouseType.setText(TextUtils.isEmpty(data.getStringExtra("selectextra"))?getString(R.string.unlimited_):data.getStringExtra("selectextra"));
                    selectStrTag=TextUtils.isEmpty(data.getStringExtra("selecttagextra"))?"":data.getStringExtra("selecttagextra");
                    break;
                /*case ExtraName.SPACE:
                    tvSelectType.setText(data.getStringExtra("selectextra"));
                    selectStrTag=data.getStringExtra("selecttagextra");
                    break;*/
                case ExtraName.PRICE:
                    tvSelectPrice.setText(TextUtils.isEmpty(data.getStringExtra("selectextra"))?getString(R.string.unlimited_):data.getStringExtra("selectextra"));
                    selectStrTag=TextUtils.isEmpty(data.getStringExtra("selecttagextra"))?"":data.getStringExtra("selecttagextra");
                    break;
            }
        }
    }
}
