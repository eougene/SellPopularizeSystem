package com.yd.org.sellpopularizesystem.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bosong.commentgallerylib.CommentGalleryContainer;
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
    public static final String CLICK_INDEX = "CLICK_INDEX";
    public static final String COMMENT_LIST = "COMMENT_LIST";
    CommentGalleryContainer commentList;
    private List<String> imgUrls = new ArrayList<>();;

    @Override
    protected int setContentView() {
        hideRightImagview();
        setTitle(R.string.floorphoto);
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
        getImgUrls(floorListData);
        commentList=new CommentGalleryContainer(imgUrls,"");
        lvFloorPlan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent it = new Intent();
                it.putExtra(CLICK_INDEX, position);
                it.putExtra(COMMENT_LIST,commentList);
                it.setClass(BuildingPlanActivity.this,CommentGalleryActivity.class);
                startActivity(it);
            }
        });
    }

    //获取所有图片路径
    private List<String> getImgUrls(List<ImageContent> floorListData) {
        for (int i = 0; i < floorListData.size(); i++) {
            imgUrls.add(Contants.DOMAIN + "/"+floorListData.get(i).getUrl());
        }
        return imgUrls;
    }


    @Override
    public void setListener() {

    }

}
