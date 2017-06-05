package com.yd.org.sellpopularizesystem.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.yd.org.sellpopularizesystem.R;
import com.yd.org.sellpopularizesystem.activity.StudyDetailaActivity;
import com.yd.org.sellpopularizesystem.activity.StudySubitemActivity;
import com.yd.org.sellpopularizesystem.adapter.StyudyAdapter;
import com.yd.org.sellpopularizesystem.application.Contants;
import com.yd.org.sellpopularizesystem.internal.PullToRefreshLayout;
import com.yd.org.sellpopularizesystem.internal.PullableListView;
import com.yd.org.sellpopularizesystem.javaBean.StudyBean;
import com.yd.org.sellpopularizesystem.utils.ActivitySkip;
import com.yd.org.sellpopularizesystem.utils.SharedPreferencesHelps;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import java.util.ArrayList;
import java.util.List;


/**
 * 学习园地
 */
public class StudyFragment extends BaseFragmentView  {
    private TextView tvPlayPlatform,tvSuccess,tvBasic,tvSaleSecrete,tvProject;
    @Override
    protected void initView(Bundle savedInstanceState) {
        setContentView(R.layout.fragment_study);
        tvPlayPlatform=getViewById(R.id.tvPlayPlatform);
        tvSuccess=getViewById(R.id.tvSuccess);
        tvBasic=getViewById(R.id.tvBasic);
        tvSaleSecrete=getViewById(R.id.tvSaleTec);
        tvProject=getViewById(R.id.tvProject);
    }

    @Override
    protected void setListener() {
        tvPlayPlatform.setOnClickListener(mOnClickListener);
        tvSuccess.setOnClickListener(mOnClickListener);
        tvBasic.setOnClickListener(mOnClickListener);
        tvSaleSecrete.setOnClickListener(mOnClickListener);
        tvProject.setOnClickListener(mOnClickListener);

    }
    private  View.OnClickListener mOnClickListener=new View.OnClickListener() {
        Bundle bundle=new Bundle();
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.tvPlayPlatform:
                    bundle.putString("tossia","palyplatform");
                    ActivitySkip.forward(getActivity(), StudySubitemActivity.class,bundle);
                    break;
                case R.id.tvSuccess:
                    bundle.putString("tossia","success");
                    ActivitySkip.forward(getActivity(), StudySubitemActivity.class,bundle);
                    break;
                case R.id.tvBasic:
                    bundle.putString("tossia","basic");
                    ActivitySkip.forward(getActivity(), StudySubitemActivity.class,bundle);
                    break;
                case R.id.tvSaleTec:
                    bundle.putString("tossia","saleTec");
                    ActivitySkip.forward(getActivity(), StudySubitemActivity.class,bundle);
                    break;
                case R.id.tvProject:
                    bundle.putString("tossia","project");
                    ActivitySkip.forward(getActivity(), StudySubitemActivity.class,bundle);
                    break;
            }
        }
    };
    @Override
    protected void processLogic(Bundle savedInstanceState) {

    }

}
