package com.yd.org.sellpopularizesystem.activity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.javaBean.ProSubunitListBean;
import com.yd.org.sellpopularizesystem.utils.MyUtils;

public class ProductSubItemDetailActivity extends BaseActivity {
    private ProSubunitListBean.ResultBean.PropertyBean bean;
    private TextView tvItStatus,tvItPrice,tvItType,tvItBatchNum,tvItUnitNum,
            tvFloorType,tvItFloor,tvItBedRoom,tvItBath,tvItCarSquare,tvItBathNum,
            tvItDrec,tvItRoomArea,tvItORoomArea,tvTotalArea,tvTotalLandArea,tvLandPrice,tvBuildingPrice;
    @Override
    protected int setContentView() {
        return R.layout.activity_product_sub_item_detail;
    }

    @Override
    public void initView() {
        Bundle bundle=getIntent().getExtras();
        bean= (ProSubunitListBean.ResultBean.PropertyBean) bundle.get("item");
        hideRightImagview();
        setTitle(getString(R.string.details));
        tvItStatus= (TextView) findViewById(R.id.tvItStatus);
        tvItPrice= (TextView) findViewById(R.id.tvItPrice);
        tvItType= (TextView) findViewById(R.id.tvItType);
        tvItBatchNum= (TextView) findViewById(R.id.tvItBatchNum);
        tvItUnitNum= (TextView) findViewById(R.id.tvItUnitNum);
        tvFloorType= (TextView) findViewById(R.id.tvFloorType);
        tvItFloor= (TextView) findViewById(R.id.tvItFloor);
        tvItBedRoom= (TextView) findViewById(R.id.tvItBedRoom);
        tvItBath= (TextView) findViewById(R.id.tvItBath);
        tvItCarSquare= (TextView) findViewById(R.id.tvItCarSquare);
        tvItBathNum= (TextView) findViewById(R.id.tvItBathNum);
        tvItDrec= (TextView) findViewById(R.id.tvItDrec);
        tvItRoomArea= (TextView) findViewById(R.id.tvItRoomArea);
        tvItORoomArea= (TextView) findViewById(R.id.tvItORoomArea);
        tvTotalArea= (TextView) findViewById(R.id.tvTotalArea);
        tvTotalLandArea=getViewById(R.id.tvTotalLandArea);
        tvLandPrice=getViewById(R.id.tvLandPrice);
        tvBuildingPrice=getViewById(R.id.tvBuildingPrice);
        initData();
    }

    private void initData() {
        Log.e("getIs_lock","getIs_lock:"+bean.getIs_lock());
        tvItStatus.setText(bean.getIs_lock()==0?getString(R.string.issaling):getString(R.string.hadsaled));
        tvItPrice.setText("$"+getString(R.string.single_blank_space)+MyUtils.addComma(bean.getPrice().split("\\.")[0]));
        tvItType.setText(bean.getCate_name());
        tvItBatchNum.setText(bean.getProduct_childs_lot_number());
        tvItUnitNum.setText(bean.getProduct_childs_unit_number());
        tvFloorType.setText(bean.getFloor_type());
        tvItFloor.setText(bean.getLevel());
        tvItBedRoom.setText(bean.getBedroom());
        tvItBath.setText(bean.getBathroom());
        tvItCarSquare.setText(bean.getCar_space());
        tvItBathNum.setText(bean.getArea());
        tvItDrec.setText(bean.getAspect());
        tvItRoomArea.setText(bean.getInternal());
        tvItORoomArea.setText(bean.getExternal());
        tvTotalArea.setText(bean.getBuilding_area());
        tvTotalLandArea.setText(bean.getLand_size());
        tvLandPrice.setText("$"+getString(R.string.single_blank_space)+MyUtils.addComma(bean.getLand_vendor_price().split("\\.")[0]));
        tvBuildingPrice.setText("$"+getString(R.string.single_blank_space)+MyUtils.addComma(bean.getHouse_vendor_price().split("\\.")[0]));
    }

    @Override
    public void setListener() {

    }
}
