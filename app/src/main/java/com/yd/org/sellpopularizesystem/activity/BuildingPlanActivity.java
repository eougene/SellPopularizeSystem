package com.yd.org.sellpopularizesystem.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.adapter.CommonAdapter;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.application.ViewHolder;
import com.yd.org.sellpopularizesystem.javaBean.FileContent;
import com.yd.org.sellpopularizesystem.javaBean.ImageContent;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class BuildingPlanActivity extends BaseActivity {
    private ListView lvFloorPlan;
    private List<FileContent> floorListData = new ArrayList<>();
    private List<FileContent> ListData = new ArrayList<>();
    private List<ImageContent> imagetData = new ArrayList<>();
    private CommonAdapter floorListAdapter;

    @Override
    protected int setContentView() {
        hideRightImagview();
        setTitle(R.string.floorphoto);
        return R.layout.activity_building_plan;
    }

    @Override
    public void initView() {
        //获取数据
        floorListData.addAll((Collection<? extends FileContent>) getIntent().getExtras().getSerializable("floorListData"));
        lvFloorPlan = getViewById(R.id.lvFloorPlan);


        for (int i=0;i<floorListData.size();i++){

            if (floorListData.get(i).getFile_type()==1){
                ImageContent imageContent=new ImageContent();
                imageContent.setUrl(floorListData.get(i).getUrl());
                imagetData.add(imageContent);
                ListData.add(floorListData.get(i));
            }
        }

        //设置适配器
        setAdapter();


    }

    private void setAdapter() {
        floorListAdapter = new CommonAdapter<FileContent>(BuildingPlanActivity.this, ListData, R.layout.floor_listview_iten_layout) {
            @Override
            public void convert(ViewHolder holder, FileContent item) {
                String strUrl = Contants.DOMAIN + "/" + item.getUrl();
                holder.setImageByUrl(R.id.ivFloorImage, Contants.DOMAIN + "/" + item.getUrl());
                holder.setText(R.id.tvTitleName, item.getDetail_name());
            }
        };
        lvFloorPlan.setAdapter(floorListAdapter);
    }

    @Override
    public void setListener() {
        lvFloorPlan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Bundle bundle = new Bundle();
                bundle.putSerializable("img_content", (Serializable) imagetData);
                ActivitySkip.forward(BuildingPlanActivity.this, ImageShowActivity.class, bundle);



            }
        });
    }

}
