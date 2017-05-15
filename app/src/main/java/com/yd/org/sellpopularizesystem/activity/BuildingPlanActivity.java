package com.yd.org.sellpopularizesystem.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.adapter.CommonAdapter;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.application.ViewHolder;
import com.yd.org.sellpopularizesystem.javaBean.ImageContent;

import net.tsz.afinal.FinalHttp;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BuildingPlanActivity extends BaseActivity {
    private ListView lvFloorPlan;
    private List<ImageContent> floorListData = new ArrayList<>();
    private CommonAdapter floorListAdapter;

    @Override
    protected int setContentView() {
        hideRightImagview();
        setTitle("平面图");
        return R.layout.activity_building_plan;
    }

    @Override
    public void initView() {
        floorListData.addAll((Collection<? extends ImageContent>) getIntent().getExtras().getSerializable("floorListData"));
        lvFloorPlan = getViewById(R.id.lvFloorPlan);
        floorListAdapter = new CommonAdapter<ImageContent>(BuildingPlanActivity.this, floorListData, R.layout.floor_listview_iten_layout) {
            @Override
            public void convert(ViewHolder holder, ImageContent item) {
                String strUrl = Contants.DOMAIN + "/" + item.getUrl();
                Log.e("tag", "convert: "+strUrl);
                holder.setImageByUrl(R.id.ivFloorImage, Contants.DOMAIN + "/" + item.getUrl());
                holder.setText(R.id.tvTitleName,item.getDetail_name());
            }
        };
        lvFloorPlan.setAdapter(floorListAdapter);
    }


    @Override
    public void setListener() {

    }

}
